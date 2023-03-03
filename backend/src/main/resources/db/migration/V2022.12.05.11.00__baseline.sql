CREATE TABLE Banner
(
    name VARCHAR(255) NOT NULL,
    CONSTRAINT PK_BANNER PRIMARY KEY (name)
);

CREATE TABLE Brand
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT PK_BRAND PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE Client
(
    id           BIGINT NOT NULL,
    address      VARCHAR(255) NULL,
    email        VARCHAR(255) NULL,
    fiscalNumber VARCHAR(255) NULL,
    name         VARCHAR(255) NULL,
    phone        VARCHAR(255) NULL,
    postalCode   VARCHAR(255) NULL,
    banner_name  VARCHAR(255) NULL,
    CONSTRAINT PK_CLIENT PRIMARY KEY (id)
);

CREATE TABLE Image
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    link     VARCHAR(255) NULL,
    brand_id BIGINT NULL,
    obsolete BIT(1) NOT NULL,
    CONSTRAINT PK_IMAGE PRIMARY KEY (id)
);

CREATE TABLE Material
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    cost DOUBLE DEFAULT 0 NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT PK_MATERIAL PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE PasswordToken
(
    token  VARCHAR(255) NOT NULL,
    user   VARCHAR(255) NOT NULL,
    expiry datetime NULL,
    CONSTRAINT PK_PASSWORDTOKEN PRIMARY KEY (token, user)
);

CREATE TABLE Plafond
(
    amount     DOUBLE       NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    brand_id   BIGINT       NOT NULL
);

CREATE TABLE Price
(
    height DOUBLE NOT NULL,
    width  DOUBLE NOT NULL,
    cost   DOUBLE NOT NULL,
    CONSTRAINT PK_PRICE PRIMARY KEY (height, width)
);

CREATE TABLE Request
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    amount          INT    NOT NULL,
    application     BIT(1) NOT NULL,
    cost            DOUBLE NULL,
    created         datetime NULL,
    lastUpdate      datetime NULL,
    observations    VARCHAR(255) NULL,
    status          VARCHAR(255) NULL,
    trackingCode    VARCHAR(255) NULL,
    client_id       BIGINT NULL,
    requester_email VARCHAR(255) NULL,
    type_id         BIGINT NULL,
    CONSTRAINT PK_REQUEST PRIMARY KEY (id)
);

CREATE TABLE RequestSlot
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    height      DOUBLE NOT NULL,
    width       DOUBLE NOT NULL,
    brand_id    BIGINT NULL,
    image_id    BIGINT NULL,
    material_id BIGINT NULL,
    cost        DOUBLE NULL,
    CONSTRAINT PK_REQUESTSLOT PRIMARY KEY (id)
);

CREATE TABLE RequestType
(
    type      VARCHAR(31) NOT NULL,
    id        BIGINT      NOT NULL,
    bottom_id BIGINT NULL,
    left_id   BIGINT NULL,
    right_id  BIGINT NULL,
    side_id   BIGINT NULL,
    top_id    BIGINT NULL,
    back_id   BIGINT NULL,
    cover_id  BIGINT NULL,
    CONSTRAINT PK_REQUESTTYPE PRIMARY KEY (id)
);

CREATE TABLE User
(
    email    VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    CONSTRAINT PK_USER PRIMARY KEY (email)
);

CREATE TABLE User_roles
(
    User_email VARCHAR(255) NOT NULL,
    roles      VARCHAR(255) NULL
);

ALTER TABLE Plafond
    ADD PRIMARY KEY (brand_id, user_email);

CREATE INDEX FK1ixbm33u6v5g9fo32oowww5s0 ON RequestType (left_id);

CREATE INDEX FK2954mepnvv5h147aj4m8j8i0m ON User_roles (User_email);

CREATE INDEX FK2vxj6nprxmj28oy0gskvwudhh ON RequestSlot (image_id);

CREATE INDEX FK52stc8loj6u4mh841u4r8jj9n ON RequestType (bottom_id);

CREATE INDEX FK5xrgxdxh0shbpj91tr6htnrhp ON RequestType (top_id);

CREATE INDEX FK8dvq41wg6vpia2j0icxpn9dcg ON Request (type_id);

CREATE INDEX FKb12xe5pqnobg78p2ogwrmiqmq ON RequestType (side_id);

