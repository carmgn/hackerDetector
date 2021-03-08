package com.carmgn.hackdetector.configuration;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "hackerdetector")
@Validated
public class HackerConfiguration implements Serializable {

    @NotNull
    private Integer attemps;

}
