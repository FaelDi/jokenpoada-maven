package tech.ada.games.jokenpo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Jokenpo StarGame", version = "1.0", description = "Um JOKENPO diferente, " +
		"onde você tem duas novas jogadas se unindo às tradicionais: Spock e Lagarto! " +
		"Divirtam-se e boa sorte!"))
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer"
)
public class JokenpoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JokenpoApplication.class, args);
	}

}
