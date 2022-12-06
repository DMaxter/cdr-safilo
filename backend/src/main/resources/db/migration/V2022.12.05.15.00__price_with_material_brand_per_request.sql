ALTER TABLE Request
    ADD brand_id BIGINT NULL;

ALTER TABLE Price
    ADD material_id BIGINT NULL;

CREATE UNIQUE INDEX IX_pk_passwordtoken ON PasswordToken (user, token);

CREATE UNIQUE INDEX IX_pk_plafond ON Plafond (user_email, brand_id);

ALTER TABLE Price
    ADD CONSTRAINT FK_PRICE_ON_MATERIAL FOREIGN KEY (material_id) REFERENCES Material (id);

ALTER TABLE Request
    ADD CONSTRAINT FK_REQUEST_ON_BRAND FOREIGN KEY (brand_id) REFERENCES Brand (id);

ALTER TABLE RequestSlot
DROP
FOREIGN KEY FKk1dar4vbql822dsubr01i2w1e;

ALTER TABLE RequestSlot
DROP
COLUMN brand_id;

ALTER TABLE Material
DROP
COLUMN cost;

ALTER TABLE Price
    DROP PRIMARY KEY, ADD PRIMARY KEY (width, height, material_id);
