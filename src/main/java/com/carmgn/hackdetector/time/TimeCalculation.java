package com.carmgn.hackdetector.time;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Minutes between two RFC 2822 timestamps, e.g. {@code Thu, 21 Dec 2000 16:01:07 +0200}.
 */
public final class TimeCalculation {

    private TimeCalculation() {
    }

    /**
     * Returns the whole minutes (rounded down) between both timestamps, whatever their
     * time zones and order. Blank or {@code null} input returns 0.
     *
     * @throws java.time.format.DateTimeParseException if a timestamp is not RFC 2822
     */
    public static long minutesBetween(String first, String second) {
        if (first == null || first.isBlank() || second == null || second.isBlank()) {
            return 0;
        }
        ZonedDateTime from = ZonedDateTime.parse(first.trim(), DateTimeFormatter.RFC_1123_DATE_TIME);
        ZonedDateTime to = ZonedDateTime.parse(second.trim(), DateTimeFormatter.RFC_1123_DATE_TIME);
        return Duration.between(from, to).abs().toMinutes();
    }
}
