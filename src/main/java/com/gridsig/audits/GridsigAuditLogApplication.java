package com.gridsig.audits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class GridsigAuditLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(GridsigAuditLogApplication.class, args);
	}

}
