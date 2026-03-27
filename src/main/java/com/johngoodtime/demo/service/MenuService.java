package com.johngoodtime.demo.service;

import com.johngoodtime.demo.model.MenuCategory;
import com.johngoodtime.demo.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuCategoryRepository categoryRepository;

    public MenuService(MenuCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<MenuCategory> getFullMenu(){
        return categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
    }
}
