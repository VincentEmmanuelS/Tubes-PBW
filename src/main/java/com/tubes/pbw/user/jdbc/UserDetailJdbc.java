package com.tubes.pbw.user.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tubes.pbw.user.model.UserDetail;
import com.tubes.pbw.user.repository.UserDetailRepository;

@Repository
public class UserDetailJdbc implements UserDetailRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<UserDetail> findByEmail(String email) {
        String sql = "SELECT * FROM userdetail WHERE email = ?";
        List<UserDetail> results = jdbcTemplate.query(sql, this::mapRowToUserDetail, email);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    // RowMapper untuk memetakan hasil query ke objek UserDetail
    private UserDetail mapRowToUserDetail(ResultSet resultSet, int rowNum) throws SQLException {
        // Mengonversi java.sql.Date ke LocalDate jika tanggal_lahir ada
        LocalDate tanggalLahir = resultSet.getDate("tanggal_lahir") != null 
                ? resultSet.getDate("tanggal_lahir").toLocalDate() : null;
        
        return new UserDetail(
            resultSet.getString("first_name"),
            resultSet.getString("last_name"),
            tanggalLahir,
            resultSet.getString("gender"),
            resultSet.getString("region"),
            resultSet.getString("email")
        );
    }

    @Override
    public void save(UserDetail userDetail) {
        String sql = "INSERT INTO userdetail (first_name, last_name, tanggal_lahir, gender, region, email) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userDetail.getFirstName(), userDetail.getLastName(), userDetail.getTanggalLahir(), 
                            userDetail.getGender(), userDetail.getRegion(), userDetail.getEmail());
    }
}