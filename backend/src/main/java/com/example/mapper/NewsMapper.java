package com.example.mapper;

import com.example.dto.NewsResponse;
import com.example.entity.NewsDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta-cdi")
public interface NewsMapper {
    
    @Mapping(target = "id", expression = "java(document.id != null ? document.id.toString() : null)")
    @Mapping(target = "type", expression = "java(document.isEvent != null ? (document.isEvent ? \"event\" : \"news\") : null)")
    NewsResponse toResponse(NewsDocument document);
    
    List<NewsResponse> toResponseList(List<NewsDocument> documents);
}
