package com.habibfr.budget_buddy;

import java.io.Serializable;

public class User implements Serializable {
    private String user_id;
    private String fullname;
    private String email;
    private String password;
    private String created_at;
    private String updated_at;

    public User(String user_id, String fullname, String email, String password, String created_at, String updated_at) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public User(String user_id, String fullname, String email, String password) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }

    public User(String user_id, String fullname, String email) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
