package com.charity.controller;

import com.charity.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping(value = "/pdf", produces = APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf() {
        byte[] pdf = reportService.generatePdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "report.pdf");
        return ResponseEntity.status(OK)  // maybe should be 201 - CREATED
                .headers(headers)
                .body(pdf);
    }
}
