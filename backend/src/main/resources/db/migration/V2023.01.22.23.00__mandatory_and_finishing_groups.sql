CREATE TABLE FinishingGroup
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT pk_finishinggroup PRIMARY KEY (id)
);

CREATE TABLE FinishingGroup_finishings
(
    FinishingGroup_id BIGINT NOT NULL,
    finishings_id     BIGINT NOT NULL,
    CONSTRAINT pk_finishinggroup_finishings PRIMARY KEY (FinishingGroup_id, finishings_id)
);

CREATE TABLE Material_additionalFinishings
(
    Material_id             BIGINT NOT NULL,
    additionalFinishings_id BIGINT NOT NULL,
    CONSTRAINT pk_material_additionalfinishings PRIMARY KEY (Material_id, additionalFinishings_id)
);

CREATE TABLE Material_mandatoryFinishings
(
    Material_id            BIGINT NOT NULL,
    mandatoryFinishings_id BIGINT NOT NULL,
    CONSTRAINT pk_material_mandatoryfinishings PRIMARY KEY (Material_id, mandatoryFinishings_id)
);

CREATE TABLE Price_finishings
(
    Price_id      BIGINT NOT NULL,
    finishings_id BIGINT NOT NULL,
    CONSTRAINT pk_price_finishings PRIMARY KEY (Price_id, finishings_id)
);

CREATE TABLE RequestSlot_finishings
(
    RequestSlot_id BIGINT NOT NULL,
    finishings_id  BIGINT NOT NULL,
    CONSTRAINT pk_requestslot_finishings PRIMARY KEY (RequestSlot_id, finishings_id)
);

ALTER TABLE Price
    ADD costPerSquareMeter DOUBLE NULL;

ALTER TABLE Price
    ADD fixedCost DOUBLE NULL;

ALTER TABLE Price
    ADD id BIGINT AUTO_INCREMENT NULL;

ALTER TABLE Price
    MODIFY costPerSquareMeter DOUBLE NOT NULL;

ALTER TABLE Price
    MODIFY fixedCost DOUBLE NOT NULL;

ALTER TABLE FinishingGroup
    ADD CONSTRAINT uc_finishinggroup_name UNIQUE (name);

ALTER TABLE FinishingGroup_finishings
    ADD CONSTRAINT fk_finfin_on_finishing FOREIGN KEY (finishings_id) REFERENCES Finishing (id);

ALTER TABLE FinishingGroup_finishings
    ADD CONSTRAINT fk_finfin_on_finishing_group FOREIGN KEY (FinishingGroup_id) REFERENCES FinishingGroup (id);

ALTER TABLE Material_additionalFinishings
    ADD CONSTRAINT fk_matadd_on_finishing FOREIGN KEY (additionalFinishings_id) REFERENCES Finishing (id);

ALTER TABLE Material_additionalFinishings
    ADD CONSTRAINT fk_matadd_on_material FOREIGN KEY (Material_id) REFERENCES Material (id);

ALTER TABLE Material_mandatoryFinishings
    ADD CONSTRAINT fk_matman_on_finishing_group FOREIGN KEY (mandatoryFinishings_id) REFERENCES FinishingGroup (id);

ALTER TABLE Material_mandatoryFinishings
    ADD CONSTRAINT fk_matman_on_material FOREIGN KEY (Material_id) REFERENCES Material (id);

ALTER TABLE Price_finishings
    ADD CONSTRAINT fk_prifin_on_finishing FOREIGN KEY (finishings_id) REFERENCES Finishing (id);

ALTER TABLE Price_finishings
    ADD CONSTRAINT fk_prifin_on_price FOREIGN KEY (Price_id) REFERENCES Price (id);

ALTER TABLE RequestSlot_finishings
    ADD CONSTRAINT fk_reqfin_on_finishing FOREIGN KEY (finishings_id) REFERENCES Finishing (id);

ALTER TABLE RequestSlot_finishings
    ADD CONSTRAINT fk_reqfin_on_request_slot FOREIGN KEY (RequestSlot_id) REFERENCES RequestSlot (id);

ALTER TABLE Price
DROP
COLUMN height;

ALTER TABLE Price
DROP
COLUMN width;

ALTER TABLE Price
DROP
COLUMN cost;

ALTER TABLE Finishing
DROP
COLUMN cost;

ALTER TABLE RequestSlot
    MODIFY cost DOUBLE NOT NULL;

ALTER TABLE Price
    MODIFY material_id BIGINT NULL;

ALTER TABLE RequestType
    MODIFY type VARCHAR (31) NULL;

ALTER TABLE Price
    ADD PRIMARY KEY (id);