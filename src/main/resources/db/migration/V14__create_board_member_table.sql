CREATE TABLE board_member(
    board_id uuid NOT NULL CONSTRAINT board_memeber_id_fk REFERENCES boards(id) on delete cascade,
    member_id uuid NOT NULL CONSTRAINT memeber_board_id_fk REFERENCES members(id) on delete cascade,
    CONSTRAINT board_member_pk PRIMARY KEY (board_id, member_id)
);
