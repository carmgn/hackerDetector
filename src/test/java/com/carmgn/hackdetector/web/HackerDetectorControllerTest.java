package com.carmgn.hackdetector.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HackerDetectorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void returnsNoContentUntilTheIpBecomesSuspiciousAndThenTheIp() throws Exception {
        for (int i = 0; i < 4; i++) {
            send("80.238.9.176," + (1615026083 + i * 30) + ",SIGNIN_FAILURE,Will.Smith")
                    .andExpect(status().isNoContent());
        }
        send("80.238.9.176,1615026263,SIGNIN_FAILURE,Will.Smith")
                .andExpect(status().isOk())
                .andExpect(content().string("80.238.9.176"));
    }

    @Test
    void ignoresMalformedLines() throws Exception {
        send("not a log line").andExpect(status().isNoContent());
    }

    private org.springframework.test.web.servlet.ResultActions send(String line) throws Exception {
        return mvc.perform(post("/hackerDetector/parseLog").contentType(MediaType.TEXT_PLAIN).content(line));
    }
}
