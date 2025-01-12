package com.tubes.pbw.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tubes.pbw.auth.RequiredRole;
import com.tubes.pbw.user.model.DataStatic;
import com.tubes.pbw.user.model.Tahun;
import com.tubes.pbw.user.model.User;
import com.tubes.pbw.user.repository.StaticRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class StaticController {
    @Autowired
    private StaticRepository jdbc;

    @Autowired
    private HttpSession session;

    @GetMapping("myStatic")
    @RequiredRole("user")
    public String halamanStatic(
            @RequestParam(required = false) Integer yearDistance,
            @RequestParam(required = false) String ridetypeDistance,
            @RequestParam(required = false) String matricDistance,
            @RequestParam(required = false) Integer yearDuration,
            @RequestParam(required = false) Integer yearActivity,
            @RequestParam(required = false) String ridetypeActivity,
            Model model) {
        // userData
        User user = (User)session.getAttribute("user");

        // Set default values if not provided
        int defaultYear = 2025;
        String defaultRideType = "run";
        String defaultMetric = "km";
        
        // Use defaults if parameters are null
        yearDistance = yearDistance != null ? yearDistance : defaultYear;
        ridetypeDistance = ridetypeDistance != null ? ridetypeDistance : defaultRideType;
        matricDistance = matricDistance != null ? matricDistance : defaultMetric;
        yearDuration = yearDuration != null ? yearDuration : defaultYear;
        yearActivity = yearActivity != null ? yearActivity : defaultYear;
        ridetypeActivity = ridetypeActivity != null ? ridetypeActivity : defaultRideType;
    
        // Fetch data
        List<DataStatic> data = jdbc.findDistance(user.getEmail(), ridetypeDistance, yearDistance, matricDistance);
        List<DataStatic> dataDuration = jdbc.findDuration(user.getEmail(), yearDuration);
        List<DataStatic> dataActivity = jdbc.findActivity(user.getEmail(), ridetypeActivity, yearActivity);
        List<Tahun> listTahun = jdbc.listYear(user.getEmail());
    
        // Pass data and selected values to the view
        model.addAttribute("tahun", listTahun);
        model.addAttribute("data", data);
        model.addAttribute("dataDuration", dataDuration);
        model.addAttribute("dataActivity", dataActivity);
        model.addAttribute("yearDistance", yearDistance);
        model.addAttribute("ridetypeDistance", ridetypeDistance);
        model.addAttribute("matricDistance", matricDistance);
        model.addAttribute("yearDuration", yearDuration);
        model.addAttribute("yearActivity", yearActivity);
        model.addAttribute("ridetypeActivity", ridetypeActivity);
    
        return "/user/static";
    }
    
}
