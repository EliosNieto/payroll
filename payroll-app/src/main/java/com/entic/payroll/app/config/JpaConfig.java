package com.entic.payroll.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    private static final Logger log = LoggerFactory.getLogger(JpaConfig.class);

    public JpaConfig() {
        log.info("JPA auditing enabled");
    }
}
