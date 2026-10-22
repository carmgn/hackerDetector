package com.carmgn.hackdetector.time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TimeCalculationTest {

    @ParameterizedTest(name = "{0} → {1} = {2} min")
    @CsvSource(delimiter = '|', nullValues = "NULL", textBlock = """
            Thu, 18 Feb 2021 16:01:07 -0500 | Thu, 18 Feb 2021 22:06:07 +0100 | 5
            Thu, 18 Feb 2021 16:05:07 -0500 | Thu, 18 Feb 2021 22:05:07 +0100 | 0
            Thu, 18 Feb 2021 22:06:07 +0100 | Thu, 18 Feb 2021 16:01:07 -0500 | 5
            Thu, 21 Dec 2000 16:01:07 +0200 | Thu, 21 Dec 2000 16:03:06 +0200 | 1
            Thu, 21 Dec 2000 23:59:00 +0000 | Fri, 22 Dec 2000 00:01:00 +0000 | 2
            ''                              | ''                              | 0
            NULL                            | NULL                            | 0
            NULL                            | Thu, 18 Feb 2021 22:05:07 +0100 | 0
            """)
    void minutesBetweenRoundsDownAcrossTimeZones(String first, String second, long expected) {
        assertThat(TimeCalculation.minutesBetween(first, second)).isEqualTo(expected);
    }

    @Test
    void rejectsTimestampsThatAreNotRfc2822() {
        assertThatThrownBy(() -> TimeCalculation.minutesBetween("2021-02-18T16:01:07Z", "Thu, 18 Feb 2021 22:06:07 +0100"))
                .isInstanceOf(DateTimeParseException.class);
    }
}
