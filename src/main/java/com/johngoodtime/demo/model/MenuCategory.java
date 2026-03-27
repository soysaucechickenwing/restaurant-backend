package com.johngoodtime.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "menu_categories")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "name_zh")
    private String nameZh;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private List<MenuItem> items;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getNameZh() { return nameZh; }
    public Integer getDisplayOrder() { return displayOrder; }
    public Boolean getIsActive() { return isActive; }
    public List<MenuItem> getItems() { return items; }
}