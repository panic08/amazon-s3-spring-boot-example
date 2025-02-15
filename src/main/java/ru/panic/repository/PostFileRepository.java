package ru.panic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.panic.entity.PostFile;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Long> {
    void deleteByObjectKey(String objectKey);
}
