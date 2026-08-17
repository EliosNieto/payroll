package com.entic.payroll.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.entic.payroll")
public class PayrollApplication {

    private static final Logger log = LoggerFactory.getLogger(PayrollApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PayrollApplication.class, args);
        log.info("ENTIC Payroll application started");
    }
}
