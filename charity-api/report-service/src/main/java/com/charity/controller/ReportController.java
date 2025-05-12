package com.charity.controller;

import com.charity.model.ReportRow;
import com.charity.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping(value = "/pdf", produces = APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf(@RequestBody List<ReportRow> rows) {
        byte[] pdf = reportService.generatePdf(rows);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "report.pdf");

        return ResponseEntity.status(CREATED)
                .headers(headers)
                .body(pdf);
    }
}
