package com.carmgn.hackdetector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.carmgn.hackdetector.impl.HackerDetectorImpl;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class HackerDetectorTest {

    private HackerDetectorImpl hackerDetector;

    @DataProvider
    public static Object[][] dataProviderHackerDetectorLog() {
        return new Object[][] {
                {new ArrayList<>(Arrays.asList(
                        "80.238.9.179,1614881565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614882565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614883625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614884685,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614885745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614886750,SIGNIN_FAILURE,Will.Smith")),0},
                {new ArrayList<>(Arrays.asList(
                        "80.238.9.179,1614887565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887685,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith")),1},
                {new ArrayList<>(Arrays.asList(
                        "80.238.9.179,1614887565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887566,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887566,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887567,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887671,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887677,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887679,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887680,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887680,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887688,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887699,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887700,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887745,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith")),2},
                {new ArrayList<>(Arrays.asList(
                        "80.238.9.179,1614887565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887566,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887566,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887567,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887671,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887677,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887679,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887680,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887680,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887688,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887699,SIGNIN_FAILURE,Henry.Cavill",
                        "80.238.9.179,1614887700,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.177,1614887745,SIGNIN_SUCCESS,Henry.Cavill",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith")),1},
                {new ArrayList<>(Arrays.asList(
                        "80.238.9.179,1614887565,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887685,SIGNIN_SUCCESS,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith")),0},
                {new ArrayList<>(Arrays.asList(
                        "80.238.9.179, ,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887685,SIGNIN_SUCCESS,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith")),0},
                {new ArrayList<>(Arrays.asList(
                    "80.238.9.179,1614887565, ,Will.Smith",
                    "80.238.9.179,1614887625,SIGNIN_FAILURE,Will.Smith",
                    "80.238.9.179,1614887685,SIGNIN_SUCCESS,Will.Smith",
                    "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                    "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith")),0},
                {new ArrayList<>(Arrays.asList(
                        " ,1614887565,SIGNIN_FAILURE,Will.Smith2",
                        "80.238.9.179,1614887625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887685,SIGNIN_SUCCESS,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith")),0},
                {new ArrayList<>(Arrays.asList(
                        " ",
                        "80.238.9.179,1614887625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887685,SIGNIN_SUCCESS,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith")),0},
                {new ArrayList<>(Arrays.asList(
                        null,
                        "80.238.9.179,1614887625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887685,SIGNIN_SUCCESS,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith")),0},
                {new ArrayList<>(Arrays.asList(
                        "80.238.9.179,aaaaaaaaaa,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887685,SIGNIN_SUCCESS,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith")),0},
                {new ArrayList<>(Arrays.asList(
                        "80.238.9.179,1614887625,SIGNIN_FAILURES,Will.Smith",
                        "80.238.9.179,1614887625,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887685,SIGNIN_SUCCESS,Will.Smith",
                        "80.238.9.179,1614887745,SIGNIN_FAILURE,Will.Smith",
                        "80.238.9.179,1614887750,SIGNIN_FAILURE,Will.Smith")),0},
                {new ArrayList<>(),0},
                {null,0}
        };
    }

    @Test
    @UseDataProvider("dataProviderHackerDetectorLog")
    public void testDetect(ArrayList<String> lines, int hack) {
        hackerDetector = new HackerDetectorImpl();
        HashSet<String> ipHackList = new HashSet<>();
        if(lines != null && !lines.isEmpty()) {
            for (String line : lines) {
                String ipHack = hackerDetector.parseLine(line);
                if(ipHack != null){
                    ipHackList.add(ipHack);
                }
            }
            Assert.assertEquals(ipHackList.size(), hack);
        }
    }
}
