package tn.esprit.microservice.reclamation_service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class   ReclamationService {

    private final ReclamationRepository reclamationRepository;

    public ReclamationService(ReclamationRepository reclamationRepository) {
        this.reclamationRepository = reclamationRepository;
    }

    public Reclamation addReclamation(Reclamation reclamation) {
        // Default values if not set
        if (reclamation.getStatut() == null) {
            reclamation.setStatut("En attente");
        }
        if (reclamation.getDateReclamation() == null) {
            reclamation.setDateReclamation(new java.util.Date());
        }
        return reclamationRepository.save(reclamation);
    }

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

    public void deleteReclamation(int id) {
        if (!reclamationRepository.existsById(id)) {
            throw new RuntimeException("Reclamation non trouvée avec ID: " + id);
        }
        reclamationRepository.deleteById(id);
    }

    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    public Optional<Reclamation> getReclamationById(int id) {
        return reclamationRepository.findById(id);
    }
}
