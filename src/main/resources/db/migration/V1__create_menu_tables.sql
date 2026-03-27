CREATE TABLE menu_categories (
                                 id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 name        VARCHAR(100) NOT NULL,
                                 name_zh     VARCHAR(100),
                                 display_order INT NOT NULL DEFAULT 0,
                                 is_active   BOOLEAN NOT NULL DEFAULT TRUE,
                                 created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE menu_items (
                            id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                            category_id     BIGINT NOT NULL,
                            name            VARCHAR(200) NOT NULL,
                            name_zh         VARCHAR(200),
                            description     TEXT,
                            price           DECIMAL(10, 2) NOT NULL,
                            image_url       VARCHAR(500),
                            is_available    BOOLEAN NOT NULL DEFAULT TRUE,
                            display_order   INT NOT NULL DEFAULT 0,
                            created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            CONSTRAINT fk_item_category FOREIGN KEY (category_id) REFERENCES menu_categories(id)
);

CREATE INDEX idx_items_category ON menu_items(category_id);
CREATE INDEX idx_items_available ON menu_items(is_available);