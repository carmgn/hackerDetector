package com.carmgn.hackdetector.web;

import com.carmgn.hackdetector.detector.HackerDetector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Carmelo Garcia
 */
@RestController
@RequestMapping("/hackerDetector")
public class HackerDetectorController {

    private final HackerDetector hackerDetector;

    public HackerDetectorController(HackerDetector hackerDetector) {
        this.hackerDetector = hackerDetector;
    }

    /**
     * Receives one log line as the request body.
     *
     * @return 200 with the suspicious IP, or 204 if the activity looks normal
     */
    @PostMapping("/parseLog")
    public ResponseEntity<String> parseLog(@RequestBody String line) {
        String suspiciousIp = hackerDetector.parseLine(line);
        return suspiciousIp == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(suspiciousIp);
    }
}
