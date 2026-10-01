ALTER TABLE carts ADD COLUMN guest_token VARCHAR(128);

CREATE UNIQUE INDEX uk_carts_guest_token
    ON carts (guest_token)
    WHERE guest_token IS NOT NULL;
