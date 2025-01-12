package com.tubes.pbw.user.model;

public class UserEvent {
    private String email;
    private Long idEvent;
    private String flag;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
