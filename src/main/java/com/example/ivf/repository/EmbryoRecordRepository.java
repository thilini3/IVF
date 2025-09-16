package com.example.ivf.repository;

import com.example.ivf.domain.EmbryoRecord;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmbryoRecordRepository extends JpaRepository<EmbryoRecord, UUID> {

    List<EmbryoRecord> findByCycle_Id(UUID cycleId);
}
