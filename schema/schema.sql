-- Creates the initial schema for the database

-- Drop any tables here if they exist
drop table app_user CASCADE;

drop table app_role CASCADE;

drop table user_role;

-- set timezone for any time stamps (if needed)
set timezone = 'America/New_York';

-- all users of the system
create table app_user (
    user_name          varchar(36) not null,
    encrypted_password char(80)    not null, -- long enough to store pbkdf2 hash with salt or sha256 hash with salt
    f_name             varchar(30),
    l_name             varchar(30),
    is_active          boolean     not null, -- if false, the user account is disabled
    constraint app_user_pk primary key (user_name)
);

-- any roles that can be assigned to users
create table app_role (
    role_id   int         not null,
    role_name varchar(30) not null,
    constraint app_role_pk primary key (role_id),
    constraint app_role_uk unique (role_name)
);

create table user_role (
    user_name varchar(36) not null,
    role_id   int         not null,
    constraint user_role_pk primary key (user_name, role_id),
    constraint user_role_fk1 foreign key (user_name) references app_user (user_name),
    constraint user_role_fk2 foreign key (role_id) references app_role (role_id)
);
