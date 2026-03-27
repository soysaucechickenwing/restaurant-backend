INSERT INTO menu_categories (name, name_zh, display_order) VALUES
                                                               ('Appetizers',     '前菜',   1),
                                                               ('Main Dishes',    '主菜',   2),
                                                               ('Rice & Noodles', '饭面',   3),
                                                               ('Beverages',      '饮料',   4);

INSERT INTO menu_items (category_id, name, name_zh, description, price, display_order) VALUES
                                                                                           (1, 'Spring Rolls (4 pcs)',  '春卷',     'Crispy vegetable spring rolls',        6.99,  1),
                                                                                           (1, 'Dumplings (6 pcs)',     '饺子',     'Pan-fried pork and cabbage dumplings', 8.99,  2),
                                                                                           (2, 'Kung Pao Chicken',      '宫保鸡丁', 'Spicy stir-fried chicken with peanuts',15.99, 1),
                                                                                           (2, 'Beef with Broccoli',    '芥兰牛肉', 'Tender beef in savory brown sauce',    16.99, 2),
                                                                                           (2, 'Mapo Tofu',             '麻婆豆腐', 'Silken tofu in spicy bean sauce',      13.99, 3),
                                                                                           (3, 'Fried Rice',            '炒饭',     'Wok-fried rice with egg and vegetables',10.99, 1),
                                                                                           (3, 'Lo Mein',               '捞面',     'Soft noodles with vegetables',         11.99, 2),
                                                                                           (4, 'Green Tea',             '绿茶',     '',                                      2.99,  1),
                                                                                           (4, 'Soda',                  '汽水',     'Coke, Diet Coke, Sprite',               2.49,  2);