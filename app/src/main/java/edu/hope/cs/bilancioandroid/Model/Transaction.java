package edu.hope.cs.bilancioandroid.Model;

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Transaction {
    private double amount;
    private String store;
    private String category;
    private String date;
    private Date dateTime;

    public Transaction(double a, String s, String c, String d) {
        amount = a;
        store = s;
        category = c;
        date = d;
        String monthWord = date.split(" ")[0];
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthWord);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        int month1 = cal.get(Calendar.MONTH);
        String month = Integer.toString(month1+1);
        String year = date.split(" ")[2];
        String day = date.split(" ")[1];
        day = day.replaceAll("[^\\d.]", "");
        String newDate = day+"/"+month+"/"+year;
        try {
            dateTime=new SimpleDateFormat("dd/MM/yyyy").parse(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public double getAmount() {
        return amount;
    }
    public String getStore() {
        return store;
    }
    public String getCategory() {
        return category;
    }
    public String getDate() {
        return date;
    }

    public static Comparator<Transaction> compareCategories(){
        return new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(o1.category, o2.category);
                if (res == 0) {
                    res = o1.category.compareTo(o2.category);
                }
                return res;
            }
        };
    }

    public static Comparator<Transaction> compareAmount() {
        return new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                if(o1.amount==o2.amount)
                    return 0;
                else if(o1.amount>o2.amount)
                    return 1;
                else
                    return -1;
            }
        };

    }

    public static Comparator<Transaction> compareAmountLow() {
        return new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                if(o1.amount==o2.amount)
                    return 0;
                else if(o1.amount<o2.amount)
                    return 1;
                else
                    return -1;
            }
        };

    }

    public static Comparator<Transaction> compareDate() {
        return new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                if(o1.dateTime.before(o2.dateTime))
                    return -1;
                else if(o1.dateTime.after(o2.dateTime))
                    return 1;
                else
                    return -1;
            }
        };

    }
}


