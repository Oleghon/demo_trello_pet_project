CREATE TABLE space_member(
    space_id uuid NOT NULL CONSTRAINT space_memeber_id_fk REFERENCES workspaces(id),
    member_id uuid NOT NULL CONSTRAINT memeber_space_id_fk REFERENCES members(id),
    CONSTRAINT space_member_pk PRIMARY KEY (space_id, member_id)
);
