package ru.panic.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Data Transfer Object (DTO) for a Post File.
 * Uses Snake Case strategy for JSON serialization.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostFileDto(Long id, String objectKey) {
}
