CREATE TABLE card_member(
    card_id uuid NOT NULL CONSTRAINT card_memeber_id_fk REFERENCES cards(id) on delete cascade,
    member_id uuid NOT NULL CONSTRAINT memeber_card_id_fk REFERENCES members(id) on delete cascade,
    CONSTRAINT card_member_pk PRIMARY KEY (card_id, member_id)
);