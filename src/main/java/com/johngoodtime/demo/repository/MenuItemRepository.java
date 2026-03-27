package com.johngoodtime.demo.repository;

import com.johngoodtime.demo.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategoryIdAndIsAvailableTrue(Long categoryId);
}
