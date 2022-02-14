alter table cards
    add reminder_id uuid;

create unique index cards_reminder_id_uindex
    on cards (reminder_id);

alter table cards
    add constraint card_reminder_id_fk
        foreign key (reminder_id) references reminders(id)
            on delete set null;
