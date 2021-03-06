package com.carmgn.hackdetector.impl;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Singleton;

import org.springframework.stereotype.Component;

import com.carmgn.hackdetector.model.LogLine;
import com.carmgn.hackdetector.repository.HackerDetector;

@Singleton
@Component
public class HackerDetectorImpl implements HackerDetector {

    private Map<String, LinkedList<LogLine>> attemps = new ConcurrentHashMap<>();

    //80.238.9.179,133612947,SIGNIN_SUCCESS,Will.Smith
    @Override
    public String parseLine(String line) {
        if(line == null || line.trim().isEmpty() || line.split(",").length != 4){
            return null;
        }
        LogLine logLine = new LogLine(line);
        //logFileRepository.save(logLine);
        String suspiciusIp = null;
        if (LogLine.Action.SIGNIN_FAILURE == logLine.getAction()) {
            LinkedList<LogLine> attempsList = attemps.get(logLine.getIp());
            if (attempsList == null) {
                attempsList = new LinkedList<>();
            } else if (attempsList.size() == 5){
                attempsList.removeFirst();
            }
            attempsList.add(logLine);
            if(checkAttemps(attempsList)){
                suspiciusIp = logLine.getIp();
            }
            attemps.put(logLine.getIp(), attempsList);
        }
        return suspiciusIp;
    }

    private boolean checkAttemps(LinkedList<LogLine> attempsList) {
        if(attempsList == null || attempsList.size() != 5){
            return false;
        }
        long first = attempsList.getFirst().getLogDate();
        long last = attempsList.getLast().getLogDate();
        boolean result = false;
        if(first != 0 && last != 0){
            long diffSec = last - first;
            long min = diffSec / 60;
            result = min <= 5;
        }
        return result;
    }

}
