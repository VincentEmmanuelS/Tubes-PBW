package com.tubes.pbw.manualEntry;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ManualEntry {
    private Integer id;
    private Double distance;
    private String matricDistance;
    private String duration;
    private Double elevation;
    private String matric_elevation;
    private String rideType;
    private LocalDateTime date;
    private String time;
    private String title;
    private String deskripsi;
    private String foto;
    private String email;
}
