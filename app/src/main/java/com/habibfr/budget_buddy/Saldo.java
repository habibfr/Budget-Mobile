package com.habibfr.budget_buddy;

public class Saldo {
    private int saldo;
    private String type;

    public Saldo(int saldo, String type) {
        this.saldo = saldo;
        this.type = type;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
