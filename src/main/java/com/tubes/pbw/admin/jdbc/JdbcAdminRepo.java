package com.tubes.pbw.admin.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tubes.pbw.admin.model.Admin;
import com.tubes.pbw.admin.repository.AdminRepository;

@Repository
public class JdbcAdminRepo implements AdminRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<Admin> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<Admin> results = jdbcTemplate.query(sql, this::mapRowToUser, email);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    private Admin mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new Admin(
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("roles")
        );
    }

}
