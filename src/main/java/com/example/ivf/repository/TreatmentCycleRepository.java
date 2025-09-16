package com.example.ivf.repository;

import com.example.ivf.domain.TreatmentCycle;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentCycleRepository extends JpaRepository<TreatmentCycle, UUID> {

    Optional<TreatmentCycle> findByCycleNumber(String cycleNumber);
}
