package com.tubes.pbw.user.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tubes.pbw.user.model.ManualEntry;
import com.tubes.pbw.user.repository.ManualEntryRepository;

@Repository
public class ManualEntryJDBC implements ManualEntryRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public void makeActivity(ManualEntry data) {
        String sql = "INSERT INTO manualEntry (distance, matric_distance, duration, elevation, matric_elevation, RideType, tanggal_event, title, deskripsi, filefoto, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                    manualentry
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
                    manualentry
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
       String sql = """
               select
                    *
                from
                    manualentry
                where
                    id_entry = ?
               """;
        List<ManualEntry> result = jdbc.query(sql, this::mapToRoManualEntry, id);

        return result.isEmpty()? Optional.empty() :Optional.of(result.get(0));
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


    @Override
    public void UpdateData(ManualEntry data) {
       String sql = """
               UPDATE 
                manualEntry
                SET 
                    distance = ?, 
                    matric_distance = ?, 
                    duration = ?, 
                    elevation = ?, 
                    matric_elevation = ?, 
                    RideType = ?, 
                    tanggal_event = ?, 
                    title = ?, 
                    deskripsi = ?, 
                    fileFoto = ? 
                WHERE 
                    id_entry = ?;
               """;
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
        data.getId());
    }


    @Override
    public Optional<ManualEntry> getEntryEvent(String email, String title, LocalDateTime date) {
        String sql = """
            select
                 *
             from
                 manualentry
             where
                 email = ? AND title = ? AND tanggal_event = ?
            """;
        List<ManualEntry> result = jdbc.query(sql, this::mapToRoManualEntry, email, title, date);

        return result.isEmpty()? Optional.empty() :Optional.of(result.get(0));
    }
    
}
