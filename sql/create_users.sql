create table users (
	id serial PRIMARY KEY,
	username varchar(64) NOT NULL UNIQUE,
	password varchar(128) NOT NULL,
	authorities varchar(128) NOT NULL,
	enabled boolean
);

create table persistent_logins (
	username varchar(64) NOT NULL,
	series varchar(64) primary key,
	token varchar(64) NOT NULL,
	last_used timestamp not null
)
