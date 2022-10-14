package ma.miaad.bonsortieservice;

import ma.miaad.bonsortieservice.entities.BonSortie;
import ma.miaad.bonsortieservice.feign.BienMobilierRestClient;
import ma.miaad.bonsortieservice.model.BienMobilier;
import ma.miaad.bonsortieservice.repository.BonSortieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;
@EnableFeignClients
@SpringBootApplication
public class BonSortieServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BonSortieServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(BonSortieRepository bonSortieRepository, BienMobilierRestClient bienMobilierRestClient, RepositoryRestConfiguration restConfiguration){
		return args -> {
			restConfiguration.exposeIdsFor(BonSortie.class);

		};

	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
			}
		};
	}
}
