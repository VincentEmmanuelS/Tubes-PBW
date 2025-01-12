package com.tubes.pbw.user.repository;

import com.tubes.pbw.user.model.UserEvent;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserEventRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Menambahkan user ke event dengan flag 'F' (default)
    public void addUserToEvent(UserEvent userEvent) {

        if (!isUserAlreadyJoinedOtherEvent(userEvent.getEmail())) {
            // Menambahkan user ke event
            String sql = "INSERT INTO user_event (email, id_event, flag) VALUES (?, ?, 'T')";
            jdbcTemplate.update(sql, userEvent.getEmail(), userEvent.getIdEvent());
        
            // Mendapatkan jumlah peserta saat ini dari event
            String selectSql = "SELECT participant FROM event WHERE id_event = ?";
            Integer currentParticipantCount = jdbcTemplate.queryForObject(selectSql, Integer.class, userEvent.getIdEvent());
        
            if (currentParticipantCount == null) {
                currentParticipantCount = 0;  // Jika tidak ada peserta sebelumnya
            }
        
            // Menambahkan 1 ke jumlah peserta
            Integer newParticipantCount = currentParticipantCount;
            
            // Log untuk memeriksa jumlah peserta
            // System.out.println("Jumlah peserta sebelum update: " + currentParticipantCount);
            // System.out.println("Jumlah peserta setelah update: " + newParticipantCount);
        
            // Memperbarui jumlah peserta di tabel event
            String updateSql = "UPDATE event SET participant = ? WHERE id_event = ?";
            jdbcTemplate.update(updateSql, newParticipantCount, userEvent.getIdEvent());
        
            // Log untuk memastikan update berhasil
            // Integer updatedParticipantCount = jdbcTemplate.queryForObject(selectSql, Integer.class, userEvent.getIdEvent());
            // System.out.println("Jumlah peserta setelah update di DB: " + updatedParticipantCount);
        }
    }

    // Memeriksa apakah user sudah terdaftar di event lain dengan flag F
    public boolean isUserAlreadyJoinedOtherEvent(String email) {
        String sql = "SELECT COUNT(*) FROM user_event WHERE email = ? AND flag = 'T'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        // System.out.println(count);
        return count != null && count > 0;
    }

    public boolean isUserAlreadyJoinedEvent(String email, Long eventId) {
        String sql = "SELECT COUNT(*) FROM user_event WHERE email = ? AND id_event = ? AND flag = 'T'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, eventId);
        return count != null && count > 0;  // Returns true if count > 0 (user is already joined)
    }

    // Memeriksa apakah user sudah terdaftar di event tertentu dengan flag T
    public boolean existsByEmailAndEventId(String email, Long eventId) {
        String sql = "SELECT COUNT(*) FROM user_event WHERE email = ? AND id_event = ? AND flag = 'T'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, eventId);
        return count != null && count > 0;  // Returns true if user already joined this event
    }

    // Update flag ke 'T' setelah user berhasil mengikuti event
    public void setFlagToTrue(String email, Long idEvent) {
        String sql = "UPDATE user_event SET flag = 'T' WHERE email = ? AND id_event = ?";
        jdbcTemplate.update(sql, email, idEvent);
    }

    // Memperbarui flag menjadi 'F' untuk semua user_event yang berhubungan dengan event tertentu
    public void setFlagToFalse(Long eventId) {
        String sql = "UPDATE user_event SET flag = 'F' WHERE id_event = ?";
        // String sql = "DELETE FROM user_event WHERE id_event = ?";
        jdbcTemplate.update(sql, eventId);
    }

    public void endEvent(Long eventId) {
        String sql = "UPDATE event SET active = 'F' WHERE id_event = ?";

        jdbcTemplate.update(sql, eventId);
    }
    
    // Fungsi untuk menyimpan UserEvent baru
    public void save(UserEvent userEvent) {
        String sql = "INSERT INTO user_event (email, id_event, flag) VALUES (?, ?, 'T')";
        jdbcTemplate.update(sql, userEvent.getEmail(), userEvent.getIdEvent());
    }

    public List<UserEvent> findByEmail(String email) {
        String sql = "SELECT email, id_event, flag FROM user_event WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, (rs, rowNum) -> {
            UserEvent userEvent = new UserEvent();
            userEvent.setEmail(rs.getString("email"));
            userEvent.setIdEvent(rs.getLong("id_event"));
            userEvent.setFlag(rs.getString("flag"));
            return userEvent;
        });
    }

    public boolean userAddActivity(int id, String email, int idActivity){
        String sql = """
                    UPDATE user_event
                    SET idactivity = ?
                    WHERE id_event =? AND email = ?;
                """;
        try{
            jdbcTemplate.update(sql, idActivity, id, email);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUserAlreadySubmit(String email, Long eventId) {
        String sql = "SELECT idactivity FROM user_event WHERE email = ? AND id_event = ? AND flag = 'T' LIMIT 1";
        List<Integer> results = jdbcTemplate.query(
            sql,
            new Object[]{email, eventId},
            (rs, rowNum) -> rs.getInt("idactivity")
        );
        return !results.isEmpty(); // Returns true if at least one record is found
    }
}
