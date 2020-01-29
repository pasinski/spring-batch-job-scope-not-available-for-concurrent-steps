package com.example.jobscopenotworkingsimpleasyncexecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
public class JobscopenotworkingsimpleasyncexecutorApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobscopenotworkingsimpleasyncexecutorApplication.class, args);
	}

}
