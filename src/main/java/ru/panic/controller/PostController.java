package ru.panic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.panic.dto.PostDto;
import ru.panic.payload.request.CreatePostRequest;
import ru.panic.payload.request.UpdatePostRequest;
import ru.panic.service.PostService;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public PostDto get(@PathVariable("id") Long id) {
        return postService.get(id);
    }

    @PostMapping
    public PostDto create(@RequestBody CreatePostRequest request) {
        return postService.create(request);
    }

    @PutMapping("/{id}")
    public PostDto update(@PathVariable("id") Long id, @RequestBody UpdatePostRequest request) {
        return postService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        postService.delete(id);
    }

}
