-- Test Data for Program Demonstration

-- Hash of password1 for this user
insert into app_user (user_name, password, f_name, l_name, is_active)
values ('ndaddona', 'ddf468c3c5106e085985c1a13f8b302219b005f71ae41b55e6e5b4e5daf5997507d96aa36ded0085', 'Nick', 'Daddona', TRUE);

-- Hash of password2 for this user
insert into app_user (user_name, password, f_name, l_name, is_active)
values ('sschmidt', 'd46dbc85e3bcf091371b66f705b2b8902d373ab06706f4f2b08fa5ace20c2f2983bdf1e5ebdcf42c', 'Sean', 'Schmidt', TRUE);

insert into app_role (role_id, role_name)
values (1, 'ROLE_USER');

insert into user_role (user_name, role_id)
values ('ndaddona', 1);

insert into user_role (user_name, role_id)
values ('sschmidt', 1);