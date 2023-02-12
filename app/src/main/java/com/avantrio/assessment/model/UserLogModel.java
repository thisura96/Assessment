package com.avantrio.assessment.model;

public class UserLogModel {
    String  user_log_date,user_log_time,user_log_location,user_log_distance;
    boolean user_log_alert_view;

    public UserLogModel(String user_log_date, String user_log_time, boolean user_log_alert_view, String user_log_location, String user_log_distance) {
        this.user_log_date = user_log_date;
        this.user_log_time = user_log_time;
        this.user_log_alert_view = user_log_alert_view;
        this.user_log_location = user_log_location;
        this.user_log_distance = user_log_distance;
    }

    public String getUser_log_date() {
        return user_log_date;
    }

    public void setUser_log_date(String user_log_date) {
        this.user_log_date = user_log_date;
    }

    public String getUser_log_time() {
        return user_log_time;
    }

    public void setUser_log_time(String user_log_time) {
        this.user_log_time = user_log_time;
    }

    public boolean getUser_log_alert_view() {
        return user_log_alert_view;
    }

    public void setUser_log_alert_view(boolean user_log_alert_view) {
        this.user_log_alert_view = user_log_alert_view;
    }

    public String getUser_log_location() {
        return user_log_location;
    }

    public void setUser_log_location(String user_log_location) {
        this.user_log_location = user_log_location;
    }

    public String getUser_log_distance() {
        return user_log_distance;
    }

    public void setUser_log_distance(String user_log_distance) {
        this.user_log_distance = user_log_distance;
    }
}
