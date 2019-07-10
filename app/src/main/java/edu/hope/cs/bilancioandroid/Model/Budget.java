package edu.hope.cs.bilancioandroid.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity (tableName = "budget")
public class Budget {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "category")
    private String category;

    private double amount;
    private String description;
    private String transactions;
    private int image;
    private String howMuchOver;
    private double allotted;

    public Budget(int uid, String category, double amount, String description) {
        this.uid = uid;
        this.category = category;
        this.amount = amount;
        this.description = description;

    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactions() {
        return transactions;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getHowMuchOver() {
        return howMuchOver;
    }

    public void setHowMuchOver(String howMuchOver) {
        this.howMuchOver = howMuchOver;
    }

    public double getAllotted() {
        return allotted;
    }

    public void setAllotted(double allotted) {
        this.allotted = allotted;
    }
}
