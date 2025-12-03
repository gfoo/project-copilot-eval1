package com.example.mapper;

import com.example.dto.NewsResponse;
import com.example.entity.NewsDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface NewsMapper {
    
    @Mapping(target = "id", expression = "java(document.id.toString())")
    @Mapping(target = "type", expression = "java(document.isEvent ? \"event\" : \"news\")")
    NewsResponse toResponse(NewsDocument document);
    
    List<NewsResponse> toResponseList(List<NewsDocument> documents);
}
