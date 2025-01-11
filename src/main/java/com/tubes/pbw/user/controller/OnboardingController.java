package com.tubes.pbw.user.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.tubes.pbw.user.model.UserDetail;
import com.tubes.pbw.user.service.UserDetailService;
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
    private UserDetailService userDetailService;

    @Autowired
    public OnboardingController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/onboarding")
    public String showOnboardingPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        String email = user.getEmail();
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
        } else {
            return "redirect:/login";
        }

        Optional<UserDetail> userDetailOptional = userDetailService.findByEmail(email);
        if (userDetailOptional.isEmpty()) {
            model.addAttribute("showOverlay", true);
            model.addAttribute("showPopupForm", true);
        } else {
            model.addAttribute("showOverlay", false);
            model.addAttribute("showPopupForm", false);
        }

        List<Long> joinedEventIds = userEventService.getJoinedEventIdsByUser(email);
        model.addAttribute("joinedEventIds", joinedEventIds);

        boolean isUserJoinedAnyEvent = userEventService.isUserAlreadyJoinedOtherEvent(email);
        model.addAttribute("isUserJoinedAnyEvent", isUserJoinedAnyEvent);

        List<Event> events = eventService.getAllEvents();
        events.forEach(event -> {
            if (event.getFileFoto() == null || event.getFileFoto().isEmpty()) {
                event.setFileFoto(null);
            }

            boolean userAlreadyJoined = userEventService.isUserAlreadyJoinedEvent(email, event.getIdEvent());
            model.addAttribute("userAlreadyJoined_" + event.getIdEvent(), userAlreadyJoined);
            // System.out.println("event detail id: " + event.getIdDetail());
        });

        model.addAttribute("events", events);
        return "user/onboarding";
    }

    @PostMapping("/onboarding")
    public String saveUserDetail(@RequestParam String firstName, 
                                @RequestParam String lastName,
                                @RequestParam LocalDate tanggalLahir,
                                @RequestParam String gender,
                                @RequestParam String region,
                                Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        String email = user.getEmail();
        
        // Cek apakah data user sudah ada di UserDetail
        Optional<UserDetail> userDetailOptional = userDetailService.findByEmail(email);
        if (userDetailOptional.isPresent()) {
            model.addAttribute("error", "User already exists!");
            return "redirect:/onboarding";
        }

        // Jika tidak ada, buat UserDetail baru dan simpan ke database
        UserDetail newUserDetail = new UserDetail(firstName, lastName, tanggalLahir, gender, region, email);
        userDetailService.save(newUserDetail);
        
        return "redirect:/onboarding";  // Redirect kembali ke halaman onboarding setelah berhasil
    }


    @PostMapping("/joinEvent")
    public String joinEvent(@RequestParam Long eventId, HttpSession session, Model model) {
        Event event = eventService.getEventById(eventId);
        UserEvent userEvent = new UserEvent();

        if (event == null) {
            model.addAttribute("error", "Event not found!");
            return "redirect:/onboarding";
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "redirect:/login";
        }

        String email = user.getEmail();
        if (userEventService.isUserAlreadyJoinedEvent(email, eventId)) {
            model.addAttribute("error", "You have already joined this event.");
            return "redirect:/onboarding";
        }

        if (event.getParticipant() < event.getLimitParticipant() && !userEventService.isUserAlreadyJoinedOtherEvent(email)) {
            event.setParticipant(event.getParticipant() + 1);
            eventService.saveUpdateEvent(event);

            userEvent.setFlag("T");

            userEvent.setEmail(email);
            userEvent.setIdEvent(eventId);

            String response = userEventService.addUserToEvent(userEvent);
            model.addAttribute("message", response);
        } else {
            model.addAttribute("error", "Limit peserta sudah tercapai!");
        }

        // Menyegarkan status partisipasi event
        model.addAttribute("userAlreadyJoined_" + eventId, true);
        
        return "redirect:/onboarding";
    }
}

