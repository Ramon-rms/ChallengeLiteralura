package com.literaluraChallenge;

import com.literaluraChallenge.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//CREADO POR J.R.R.L GRACIAS A ALURALATAM Y ORACLE ONE
//UTILIZANDO CURSOS DE ALURALATAM Y RECURSOS DE INTERNET.

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private Principal principal;

    public LiteraluraApplication(Principal principal) {
        this.principal = principal;
    }

    public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.ejecutar();
	}
}
