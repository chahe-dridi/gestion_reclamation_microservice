package tn.esprit.microservice.reclamation_service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.internet.MimeMessage;

@Service
public class   ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final JavaMailSender mailSender;

   /* public ReclamationService(ReclamationRepository reclamationRepository) {
        this.reclamationRepository = reclamationRepository;
    }*/
   public ReclamationService(ReclamationRepository reclamationRepository, JavaMailSender mailSender) {
       this.reclamationRepository = reclamationRepository;
       this.mailSender = mailSender;
   }

    /*public Reclamation addReclamation(Reclamation reclamation) {
        // Default values if not set
        if (reclamation.getStatut() == null) {
            reclamation.setStatut("En attente");
        }
        if (reclamation.getDateReclamation() == null) {
            reclamation.setDateReclamation(new java.util.Date());
        }
        return reclamationRepository.save(reclamation);
    }*/

    // Add reclamation with default values
    // http://localhost:8083/reclamations

    public Reclamation addReclamation(Reclamation reclamation) {
        if (reclamation.getStatut() == null) {
            reclamation.setStatut("En attente");
        }
        if (reclamation.getDateReclamation() == null) {
            reclamation.setDateReclamation(new java.util.Date());
        }

        Reclamation saved = reclamationRepository.save(reclamation);
        sendReclamationEmail(saved); // 📧 Send email after saving
        return saved;
    }

    // Update reclamation
    public Reclamation updateReclamation(int id, Reclamation updatedReclamation) {
        return reclamationRepository.findById(id).map(existing -> {
            existing.setDescription(updatedReclamation.getDescription());
            existing.setOrderId(updatedReclamation.getOrderId());
            existing.setType(updatedReclamation.getType());
            existing.setEmailClient(updatedReclamation.getEmailClient());
            existing.setStatut(updatedReclamation.getStatut());
            return reclamationRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Reclamation non trouvée avec ID: " + id));
    }

    // Delete reclamation
    public void deleteReclamation(int id) {
        if (!reclamationRepository.existsById(id)) {
            throw new RuntimeException("Reclamation non trouvée avec ID: " + id);
        }
        reclamationRepository.deleteById(id);
    }

    // Get all reclamations
    // http://localhost:8083/reclamations
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }


    public Optional<Reclamation> getReclamationById(int id) {
        return reclamationRepository.findById(id);
    }



    // filter reclamations by type
    // http://localhost:8083/reclamations/filter?type=PRODUIT_ENDOMMAGE
    public List<Reclamation> getReclamationsByType(TypeReclamation type) {
        return reclamationRepository.findByType(type);
    }


    // Get reclamation statistics
    //  http://localhost:8083/reclamations/stats
    public Map<String, Object> getReclamationStats() {
        List<Reclamation> all = reclamationRepository.findAll();

        long total = all.size();

        Map<String, Long> byType = all.stream()
                .collect(Collectors.groupingBy(r -> r.getType().toString(), Collectors.counting()));

        Map<String, Long> byStatus = all.stream()
                .collect(Collectors.groupingBy(Reclamation::getStatut, Collectors.counting()));

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("byType", byType);
        stats.put("byStatus", byStatus);

        return stats;
    }

    // Get monthly reclamation statistics
    // http://localhost:8083/reclamations/stats/monthly
    public Map<String, Long> getMonthlyReclamationStats() {
        List<Reclamation> all = reclamationRepository.findAll();

        return all.stream()
                .collect(Collectors.groupingBy(
                        r -> {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM");
                            return sdf.format(r.getDateReclamation());
                        },
                        Collectors.counting()
                ));
    }




    // Send email to client after reclamation is added
    // http://localhost:8083/reclamations
    private void sendReclamationEmail(Reclamation reclamation) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(reclamation.getEmailClient());
            helper.setSubject("Confirmation de votre réclamation");
            helper.setText(
                    "<h3>Bonjour,</h3>" +
                            "<p>Nous avons bien reçu votre réclamation concernant: <strong>" + reclamation.getType() + "</strong>.</p>" +
                            "<p>ID de la réclamation: " + reclamation.getId() + "</p>" +
                            "<p>Description: " + reclamation.getDescription() + "</p>" +
                            "<p>Date de la réclamation: " + reclamation.getDateReclamation() + "</p>" +
                            "<p>ID de la commande: " + reclamation.getOrderId() + "</p>" +
                            "<p>Type de réclamation: " + reclamation.getType() + "</p>" +
                            "<p>Statut actuel: " + reclamation.getStatut() + "</p>" +
                            "<br><p>Merci pour votre retour.</p><p>Service Client</p>",
                    true // enable HTML
            );

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }


    }



}
