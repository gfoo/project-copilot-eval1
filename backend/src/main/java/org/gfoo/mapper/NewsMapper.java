package org.gfoo.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.gfoo.dto.NewsResponse;
import org.gfoo.entity.NewsDocument;

@Mapper(componentModel = "jakarta")
public interface NewsMapper {

    @Mapping(target = "id", expression = "java(document.id != null ? document.id.toString() : null)")
    @Mapping(target = "type",
            expression = "java(document.isEvent != null ? (document.isEvent ? \"event\" : \"news\") : null)")
    NewsResponse toResponse(NewsDocument document);

    List<NewsResponse> toResponseList(List<NewsDocument> documents);
}
