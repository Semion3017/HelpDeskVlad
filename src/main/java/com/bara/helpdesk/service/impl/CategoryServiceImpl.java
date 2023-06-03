package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.CategoryDto;
import com.bara.helpdesk.mapper.CategoryMapper;
import com.bara.helpdesk.repository.CategoryRepository;
import com.bara.helpdesk.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAll(){
        return categoryRepository.findAll().stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }
}
