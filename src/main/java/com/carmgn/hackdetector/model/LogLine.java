package com.carmgn.hackdetector.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class LogLine {

    private static long TIME_MILLIS = 1000;
    public enum Action {
        SIGNIN_SUCCESS,
        SIGNIN_FAILURE
    }

    private String ip;

    private long logDate;

    private Action action = Action.SIGNIN_SUCCESS;

    private String userName;


    public LogLine(String line) {
        String[] lineSplited = line.split(",");
        if (lineSplited.length > 0) {
            this.ip = lineSplited[0];
        }
        if (lineSplited.length > 1) {
            this.logDate = parseDate(lineSplited[1]);
        }
        if (lineSplited.length > 2) {
            this.action = parseAction(lineSplited[2]);
        }
        if (lineSplited.length > 3) {
            this.userName = lineSplited[3];
        }
    }

    private Action parseAction(String action) {
        if (action == null || action.trim().isEmpty()){
            return null;
        }
        try {
            return Action.valueOf(action);
        } catch (IllegalArgumentException iae){
            log.error("Error parsing action", iae);
        }
        return null;

    }

    private long parseDate(String epochDate) {
        if (epochDate == null || epochDate.trim().isEmpty()){
            return 0;
        }
        long dateEpoch = 0;
        try {
            dateEpoch = Long.parseLong(epochDate);
        } catch (NumberFormatException nfe){
           log.error("Error parsing epoch Date", nfe);
        }
        return dateEpoch;
    }

}
