create table users (
	id serial PRIMARY KEY,
	username varchar(64) NOT NULL UNIQUE,
	password varchar(128) NOT NULL,
	enabled boolean
);

create table user_roles (
	id SERIAL PRIMARY KEY ,
	user_id   BIGINT      NOT NULL,
	role VARCHAR(32) NOT NULL,
	CONSTRAINT user_roles_user_id_role_key UNIQUE (user_id, role)
);

create table persistent_logins (
	username varchar(64) NOT NULL,
	series varchar(64) primary key,
	token varchar(64) NOT NULL,
	last_used timestamp not null
);

insert into users VALUES (1, 'admin', '$2a$10$VRaeqnVKE0NQFDdqYO3eZ.4YO9zo8p4NfsOhBAVQXo9JyLvI.x.rC', true);
insert into users VALUES (2, 'user', '$2a$10$PJf4I.aiDYJBsNMXAsyI4OQLpBChkYL3qJhM2mn3KzmBkDI90.aje', true);

insert into user_roles VALUES (1, 1, 'ROLE_ADMIN');
insert into user_roles VALUES (2, 1, 'ROLE_USER');
insert into user_roles VALUES (3, 2, 'ROLE_USER');

