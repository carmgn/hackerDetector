package com.carmgn.hackdetector.detector;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * Keeps, per IP, the timestamps of its recent failed logins and flags the IP when
 * {@code attempts} of them fall inside the configured window.
 *
 * <p>The window is measured on the timestamps written in the log, not on the wall clock,
 * so the result is the same whether lines arrive in real time or are replayed.
 *
 * <p>Thread-safe: every update of an IP happens inside {@link ConcurrentHashMap#compute},
 * which runs atomically per key, so concurrent requests for different IPs never block
 * each other and requests for the same IP are serialised.
 */
@Component
public class SlidingWindowHackerDetector implements HackerDetector {

    /** How often (in failed lines) IPs with no recent activity are purged. */
    static final int PURGE_EVERY = 10_000;

    private final int attempts;
    private final long windowSeconds;
    private final Map<String, Deque<Long>> failuresByIp = new ConcurrentHashMap<>();
    private final AtomicLong latestEpochSecond = new AtomicLong(Long.MIN_VALUE);
    private final AtomicLong failuresSeen = new AtomicLong();

    public SlidingWindowHackerDetector(HackerDetectorProperties properties) {
        this.attempts = properties.attempts();
        this.windowSeconds = properties.window().toSeconds();
    }

    @Override
    public String parseLine(String line) {
        return LogLine.parse(line)
                .filter(logLine -> logLine.action() == LogLine.Action.SIGNIN_FAILURE)
                .filter(this::registerFailure)
                .map(LogLine::ip)
                .orElse(null);
    }

    /** Records the failure and returns whether the IP has reached the threshold. */
    private boolean registerFailure(LogLine failure) {
        long now = failure.epochSecond();
        latestEpochSecond.accumulateAndGet(now, Math::max);

        int[] failuresInWindow = new int[1];
        failuresByIp.compute(failure.ip(), (ip, timestamps) -> {
            Deque<Long> recent = timestamps != null ? timestamps : new ArrayDeque<>();
            recent.addLast(now);
            evictOlderThan(recent, now - windowSeconds);
            failuresInWindow[0] = recent.size();
            return recent;
        });

        if (failuresSeen.incrementAndGet() % PURGE_EVERY == 0) {
            purgeInactiveIps();
        }
        return failuresInWindow[0] >= attempts;
    }

    /** Drops IPs whose last failure is older than the window, so memory stays bounded. */
    void purgeInactiveIps() {
        long limit = latestEpochSecond.get() - windowSeconds;
        failuresByIp.forEach((ip, ignored) -> failuresByIp.computeIfPresent(ip, (key, timestamps) -> {
            evictOlderThan(timestamps, limit);
            return timestamps.isEmpty() ? null : timestamps;
        }));
    }

    int trackedIps() {
        return failuresByIp.size();
    }

    private static void evictOlderThan(Deque<Long> timestamps, long limit) {
        while (!timestamps.isEmpty() && timestamps.peekFirst() < limit) {
            timestamps.pollFirst();
        }
    }
}
