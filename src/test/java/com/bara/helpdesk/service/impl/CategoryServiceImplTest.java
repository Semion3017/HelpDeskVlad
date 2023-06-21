package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.exception.CategoryNotFoundException;
import com.bara.helpdesk.entity.Category;
import com.bara.helpdesk.repository.CategoryRepository;
import com.bara.helpdesk.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final CategoryService categoryService = new CategoryServiceImpl(categoryRepository);

    @Test
    void shouldReturnCategory() {
        Long id = 1L;
        Category category = Category.builder().id(id).name("name").build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        Assertions.assertEquals(category, categoryService.getById(id));
    }

    @Test
    void shouldThrowWhenCategoryIsNotFound() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(CategoryNotFoundException.class, ()-> categoryService.getById(id));
        verify(categoryRepository).findById(id);
    }
}