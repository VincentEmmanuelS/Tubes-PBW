package com.tubes.pbw.user.controller;

import java.time.LocalDate;
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

        // Periksa apakah user ada dalam session
        if (user == null) {
            return "redirect:/login";
        }

        String email = user.getEmail();
        Optional<User> userOptional = userService.findByEmail(email);

        // Periksa apakah data user ada
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
        } else {
            return "redirect:/login";
        }

        // Periksa apakah user sudah ada di UserDetail
        Optional<UserDetail> userDetailOptional = userDetailService.findByEmail(email);
        if (userDetailOptional.isEmpty()) {
            // Jika user belum melengkapi data, tampilkan overlay dan form onboarding
            model.addAttribute("showOverlay", true); // Tambahkan flag untuk menampilkan overlay
            model.addAttribute("showPopupForm", true); // Tambahkan flag untuk menampilkan popup form
        } else {
            model.addAttribute("showOverlay", false);
            model.addAttribute("showPopupForm", false);
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
        // Ambil event berdasarkan id
        Event event = eventService.getEventById(eventId);
        UserEvent userEvent = new UserEvent();

        // Periksa apakah event ditemukan
        if (event == null) {
            model.addAttribute("error", "Event not found!");
            return "redirect:/onboarding";
        }

        // Cek apakah user ada dalam session
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "redirect:/login";
        }

        // Cek apakah user sudah terdaftar di event
        if (userEventService.isUserAlreadyJoinedEvent(user.getEmail(), eventId)) {
            model.addAttribute("error", "You have already joined this event.");
            return "redirect:/onboarding";
        }

        // Cek apakah peserta sudah mencapai limit
        if (event.getParticipant() < event.getLimitParticipant() && !userEventService.isUserAlreadyJoinedOtherEvent(user.getEmail())) {
            // Jika belum mencapai limit, update jumlah peserta
            event.setParticipant(event.getParticipant() + 1);   // Menambah jumlah peserta di objek Event
            eventService.saveUpdateEvent(event);                // Menyimpan perubahan jumlah peserta ke database

            // Mengisi userEvent dengan data yang diperlukan
            userEvent.setEmail(user.getEmail());
            userEvent.setIdEvent(eventId);

            // Menambahkan user ke event melalui service
            String response = userEventService.addUserToEvent(userEvent);
            model.addAttribute("message", response); // Pesan hasil operasi
        } else {
            // Jika sudah mencapai limit, tampilkan pesan kesalahan
            model.addAttribute("error", "Limit peserta sudah tercapai!");
        }

        return "redirect:/onboarding";
    }

}
