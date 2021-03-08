package com.carmgn.hackdetector.utils;

import java.util.LinkedList;

import com.carmgn.hackdetector.cache.ExpiringCache;
import com.carmgn.hackdetector.model.LogLine;

public class LogUtils {

    public static String checkFailedAttemp(LogLine logLine, String suspiciusIp, ExpiringCache<String, LinkedList<Long>> expiringCache, Integer attemps) {
        LinkedList<Long> attempsList = expiringCache.get(logLine.getIp());
        if (attempsList == null) {
            attempsList = new LinkedList<>();
        }

        if (attempsList.size() == attemps){
            attempsList.removeFirst();
        }

        attempsList.add(logLine.getLogDate());
        if(checkAttemps(attempsList, attemps)){
            suspiciusIp = logLine.getIp();
        }
        expiringCache.put(logLine.getIp(), attempsList);
        return suspiciusIp;
    }

    private static boolean checkAttemps(LinkedList<Long> attempsList, Integer attemps) {
        if(attempsList == null || attempsList.size() != attemps){
            return false;
        }
        long first = attempsList.getFirst();
        long last = attempsList.getLast();
        boolean result = false;
        if (first != 0 && last != 0){
            long diffSec = last - first;
            long min = diffSec / 60;
            result = min <= 5;
        }
        return result;
    }
}
