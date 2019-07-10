package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class Results extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    String id;
    String correct = "Congratulations! That was the correct answer!";
    ArrayList<Budget> budgets;
    ArrayList<Budget> currentBudgets;
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    int oldNumberOfTransactions=0;
    int newNumberOfTransactions = 0;
    Converters converters;
    String category;
    String amountStr;
    Double amount;
    String type;
    TextView big;
    TextView message;
    String badAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TeachingMode);
        setContentView(R.layout.activity_results);
        big = (TextView) findViewById(R.id.correct);
        message = (TextView) findViewById(R.id.message_results);
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                currentBudgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                for(Budget budget: currentBudgets){
                    String tra = budget.getTransactions();
                    ArrayList<Transaction> transactions = converters.fromString(tra);
                    if(transactions==null){continue;}
                    newNumberOfTransactions+=transactions.size();
                }
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        id = prefs.getString("ID","");
        category = id.substring(2,3);
        switch(category){
            case("0"):
                category = "Clothing";
                break;
            case("1"):
                category = "Education";
                break;
            case("2"):
                category = "Entertainment";
                break;
            case("3"):
                category = "Food";
                break;
            case("4"):
                category = "Home Care";
                break;
            case("5"):
                category = "Personal Care";
                break;
            case("6"):
                category = "Phone";
                break;
            case("7"):
                category = "Rent";
                break;
            case("8"):
                category = "Transportation";
                break;
            case("9"):
                category = "Utilities";
                break;
        }
        type = id.substring(4,5);
        amountStr = id.substring(6);
        amount = Double.parseDouble(amountStr);
        amount = amount*.01;
        DecimalFormat df = new DecimalFormat("0.00");
        String entry = df.format(amount);
        amountStr = "$" + entry;
        String holder = prefs.getString("teaching budgets", "");
        Type listType = new TypeToken<ArrayList<Budget>>() {}.getType();
        budgets =  new Gson().fromJson(holder, listType);
        for(Budget budget: budgets){
            String tra = budget.getTransactions();
            ArrayList<Transaction> transactions = converters.fromString(tra);
            if(transactions==null){continue;}
            oldNumberOfTransactions+=transactions.size();
        }
        TextView done = (TextView) findViewById(R.id.done_results);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScenarioActivity.class);
                editor.putBoolean("Scenario", false);
                editor.putString("ID", "");
                editor.putString("teaching budgets", "");
                editor.apply();
                startActivity(intent);
            }
        });
        TextView retry = (TextView) findViewById(R.id.retry_results);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Overview.class);
                Thread go = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tDb.teachingModeDao().deleteAll();
                        for(Budget budget: budgets){
                            tDb.teachingModeDao().insertAll(budget);
                        }
                    }
                });
                go.start();
                try {
                    go.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        if(checkIfCorrect()){
            big.setText("Correct");
            big.setTextColor(Color.GREEN);
            message.setText(correct);
            retry.setVisibility(View.GONE);
        }
        else{
            big.setText("Incorrect");
            big.setTextColor(Color.RED);
            message.setText(badAnswer);
        }

    }

    public boolean checkIfCorrect(){
        switch (type){
            case("0"):
                Budget currentBudget = null;
                Budget oldBudget = null;
                for(Budget budget: budgets){
                    if(budget.getCategory().equals(category)){
                        oldBudget = budget;
                        break;
                    }
                }
                for(Budget budget: currentBudgets) {
                    if (budget.getCategory().equals(category)) {
                        currentBudget = budget;
                        break;
                    }
                }
                if(oldBudget.getAllotted()-amount==currentBudget.getAllotted()){
                    return true;
                }
                else{
                    badAnswer = "The budget category "+category+" was not properly changed.";
                    return false;
                }
            case("1"):
                currentBudget = null;
                oldBudget = null;
                for(Budget budget: budgets){
                    if(budget.getCategory().equals(category)){
                        oldBudget = budget;
                        break;
                    }
                }
                for(Budget budget: currentBudgets) {
                    if (budget.getCategory().equals(category)) {
                        currentBudget = budget;
                        break;
                    }
                }
                if(oldBudget.getAllotted()+amount==currentBudget.getAllotted()){
                    return true;
                }
                else{
                    badAnswer = "The budget category "+category+" was not properly changed.";
                    return false;
                }
            case("2"):
                double oldTotal = 0.0;
                double newTotal = 0.0;
                for(Budget budget: budgets){
                    oldTotal+=budget.getAllotted();
                }
                for(Budget budget: currentBudgets) {
                    newTotal+= budget.getAllotted();
                }
                if(oldTotal==newTotal-amount){
                    return true;
                }
                else{
                  badAnswer = "Income was not adjusted properly.";
                    return false;
                }
            case("3"):
                oldTotal = 0.0;
                newTotal = 0.0;
                for(Budget budget: budgets){
                    oldTotal+=budget.getAllotted();
                }
                for(Budget budget: currentBudgets) {
                    newTotal+= budget.getAllotted();
                }
                if(oldTotal==newTotal+amount){
                    return true;
                }
                else{
                    badAnswer = "Income was not adjusted properly.";
                    return false;
                }
            case("4"):
                oldBudget = null;
                for(Budget budget: budgets){
                    if(budget.getCategory().equals(category)){
                        oldBudget = budget;
                        break;
                    }
                }
                if(oldBudget.getAmount()<amount){
                    oldNumberOfTransactions++;
                    double over = amount - oldBudget.getAmount();
                    amount -= over;
                }
                if(newNumberOfTransactions<oldNumberOfTransactions+1){
                    badAnswer = "There was no additional transaction made.";
                    return false;
                }
                if(newNumberOfTransactions>oldNumberOfTransactions+1){
                    badAnswer = "There were too many transactions made.";
                    return false;
                }
                currentBudget = null;
                for(Budget budget: currentBudgets) {
                    if (budget.getCategory().equals(category)) {
                        currentBudget = budget;
                        break;
                    }
                }
                if(oldBudget.getAmount()==currentBudget.getAmount()+amount){
                    return true;
                }
                else{
                    Double difference = oldBudget.getAmount()-currentBudget.getAmount();
                    DecimalFormat df = new DecimalFormat("0.00");
                    String entry = df.format(difference);
                    entry = "$" + entry;
                    badAnswer = "The entered transaction had the wrong amount. The transaction had a value of " + entry +" and it should have been "+ amountStr +".";
                    return false;
                }
            case("5"):
                if(newNumberOfTransactions<oldNumberOfTransactions+1){
                    badAnswer = "There was no additional transaction made.";
                    return false;
                }
                if(newNumberOfTransactions>oldNumberOfTransactions+1){
                    badAnswer = "There were too many transactions made.";
                    return false;
                }
                currentBudget = null;
                oldBudget = null;
                for(Budget budget: budgets){
                    if(budget.getCategory().equals("Extra Money")){
                        oldBudget = budget;
                        break;
                    }
                }
                for(Budget budget: currentBudgets) {
                    if (budget.getCategory().equals("Extra Money")) {
                        currentBudget = budget;
                        break;
                    }
                }
                if(oldBudget.getAmount()==currentBudget.getAmount()-amount){
                    return true;
                }
                else{
                    Double difference = -oldBudget.getAmount()+currentBudget.getAmount();
                    DecimalFormat df = new DecimalFormat("0.00");
                    String entry = df.format(difference);
                    entry = "$" + entry;
                    badAnswer = "The entered transaction had the wrong amount. The transaction had a value of " + entry +" and it should have been "+ amountStr +".";
                    return false;
                }
            case("6"):
                String answer = prefs.getString("Answer", "");
                if (answer.equals("Yes")){
                    return true;
                }
                else{
                    currentBudget = null;
                    for(Budget budget: currentBudgets) {
                        if (budget.getCategory().equals(category)) {
                            currentBudget = budget;
                            break;
                        }
                    }
                    DecimalFormat df = new DecimalFormat("0.00");
                    String entry = df.format(currentBudget.getAmount());
                    entry = "$" + entry;
                   badAnswer = "The "+category+" Budget has "+entry+", which is greater than the amount of "+amountStr+". You can afford this.";
                    return false;
                }
            case("7"):
                answer = prefs.getString("Answer", "");
                if (answer.equals("No")){
                    return true;
                }
                else{
                    currentBudget = null;
                    for(Budget budget: currentBudgets) {
                        if (budget.getCategory().equals(category)) {
                            currentBudget = budget;
                            break;
                        }
                    }
                    DecimalFormat df = new DecimalFormat("0.00");
                    String entry = df.format(currentBudget.getAmount());
                    entry = "$" + entry;
                    badAnswer = "The "+category+" Budget has "+entry+", which is less than the amount of "+amountStr+". You can't afford this.";
                    return false;
                }
            case("8"):
                currentBudget = null;
                answer = prefs.getString("Answer", "");
                double money = Double.parseDouble(answer.replaceAll("[^\\d.]", ""));
                for(Budget budget: currentBudgets) {
                    if (budget.getCategory().equals(category)) {
                        currentBudget = budget;
                        break;
                    }
                }
                if(money==currentBudget.getAmount()-amount){
                    return true;
                }
                else{
                    currentBudget = null;
                    for(Budget budget: budgets) {
                        if (budget.getCategory().equals(category)) {
                            currentBudget = budget;
                            break;
                        }
                    }
                    Double left = currentBudget.getAmount();
                    DecimalFormat df = new DecimalFormat("0.00");
                    String entry = df.format(currentBudget.getAmount());
                    entry = "$" + entry;
                    Double that = left-amount;
                    String diff = df.format(that);
                    diff = "$" + diff;
                    badAnswer = "The transaction amount was "+amountStr+". The "+category+" Budget has "+ entry +", and after this transaction it would have "+ diff +" left.";
                    return false;
                }

            case("9"):
                answer = prefs.getString("Answer", "");
                if (answer.equals("No")){
                    return true;
                }
                else{
                    currentBudget = null;
                    for(Budget budget: currentBudgets) {
                        if (budget.getCategory().equals(category)) {
                            currentBudget = budget;
                            break;
                        }
                    }
                    DecimalFormat df = new DecimalFormat("0.00");
                    String entry = df.format(currentBudget.getAmount());
                    entry = "$" + entry;
                    badAnswer = "The "+category+" Budget has "+entry+", which is greater than the amount of "+amountStr+". You can afford this.";
                    return false;
                }
        }
        return false;
    }
}
