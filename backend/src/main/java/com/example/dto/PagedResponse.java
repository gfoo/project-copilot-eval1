package com.example.dto;

import java.util.List;

/**
 * Generic paginated response wrapper for REST API endpoints.
 * 
 * <p>This record encapsulates paginated data along with pagination metadata
 * to provide clients with all necessary information for navigating through
 * large datasets.</p>
 * 
 * @param <T> The type of data items in the response
 * @param data    List of items for the current page
 * @param page    Current page number (0-indexed)
 * @param size    Number of items per page
 * @param total   Total number of items across all pages
 */
public record PagedResponse<T>(
    List<T> data,
    int page,
    int size,
    long total
) {}
