package com.charity.controller;

import com.charity.dto.AddMoneyRequest;
import com.charity.dto.CollectionBoxDto;
import com.charity.dto.CreateCollectionBoxRequest;
import com.charity.service.CollectionBoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boxes")
public class CollectionBoxController {

    private final CollectionBoxService collectionBoxService;

    @PostMapping
    public void createCollectionBox(@RequestBody CreateCollectionBoxRequest request) {
        collectionBoxService.createCollectionBox(request);
    }

    @GetMapping
    public ResponseEntity<List<CollectionBoxDto>> getCollectionBoxes() {
        return ResponseEntity.ok(collectionBoxService.getAllCollectionBoxes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionBoxDto> getCollectionBoxById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(collectionBoxService.getCollectionBoxById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollectionBox(@PathVariable("id") Long id) {
        collectionBoxService.deleteCollectionBox(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/assign/{boxId}/events/{eventId}")
    public ResponseEntity<CollectionBoxDto> assignBoxToEvent(@PathVariable("boxId") Long boxId,
                                                             @PathVariable("eventId") Long eventId) {

        return ResponseEntity.ok(collectionBoxService.assignBoxToEvent(boxId, eventId));
    }

    @PatchMapping("/add-money")
    public ResponseEntity<Void> addMoneyToBox(@RequestBody AddMoneyRequest request) {
        collectionBoxService.addMoneyToBox(request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{boxId}/transfer/{eventId}")
    public ResponseEntity<CollectionBoxDto> transferMoneyToFundraisingEvent(@PathVariable("boxId") Long boxId,
                                                                            @PathVariable("eventId") Long eventId) {
        return ResponseEntity.ok(collectionBoxService.transferMoneyToFundraisingEvent(boxId, eventId));
    }
}
