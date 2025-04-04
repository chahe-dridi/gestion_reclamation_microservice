package tn.esprit.microservice.reclamation_service;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class ReclamationServiceApplication {

    private final ReclamationRepository reclamationRepository;

    public ReclamationServiceApplication(ReclamationRepository reclamationRepository) {
        this.reclamationRepository = reclamationRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReclamationServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner init() {
        return args -> {
            if (reclamationRepository.count() == 0) {
                reclamationRepository.saveAll(List.of(
                        new Reclamation(new Date(), "Produit cassé à la livraison", 1, TypeReclamation.PRODUIT_ENDOMMAGE, "client1@example.com"),
                        new Reclamation(new Date(), "Livraison en retard de 5 jours", 2, TypeReclamation.RETARD_LIVRAISON, "client2@example.com"),
                        new Reclamation(new Date(), "Produit différent de la commande", 3, TypeReclamation.ERREUR_COMMANDE, "client3@example.com")
                ));
            }

            reclamationRepository.findAll().forEach(System.out::println);
        };
    }
}
