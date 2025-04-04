package tn.esprit.microservice.reclamation_service;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Reclamation implements Serializable {

    @Serial
    private static final long serialVersionUID = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReclamation;

    @Column(length = 1000)
    private String description;

    private int orderId;

    private String statut; // Default: "En attente"

    @Enumerated(EnumType.STRING)
    private TypeReclamation type;

    private String emailClient;

    public Reclamation() {}

    public Reclamation(Date dateReclamation, String description, int orderId, TypeReclamation type, String emailClient) {
        this.dateReclamation = dateReclamation;
        this.description = description;
        this.orderId = orderId;
        this.type = type;
        this.emailClient = emailClient;
        this.statut = "En attente";
    }

    @PrePersist
    public void prePersist() {
        if (statut == null) {
            statut = "En attente";
        }
        if (dateReclamation == null) {
            dateReclamation = new Date();
        }
    }

    // Getters and Setters...
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateReclamation() {
        return dateReclamation;
    }

    public void setDateReclamation(Date dateReclamation) {
        this.dateReclamation = dateReclamation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public TypeReclamation getType() {
        return type;
    }

    public void setType(TypeReclamation type) {
        this.type = type;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }
}