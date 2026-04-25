package com.Gestao_de_Contas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // ← sem isso o @Scheduled não funciona
@EnableJpaRepositories
public class GestaoDeContasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoDeContasApplication.class, args);
	}

}
