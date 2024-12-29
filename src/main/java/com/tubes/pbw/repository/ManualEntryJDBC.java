package com.tubes.pbw.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tubes.pbw.model.ManualEntry;

@Repository
public class ManualEntryJDBC implements ManualEntryRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public void makeActivity(ManualEntry data) {
        String sql = "INSERT INTO manualEntryDummy (distance, matric_distance, duration, elevation, matric_elevation, RideType, tanggal_event, title, deskripsi, filefoto, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(sql, 
            data.getDistance(),
            data.getMatricDistance(),
            data.getDuration(),
            data.getElevation(),
            data.getMatric_elevation(),
            data.getRideType(),
            data.getDate(),
            data.getTitle(),
            data.getDeskripsi(),
            data.getNamafoto(),
            data.getEmail()
        );
    }
    

    @Override
    public List<ManualEntry> findAllEntry(String email, String sortBy, String sortOrder) {
        String sqlString = 
                """
                select
                    *
                from
                    manualentrydummy
                where
                    email = ?
                order by 
                    %s %s;
                """.formatted(sortBy, sortOrder);  // Dynamic SQL for sorting
        return jdbc.query(sqlString, this::mapToRoManualEntry, email);
    }

    public List<ManualEntry> findByTitleContaining(String keyword, String email, String sortBy, String sortOrder) {
        String sqlString = 
                """
                select
                    *
                from
                    manualentrydummy
                where
                    email = ? and title like ?
                order by 
                    %s %s;
                """.formatted(sortBy, sortOrder);  // Dynamic SQL for sorting
        return jdbc.query(sqlString, this::mapToRoManualEntry, email, "%" + keyword + "%");
    }

    public ManualEntry mapToRoManualEntry(ResultSet resultSet, int rowNum) throws SQLException {
        return new ManualEntry(resultSet.getInt("id_entry"),
            resultSet.getDouble("distance"), 
            resultSet.getString("matric_distance"), 
            resultSet.getObject("duration", LocalTime.class), 
            resultSet.getDouble("elevation"), 
            resultSet.getString("matric_elevation"), 
            resultSet.getString("RideType"), 
            resultSet.getObject("tanggal_event", LocalDateTime.class), 
            resultSet.getString("title"), 
            resultSet.getString("deskripsi"), 
            resultSet.getString("email"), 
            resultSet.getString("filefoto"), null);
    }

    @Override
    public Optional<ManualEntry> getEntry(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'getEntry'");
    }


    @Override
    public void deleteManualentry(Integer id) {
       String sql = 
               """
               DELETE FROM manualentrydummy
               WHERE id_entry = ?;
               """;
        jdbc.update(sql, id);
    }
    
}
