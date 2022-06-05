CREATE TABLE users (
	id int4 NOT NULL,
	"name" varchar(45) NULL DEFAULT NULL::character varying,
	sex bpchar(1) NULL DEFAULT NULL::bpchar,
	searchinggender bpchar(1) NULL DEFAULT NULL::bpchar,
	age int4 NULL,
	city varchar(45) NULL DEFAULT NULL::character varying,
	description varchar(4000) NULL DEFAULT NULL::character varying,
	image varchar(400) NULL DEFAULT NULL::character varying,
	status varchar(45) NULL DEFAULT NULL::character varying,
	previoususerid int4 NULL,
	lastliked int4 NULL,
	isactive bpchar(1) NULL DEFAULT NULL::bpchar,
	lastactivitydate date NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE users_dislike (
	id int4 NOT NULL,
	anotherid int4 NOT NULL,
	entrydate date NULL
);

CREATE TABLE users_like (
	id int4 NOT NULL,
	anotherid int4 NOT NULL
);
