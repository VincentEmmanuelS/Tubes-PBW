package com.tubes.pbw.user.service;

import com.tubes.pbw.admin.model.Event;
import com.tubes.pbw.admin.model.EventDetail;
import com.tubes.pbw.user.model.UserEvent;
import com.tubes.pbw.user.repository.UserEventRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventService {
    private final UserEventRepository userEventRepository;
    private final JdbcTemplate jdbcTemplate;

    public UserEventService(UserEventRepository userEventRepository, JdbcTemplate jdbcTemplate) {
        this.userEventRepository = userEventRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // Menambahkan user ke event jika belum terdaftar
    public String addUserToEvent(UserEvent userEvent) {
        // Memeriksa apakah user sudah memiliki flag T
        if (userEventRepository.isUserAlreadyJoinedOtherEvent(userEvent.getEmail())) {
            return "User already has a flag 'T', cannot join another event.";
        }

        // Jika user belum terdaftar di event lain dan tidak memiliki flag T, tambahkan ke event
        userEventRepository.addUserToEvent(userEvent);

        // Update flag menjadi 'T' setelah berhasil bergabung
        userEventRepository.setFlagToTrue(userEvent.getEmail(), userEvent.getIdEvent());
        
        return "User successfully joined the event and flag set to 'T'.";
    }

    public boolean isUserAlreadyJoinedEvent(String email, Long eventId) {
        return userEventRepository.isUserAlreadyJoinedEvent(email, eventId);
    }

    public void saveUpdateEvent(Event event) {
        String sql = "UPDATE event SET participant = ? WHERE id_event = ?";
        jdbcTemplate.update(sql, event.getParticipant(), event.getIdEvent());
    }

    public boolean isUserAlreadyJoinedOtherEvent(String email) {
        boolean check = userEventRepository.isUserAlreadyJoinedOtherEvent(email);
        // System.out.println(check);
        return check;
    }

    // @Override
    public List<Long> getJoinedEventIdsByUser(String email) {
        // Ambil semua UserEvent berdasarkan email user
        List<UserEvent> userEvents = userEventRepository.findByEmail(email);

        // Ekstrak ID event dari setiap UserEvent
        List<Long> joinedEventIds = userEvents.stream()
                                            .map(UserEvent::getIdEvent)
                                            .collect(Collectors.toList());

        return joinedEventIds;
    }
    
}
