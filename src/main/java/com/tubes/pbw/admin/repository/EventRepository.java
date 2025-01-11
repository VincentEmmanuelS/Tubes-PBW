package com.tubes.pbw.admin.repository;

import com.tubes.pbw.admin.model.Event;
import com.tubes.pbw.admin.model.EventDetail;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;

    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Menyimpan detail event
    public Long saveEventDetail(EventDetail eventDetail) {
        String sql = "INSERT INTO eventdetail (distance, matric_distance, elevation, matric_elevation, ridetype, waktu_mulai, waktu_selesai, title, deskripsi, file_foto, limit_participant, email) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_eventdetail";
    
        // Menggunakan query untuk mendapatkan id_eventdetail yang baru disimpan
        Long id = jdbcTemplate.queryForObject(sql, Long.class, 
            eventDetail.getDistance(),
            eventDetail.getMatricDistance(),
            eventDetail.getElevation(),
            eventDetail.getMatricElevation(),
            eventDetail.getRideType(),
            eventDetail.getWaktuMulai(),
            eventDetail.getWaktuSelesai(),
            eventDetail.getTitle(),
            eventDetail.getDeskripsi(),
            eventDetail.getFileFoto(),
            eventDetail.getLimitParticipant(),
            eventDetail.getEmail());

        eventDetail.setIdEventDetail(id);
        return id;
    }

    // Menyimpan event yang merujuk ke eventdetail
    public Long saveEvent(Event event) {
        String sql = "INSERT INTO event (title, deskripsi, limit_participant, participant, file_foto, id_detail) " + 
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING id_event";
        // Use queryForObject to get the returned id_event
        return jdbcTemplate.queryForObject(sql, Long.class, 
            event.getTitle(), 
            event.getDeskripsi(), 
            event.getLimitParticipant(), 
            event.getParticipant(),
            event.getFileFoto(),
            event.getIdDetail());
    }

    public void saveUpdateEvent(Event event) {
        String sql = "UPDATE event SET participant = ? WHERE id_event = ?";
        jdbcTemplate.update(sql, event.getParticipant(), event.getIdEvent());
    }

    // Mendapatkan semua event
    public List<Event> getAllEvents() {
        String sql = "SELECT * FROM event WHERE active = 'T' ORDER BY id_event DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Event event = new Event();
            event.setIdEvent(rs.getLong("id_event"));
            event.setTitle(rs.getString("title"));
            event.setDeskripsi(rs.getString("deskripsi"));
            event.setLimitParticipant(rs.getInt("limit_participant"));
            event.setParticipant(rs.getInt("participant"));
            event.setDeskripsi(rs.getString("deskripsi") != null ? rs.getString("deskripsi") : "");
            event.setFileFoto(rs.getString("file_foto"));
            event.setIdDetail(rs.getLong("id_detail"));
            return event;
        });
    }

    // Mendapatkan event berdasarkan ID
    public Optional<Event> findById(Long eventId) {
        String sql = "SELECT * FROM event WHERE id_event = ?";
        
        try {
            Event event = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Event e = new Event();
                e.setIdEvent(rs.getLong("id_event"));
                e.setTitle(rs.getString("title"));
                e.setDeskripsi(rs.getString("deskripsi"));
                e.setLimitParticipant(rs.getInt("limit_participant"));
                e.setParticipant(rs.getInt("participant"));
                e.setFileFoto(rs.getString("file_foto"));
                e.setIdDetail(rs.getLong("id_detail"));
                return e;
            }, eventId);
            
            return Optional.ofNullable(event);
        } catch (EmptyResultDataAccessException e) {
            // Jika tidak ada event ditemukan dengan ID tersebut, kembalikan Optional.empty()
            return Optional.empty();
        }
    }

    
}

