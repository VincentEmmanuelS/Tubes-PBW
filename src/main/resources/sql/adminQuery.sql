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
