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
<<<<<<< HEAD
	
from
	manualEntryDummy

--distance grafik
select
	sum(distance) as totaldistance , month
from
	(SELECT 
		Extract(Day from tanggal_event) as tanggal, EXTRACT(MONTH FROM tanggal_event) as month, EXTRACT(YEAR FROM tanggal_event) as year , distance, matric_distance, elevation, matric_elevation, duration, ridetype, email
	FROM manualentrydummy
	ORDER BY tanggal_event)as data1
where
	email = 'user1@gmail.com' AND matric_distance = 'km' AND ridetype = 'run' and year = 2024
group by
	month

--totalactivity
select
	count(rideType) as totaldistance , month
from
	(SELECT 
		Extract(Day from tanggal_event) as tanggal, EXTRACT(MONTH FROM tanggal_event) as month, EXTRACT(YEAR FROM tanggal_event) as year , distance, matric_distance, elevation, matric_elevation, duration, ridetype, email
	FROM manualentrydummy
	ORDER BY tanggal_event)as data1
where
	email = 'user1@gmail.com' and year = 2024 and ridetype ='bike'
group by
	month


select
	CAST(EXTRACT(EPOCH FROM SUM(duration))/ 3600 AS decimal(6,3)) AS value , month
from
	(SELECT 
		Extract(Day from tanggal_event) as tanggal, EXTRACT(MONTH FROM tanggal_event) as month, EXTRACT(YEAR FROM tanggal_event) as year , distance, matric_distance, elevation, matric_elevation, duration, ridetype, email
	FROM manualentrydummy
	ORDER BY tanggal_event)as data1
where
	email = 'user1@gmail.com' and year = 2024
group by
	month

--list tahun
select
	distinct extract(year from tanggal_event) as year
from
	manualentrydummy
where
	email = 'user1@gmail.com'
	
--delete data
DELETE FROM manualentrydummy
WHERE email IS NULL;

update manualentrydummy
set
	ridetype ='bike'
where 
	EXTRACT(MONTH FROM tanggal_event) = 12
=======
	*
from
	manualEntryDummy

-- buat join event
drop table user_event;
CREATE TABLE user_event (
    email VARCHAR(30) NOT NULL,
    id_event BIGINT NOT NULL,
	flag CHAR(1) DEFAULT 'T',	-- T = true, F = false
    PRIMARY KEY (email, id_event),
    FOREIGN KEY (email) REFERENCES users(email),
    FOREIGN KEY (id_event) REFERENCES event(id_event)
);
>>>>>>> 8ed6281764ac393928a4dac961e4f0363c844673