CREATE INDEX FKcs4r56k816ipx84tkow408oyi ON Plafond (user_email);

CREATE INDEX FKgcn9kdhjsi843lh1ipj5desru ON RequestType (right_id);

CREATE INDEX FKgnnud2k1k2gxmthh524nli6nr ON Image (brand_id);

CREATE INDEX FKh93egois3nnejngasemn9v7et ON Request (requester_email);

CREATE INDEX FKhwguo9rhk59youasf4u7xrgyd ON Client (banner_name);

CREATE INDEX FKjh35t8kcqdo4twn32883mm5to ON RequestSlot (material_id);

CREATE INDEX FKk1dar4vbql822dsubr01i2w1e ON RequestSlot (brand_id);

CREATE INDEX FKkthxhlugti4vdjg5uq4ietydk ON RequestType (back_id);

CREATE INDEX FKlnocx4laar56a4p71ggdagcd8 ON RequestType (cover_id);

CREATE INDEX FKt6mphkrxvfdkfdct8aa30wkc0 ON Request (client_id);

ALTER TABLE RequestType
    ADD CONSTRAINT FK1ixbm33u6v5g9fo32oowww5s0 FOREIGN KEY (left_id) REFERENCES RequestSlot (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE User_roles
    ADD CONSTRAINT FK2954mepnvv5h147aj4m8j8i0m FOREIGN KEY (User_email) REFERENCES User (email) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE RequestSlot
    ADD CONSTRAINT FK2vxj6nprxmj28oy0gskvwudhh FOREIGN KEY (image_id) REFERENCES Image (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE RequestType
    ADD CONSTRAINT FK45vcl2mjuio563q6olmgvaod8 FOREIGN KEY (id) REFERENCES Request (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE RequestType
    ADD CONSTRAINT FK52stc8loj6u4mh841u4r8jj9n FOREIGN KEY (bottom_id) REFERENCES RequestSlot (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE RequestType
    ADD CONSTRAINT FK5xrgxdxh0shbpj91tr6htnrhp FOREIGN KEY (top_id) REFERENCES RequestSlot (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE Request
    ADD CONSTRAINT FK8dvq41wg6vpia2j0icxpn9dcg FOREIGN KEY (type_id) REFERENCES RequestType (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE RequestType
    ADD CONSTRAINT FKb12xe5pqnobg78p2ogwrmiqmq FOREIGN KEY (side_id) REFERENCES RequestSlot (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE Plafond
    ADD CONSTRAINT FKcs4r56k816ipx84tkow408oyi FOREIGN KEY (user_email) REFERENCES User (email) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE RequestType
    ADD CONSTRAINT FKgcn9kdhjsi843lh1ipj5desru FOREIGN KEY (right_id) REFERENCES RequestSlot (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE Image
    ADD CONSTRAINT FKgnnud2k1k2gxmthh524nli6nr FOREIGN KEY (brand_id) REFERENCES Brand (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE Request
    ADD CONSTRAINT FKh93egois3nnejngasemn9v7et FOREIGN KEY (requester_email) REFERENCES User (email) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE Client
    ADD CONSTRAINT FKhwguo9rhk59youasf4u7xrgyd FOREIGN KEY (banner_name) REFERENCES Banner (name) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE RequestSlot
    ADD CONSTRAINT FKjh35t8kcqdo4twn32883mm5to FOREIGN KEY (material_id) REFERENCES Material (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE Plafond
    ADD CONSTRAINT FKjx2k9v331fd3a4ikj37xlws3 FOREIGN KEY (brand_id) REFERENCES Brand (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE RequestSlot
    ADD CONSTRAINT FKk1dar4vbql822dsubr01i2w1e FOREIGN KEY (brand_id) REFERENCES Brand (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE RequestType
    ADD CONSTRAINT FKkthxhlugti4vdjg5uq4ietydk FOREIGN KEY (back_id) REFERENCES RequestSlot (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE RequestType
    ADD CONSTRAINT FKlnocx4laar56a4p71ggdagcd8 FOREIGN KEY (cover_id) REFERENCES RequestSlot (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE Request
    ADD CONSTRAINT FKt6mphkrxvfdkfdct8aa30wkc0 FOREIGN KEY (client_id) REFERENCES Client (id) ON UPDATE RESTRICT ON DELETE RESTRICT;
