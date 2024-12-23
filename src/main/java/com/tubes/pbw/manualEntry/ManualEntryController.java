package com.tubes.pbw.manualEntry;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class ManualEntryController {    
    
    @GetMapping("/manualentry")
    public String halamanManualEntry(ManualEntry manualEntry){
        return "/ManualEntry/ManualEntry";
    }

    @PostMapping("/manualentry")
    public String insertDataActivity(@Valid ManualEntry manualEntry, BindingResult bindingResult){
        

        System.out.println("Distance :"+ manualEntry.getDistance());
        System.out.println("Matric Distance :"+manualEntry.getMatricDistance());
        System.out.println("Duration :"+manualEntry.getDuration());
        System.out.println("elevation :"+manualEntry.getElevation());
        System.out.println("Matric elevation :"+manualEntry.getMatric_elevation());
        System.out.println("Ride Type :"+manualEntry.getRideType());
        System.out.println("Date :"+manualEntry.getDate().toString());
        System.out.println("Title :"+manualEntry.getTitle());
        System.out.println("Deskripsi :"+manualEntry.getDeskripsi());
        System.out.println("Email :"+manualEntry.getEmail());
        System.out.println("Foto: " + (manualEntry.getFoto() != null ? "File uploaded" : "No file uploaded"));


        return "redirect:/result";
    }

    @GetMapping("/result")
    @ResponseBody
    public String berhasil(){
        return "berhasil";
    }
}
