package org.coteji.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.coteji.fs", "org.coteji.jira"})
public class CotejiConfig {
    @Bean
    Runner runner() {
        return new Runner();
    }
}
