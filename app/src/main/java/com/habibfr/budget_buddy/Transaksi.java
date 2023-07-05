package com.habibfr.budget_buddy;

import java.util.Date;

public class Transaksi {
    private int transaction_id;
    private int user_id;
    private String title;
    private String date;
    private String type;
    private String amount;
    private String additional_info;
    private String created_at;

    public Transaksi(int transaction_id, int user_id, String title, String date, String type, String amount, String additional_info, String created_at) {
        this.transaction_id = transaction_id;
        this.user_id = user_id;
        this.title = title;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.additional_info = additional_info;
        this.created_at = created_at;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
