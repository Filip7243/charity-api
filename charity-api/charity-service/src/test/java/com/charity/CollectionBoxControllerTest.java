package com.charity;

import com.charity.dto.AddMoneyRequest;
import com.charity.dto.CollectionBoxDto;
import com.charity.dto.CreateCollectionBoxRequest;
import com.charity.dto.CreateFundraisingEventRequest;
import com.charity.service.CollectionBoxService;
import com.charity.service.FundraisingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.charity.model.CurrencyCode.USD;
import static java.math.RoundingMode.HALF_EVEN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CollectionBoxControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CollectionBoxService collectionBoxService;
    @Autowired
    private FundraisingEventService fundraisingEventService;

    @Test
    void shouldAddMoneyToBox() throws Exception {
        var createEventRequest = new CreateFundraisingEventRequest("Test event", USD);
        fundraisingEventService.createFundraisingEvent(createEventRequest);

        var createBoxRequest = new CreateCollectionBoxRequest(Set.of(USD), 1L);
        collectionBoxService.createCollectionBox(createBoxRequest);

        List<CollectionBoxDto> boxes = collectionBoxService.getAllCollectionBoxes();
        Long boxId = boxes.get(0).boxId();

        var addMoneyRequest = new AddMoneyRequest(boxId, BigDecimal.ONE.setScale(2, HALF_EVEN), USD);

        mockMvc.perform(patch("/v1/boxes/add-money")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addMoneyRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldAssignBoxToEvent() throws Exception {
        var createEventRequest = new CreateFundraisingEventRequest("Test event", USD);
        fundraisingEventService.createFundraisingEvent(createEventRequest);

        var eventId = 1L;
        var boxId = 1L;
        var createBoxRequest = new CreateCollectionBoxRequest(new HashSet<>(Set.of(USD)), null);
        collectionBoxService.createCollectionBox(createBoxRequest);

        mockMvc.perform(patch("/v1/boxes/assign/{boxId}/events/{eventId}", boxId, eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boxId").value(boxId))
                .andExpect(jsonPath("$.isAssigned").value(true))
                .andExpect(jsonPath("$.isEmpty").value(true))
                .andExpect(jsonPath("$.currencyCodes").value(Set.of(USD)));
    }
}
