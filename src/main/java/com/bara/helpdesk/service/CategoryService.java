package com.bara.helpdesk.service;

import com.bara.helpdesk.dto.CategoryDto;
import com.bara.helpdesk.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAll();

    Category getById(Long id);
}
