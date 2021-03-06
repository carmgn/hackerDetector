package com.carmgn.hackdetector.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Entity
@Table(name = "logLine")
@EntityListeners(AuditingEntityListener.class)
public class LogLine {

    private static long TIME_MILLIS = 1000;
    public enum Action {
        SIGNIN_SUCCESS,
        SIGNIN_FAILURE
    }

    private String ip;

    private long logDate;

    private Action action;

    private String userName;


    public LogLine(String line) {
        if (line == null || line.trim().isEmpty() || line.split(",").length != 4){
            return;
        }
        String[] lineSplited = line.split(",");
        this.ip = lineSplited[0];
        this.logDate = parseDate(lineSplited[1]);
        this.action = parseAction(lineSplited[2]);
        this.userName = lineSplited[3];
    }

    public LogLine() {
    }

    @Id
    String getId() {
        return ip;
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

    void setId(String id) {
        this.ip = id;
    }
}
