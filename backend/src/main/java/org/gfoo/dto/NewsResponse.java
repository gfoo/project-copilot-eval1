package org.gfoo.dto;

/**
 * DTO representing a News or Event response from the API.
 * 
 * <p>This record is used to serialize news data to JSON format for REST API responses.
 * It maps fields from the {@code NewsDocument} entity to a simplified representation
 * suitable for client consumption.</p>
 * 
 * <p><b>Field Mappings:</b></p>
 * <ul>
 *   <li>{@code id} - Maps to the MongoDB ObjectId as a string</li>
 *   <li>{@code title} - The news/event title</li>
 *   <li>{@code type} - "news" or "event" (derived from {@code NewsDocument.isEvent})</li>
 *   <li>{@code catchLine} - Brief description or tagline</li>
 *   <li>{@code language} - Language code (e.g., "fr", "en")</li>
 *   <li>{@code status} - Current status (e.g., "usable", "deleted")</li>
 * </ul>
 * 
 * @param id         Unique identifier (ObjectId as string)
 * @param title      Title of the news or event
 * @param type       Type indicator: "news" or "event"
 * @param catchLine  Brief description or tagline
 * @param language   Language code (fr/en)
 * @param status     Status indicator (usable/deleted)
 */
public record NewsResponse(
    String id,
    String title,
    String type,
    String catchLine,
    String language,
    String status
) {}
