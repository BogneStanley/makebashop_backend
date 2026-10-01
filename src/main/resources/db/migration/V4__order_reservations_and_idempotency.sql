ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS cart_id BIGINT,
    ADD COLUMN IF NOT EXISTS idempotency_key VARCHAR(255),
    ADD COLUMN IF NOT EXISTS request_fingerprint CHAR(64),
    ADD COLUMN IF NOT EXISTS reservation_expires_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS paid_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS cancelled_at TIMESTAMP;

CREATE UNIQUE INDEX IF NOT EXISTS uk_orders_order_number
    ON orders (order_number)
    WHERE order_number IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uk_orders_idempotency_key
    ON orders (idempotency_key)
    WHERE idempotency_key IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_orders_status_reservation_expires_at
    ON orders (status, reservation_expires_at);

CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders (created_at);
CREATE INDEX IF NOT EXISTS idx_products_active_created_at ON products (is_active, created_at DESC);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'chk_product_variants_stock_non_negative'
    ) THEN
        ALTER TABLE product_variants
            ADD CONSTRAINT chk_product_variants_stock_non_negative
            CHECK (stock_quantity >= 0) NOT VALID;
    END IF;
END $$;
