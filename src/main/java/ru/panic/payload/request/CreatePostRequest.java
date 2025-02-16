package ru.panic.payload.request;

/**
 * Request body for creating a new post.
 */
public record CreatePostRequest(String title, String description) {
}
