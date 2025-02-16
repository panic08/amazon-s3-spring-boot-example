package ru.panic.payload.request;

/**
 * Request body for updating an existing post.
 */
public record UpdatePostRequest(String title, String description) {
}
