package com.bara.helpdesk.mapper;

import com.bara.helpdesk.dto.CategoryDto;
import com.bara.helpdesk.entity.Category;

public class CategoryMapper {
    public static Category toEntity(CategoryDto dto){
        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    public static CategoryDto toDto(Category category){
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
