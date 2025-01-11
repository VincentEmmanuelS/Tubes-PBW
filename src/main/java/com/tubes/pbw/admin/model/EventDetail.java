package com.tubes.pbw.admin.model;

import java.sql.Timestamp;

public class EventDetail {
    private Long idEventDetail;
    private Double distance;
    private String matricDistance;
    private Double elevation;
    private String matricElevation;
    private String rideType;
    private Timestamp waktuMulai;
    private Timestamp waktuSelesai;
    private String title;
    private String deskripsi;
    private String fileFoto;
    private Integer limitParticipant;
    private String email;

    // Getters and Setters

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getMatricDistance() {
        return matricDistance;
    }

    public void setMatricDistance(String matricDistance) {
        this.matricDistance = matricDistance;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public String getMatricElevation() {
        return matricElevation;
    }

    public void setMatricElevation(String matricElevation) {
        this.matricElevation = matricElevation;
    }

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public Timestamp getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(Timestamp waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public Timestamp getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(Timestamp waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFileFoto() {
        return fileFoto;
    }

    public void setFileFoto(String fileFoto) {
        this.fileFoto = fileFoto;
    }

    public Integer getLimitParticipant() {
        return limitParticipant;
    }

    public void setLimitParticipant(Integer limitParticipant) {
        this.limitParticipant = limitParticipant;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIdEventDetail() {
        return idEventDetail;
    }

    public void setIdEventDetail(Long id) {
        this.idEventDetail = id;
    }
}
