package com.carmgn.hackdetector.impl;

import java.util.LinkedList;

import javax.ejb.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.carmgn.hackdetector.cache.ExpiringCache;
import com.carmgn.hackdetector.configuration.HackerConfiguration;
import com.carmgn.hackdetector.model.LogLine;
import com.carmgn.hackdetector.repository.HackerDetector;
import com.carmgn.hackdetector.utils.LogUtils;

import lombok.Data;

@Component
@Data
@Singleton
public class HackerDetectorImpl implements HackerDetector {

    @Autowired
    private HackerConfiguration configuration;

    private final ExpiringCache<String, LinkedList<Long>> expiringCache = new ExpiringCache<>(60000L);

    @Override
    public String parseLine(String line) {
        if(line == null || line.trim().isEmpty()){
            return null;
        }
        LogLine logLine = new LogLine(line);
        String suspiciusIp = null;
        if (LogLine.Action.SIGNIN_FAILURE == logLine.getAction()) {
            suspiciusIp = LogUtils.checkFailedAttemp(logLine, suspiciusIp, expiringCache, configuration.getAttemps());
        }
        return suspiciusIp;
    }

}
