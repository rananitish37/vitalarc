package com.vitalarc.reporting.service;

import com.vitalarc.reporting.dto.RecommendationSnapshot;
import com.vitalarc.reporting.dto.TrainingLoadSnapshot;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds a simple, legible one-page PDF using PDFBox's low-level content-stream API.
 * There's no "write paragraph" helper in PDFBox - text has to be manually wrapped and
 * positioned line by line, which is exactly what wrapText/writeLine below handle.
 * For a project this size, that manual control is fine; a much longer document would
 * be a good reason to reach for a templating layer instead.
 */
@Component
public class PdfReportBuilder {

    private static final float MARGIN = 50;
    private static final float PAGE_WIDTH = PDRectangle.LETTER.getWidth();
    private static final float MAX_TEXT_WIDTH = PAGE_WIDTH - (2 * MARGIN);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy");

    public byte[] build(LocalDate weekStart, LocalDate weekEnd, TrainingLoadSnapshot load, RecommendationSnapshot recommendation) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            var titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            var headingFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            var bodyFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float y = PDRectangle.LETTER.getHeight() - MARGIN;

                y = writeLine(content, titleFont, 20, MARGIN, y, "VitalArc Weekly Training Report");
                y -= 6;
                y = writeLine(content, bodyFont, 11, MARGIN, y,
                        "Week of " + weekStart.format(DATE_FORMAT) + " - " + weekEnd.format(DATE_FORMAT));
                y -= 20;

                y = writeLine(content, headingFont, 14, MARGIN, y, "Training Load Summary");
                y -= 18;
                y = writeLine(content, bodyFont, 11, MARGIN, y,
                        String.format("Acute load (7-day average): %.1f", load.acuteLoad()));
                y -= 16;
                y = writeLine(content, bodyFont, 11, MARGIN, y,
                        String.format("Chronic load (28-day average): %.1f", load.chronicLoad()));
                y -= 16;
                y = writeLine(content, bodyFont, 11, MARGIN, y,
                        String.format("ACWR: %.2f  (%s)", load.acwr(), load.riskZone()));
                y -= 26;

                y = writeLine(content, headingFont, 14, MARGIN, y, "AI Coach Recommendation");
                y -= 18;
                for (String line : wrapText(recommendation.recommendationText(), bodyFont, 11, MAX_TEXT_WIDTH)) {
                    y = writeLine(content, bodyFont, 11, MARGIN, y, line);
                    y -= 15;
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to build PDF report", e);
        }
    }

    private float writeLine(PDPageContentStream content, PDType1Font font, float fontSize, float x, float y, String text) throws IOException {
        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
        return y;
    }

    private List<String> wrapText(String text, PDType1Font font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split("\\s+")) {
            String candidate = currentLine.isEmpty() ? word : currentLine + " " + word;
            float width = font.getStringWidth(candidate) / 1000 * fontSize;
            if (width > maxWidth && !currentLine.isEmpty()) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                currentLine = new StringBuilder(candidate);
            }
        }
        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }
        return lines;
    }
}