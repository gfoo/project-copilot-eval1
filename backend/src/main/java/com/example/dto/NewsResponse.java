package com.example.dto;

public record NewsResponse(
    String id,
    String title,
    String type,
    String catchLine,
    String language,
    String status
) {}
