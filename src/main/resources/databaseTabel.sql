--hapus table
drop table if exists users cascade;
drop table if exists userdetail cascade;
drop table if exists userdummy cascade;
drop table if exists manualEntryDummy cascade;
drop table if exists manualEntry cascade;

--bikin table
create table users (id serial, email varchar(30) primary key, password varchar(60), roles varchar(10))
create table userdetail (first_name varchar(30), last_name varchar(30), tanggal_lahir date, gender varchar(10), region varchar(20), email varchar(30) references users(email))
create table userdummy(id serial, email varchar(30) primary key, password varchar(60), roles varchar(10))
create table manualEntry(id_entry serial,distance decimal(10,2), matric_distance varchar(10), duration time, elevation decimal(10,2), matric_elevation varchar(10), RideType varchar(10), tanggal_event TIMESTAMP not null, title varchar(50), deskripsi varchar(100), fileFoto varchar(100), email varchar(30) references users(email))
create table manualEntryDummy(id_entry serial,distance decimal(10,2), matric_distance varchar(10), duration time, elevation decimal(10,2), matric_elevation varchar(10), RideType varchar(10), tanggal_event TIMESTAMP not null, title varchar(50), deskripsi varchar(100), fileFoto varchar(100), email varchar(30) references userdummy(email))

--insert data dummy
insert into userdummy (email, password, roles) values ('admin1@gmail.com', '1234', 'admin'), ('user1@gmail.com', '1234','user')



--testing
select
	*
from
	manualEntryDummy