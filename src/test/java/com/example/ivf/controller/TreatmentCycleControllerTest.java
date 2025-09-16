package com.example.ivf.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.ivf.domain.enums.CycleStatus;
import com.example.ivf.domain.enums.CycleType;
import com.example.ivf.domain.enums.EmbryoGrade;
import com.example.ivf.domain.enums.EmbryoStage;
import com.example.ivf.domain.enums.EmbryoStatus;
import com.example.ivf.domain.enums.ProcedureType;
import com.example.ivf.domain.enums.TimelineEventType;
import com.example.ivf.domain.enums.TimelineStatus;
import com.example.ivf.dto.CreateTreatmentCycleRequest;
import com.example.ivf.dto.CryopreservationRequest;
import com.example.ivf.dto.EmbryoObservationRequest;
import com.example.ivf.dto.ScheduleProcedureRequest;
import com.example.ivf.dto.TimelineEventRequest;
import com.example.ivf.dto.UpdateCycleStatusRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TreatmentCycleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldManageTreatmentCycleLifecycle() throws Exception {
        CreateTreatmentCycleRequest createRequest = new CreateTreatmentCycleRequest(
            CycleType.FRESH,
            "couple-001",
            "clinician-123",
            LocalDate.now(),
            Set.of("CONSENT_OPU")
        );

        String createResponse = mockMvc.perform(post("/api/cycles")
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("ivf-admin", "StrongPassword1!"))
                .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Map<String, Object> createdCycle = objectMapper.readValue(createResponse, new TypeReference<>() {});
        String cycleId = (String) createdCycle.get("id");
        assertThat(cycleId).isNotBlank();

        TimelineEventRequest eventRequest = new TimelineEventRequest(
            TimelineEventType.STIMULATION_START,
            "Begin stimulation",
            OffsetDateTime.now(ZoneOffset.UTC),
            TimelineStatus.SCHEDULED,
            null
        );

        mockMvc.perform(post("/api/cycles/" + cycleId + "/timeline-events")
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("ivf-admin", "StrongPassword1!"))
                .content(objectMapper.writeValueAsString(eventRequest)))
            .andExpect(status().isCreated());

        ScheduleProcedureRequest procedureRequest = new ScheduleProcedureRequest(
            ProcedureType.IVF,
            OffsetDateTime.now(ZoneOffset.UTC).plusDays(1),
            "embryologist-77",
            "witness-1",
            "Standard IVF",
            List.of()
        );

        String procedureResponse = mockMvc.perform(post("/api/cycles/" + cycleId + "/procedures")
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("ivf-admin", "StrongPassword1!"))
                .content(objectMapper.writeValueAsString(procedureRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Map<String, Object> procedure = objectMapper.readValue(procedureResponse, new TypeReference<>() {});
        String procedureId = (String) procedure.get("id");
        assertThat(procedureId).isNotBlank();

        EmbryoObservationRequest embryoObservationRequest = new EmbryoObservationRequest(
            1,
            EmbryoStage.ZYGOTE,
            EmbryoGrade.GOOD,
            EmbryoStatus.CULTURING,
            OffsetDateTime.now(ZoneOffset.UTC)
        );

        String embryoResponse = mockMvc.perform(post("/api/cycles/" + cycleId + "/embryos")
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("ivf-admin", "StrongPassword1!"))
                .content(objectMapper.writeValueAsString(embryoObservationRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Map<String, Object> embryo = objectMapper.readValue(embryoResponse, new TypeReference<>() {});
        String embryoId = (String) embryo.get("id");
        assertThat(embryoId).isNotBlank();

        CryopreservationRequest cryoRequest = new CryopreservationRequest(
            "Main Facility",
            "Tank-1",
            "Canister-2",
            "Position-3",
            OffsetDateTime.now(ZoneOffset.UTC)
        );

        mockMvc.perform(post("/api/cycles/" + cycleId + "/embryos/" + embryoId + "/cryopreservation")
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("ivf-admin", "StrongPassword1!"))
                .content(objectMapper.writeValueAsString(cryoRequest)))
            .andExpect(status().isOk());

        UpdateCycleStatusRequest statusRequest = new UpdateCycleStatusRequest(CycleStatus.COMPLETED, LocalDate.now());
        mockMvc.perform(patch("/api/cycles/" + cycleId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("ivf-admin", "StrongPassword1!"))
                .content(objectMapper.writeValueAsString(statusRequest)))
            .andExpect(status().isOk());

        String fetchedCycle = mockMvc.perform(get("/api/cycles/" + cycleId)
                .with(httpBasic("ivf-admin", "StrongPassword1!")))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Map<String, Object> fetched = objectMapper.readValue(fetchedCycle, new TypeReference<>() {});
        assertThat(fetched.get("status")).isEqualTo(CycleStatus.COMPLETED.name());
        assertThat(fetched).containsKeys("laboratoryProcedures", "embryoRecords", "timelineEvents");
    }
}
