package com.carmgn.hackdetector.detector;

import java.util.Optional;

/**
 * One parsed log line: {@code 80.238.9.179,1336129471,SIGNIN_FAILURE,Will.Smith}.
 */
public record LogLine(String ip, long epochSecond, Action action, String username) {

    public enum Action { SIGNIN_SUCCESS, SIGNIN_FAILURE }

    /**
     * Parses a raw line. Malformed lines (missing fields, blank IP, non-numeric date or
     * unknown action) are ignored rather than failing the whole stream.
     */
    public static Optional<LogLine> parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return Optional.empty();
        }
        String[] fields = raw.split(",", -1);
        if (fields.length != 4) {
            return Optional.empty();
        }
        String ip = fields[0].trim();
        if (ip.isEmpty()) {
            return Optional.empty();
        }
        try {
            long epochSecond = Long.parseLong(fields[1].trim());
            Action action = Action.valueOf(fields[2].trim());
            return Optional.of(new LogLine(ip, epochSecond, action, fields[3].trim()));
        } catch (IllegalArgumentException e) {
            // NumberFormatException is an IllegalArgumentException too.
            return Optional.empty();
        }
    }
}
