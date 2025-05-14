package com.charity.service;

import com.charity.client.CharityServiceClient;
import com.charity.exception.PdfGenerationException;
import com.charity.model.FundraisingEventDto;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final TemplateEngine templateEngine;
    private final CharityServiceClient charityServiceClient;

    public byte[] generatePdf() {
        FundraisingEventDto[] allFundraisingEvents = charityServiceClient.getAllFundraisingEvents();

        log.info("Generating PDF report with {} rows", allFundraisingEvents.length);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Context context = new Context();
            List<FundraisingEventDto> rows = List.of(allFundraisingEvents);
            context.setVariable("rows", rows);
            String html = templateEngine.process("report", context);

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();

            log.info("PDF report generated successfully");

            return os.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PdfGenerationException("PDF generation failed");
        }
    }
}
