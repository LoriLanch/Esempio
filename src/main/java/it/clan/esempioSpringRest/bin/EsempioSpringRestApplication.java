package it.clan.esempioSpringRest.bin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "it.clan.esempioSpringRest.rest")
public class EsempioSpringRestApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(EsempioSpringRestApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
