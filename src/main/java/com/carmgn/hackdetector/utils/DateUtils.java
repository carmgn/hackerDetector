package com.carmgn.hackdetector.utils;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {

    private static String RFC_PATTERN = "EEE, dd MMM yyyy HH:mm:ss Z";

    private static long minutes = 60;

    public static long minutesBetween(String first, String second){
        if(first == null || first.trim().isEmpty() || second == null || second.trim().isEmpty()){
            return 0;
        }
        ZonedDateTime ldtFirst = ZonedDateTime.parse(first, DateTimeFormatter.ofPattern(RFC_PATTERN));
        ZonedDateTime ldtSecond = ZonedDateTime.parse(second, DateTimeFormatter.ofPattern(RFC_PATTERN));
        Duration duration2 = Duration.between(ldtFirst, ldtSecond);
        return duration2.getSeconds()/minutes;
    }
}
