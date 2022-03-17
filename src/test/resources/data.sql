insert into users(id, first_name, created_date, created_by, last_name, email)
values ('7ee897d3-9065-421d-93bd-7ad5f30c3bd9','test name','2020-09-16 14:15:32.464391','test creator','test lastname','email@test.com');

insert into members (id, created_by, created_date,  role, user_id)
values ('7ee897d3-9065-421d-93bd-7ad5f30c5bd4','test','2020-09-16 14:15:32.464391', 'ADMIN', '7ee897d3-9065-421d-93bd-7ad5f30c3bd9');

insert into workspaces (id, created_by, created_date, name, description, visibility)
values('7ee897d3-9065-471d-53bd-7ad5f30c5bd4','test','2020-09-16 14:15:32.464391', 'test space','test desc', 'PUBLIC');

insert into boards(id, created_by, created_date, name, description, archived, visibility, workspace_id)
values ('7ee897d3-9065-821d-93bd-4ad6f30c5bd4','test','2020-09-16 14:15:32.464391','test name','test desc',
false, 'PUBLIC', '7ee897d3-9065-471d-53bd-7ad5f30c5bd4');

insert into cardlists(id, created_by, created_date, name, archived, board_id)
values ('7ee897d3-9065-885d-93bd-4ad6f30c5fd4','test','2020-09-16 14:15:32.464391','test name', false, '7ee897d3-9065-821d-93bd-4ad6f30c5bd4');

insert into reminders(id, starts, alive)
values ('7ee837d3-9525-885d-93bd-4ad6f38c5fd4','2020-09-16 14:15:32.464391', false);

insert into cards(id, created_by, created_date, name, description, archived, cardlist_id, reminder_id)
values ('7ee897d3-9535-885d-93bd-4ad6f36c5fd4','test','2020-09-16 14:15:32.464391','test name', 'test desc', false, '7ee897d3-9065-885d-93bd-4ad6f30c5fd4', '7ee837d3-9525-885d-93bd-4ad6f38c5fd4');

insert into checklists(id, created_by, created_date, name, card_id)
VALUES ('7ee894d3-9575-885d-93bd-7ad6f36c5fd7','test','2020-09-16 14:15:32.464391','test name', '7ee897d3-9535-885d-93bd-4ad6f36c5fd4');

insert into items(id, name, checked, checklist_id)
VALUES ('7ee894d3-9675-845d-43bd-7ad6f36c1fd7','test check', true, '7ee894d3-9575-885d-93bd-7ad6f36c5fd7');

insert into comments(id, created_by, created_date, text, card_id)
values ('7ee817d3-9535-885d-93bd-3ad6f36c5fd8','test','2020-09-16 14:15:32.464391','test name', '7ee897d3-9535-885d-93bd-4ad6f36c5fd4');

insert into labels(id, created_by, created_date, color_name)
VALUES ('7ee817d3-9835-815d-93bd-2ad6f33c5fd8','test','2020-09-16 14:15:32.464391','test label');

insert into space_member(space_id, member_id) VALUES ('7ee897d3-9065-471d-53bd-7ad5f30c5bd4', '7ee897d3-9065-421d-93bd-7ad5f30c5bd4');

insert into board_member(board_id, member_id) VALUES ('7ee897d3-9065-821d-93bd-4ad6f30c5bd4', '7ee897d3-9065-421d-93bd-7ad5f30c5bd4');

insert into card_member(card_id, member_id) VALUES ('7ee897d3-9535-885d-93bd-4ad6f36c5fd4', '7ee897d3-9065-421d-93bd-7ad5f30c5bd4');

insert into label_card(label_id, card_id) VALUES ('7ee817d3-9835-815d-93bd-2ad6f33c5fd8', '7ee897d3-9535-885d-93bd-4ad6f36c5fd4');