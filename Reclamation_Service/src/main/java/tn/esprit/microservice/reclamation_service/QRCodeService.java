package tn.esprit.microservice.reclamation_service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;

@Service
public class QRCodeService {

    private static final String QR_CODE_PATH = "src/main/resources/static/reclamation-qr/";

    private final ReclamationService reclamationService;

    public QRCodeService(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }

    public String generateQRCodeForReclamation(int reclamationId) throws IOException {
        Reclamation reclamation = reclamationService.getReclamationById(reclamationId)
                .orElseThrow(() -> new IllegalArgumentException("Reclamation not found"));

        String qrContent = "Reclamation ID: " + reclamation.getId() +
                "\nDescription: " + reclamation.getDescription() +
                "\nOrder ID: " + reclamation.getOrderId() +
                "\nDate: " + reclamation.getDateReclamation() +
                "\nType: " + reclamation.getType() +
                "\nStatus: " + reclamation.getStatut() +
                "\nClient Email: " + reclamation.getEmailClient();

        String fileName = reclamationId + ".png";
        Path path = Paths.get(QR_CODE_PATH, fileName);
        File file = path.toFile();

        if (file.exists()) {
            file.delete(); // Replace if already exists
        }

        try {
            Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.MARGIN, 1);

            BitMatrix matrix = new MultiFormatWriter()
                    .encode(qrContent, BarcodeFormat.QR_CODE, 200, 200, hintMap);
            MatrixToImageWriter.writeToFile(matrix, "PNG", file);

            return "http://localhost:8083/reclamation-qr/" + fileName;
        } catch (Exception e) {
            throw new IOException("QR Code generation failed", e);
        }
    }
}

