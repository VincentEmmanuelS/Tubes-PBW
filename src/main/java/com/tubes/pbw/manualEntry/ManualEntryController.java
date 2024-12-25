package com.tubes.pbw.manualEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class ManualEntryController {    
    
    private static final String UPLOAD_DIR = "src/main/resources/static";

    @Autowired
    private ManualEntryRepository jdbc;

    @GetMapping("/manualentry")
    public String halamanManualEntry(ManualEntry manualEntry){
        return "/ManualEntry/ManualEntry";
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
            return "/ManualEntry/ManualEntry";
        }

        if (!manualEntry.getFoto().isEmpty()) {
            String contentType = manualEntry.getFoto().getContentType();
            if (contentType == null || 
                !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                bindingResult.rejectValue("foto", "error.foto", "Only JPEG and PNG files are allowed.");
                return "/ManualEntry/ManualEntry"; // Return to the same form view with an error message
            }
            try{
                Path uploadfile = Paths.get(UPLOAD_DIR+"/imageActivity/");
                Files.createDirectories(uploadfile);

                String filename = UUID.randomUUID().toString() + "_" + manualEntry.getFoto().getOriginalFilename();
                
                manualEntry.setNamafoto("/imageActivity/"+filename);

                Path destFile = uploadfile.resolve(filename);

                Files.copy(manualEntry.getFoto().getInputStream(), destFile);

            }catch(Exception e){
                bindingResult.rejectValue("foto", "error.foto", "Fail to upload file to server");
                return "/ManualEntry/ManualEntry";
            }
            
        }
        manualEntry.setEmail("user1@gmail.com");

        jdbc.makeActivity(manualEntry);
        

        return "redirect:/result";
    }

    @GetMapping("/result")
    @ResponseBody
    public String berhasil(){
        return "berhasil";
    }
}
