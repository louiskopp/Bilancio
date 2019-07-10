package edu.hope.cs.bilancioandroid.Database;

import android.arch.persistence.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Model.Transaction;

public class Converters {
    @TypeConverter
    public static ArrayList<Transaction> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Transaction>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromTransactions(ArrayList<Transaction> transactions) {
        Gson gson = new Gson();
        String json = gson.toJson(transactions);
        return json;
    }

}
