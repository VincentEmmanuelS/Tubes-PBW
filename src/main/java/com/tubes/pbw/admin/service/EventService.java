package com.tubes.pbw.admin.service;

import com.tubes.pbw.admin.model.Event;
import com.tubes.pbw.admin.model.EventDetail;
import com.tubes.pbw.admin.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void saveEventDetail(EventDetail eventDetail) {
        eventRepository.saveEventDetail(eventDetail);
    }

    public void saveEvent(Event event) {
        eventRepository.saveEvent(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }

    public void saveUpdateEvent(Event event) {
        eventRepository.saveUpdateEvent(event);
    }

    public Event getEventById(Long eventId) {
        // Mengambil event berdasarkan ID
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        return eventOptional.orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public EventDetail getEventDetailById(Long id) {
        long eventDetailId = eventRepository.findEventDetailIdByEventId(id);
        Optional<EventDetail> eventDetailOptional = eventRepository.findEventDetailById(eventDetailId);
        return eventDetailOptional.orElseThrow(() -> new RuntimeException("EventDetail not found"));
    }
}

