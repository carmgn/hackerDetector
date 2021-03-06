package com.carmgn.hackdetector.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carmgn.hackdetector.impl.HackerDetectorImpl;


/**
 *
 * @author Carmelo Garcia
 */
@RestController
@RequestMapping("/hackerDetector")
public class HackerDetectorController {

    @Autowired
    HackerDetectorImpl hackerDetector;
    /**
     * @return ip Hack
     */
    @PostMapping("/parseLog")
    public String getPcCompOfertas(@Valid @RequestBody String line) {
        return hackerDetector.parseLine(line);
    }

}
