package com.vitalarc.reporting.storage;

/**
 * Same pattern as AiClient in ai-coach-service: business logic depends on this
 * interface, never on a concrete storage backend. Today it's local disk (simplest
 * thing that works before AWS is set up); on Day 3 an S3ReportStorage implementation
 * gets added and swapped in via configuration - ReportService itself doesn't change.
 */
public interface ReportStorage {

    /**
     * Stores the PDF bytes and returns a reference (path, key, URL - interpretation
     * is up to the implementation) that can later be used to retrieve it.
     */
    String store(String fileName, byte[] pdfBytes);

    byte[] retrieve(String storageReference);
}