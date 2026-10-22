package com.carmgn.hackdetector.detector;

/**
 * Contract defined by the exercise: called once for every new log line.
 */
public interface HackerDetector {

    /**
     * @param line a log line in the format {@code ip,epochSeconds,action,username}
     * @return the IP address if the activity is suspicious, otherwise {@code null}
     */
    String parseLine(String line);
}
