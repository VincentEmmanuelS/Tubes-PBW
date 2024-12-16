select
	*
from
	userdummy
create table users (id serial, email varchar(30) primary key, password varchar(60), roles varchar(10))
create table userdetail (first_name varchar(30), last_name varchar(30), tanggal_lahir date, gender varchar(10), region varchar(20), email varchar(30) references users(email))
create table userdummy(id serial, email varchar(30) primary key, password varchar(60), roles varchar(10))

insert into userdummy (email, password, roles) values ('admin1@gmail.com', '1234', 'admin'), ('user1@gmail.com', '1234','user')