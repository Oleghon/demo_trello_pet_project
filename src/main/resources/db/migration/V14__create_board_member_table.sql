CREATE TABLE board_member(
    board_id uuid NOT NULL CONSTRAINT board_memeber_id_fk REFERENCES boards(id),
    member_id uuid NOT NULL CONSTRAINT memeber_board_id_fk REFERENCES members(id),
    CONSTRAINT board_member_pk PRIMARY KEY (board_id, member_id)
);
