package ru.panic.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * Data Transfer Object (DTO) for a Post.
 * Uses Snake Case strategy for JSON serialization.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostDto(Long id, String title, String description, List<PostFileDto> files) {
}
