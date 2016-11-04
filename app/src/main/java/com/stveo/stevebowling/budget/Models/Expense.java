package com.stveo.stevebowling.budget.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by stevebowling on 11/2/16.
 */

public class Expense {

    @SerializedName("id")
    private Integer id;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("categoryId")
    private Integer categoryId;

    @SerializedName("date")
    private Date date;

    @SerializedName("note")
    private String note;


    public Expense() {

    }
    public Expense(Double amount, Integer categoryId, Date date, String note) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.date = date;
        this.note = note;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
