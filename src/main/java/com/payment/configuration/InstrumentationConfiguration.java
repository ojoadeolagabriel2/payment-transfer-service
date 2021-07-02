package com.payment.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InstrumentationConfiguration {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricConfig(@Value("${spring.application.name}") String applicationName) {
        log.debug("initializing MeterRegistryCustomizer with application name: {}", applicationName);
        return registry -> registry.config()
                .commonTags("application", applicationName);
    }
}
