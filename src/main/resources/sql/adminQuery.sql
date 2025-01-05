SELECT 
    ud.first_name, 
    ud.last_name, 
    ud.tanggal_lahir, 
    ud.gender, 
    ud.region, 
    u.email, 
    u.active
FROM 
    userdetail ud
JOIN 
    users u
ON 
    ud.email = u.email
WHERE 
    u.roles = 'user';

-- Detail ketika admin membuat event
CREATE TABLE eventdetail (id_eventdetail serial, distance decimal(10,2), matric_distance varchar(10), elevation decimal(10,2), matric_elevation varchar(10), rideType varchar(10), waktu_mulai TIMESTAMP not null, waktu_selesai TIMESTAMP not null, title varchar(50), deskripsi varchar(100), file_foto varchar(100), limit_participant int, email varchar(30) references users(email))

-- Tampilan event di dashboard
CREATE TABLE event (id_event serial, title varchar(50), deskripsi varchar(100), limit_participant int, id_detail int not null references eventdetail(id_eventdetail))