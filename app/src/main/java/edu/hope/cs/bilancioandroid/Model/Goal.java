package edu.hope.cs.bilancioandroid.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "goal")
public class Goal {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "nameOfGoal")
    private String nameOfGoal;

    private double amountTotal;
    private double amountSaved;
    private String date;

    public Goal(int uid, String nameOfGoal, double amountTotal, double amountSaved){
        this.uid = uid;
        this.nameOfGoal = nameOfGoal;
        this.amountTotal = amountTotal;
        this.amountSaved = amountSaved;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public double getAmountSaved() {
        return amountSaved;
    }

    public double getAmountTotal() {
        return amountTotal;
    }

    public String getNameOfGoal() {
        return nameOfGoal;
    }

    public void setAmountSaved(double amountSaved) {
        this.amountSaved = amountSaved;
    }

    public void setAmountTotal(double amountTotal) {
        this.amountTotal = amountTotal;
    }

    public void setNameOfGoal(String nameOfGoal) {
        this.nameOfGoal = nameOfGoal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
