package com.example.ivf.service.integration;

import com.example.ivf.config.ExternalIntegrationProperties;
import com.example.ivf.domain.TreatmentCycle;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class LoggingCycleIntegrationPublisher implements CycleIntegrationPublisher {

    private static final Logger log = LoggerFactory.getLogger(LoggingCycleIntegrationPublisher.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final RestClient fhirRestClient;
    private final RestClient analyticsRestClient;

    public LoggingCycleIntegrationPublisher(ExternalIntegrationProperties properties) {
        this.fhirRestClient = createRestClient(properties.fhir());
        this.analyticsRestClient = createRestClient(properties.analytics());
    }

    private RestClient createRestClient(ExternalIntegrationProperties.Fhir fhir) {
        if (fhir == null || !fhir.enabled() || fhir.baseUrl() == null || fhir.baseUrl().isBlank()) {
            return null;
        }
        return RestClient.builder().baseUrl(fhir.baseUrl()).build();
    }

    private RestClient createRestClient(ExternalIntegrationProperties.Analytics analytics) {
        if (analytics == null || !analytics.enabled() || analytics.baseUrl() == null || analytics.baseUrl().isBlank()) {
            return null;
        }
        return RestClient.builder().baseUrl(analytics.baseUrl()).build();
    }

    @Override
    public void publishCycleSnapshot(TreatmentCycle cycle) {
        if (fhirRestClient == null) {
            log.debug("FHIR integration disabled - skipping cycle snapshot for {}", cycle.getCycleNumber());
            return;
        }
        Map<String, Object> payload = buildCarePlanPayload(cycle);
        try {
            fhirRestClient.post()
                .uri("/CarePlan")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
        } catch (Exception ex) {
            log.warn("Failed to publish cycle snapshot to FHIR endpoint: {}", ex.getMessage());
        }
    }

    @Override
    public void notifyStatusChange(TreatmentCycle cycle) {
        if (analyticsRestClient == null) {
            log.debug("Analytics integration disabled - skipping status notification for {}", cycle.getCycleNumber());
            return;
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("cycleNumber", cycle.getCycleNumber());
        payload.put("status", cycle.getStatus());
        payload.put("type", cycle.getType());
        payload.put("startDate", cycle.getStartDate() != null ? DATE_FORMAT.format(cycle.getStartDate()) : null);
        payload.put("endDate", cycle.getEndDate() != null ? DATE_FORMAT.format(cycle.getEndDate()) : null);
        try {
            analyticsRestClient.post()
                .uri("/ivf/cycles/status")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
        } catch (Exception ex) {
            log.warn("Failed to publish cycle status change: {}", ex.getMessage());
        }
    }

    private Map<String, Object> buildCarePlanPayload(TreatmentCycle cycle) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("resourceType", "CarePlan");
        payload.put("title", "IVF Treatment Cycle");
        payload.put("status", cycle.getStatus().name().toLowerCase());
        payload.put("intent", "plan");
        payload.put("description", "Cycle " + cycle.getCycleNumber());
        payload.put("subject", Map.of("reference", "Couple/" + cycle.getCoupleId()));
        payload.put("period", Map.of(
            "start", cycle.getStartDate() != null ? DATE_FORMAT.format(cycle.getStartDate()) : null,
            "end", cycle.getEndDate() != null ? DATE_FORMAT.format(cycle.getEndDate()) : null
        ));
        return payload;
    }
}
