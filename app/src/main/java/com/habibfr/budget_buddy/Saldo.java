package com.habibfr.budget_buddy;

public class Saldo {
    private int user_id;
    private String fullname;
    private String saldo;

    public Saldo(int user_id, String fullname, String saldo) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.saldo = saldo;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
}
