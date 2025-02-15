package ru.panic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.panic.dto.PostDto;
import ru.panic.entity.Post;

@Mapper(componentModel = "spring")
public interface PostToPostDtoMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "files", source = "files")
    })
    PostDto postToPostDto(Post post);

}
