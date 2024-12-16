package com.tubes.pbw.manualEntry;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManualEntryController {    
    
    @GetMapping("/manualentry")
    public String halamanManualEntry(){
        return "/ManualEntry/ManualEntry";
    }
}
