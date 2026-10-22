package com.carmgn.hackdetector.detector;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Detection rule: {@code attempts} failed logins from the same IP within {@code window}.
 */
@Validated
@ConfigurationProperties(prefix = "hackerdetector")
public record HackerDetectorProperties(
        @Min(1) int attempts,
        @NotNull Duration window) {
}
