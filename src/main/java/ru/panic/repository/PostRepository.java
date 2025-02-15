package ru.panic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.panic.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
