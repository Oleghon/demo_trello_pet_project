create TABLE cardlists(
    id uuid NOT NULL PRIMARY KEY,
    created_by VARCHAR(200),
    updated_by VARCHAR(200),
    created_date TIMESTAMP without time zone NOT NULL,
    updated_date TIMESTAMP without time zone,
    name VARCHAR(200) NOT NULL,
    archived BOOLEAN NOT NULL,
    board_id uuid NOT NULL CONSTRAINT cardlist_board_id_fk REFERENCES boards(id) on delete cascade
);