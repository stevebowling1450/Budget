package com.stveo.stevebowling.budget.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by stevebowling on 11/1/16.
 */

public class Category {


    @SerializedName("id")
    private Integer id;


    @SerializedName("name")
    private String name;


    @SerializedName("amount")
    private Double amount;

    @SerializedName("startDate")
    private Date startDate;

    public Category() {
    }


    public Category(String name, Double amount) {
        this.name = name;
        this.amount = amount;
        this.startDate = new Date();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
