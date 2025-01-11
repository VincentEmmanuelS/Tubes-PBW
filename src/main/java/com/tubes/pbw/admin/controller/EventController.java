package com.tubes.pbw.admin.controller;

import com.tubes.pbw.admin.model.Event;
import com.tubes.pbw.admin.model.EventDetail;
import com.tubes.pbw.admin.service.EventService;
import com.tubes.pbw.user.repository.UserEventRepository;

import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.nio.file.Path;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class EventController {

    private final EventService eventService;
    private UserEventRepository userEventRepository;

    public EventController(EventService eventService, UserEventRepository userEventRepository) {
        this.eventService = eventService;
        this.userEventRepository = userEventRepository;
    }

    // Menampilkan form untuk menambah event
    @GetMapping("/add_event")
    public String showEventForm() {
        return "admin/eventEntry";
    }

    // Menambahkan event dan detail event
    @PostMapping("/add_event")
    public String addEvent(
            @RequestParam String title,
            @RequestParam String deskripsi,
            @RequestParam(required = false) Integer limitParticipant,
            @RequestParam Double distance,
            @RequestParam String matricDistance,
            @RequestParam Double elevation,
            @RequestParam String matricElevation,
            @RequestParam String rideType,
            @RequestParam String waktuMulai,
            @RequestParam String waktuSelesai,
            @RequestParam("fileFoto") MultipartFile fileFoto,
            HttpSession session) {

        waktuMulai = waktuMulai.replace("T", " ");
        waktuSelesai = waktuSelesai.replace("T", " ");
                
        // Format waktu
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try {
            // Convert to LocalDateTime
            LocalDateTime mulai = LocalDateTime.parse(waktuMulai, formatter);
            LocalDateTime selesai = LocalDateTime.parse(waktuSelesai, formatter);

            // Convert LocalDateTime to Timestamp
            Timestamp timestampMulai = Timestamp.valueOf(mulai);
            Timestamp timestampSelesai = Timestamp.valueOf(selesai);

            EventDetail eventDetail = new EventDetail();
            eventDetail.setDistance(distance);
            eventDetail.setMatricDistance(matricDistance);
            eventDetail.setElevation(elevation);
            eventDetail.setMatricElevation(matricElevation);
            eventDetail.setRideType(rideType);
            eventDetail.setWaktuMulai(timestampMulai);
            eventDetail.setWaktuSelesai(timestampSelesai);
            eventDetail.setTitle(title);
            eventDetail.setDeskripsi(deskripsi);
            // eventDetail.setFileFoto(fileFoto);
            if (!fileFoto.isEmpty()) {
                try {
                    // Direktori upload
                    String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/upload"; 
                    Path uploadPath = Paths.get(uploadDir);

                    // Buat folder jika belum ada
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    // File name
                    String originalFileName = fileFoto.getOriginalFilename();
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                    String fileName = UUID.randomUUID().toString() + fileExtension;

                    // Set path
                    Path destinationFile = uploadPath.resolve(fileName);
                    Files.copy(fileFoto.getInputStream(), destinationFile);

                    // Save file to DB
                    eventDetail.setFileFoto("upload/" + fileName);

                } catch (IOException e) {
                    e.printStackTrace();
                    return "error"; // Error handling
                }
            } 
            
            if (limitParticipant == null || limitParticipant.equals("")) {
                eventDetail.setLimitParticipant(Integer.MAX_VALUE);     // No limit
            } 
            else {
                eventDetail.setLimitParticipant(limitParticipant);
            }
            eventDetail.setEmail((String) session.getAttribute("email"));
            eventService.saveEventDetail(eventDetail);

            Event event = new Event();
            event.setTitle(title);
            event.setDeskripsi(deskripsi);

            if (limitParticipant == null || limitParticipant.equals("")) {
                event.setLimitParticipant(Integer.MAX_VALUE);           // No limit
            } else {
                event.setLimitParticipant(limitParticipant);
            }

            event.setFileFoto(eventDetail.getFileFoto());
            event.setIdDetail(eventDetail.getIdEventDetail());

            eventService.saveEvent(event);

        } catch (DateTimeParseException e) {
            // Parsing error
            e.printStackTrace();
            return "error";
        }

        return "redirect:/onboarding_admin";
    }

    // Menampilkan event pada dashboard admin
    @GetMapping("/onboarding_admin")
    public String viewAllEvents(Model model) {

        List<Event> events = eventService.getAllEvents();

        // Menambahkan flag untuk hide jika fileFoto kosong
        events.forEach(event -> {
            if (event.getFileFoto() == null || event.getFileFoto().isEmpty()) {
                event.setFileFoto(null); // Set fileFoto ke null untuk kemudahan pengecekan
            }
        });

        model.addAttribute("events", eventService.getAllEvents());
        return "admin/onboarding";
    }

    // Menangani aksi saat tombol "End" diklik
    @PostMapping("/end_event")
    public String endEvent(@RequestParam("eventId") Long eventId) {
        try {
            // Perbarui flag menjadi false untuk semua user_event yang berhubungan dengan event ini
            userEventRepository.setFlagToFalse(eventId);
            userEventRepository.endEvent(eventId);

            // Mengarahkan kembali ke halaman yang menampilkan daftar event dengan status baru
            return "redirect:/onboarding_admin";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
}
