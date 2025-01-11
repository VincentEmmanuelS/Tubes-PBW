package com.tubes.pbw.admin.model;

public class Event {
    private Long idEvent;
    private String title;
    private String deskripsi;
    private Integer limitParticipant;
    private Integer participant = 0;
    private String fileFoto;
    private Long idDetail;

    // Getters and Setters

    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
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

    public Integer getLimitParticipant() {
        return limitParticipant;
    }

    public void setLimitParticipant(Integer limitParticipant) {
        this.limitParticipant = limitParticipant;
    }

    public Long getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(Long idDetail) {
        this.idDetail = idDetail;
    }

    public String getFileFoto() {
        return fileFoto;
    }

    public void setFileFoto(String fileFoto) {
        this.fileFoto = fileFoto;
    }

    public int getParticipant() {
        return participant;
    }

    public void setParticipant(int participant) {
        this.participant = participant;
    }
}

