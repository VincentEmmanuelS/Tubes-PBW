package com.tubes.pbw.manualEntry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
    public List<ManualEntry> findAllEntry() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllEntry'");
    }

    @Override
    public Optional<ManualEntry> getEntry(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEntry'");
    }
    
}
