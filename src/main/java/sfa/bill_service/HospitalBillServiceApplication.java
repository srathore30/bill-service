package sfa.bill_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
public class HospitalBillServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(HospitalBillServiceApplication.class, args);
	}

}
