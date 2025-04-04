package tn.esprit.microservice.reclamation_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReclamationRepository  extends JpaRepository<Reclamation, Integer> {

}
