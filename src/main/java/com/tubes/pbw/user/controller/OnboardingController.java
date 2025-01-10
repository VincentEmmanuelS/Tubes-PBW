package com.tubes.pbw.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tubes.pbw.admin.model.Event;
import com.tubes.pbw.admin.service.EventService;
import com.tubes.pbw.user.model.User;
import com.tubes.pbw.user.model.UserEvent;
import com.tubes.pbw.user.service.UserEventService;
import com.tubes.pbw.user.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OnboardingController {

    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserEventService userEventService;

    @Autowired
    public OnboardingController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/onboarding")
    public String showOnboardingPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        // System.out.println(user + " from onboarding");

        // Periksa apakah user ada dalam session
        if (user != null) {
            String email = user.getEmail();

            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isPresent()) {
                model.addAttribute("user", userOptional.get());
            }
        }

        List<Event> events = eventService.getAllEvents();

        // Menambahkan flag untuk hide jika fileFoto kosong
        events.forEach(event -> {
            if (event.getFileFoto() == null || event.getFileFoto().isEmpty()) {
                event.setFileFoto(null); // Set fileFoto ke null untuk kemudahan pengecekan
            }

            boolean userAlreadyJoined = userEventService.isUserAlreadyJoinedEvent(user.getEmail(), event.getIdEvent());
            model.addAttribute("userAlreadyJoined_" + event.getIdEvent(), userAlreadyJoined);
        });

        model.addAttribute("events", events);
        return "user/onboarding";
    }

    @PostMapping("/joinEvent")
    public String joinEvent(@RequestParam Long eventId, HttpSession session, Model model) {
        // Ambil event berdasarkan id
        System.out.println("Event ID: " + eventId);
        Event event = eventService.getEventById(eventId);
        UserEvent userEvent = new UserEvent();

        // Periksa apakah event ditemukan
        if (event == null) {
            System.out.println("event not found");
            model.addAttribute("error", "Event not found!");
            return "redirect:/onboarding";
        }

        // Cek apakah user ada dalam session
        User user = (User) session.getAttribute("user");
        if (user == null) {
            System.out.println("user not found");
            model.addAttribute("error", "User not logged in.");
            return "redirect:/login";
        }

        // Cek apakah user sudah terdaftar di event
        if (userEventService.isUserAlreadyJoinedEvent(user.getEmail(), eventId)) {
            System.out.println("you already join");
            model.addAttribute("error", "You have already joined this event.");
            return "redirect:/onboarding";
        }

        // Cek apakah peserta sudah mencapai limit
        if (event.getParticipant() < event.getLimitParticipant() && !userEventService.isUserAlreadyJoinedOtherEvent(user.getEmail())) {
            System.out.println("user join event from controller");
            
            // Jika belum mencapai limit, update jumlah peserta
            event.setParticipant(event.getParticipant() + 1);   // Menambah jumlah peserta di objek Event
            eventService.saveUpdateEvent(event);                // Menyimpan perubahan jumlah peserta ke database

            // Mengisi userEvent dengan data yang diperlukan
            userEvent.setEmail(user.getEmail());
            userEvent.setIdEvent(eventId);

            // Menambahkan user ke event melalui service
            String response = userEventService.addUserToEvent(userEvent);
            model.addAttribute("message", response); // Pesan hasil operasi
        } 
        else {
            System.out.println("limit");
            // Jika sudah mencapai limit, tampilkan pesan kesalahan
            model.addAttribute("error", "Limit peserta sudah tercapai!");
        }

        // Arahkan kembali ke halaman onboarding
        return "redirect:/onboarding";
    }

}
