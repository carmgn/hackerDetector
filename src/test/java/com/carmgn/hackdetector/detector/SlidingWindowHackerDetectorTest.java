package com.carmgn.hackdetector.detector;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SlidingWindowHackerDetectorTest {

    private SlidingWindowHackerDetector detector;

    @BeforeEach
    void setUp() {
        detector = new SlidingWindowHackerDetector(new HackerDetectorProperties(5, Duration.ofMinutes(5)));
    }

    /** The scenarios of the original exercise solution. */
    static Stream<Arguments> logs() {
        return Stream.of(
                Arguments.of("failures spread over more than 5 minutes", List.of(
                        "80.238.9.179,1614881565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614882565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614883625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614884685,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614885745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614886750,SIGNIN_FAILURE,Will.Smith"), 0),
                Arguments.of("six failures in three minutes", List.of(
                        "80.238.9.179,1614887565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887685,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith"), 1),
                Arguments.of("two attackers interleaved", List.of(
                        "80.238.9.179,1614887565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887566,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887566,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887567,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887671,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887677,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887679,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887680,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887680,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887688,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887699,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887700,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887745,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith"), 2),
                Arguments.of("successful logins do not count", List.of(
                        "80.238.9.179,1614887565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887566,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887566,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887567,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887671,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887677,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887680,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.177,1614887699,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.177,1614887745,SIGNIN_SUCCESS,Henry.Cavill",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith"), 1),
                Arguments.of("malformed lines are ignored", Arrays.asList(
                        null,
                        " ",
                        "80.238.9.179, ,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,aaaaaaaaaa,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887565, ,Will.Smith",
                        "80.238.9.179,1614887625,SIGNIN_FAILURES,Will.Smith",
                        " ,1614887565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887625,SIGNIN_FAILURE",
                        "80.238.9.179,1614887625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887685,SIGNIN_SUCCESS,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith"), 0),
                Arguments.of("empty log", List.of(), 0));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("logs")
    void detectsSuspiciousIps(String scenario, List<String> lines, int expectedSuspiciousIps) {
        Set<String> suspicious = new HashSet<>();
        for (String line : lines) {
            String ip = detector.parseLine(line);
            if (ip != null) {
                suspicious.add(ip);
            }
        }
        assertThat(suspicious).hasSize(expectedSuspiciousIps);
    }

    @Test
    void theWindowIncludesExactlyFiveMinutes() {
        long t = 1_614_887_565L;
        for (long offset : new long[] {0, 60, 120, 180}) {
            assertThat(detector.parseLine(failure("10.0.0.1", t + offset))).isNull();
        }
        assertThat(detector.parseLine(failure("10.0.0.1", t + 300))).isEqualTo("10.0.0.1");
    }

    @Test
    void aFailureJustOutsideTheWindowIsForgotten() {
        long t = 1_614_887_565L;
        for (long offset : new long[] {0, 60, 120, 180}) {
            detector.parseLine(failure("10.0.0.1", t + offset));
        }
        assertThat(detector.parseLine(failure("10.0.0.1", t + 301))).isNull();
    }

    @Test
    void keepsFlaggingTheIpWhileTheAttackContinues() {
        long t = 1_614_887_565L;
        List<String> results = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            results.add(detector.parseLine(failure("10.0.0.1", t + i * 10)));
        }
        assertThat(results).containsExactly(null, null, null, null, "10.0.0.1", "10.0.0.1", "10.0.0.1");
    }

    @Test
    void forgetsInactiveIpsSoMemoryStaysBounded() {
        long t = 1_614_887_565L;
        detector.parseLine(failure("10.0.0.1", t));
        detector.parseLine(failure("10.0.0.2", t + 1_000));

        detector.purgeInactiveIps();

        assertThat(detector.trackedIps()).isEqualTo(1);
    }

    @Test
    void isSafeUnderConcurrentLoad() throws InterruptedException {
        long t = 1_614_887_565L;
        Set<String> suspicious = ConcurrentHashMap.newKeySet();
        try (ExecutorService pool = Executors.newFixedThreadPool(16)) {
            // 200 attacker IPs, each sending 5 failures from different threads at once.
            for (int ip = 0; ip < 200; ip++) {
                for (int attempt = 0; attempt < 5; attempt++) {
                    String line = failure("10.0.0." + ip, t + attempt);
                    pool.submit(() -> suspicious.add(Objects.requireNonNullElse(detector.parseLine(line), "")));
                }
            }
        }
        suspicious.remove("");
        assertThat(suspicious).hasSize(200);
    }

    private static String failure(String ip, long epochSecond) {
        return ip + "," + epochSecond + ",SIGNIN_FAILURE,Will.Smith";
    }
}
