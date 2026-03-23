package com.Gestao_de_Contas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // ← sem isso o @Scheduled não funciona
public class GestaoDeContasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoDeContasApplication.class, args);
	}

}
