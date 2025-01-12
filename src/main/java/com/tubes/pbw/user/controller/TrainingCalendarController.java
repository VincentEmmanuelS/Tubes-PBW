package com.tubes.pbw.user.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tubes.pbw.user.model.ManualEntry;
import com.tubes.pbw.user.repository.ManualEntryRepository;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class TrainingCalendarController {    
    
    private static final String UPLOAD_DIR = "src/main/resources/static";

    @Autowired
    private ManualEntryRepository jdbc;

    @GetMapping("/TrainingCalendar")
    public String halamanTrainingCalendar(){
        return "user/TrainingCalendar";
    }
}