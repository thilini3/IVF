package com.example.ivf.repository;

import com.example.ivf.domain.LaboratoryProcedure;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratoryProcedureRepository extends JpaRepository<LaboratoryProcedure, UUID> {
}
