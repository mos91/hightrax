create table hightrax_test.users (
	id serial PRIMARY KEY,
	username varchar(64) NOT NULL UNIQUE,
	password varchar(128) NOT NULL,
	authorities varchar(128) NOT NULL,
	enabled boolean
)
