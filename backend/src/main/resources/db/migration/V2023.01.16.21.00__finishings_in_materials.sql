CREATE TABLE Finishing_exclusiveMaterials
(
    Finishing_id          BIGINT NOT NULL,
    exclusiveMaterials_id BIGINT NOT NULL,
    CONSTRAINT pk_finishing_exclusivematerials PRIMARY KEY (Finishing_id, exclusiveMaterials_id)
);

CREATE TABLE Material_finishings
(
    Material_id   BIGINT NOT NULL,
    finishings_id BIGINT NOT NULL,
    CONSTRAINT pk_material_finishings PRIMARY KEY (Material_id, finishings_id)
);

ALTER TABLE Finishing_exclusiveMaterials
    ADD CONSTRAINT fk_finexc_on_finishing FOREIGN KEY (Finishing_id) REFERENCES Finishing (id);

ALTER TABLE Finishing_exclusiveMaterials
    ADD CONSTRAINT fk_finexc_on_material FOREIGN KEY (exclusiveMaterials_id) REFERENCES Material (id);

ALTER TABLE Material_finishings
    ADD CONSTRAINT fk_matfin_on_finishing FOREIGN KEY (finishings_id) REFERENCES Finishing (id);

ALTER TABLE Material_finishings
    ADD CONSTRAINT fk_matfin_on_material FOREIGN KEY (Material_id) REFERENCES Material (id);