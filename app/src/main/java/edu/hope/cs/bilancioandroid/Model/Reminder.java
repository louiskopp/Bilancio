package edu.hope.cs.bilancioandroid.Model;

import android.app.PendingIntent;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "reminder")
public class Reminder {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "title")
    private String title;
    private String type;
    private String date;
    private String howOften;

    public Reminder(int uid, String title, String type, String date) {
        this.uid = uid;
        this.title = title;
        this.type = type;
        this.date = date;
        this.howOften = howOften;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getHowOften() {
        return howOften;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setHowOften(String howOften) {
        this.howOften = howOften;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

}
