package com.example.ivf.service.integration;

import com.example.ivf.domain.TreatmentCycle;

public interface CycleIntegrationPublisher {

    void publishCycleSnapshot(TreatmentCycle cycle);

    void notifyStatusChange(TreatmentCycle cycle);
}
