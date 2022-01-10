CREATE TABLE reminders(
    id uuid NOT NULL PRIMARY KEY,
    created_by VARCHAR(200),
    updated_by VARCHAR(200),
    created_date TIMESTAMP without time zone NOT NULL,
    updated_date TIMESTAMP without time zone,
    starts TIMESTAMP without time zone,
    ends TIMESTAMP without time zone,
    remind_on TIMESTAMP without time zone,
    alive BOOLEAN,
    card_id uuid NOT NULL CONSTRAINT reminder_card_id_fk REFERENCES cards(id) on delete cascade
);
CREATE UNIQUE INDEX reminder_card_id_uindex ON reminders(id);

