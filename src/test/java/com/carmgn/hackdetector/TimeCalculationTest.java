package com.carmgn.hackdetector;


import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.carmgn.hackdetector.utils.DateUtils;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class TimeCalculationTest {

    @DataProvider
    public static Object[][] dataProviderTimeCalculation() {
        return new Object[][] {
                {"Thu, 18 Feb 2021 16:01:07 -0500", "Thu, 18 Feb 2021 22:06:07 +0100",5},
                {"Thu, 18 Feb 2021 16:05:07 -0500", "Thu, 18 Feb 2021 22:05:07 +0100",0},
                {"","",0},
                {" "," ",0},
                {null,null,0},
                {null,"Thu, 18 Feb 2021 22:05:07 +0100",0},
                {"Thu, 18 Feb 2021 22:05:07 +0100",null,0},
        };
    }

    @Test
    @UseDataProvider("dataProviderTimeCalculation")
    public void testDetect(String firstDate, String secondDate, long minutes) {
       long result = DateUtils.minutesBetween(firstDate, secondDate);
       Assert.assertEquals(result,minutes);
    }
}
