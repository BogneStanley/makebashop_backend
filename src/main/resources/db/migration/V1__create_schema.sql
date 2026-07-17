CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    avatar          VARCHAR(255),
    password        VARCHAR(255) NOT NULL,
    role            VARCHAR(255) NOT NULL,
    is_activate     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP
);

CREATE TABLE categories (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) UNIQUE,
    description TEXT
);

CREATE TABLE products (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255),
    description VARCHAR(255),
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE product_category (
    product_id  BIGINT NOT NULL REFERENCES products (id),
    category_id BIGINT NOT NULL REFERENCES categories (id),
    PRIMARY KEY (product_id, category_id)
);

CREATE TABLE product_variants (
    id             BIGSERIAL PRIMARY KEY,
    sku            VARCHAR(255),
    currency_code  VARCHAR(255),
    price          NUMERIC(38, 2),
    stock_quantity INTEGER,
    size           VARCHAR(255),
    color          VARCHAR(255),
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP,
    product_id     BIGINT REFERENCES products (id)
);

CREATE TABLE product_images (
    id           BIGSERIAL PRIMARY KEY,
    url          VARCHAR(255),
    storage_key  VARCHAR(255),
    content_type VARCHAR(255),
    file_name    VARCHAR(255),
    is_primary   BOOLEAN NOT NULL DEFAULT FALSE,
    position     INTEGER NOT NULL,
    product_id   BIGINT REFERENCES products (id)
);

CREATE TABLE carts (
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT
);

CREATE TABLE cart_items (
    id                 BIGSERIAL PRIMARY KEY,
    cart_id            BIGINT REFERENCES carts (id),
    product_id         BIGINT REFERENCES products (id),
    product_variant_id BIGINT REFERENCES product_variants (id),
    quantity           INTEGER
);

CREATE TABLE orders (
    id                     BIGSERIAL PRIMARY KEY,
    order_number           VARCHAR(255),
    customer_first_name    VARCHAR(255),
    customer_last_name     VARCHAR(255),
    customer_email         VARCHAR(255),
    customer_phone_number  VARCHAR(255),
    status                 VARCHAR(255),
    note                   VARCHAR(255),
    created_at             TIMESTAMP,
    updated_at             TIMESTAMP
);

CREATE TABLE order_items (
    id                 BIGSERIAL PRIMARY KEY,
    quantity           INTEGER NOT NULL,
    price              NUMERIC(38, 2),
    currency_code      VARCHAR(255),
    order_id           BIGINT REFERENCES orders (id),
    product_id         BIGINT REFERENCES products (id),
    product_variant_id BIGINT REFERENCES product_variants (id)
);

CREATE TABLE contact_settings (
    id         BIGINT PRIMARY KEY,
    contacts   JSONB        NOT NULL DEFAULT '{}'::jsonb,
    updated_at TIMESTAMPTZ  NOT NULL
);

CREATE TABLE product_highlight_slots (
    id         BIGSERIAL PRIMARY KEY,
    list_type  VARCHAR(255) NOT NULL,
    product_id BIGINT       NOT NULL REFERENCES products (id),
    position   INTEGER      NOT NULL,
    CONSTRAINT uk_product_highlight_slots_list_type_position UNIQUE (list_type, position),
    CONSTRAINT uk_product_highlight_slots_list_type_product_id UNIQUE (list_type, product_id)
);

CREATE INDEX idx_product_variants_product_id ON product_variants (product_id);
CREATE INDEX idx_product_images_product_id ON product_images (product_id);
CREATE INDEX idx_cart_items_cart_id ON cart_items (cart_id);
CREATE INDEX idx_order_items_order_id ON order_items (order_id);
CREATE INDEX idx_product_highlight_slots_product_id ON product_highlight_slots (product_id);
