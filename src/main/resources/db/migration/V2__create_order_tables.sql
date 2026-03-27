CREATE TABLE orders (
                        id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
                        order_number        VARCHAR(20) NOT NULL UNIQUE,
                        status              ENUM('PENDING_PAYMENT','PAID','PREPARING','READY','COMPLETED','CANCELLED')
                        NOT NULL DEFAULT 'PENDING_PAYMENT',
                        order_type          ENUM('PICKUP','DELIVERY') NOT NULL DEFAULT 'PICKUP',
                        subtotal            DECIMAL(10, 2) NOT NULL,
                        tax                 DECIMAL(10, 2) NOT NULL,
                        total_amount        DECIMAL(10, 2) NOT NULL,
                        stripe_payment_id   VARCHAR(200),
                        customer_name       VARCHAR(200) NOT NULL,
                        customer_phone      VARCHAR(20) NOT NULL,
                        customer_email      VARCHAR(200),
                        delivery_address    TEXT,
                        special_instructions TEXT,
                        estimated_ready_at  TIMESTAMP,
                        created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
                             id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id        BIGINT NOT NULL,
                             menu_item_id    BIGINT NOT NULL,
                             menu_item_name  VARCHAR(200) NOT NULL,
                             unit_price      DECIMAL(10, 2) NOT NULL,
                             quantity        INT NOT NULL,
                             subtotal        DECIMAL(10, 2) NOT NULL,
                             special_instructions VARCHAR(500),
                             CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id),
                             CONSTRAINT fk_order_item_menu  FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

CREATE INDEX idx_orders_status     ON orders(status);
CREATE INDEX idx_orders_created    ON orders(created_at);
CREATE INDEX idx_order_items_order ON order_items(order_id);