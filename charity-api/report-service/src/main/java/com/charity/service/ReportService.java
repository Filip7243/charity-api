package com.charity.service;

import com.charity.model.ReportRow;
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

    public byte[] generatePdf(List<ReportRow> rows) {
        log.info("Generating PDF report with {} rows", rows.size());

        if (rows.isEmpty()) {
            log.warn("No rows provided for PDF generation");
            return new byte[0];
        }

        Context context = new Context();
        context.setVariable("rows", rows);
        String html = templateEngine.process("report", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();

            log.info("PDF report generated successfully");

            return os.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("PDF generation failed", e);
        }
    }
}
