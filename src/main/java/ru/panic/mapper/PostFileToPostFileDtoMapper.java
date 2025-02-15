package ru.panic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.panic.dto.PostFileDto;
import ru.panic.entity.PostFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostFileToPostFileDtoMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "objectKey", source = "objectKey")
    })
    List<PostFileDto> postFileListToPostFileDtoList(List<PostFile> postFileList);

}
