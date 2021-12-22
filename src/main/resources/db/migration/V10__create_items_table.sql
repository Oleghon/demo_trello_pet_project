CREATE TABLE items(
    id uuid NOT NULL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    check BOOLEAN NOT NULL,
    checklist_id uuid NOT NULL CONSTRAINT item_checklist_id_fk REFERENCES checklists(id)
);