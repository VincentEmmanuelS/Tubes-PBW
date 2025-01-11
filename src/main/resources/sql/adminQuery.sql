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

-- Tabel eventdetail untuk menyimpan detail event
CREATE TABLE eventdetail (
    id_eventdetail serial PRIMARY KEY,
    distance decimal(10,2),
    matric_distance varchar(10),
    elevation decimal(10,2),
    matric_elevation varchar(10),
    rideType varchar(10),
    waktu_mulai TIMESTAMP NOT NULL,
    waktu_selesai TIMESTAMP NOT NULL,
    title varchar(50),
    deskripsi varchar(100),
    file_foto varchar(100),
    limit_participant int,
    email varchar(30),
    CONSTRAINT fk_user_email FOREIGN KEY (email) REFERENCES users(email)
);

-- Tabel event untuk menyimpan event yang ditampilkan di dashboard
drop table event;
CREATE TABLE event (
    id_event serial PRIMARY KEY,
    title varchar(50),
    deskripsi varchar(100),
    limit_participant int,
    participant int,
    file_foto varchar(100),
    id_detail int NOT NULL,
    active CHAR(1) DEFAULT 'T',
    CONSTRAINT fk_eventdetail FOREIGN KEY (id_detail) REFERENCES eventdetail(id_eventdetail)
);
