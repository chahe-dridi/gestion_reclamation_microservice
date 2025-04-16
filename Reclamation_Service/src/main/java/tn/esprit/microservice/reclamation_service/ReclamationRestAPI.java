package tn.esprit.microservice.reclamation_service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/reclamations")
public class ReclamationRestAPI {


    /*  public ReclamationRestAPI(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }*/
    /* public ReclamationRestAPI(ReclamationService reclamationService, QRCodeService qrCodeService) {
        this.reclamationService = reclamationService;
        this.qrCodeService = qrCodeService;
    }*/
    private final ReclamationService reclamationService;

    private final QRCodeService qrCodeService;

    private final PdfService pdfService;

    public ReclamationRestAPI(ReclamationService reclamationService, QRCodeService qrCodeService, PdfService pdfService) {
        this.reclamationService = reclamationService;
        this.qrCodeService = qrCodeService;
        this.pdfService = pdfService;
    }

    // Endpoint to create a reclamation
    // http://localhost:8083/reclamations
    @PostMapping
    public ResponseEntity<Reclamation> createReclamation(@RequestBody Reclamation reclamation) {
        return ResponseEntity.ok(reclamationService.addReclamation(reclamation));
    }

    // Endpoint to update a reclamation
    // http://localhost:8083/reclamations/1
    @PutMapping("/{id}")
    public ResponseEntity<Reclamation> updateReclamation(@PathVariable int id, @RequestBody Reclamation reclamation) {
        return ResponseEntity.ok(reclamationService.updateReclamation(id, reclamation));
    }

    // Endpoint to delete a reclamation
    // http://localhost:8083/reclamations/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable int id) {
        reclamationService.deleteReclamation(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to get all reclamations
    // http://localhost:8093/reclamations
    @GetMapping
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }

    // Optional: endpoint to get reclamations by order ID
    // http://localhost:8093/reclamations/1
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Reclamation>> getReclamationById(@PathVariable int id) {
        return ResponseEntity.ok(reclamationService.getReclamationById(id));
    }

    // Optional: simple hello endpoint
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Reclamation Microservice!";
    }

    // Optional: endpoint to get reclamations by type
    // http://localhost:8093/reclamations/filter?type=ERREUR_COMMANDE
    @GetMapping("/filter")
    public ResponseEntity<List<Reclamation>> getReclamationsByType(@RequestParam TypeReclamation type) {
        return ResponseEntity.ok(reclamationService.getReclamationsByType(type));
    }

    // Optional: endpoint to get reclamation statistics
    // http://localhost:8093/reclamations/stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getReclamationStats() {
        return ResponseEntity.ok(reclamationService.getReclamationStats());
    }

    // Optional: endpoint to get monthly statistics
        // http://localhost:8093/reclamations/stats/monthly
    @GetMapping("/stats/monthly")
    public ResponseEntity<Map<String, Long>> getMonthlyStats() {
        return ResponseEntity.ok(reclamationService.getMonthlyReclamationStats());
    }


    // Endpoint to generate QR code for a
    // http://localhost:8083/reclamations/1/qr
    @GetMapping("/{id}/qr")
    public ResponseEntity<String> generateQRCode(@PathVariable int id) {
        try {
            String url = qrCodeService.generateQRCodeForReclamation(id);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la génération du QR Code: " + e.getMessage());
        }
    }
    // Endpoint to serve QR code image
    // http://localhost:8093/reclamations/qr/1.png
    @GetMapping("/qr/{filename:.+}")
    public ResponseEntity<ByteArrayResource> serveQrCodeImage(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("src/main/resources/static/reclamation-qr/", filename);
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(filePath);
        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(imageBytes.length)
                .body(resource);
    }






    // Endpoint to download all reclamations as a PDF
    // http://localhost:8083/reclamations/pdf

    @GetMapping("/pdf")
    public ResponseEntity<ByteArrayResource> downloadAllReclamationsPdf() {
        try {
            List<Reclamation> reclamations = reclamationService.getAllReclamations();
            if (reclamations.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            byte[] pdfBytes = pdfService.generateAllReclamationsPdf(reclamations);
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all_reclamations.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

   /*
    // recieve an email when adding a new reclamation
    // http://localhost:8083/reclamations
   {

        "dateReclamation": "2025-04-04T08:06:07.188+00:00",
            "description": "Produit cassé à la livraison",
            "orderId": 1,
            "statut": "En attente",
            "type": "PRODUIT_ENDOMMAGE",
            "emailClient": "chaher.dridi.100@gmail.com"
    }
    */




}
