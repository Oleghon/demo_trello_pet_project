CREATE TABLE label_card(
    label_id uuid NOT NULL CONSTRAINT label_card_id_fk REFERENCES labels(id),
    card_id uuid NOT NULL CONSTRAINT card_label_id_fk REFERENCES cards(id) on delete cascade,
    CONSTRAINT label_card_pk PRIMARY KEY (label_id, card_id)
);