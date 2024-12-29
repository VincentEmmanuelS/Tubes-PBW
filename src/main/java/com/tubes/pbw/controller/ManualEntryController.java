package com.tubes.pbw.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tubes.pbw.model.ManualEntry;
import com.tubes.pbw.repository.ManualEntryRepository;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class ManualEntryController {    
    
    private static final String UPLOAD_DIR = "src/main/resources/static";

    @Autowired
    private ManualEntryRepository jdbc;

    @GetMapping("/manualentry")
    public String halamanManualEntry(ManualEntry manualEntry){
        return "manualEntry";
    }

    @PostMapping("/manualentry")
    public String insertDataActivity(@Valid ManualEntry manualEntry, BindingResult bindingResult){
        
        // test data 
        // System.out.println("Distance :"+ manualEntry.getDistance());
        // System.out.println("Matric Distance :"+manualEntry.getMatricDistance());
        // System.out.println("Duration :"+manualEntry.getDuration());
        // System.out.println("elevation :"+manualEntry.getElevation());
        // System.out.println("Matric elevation :"+manualEntry.getMatric_elevation());
        // System.out.println("Ride Type :"+manualEntry.getRideType());
        // System.out.println("Date :"+manualEntry.getDate().toString());
        // System.out.println("Title :"+manualEntry.getTitle());
        // System.out.println("Deskripsi :"+manualEntry.getDeskripsi());
        // System.out.println("Email :"+manualEntry.getEmail());
        // System.out.println("Foto: " + (manualEntry.getFoto() != null ? manualEntry.getFoto().getOriginalFilename() : "No file uploaded"));

        if(bindingResult.hasErrors()){
            return "manualEntry";
        }

        //distance error handling
        if(manualEntry.getDistance()==null){
            bindingResult.rejectValue("distance", "error.distance", "Please enter a value");
            return "manualEntry";
        }else if(manualEntry.getDistance()<0){
            bindingResult.rejectValue("distance", "error.distance", "Please enter a value positive number");
            return "manualEntry";
        }else{
            if(manualEntry.getMatricDistance().equals("km")){
                if(manualEntry.getDistance()>99999.99){
                    bindingResult.rejectValue("distance", "error.distance", "Please enter a value less than or equal to 9999.99");
                    return "manualEntry";
                }
            }else{
                if(manualEntry.getDistance()>9999999.99){
                    bindingResult.rejectValue("distance", "error.distance", "Please enter a value less than or equal to 999999.99");
                    return "manualEntry";
                } 
            }
        }

        //elevation error handling
        if(manualEntry.getElevation()==null){
            bindingResult.rejectValue("elevation", "error.elevation", "Please enter a value");
            return "manualEntry";
        }else if(manualEntry.getElevation()<0 || manualEntry.getElevation()>99999.99 ){
            bindingResult.rejectValue("elevation", "error.elevation", "Please enter a value positive number and less then or equal to 9999,99");
            return "manualEntry";
        }

        //title error handling
        if(manualEntry.getTitle().isEmpty()){
            bindingResult.rejectValue("title", "error.title", "Please specify the activity title");
            return "manualEntry";
        }

         //get from http sesion
        manualEntry.setEmail("user1@gmail.com");

        //handling file foto 
        if (!manualEntry.getFoto().isEmpty()) {
            String contentType = manualEntry.getFoto().getContentType();
            
            //format file handling
            if (contentType == null || 
                !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                bindingResult.rejectValue("foto", "error.foto", "Only JPEG and PNG files are allowed.");
                return "manualEntry"; // Return to the same form view with an error message
            }

            //save file foto to server
            try{
                Path uploadfile = Paths.get(UPLOAD_DIR+"/upload/");
                Files.createDirectories(uploadfile);

                String filename = UUID.randomUUID().toString() + "_" + manualEntry.getFoto().getOriginalFilename();
                
                manualEntry.setNamafoto("/upload/"+filename);

                Path destFile = uploadfile.resolve(filename);

                //insert to database
                jdbc.makeActivity(manualEntry);

                Files.copy(manualEntry.getFoto().getInputStream(), destFile);

            }catch(Exception e){
                bindingResult.rejectValue("foto", "error.foto", "Fail to upload file to server");
                return "manualEntry";
            }
            
        }else{
             
            //insert to database
            jdbc.makeActivity(manualEntry);
        }

       
       
        

        return "redirect:/result";
    }

    @GetMapping("/result")
    @ResponseBody
    public String berhasil(){
        return "berhasil";
    }
}
