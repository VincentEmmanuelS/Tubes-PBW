package com.tubes.pbw.user.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ManualEntry {
    private Integer id;
    private Double distance;
    private String matricDistance;
    private LocalTime duration; //hh:mm:ss
    private Double elevation;
    private String matric_elevation;
    private String rideType;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;
    private String title;
    private String deskripsi;
    private String email;
    //bisa null 
    private String namafoto;
    //bisa null
    private MultipartFile foto;
}
