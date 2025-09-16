package com.example.ivf.repository;

import com.example.ivf.domain.QualityAlert;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualityAlertRepository extends JpaRepository<QualityAlert, UUID> {
}
