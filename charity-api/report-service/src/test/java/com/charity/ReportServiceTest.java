package com.charity;

import com.charity.client.CharityServiceClient;
import com.charity.exception.PdfGenerationException;
import com.charity.model.CurrencyCode;
import com.charity.model.FundraisingEventDto;
import com.charity.service.ReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private CharityServiceClient charityServiceClient;
    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    public ReportService reportService;

    @Test
    void shouldGeneratePdf() {
        FundraisingEventDto e1 = new FundraisingEventDto(
                "Event_1",
                1,
                BigDecimal.ZERO,
                CurrencyCode.EUR
        );

        FundraisingEventDto e2 = new FundraisingEventDto(
                "Event_2",
                4,
                BigDecimal.ZERO,
                CurrencyCode.EUR
        );

        FundraisingEventDto[] events = {e1, e2};

        when(charityServiceClient.getAllFundraisingEvents()).thenReturn(events);
        when(templateEngine.process(eq("report"), any(Context.class))).thenReturn("<html></html>");

        byte[] pdf = reportService.generatePdf();
        assert pdf != null;
        assert pdf.length > 0;
    }

    @Test
    void shouldThrowExceptionWhenPdfGenerationFails() {
        FundraisingEventDto e1 = new FundraisingEventDto(
                "Event_1",
                1,
                BigDecimal.ZERO,
                CurrencyCode.EUR
        );

        FundraisingEventDto[] events = {e1};

        when(charityServiceClient.getAllFundraisingEvents()).thenReturn(events);
        when(templateEngine.process(eq("report"), any(Context.class)))
                .thenThrow(new RuntimeException("Thymeleaf error"));

        try {
            reportService.generatePdf();
        } catch (Exception e) {
            assert e instanceof PdfGenerationException;
            assert e.getMessage().equals("PDF generation failed");
        }
    }
}
