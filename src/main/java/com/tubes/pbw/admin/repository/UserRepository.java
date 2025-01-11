package com.tubes.pbw.admin.repository;

import com.tubes.pbw.admin.model.UserDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UserDetail> getAllUsers() {
        String query = """
                SELECT ud.first_name, ud.last_name, ud.tanggal_lahir, 
                       ud.gender, ud.region, u.email, u.active
                FROM userdetail ud
                JOIN users u ON ud.email = u.email
                WHERE u.roles = 'user'
                """;

        @SuppressWarnings("unused")
        RowMapper<UserDetail> rowMapper = (rs, rowNum) -> new UserDetail(
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("tanggal_lahir").toString(),
                rs.getString("gender"),
                rs.getString("region"),
                rs.getString("email"),
                rs.getBoolean("active")
        );

        return jdbcTemplate.query(query, rowMapper);
    }

    public UserDetail getUserByEmail(String email) {
        String query = """
            SELECT ud.first_name, ud.last_name, ud.tanggal_lahir, 
                   ud.gender, ud.region, u.email, u.active
            FROM userdetail ud
            JOIN users u ON ud.email = u.email
            WHERE u.email = ?
        """;

        @SuppressWarnings("unused")
        RowMapper<UserDetail> rowMapper = (rs, rowNum) -> new UserDetail(
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getDate("tanggal_lahir").toString(),
            rs.getString("gender"),
            rs.getString("region"),
            rs.getString("email"),
            rs.getBoolean("active")
        );

        return jdbcTemplate.queryForObject(query, rowMapper, email);
    }

    public UserDetail updateUserStatus(UserDetail user) {
        String query = "UPDATE users SET active = ? WHERE email = ?";
        jdbcTemplate.update(query, user.isActive(), user.getEmail());
        return user;
    }
}
