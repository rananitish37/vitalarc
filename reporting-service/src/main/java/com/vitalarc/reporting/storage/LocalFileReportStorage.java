package com.vitalarc.reporting.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class LocalFileReportStorage implements ReportStorage {

    private final Path storageDir;

    public LocalFileReportStorage(@Value("${reporting.local-storage-dir:./reports-storage}") String storageDir) {
        this.storageDir = Path.of(storageDir);
        try {
            Files.createDirectories(this.storageDir);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create report storage directory", e);
        }
    }

    @Override
    public String store(String fileName, byte[] pdfBytes) {
        Path filePath = storageDir.resolve(fileName);
        try {
            Files.write(filePath, pdfBytes);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write report file", e);
        }
        return filePath.toString();
    }

    @Override
    public byte[] retrieve(String storageReference) {
        try {
            return Files.readAllBytes(Path.of(storageReference));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read report file", e);
        }
    }
}