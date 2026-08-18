package com.entic.payroll.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.entic.payroll.persistence.repository")
@EntityScan(basePackages = "com.entic.payroll.persistence.entity")
public class JpaConfig {

    private static final Logger log = LoggerFactory.getLogger(JpaConfig.class);

    public JpaConfig() {
        log.info("JPA auditing enabled");
        log.info("JPA repositories enabled for package: com.entic.payroll.persistence.repository");
        log.info("JPA entity scan enabled for package: com.entic.payroll.persistence.entity");
    }
}
