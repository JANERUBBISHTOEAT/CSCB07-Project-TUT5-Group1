package com.example.sign_up;

// Include three sessions: Winter, Summer, Fall

import java.util.ArrayList;
import java.util.List;

public class Session {
    Boolean winter;
    Boolean summer;
    Boolean fall;

    public Session() {
        this.winter = false;
        this.summer = false;
        this.fall = false;
    }

    public Session(Boolean winter, Boolean summer, Boolean fall) {
        this.winter = winter;
        this.summer = summer;
        this.fall = fall;
    }

    public Boolean getWinter() {
        return winter;
    }

    public void setWinter(Boolean winter) {
        this.winter = winter;
    }

    public Boolean getSummer() {
        return summer;
    }

    public void setSummer(Boolean summer) {
        this.summer = summer;
    }

    public Boolean getFall() {
        return fall;
    }

    public void setFall(Boolean fall) {
        this.fall = fall;
    }
}