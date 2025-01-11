package com.tubes.pbw.user.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tubes.pbw.admin.model.EventDetail;
import com.tubes.pbw.admin.service.EventService;
import com.tubes.pbw.user.jdbc.ManualEntryJDBC;
import com.tubes.pbw.user.model.ManualEntry;
import com.tubes.pbw.user.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class SubmitEventController {

    @Autowired
    private ManualEntryJDBC manualEntryJDBC;

    @Autowired
    private EventService eventService;

    @GetMapping("/submitEvent")
    public String showEventForm(@RequestParam("id") Long id, Model model) {
        EventDetail eventDetail = eventService.getEventDetailById(id);
        if (eventDetail != null) {
            ManualEntry manualEntry = new ManualEntry();
            manualEntry.setDistance(eventDetail.getDistance());
            manualEntry.setMatricDistance(eventDetail.getMatricDistance());
            manualEntry.setElevation(eventDetail.getElevation());
            manualEntry.setMatric_elevation(eventDetail.getMatricElevation());
            manualEntry.setRideType(eventDetail.getRideType());
            manualEntry.setTitle(eventDetail.getTitle());
            manualEntry.setDeskripsi(eventDetail.getDeskripsi());
            manualEntry.setNamafoto(eventDetail.getFileFoto());

            // System.out.println(eventDetail.getIdEventDetail());
            // System.out.println("tanggal: " + manualEntry.getDate());

            model.addAttribute("manualEntry", manualEntry);
            model.addAttribute("eventDetail", eventDetail);
        }

        return "user/submitEvent"; // Return the template with event details populated
    }

    @PostMapping("/submitEvent")
    public String submitEventEntry(@ModelAttribute("manualEntry") ManualEntry manualEntry, 
                                    HttpSession session, 
                                    Model model) {
        
        // System.out.println("Title: " + manualEntry.getTitle());  // Debugging
        // System.out.println("Distance: " + manualEntry.getDistance());
        // System.out.println("Matric Distance: " + manualEntry.getMatricDistance());
        // System.out.println("Deskripsi: " + manualEntry.getDeskripsi());
        // System.out.println(manualEntry);

        // Mendapatkan data user dari session
        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "User  not logged in.");
            return "redirect:/login";
        }

        // Set email pengguna ke manualEntry
        manualEntry.setEmail(user.getEmail());
        System.out.println(manualEntry.getTitle());

        // Simpan manualEntry ke database
        try {
            // System.out.println("success");
            manualEntryJDBC.makeActivity(manualEntry); // Pastikan metode ini menyimpan ke tabel manualentrydummy
        } catch (Exception e) {
            // System.out.println("error");
            // e.printStackTrace();
            model.addAttribute("error", "Failed to submit the event. Please try again.");
            return "user/submitEvent"; // Kembali ke form jika ada kesalahan
        }

        return "redirect:/onboarding"; // Redirect setelah pengiriman berhasil
    }
}