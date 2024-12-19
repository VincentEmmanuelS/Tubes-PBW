package com.tubes.pbw.manualEntry;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ManualEntry {
    private Integer id;
    private Double distance;
    private String matricDistance;
    private LocalTime duration; //hh:mm:ss
    private Double elevation;
    private String matric_elevation;
    private String rideType;
    private LocalDateTime date;
    private String title;
    private String deskripsi;
    private String email;
    private String namafoto;
    private MultipartFile foto;
}
