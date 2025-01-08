package com.tubes.pbw.user.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tubes.pbw.user.model.ManualEntry;
import com.tubes.pbw.user.repository.ManualEntryRepository;

import jakarta.validation.Valid;



@Controller
public class MyActivityController {
    
    @Autowired
    private ManualEntryRepository jdbc;
    
    private static final String UPLOAD_DIR = "src/main/resources/static";

    @GetMapping("myActivity")
    
    public String halamanActivity(@RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "sortBy", defaultValue = "tanggal_event") String sortBy,
                                  @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder,
                                  Model model) {
        List<ManualEntry> result;

        if (keyword != null && !keyword.isEmpty()) {
            // Perform search by title if a keyword is provided
            result = jdbc.findByTitleContaining(keyword, "user1@gmail.com", sortBy, sortOrder);
        } else {
            // Otherwise, fetch all entries for the user
            result = jdbc.findAllEntry("user1@gmail.com", sortBy, sortOrder);
        }

        model.addAttribute("object", result);
        model.addAttribute("keyword", keyword);  // Add the keyword to preserve in the view
        model.addAttribute("sortBy", sortBy);    // Add sorting info for the view
        model.addAttribute("sortOrder", sortOrder);  // Add sorting info for the view
        return "user/MyActivity";
    }

    @PostMapping("delete")
    public String deleteData(@RequestParam("id") Integer id){
        ManualEntry data = jdbc.getEntry(id).get();
        System.out.println(data.getNamafoto());
        if (!data.getNamafoto().isEmpty() && data.getNamafoto() != null) {
            System.out.println("masuk if");
        
            // Get the absolute path of the static folder
            String staticDir = new File("src/main/resources/static").getAbsolutePath();
        
            // Construct the full path to the file
            String oldPathfoto = staticDir + data.getNamafoto();
            Path oldFile = Paths.get(oldPathfoto);
        
            try {
                // Attempt to delete the file
                if (Files.deleteIfExists(oldFile)) {
                    System.out.println("File deleted successfully.");
                } else {
                    System.out.println("File not found: " + oldFile.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        jdbc.deleteManualentry(id);
        return "redirect:/myActivity";
    }

    @GetMapping("edit")
    public String halamanEdit(@RequestParam("id") Integer id, Model model) {
        Optional<ManualEntry> optionalEntry = jdbc.getEntry(id);
        if (optionalEntry.isPresent()) {
            ManualEntry manualEntry = optionalEntry.get();
            // Debugging: Print the values
            // System.out.println("Duration: " + manualEntry.getDuration());
            System.out.println("Date: " + manualEntry.getDate());
            String durasi = manualEntry.getDuration().toString();
            String tanggal = manualEntry.getDate().toString();
            System.out.println("Duration: " + durasi);
            System.out.println("Date: " + tanggal);
            model.addAttribute("durasi", durasi);
            model.addAttribute("manualEntry", manualEntry);
        } else {
            // Handle missing entry
        }
        return "user/editPage";
        
    }

    @PostMapping("/update")
    public String updateData(@Valid ManualEntry manualEntry, BindingResult bindingResult, Model model) {
        System.out.println("Id :" + manualEntry.getId()); 
        System.out.println("Distance :" + manualEntry.getDistance());
        System.out.println("Matric Distance :" + manualEntry.getMatricDistance());
        System.out.println("Duration :" + manualEntry.getDuration());
        System.out.println("Elevation :" + manualEntry.getElevation());
        System.out.println("Matric Elevation :" + manualEntry.getMatric_elevation());
        System.out.println("Ride Type :" + manualEntry.getRideType());
        System.out.println("Date :" + (manualEntry.getDate() != null ? manualEntry.getDate().toString() : "null"));
        System.out.println("Title :" + manualEntry.getTitle());
        System.out.println("Deskripsi :" + manualEntry.getDeskripsi());
        System.out.println("Email :" + manualEntry.getEmail());
        System.out.println("Foto: " + (manualEntry.getFoto() != null ? manualEntry.getFoto().getOriginalFilename() : "No file uploaded"));
        System.out.println("Nama Foto : " + manualEntry.getNamafoto());

        // If there are validation errors, return the form with existing data and errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("manualEntry", manualEntry);
            return "user/editPage";
        }

     
        //check kalau ada foto yang mau diganti
        if (!manualEntry.getFoto().isEmpty()) {
            //chechk jika ada file sebelumnya
            if (!manualEntry.getNamafoto().isEmpty() && manualEntry.getNamafoto() != null) {
                System.out.println("masuk if");
            
                // Get the absolute path of the static folder
                String staticDir = new File("src/main/resources/static").getAbsolutePath();
            
                // Construct the full path to the file
                String oldPathfoto = staticDir + manualEntry.getNamafoto();
                Path oldFile = Paths.get(oldPathfoto);
            
                try {
                    // Attempt to delete the file
                    if (Files.deleteIfExists(oldFile)) {
                        System.out.println("File deleted successfully.");
                    } else {
                        System.out.println("File not found: " + oldFile.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            String contentType = manualEntry.getFoto().getContentType();
            
            //format file handling
            if (contentType == null || 
                !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                bindingResult.rejectValue("foto", "error.foto", "Only JPEG and PNG files are allowed.");
                model.addAttribute("manualEntry", manualEntry);
                return "user/editPage";
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
                model.addAttribute("manualEntry", manualEntry);
                return "user/editPage";
            }
            
        }

        jdbc.UpdateData(manualEntry);

        // Reload the edit page with updated data
        return "redirect:/myActivity";
    }
}
