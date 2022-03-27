alter table cards
    add checklist_id uuid;

create unique index cards_checklist_id_uindex
    on cards (checklist_id);

alter table cards
    add constraint card_checklist_id_fk
        foreign key (checklist_id) references checklists(id)
            on delete set null;
