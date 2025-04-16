package tn.esprit.microservice.reclamation_service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.awt.Color;

@Service
public class PdfService {

    public byte[] generateAllReclamationsPdf(List<Reclamation> reclamations) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4.rotate()); // landscape for wide table
        PdfWriter.getInstance(document, out);
        document.open();

        // Title
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Liste des Réclamations", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));

        // Table setup
        PdfPTable table = new PdfPTable(7); // 7 columns
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[] { 1.2f, 4f, 2f, 3f, 2f, 2f, 2.5f });

        // Header style
        Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        String[] headers = { "ID", "Description", "Order ID", "Email Client", "Statut", "Type", "Date" };

        for (String h : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(h, headFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(headerCell);
        }

        // Data rows
        Font dataFont = new Font(Font.HELVETICA, 10);
        for (Reclamation r : reclamations) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(r.getId()), dataFont)));
            table.addCell(new PdfPCell(new Phrase(r.getDescription(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(r.getOrderId()), dataFont)));
            table.addCell(new PdfPCell(new Phrase(r.getEmailClient(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(r.getStatut()), dataFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(r.getType()), dataFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(r.getDateReclamation()), dataFont)));
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }
}
