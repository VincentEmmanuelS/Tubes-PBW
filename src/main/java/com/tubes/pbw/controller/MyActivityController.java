package com.tubes.pbw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tubes.pbw.model.ManualEntry;
import com.tubes.pbw.repository.ManualEntryRepository;



@Controller
public class MyActivityController {
    
    @Autowired
    private ManualEntryRepository jdbc;
    
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
        return "MyActivity";
    }

    @PostMapping("delete")
    public String deleteData(@RequestParam("id") Integer id){
        jdbc.deleteManualentry(id);
        return "MyActivity";
    }
}
