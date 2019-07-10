package edu.hope.cs.bilancioandroid.Model;

import java.text.DecimalFormat;

public class Scenario {

    private String timeContext;
    private String categoryString;
    private String type;
    private Double amount;
    private String instructionPhrase;
    private String moneyStr;
    private String specificBudgetAction;
    private String fullStr;
    private String categoryHolder;

    public Scenario(String id){
        timeContext = id.substring(0,1);
        int time = Integer.valueOf(timeContext);
        categoryString = id.substring(2,3);
        type = id.substring(4,5);
        String hold = id.substring(6);
        amount = Double.parseDouble(hold);
        amount = amount*.01;
        DecimalFormat df = new DecimalFormat("0.00");
        moneyStr = df.format(amount);
        moneyStr = "$" + moneyStr;
        if(time>2){
            time = time%3;
            timeContext = String.valueOf(time);
        }

        switch(timeContext){
            case("0"):
                timeContext = "It's the beginning of the month.";
                break;
            case("1"):
                timeContext = "It's the middle of the month.";
                break;
            case("2"):
                timeContext = "It's the end of the month.";
                break;
        }

        switch(categoryString){
            case("0"):
                categoryString = "Clothing";
                break;
            case("1"):
                categoryString = "Education";
                break;
            case("2"):
                categoryString = "Entertainment";
                break;
            case("3"):
                categoryString = "Food";
                break;
            case("4"):
                categoryString = "Home Care";
                break;
            case("5"):
                categoryString = "Personal Care";
                break;
            case("6"):
                categoryString = "Phone";
                break;
            case("7"):
                categoryString = "Rent";
                break;
            case("8"):
                categoryString = "Transportation";
                break;
            case("9"):
                categoryString = "Utilities";
                break;
        }

        switch(type){
            case("0"):
                type  = "adjustmentPositive";
                break;
            case("1"):
                type  = "adjustmentNegative";
                break;
            case("2"):
                type  = "adjustmentIncomePositive";
                break;
            case("3"):
                type  = "adjustmentIncomeNegative";
                break;
            case("4"):
                type  = "transactionExpense";
                break;
            case("5"):
                type  = "transactionIncome";
                categoryHolder = categoryString;
                categoryString="Income";
                break;
            case("6"):
                type  = "predictiveTrue";
                break;
            case("7"):
                type  = "predictiveFalse";
                break;
            case("8"):
                type  = "howMuchIsLeftTransaction";
                break;
            case("9"):
                type  = "predictiveFalse";
                break;
        }

        if(type.equals("adjustmentIncomePositive")){
            instructionPhrase = "Adjust your budgets to account for a " + moneyStr + " increase in income.";
        }  else if (type.equals("adjustmentIncomeNegative")) {
            instructionPhrase =  "Adjust your budgets to account for a " + moneyStr + " decrease in income.";
        } else if (type.equals("adjustmentNegative")) {
            instructionPhrase = "Adjust your budgets to account for a " + moneyStr + " increase in your "+ categoryString +" budget.";
        } else if (type.equals("adjustmentPositive")) {
            instructionPhrase = "Adjust your budgets to account for a "+moneyStr+" decrease in your "+categoryString+" budget.";
        } else if(type.equals("transactionExpense")) {
            instructionPhrase = "You spent "+moneyStr+".  Enter this transaction.";
        } else if(type.equals("transactionIncome")) {
            instructionPhrase = "You got "+moneyStr+".  Enter this income.";
        } else if(type.equals("predictiveTrue")) {
            instructionPhrase = "It cost "+moneyStr+".  Can you afford this?";
        } else if(type.equals("predictiveFalse")) {
            instructionPhrase = "It cost "+moneyStr+".  Can you afford this?";
        } else if(type.equals("howMuchIsLeftTransaction")) {
            instructionPhrase = "It will cost "+moneyStr+". If you make this purchase, how much would you have left in your "+categoryString+" budget?";
        } else if (categoryString.equals("Rent") || categoryString.equals("Phone") || categoryString.equals("Utilities")){
            instructionPhrase = "It cost "+moneyStr+".  Enter this transaction.";
        } else {
            instructionPhrase ="We have a problem in the amount generating statement in buildFromInput";
        }

        switch (categoryString) {
            case "Clothing":
                switch (type) {
                    case "transactionExpense":
                        specificBudgetAction = "You just bought some clothes at the mall.";
                        break;
                    case "predictiveFalse":
                        specificBudgetAction = "You want to buy a new shirt.";
                        break;
                    case "howMuchIsLeftTransaction":
                        specificBudgetAction = "You want to buy a cool hat.";
                        break;
                    case "predictiveTrue":
                        specificBudgetAction = "You want to buy some new pants.";
                        break;
                }
                break;
            case "Education":
                switch (type) {
                    case "transactionExpense":
                        specificBudgetAction = "You just bought all of your textbooks.";
                        break;
                    case "predictiveFalse":
                        specificBudgetAction = "You want to buy the largest pad of sticky notes that they sell.";
                        break;
                    case "howMuchIsLeftTransaction":
                        specificBudgetAction = "You want to buy some more pencils for class.";
                        break;
                    case "predictiveTrue":
                        specificBudgetAction = "You want a fancy new pen for taking notes.";
                        break;
                }
                break;
            case "Entertainment":
                switch (type) {
                    case "transactionExpense":
                        specificBudgetAction = "You spent a night out with friends at the movie theater";
                        break;
                    case "predictiveFalse":
                        specificBudgetAction = "You want to go bowling.";
                        break;
                    case "howMuchIsLeftTransaction":
                        specificBudgetAction = "You want to buy a new video game.";
                        break;
                    case "predictiveTrue":
                        specificBudgetAction = "You want to go see the newest action movie.";
                        break;
                }
                break;
            case "Food":
                switch (type) {
                    case "transactionExpense":
                        specificBudgetAction = "You just got back from the grocery store.";
                        break;
                    case "predictiveFalse":
                        specificBudgetAction = "You really want a pizza right now.";
                        break;
                    case "howMuchIsLeftTransaction":
                        specificBudgetAction = "You want to buy your groceries.";
                        break;
                    case "predictiveTrue":
                        specificBudgetAction = "You really want ice cream from the store.";
                        break;
                }
                break;
            case "Home Care":
                switch (type) {
                    case "transactionExpense":
                        specificBudgetAction = "You bought a pack of batteries at the store.";
                        break;
                    case "predictiveFalse":
                        specificBudgetAction = "You want to get a spare pack of batteries.";
                        break;
                    case "howMuchIsLeftTransaction":
                        specificBudgetAction = "You want to buy a new vaccuum cleaner.";
                        break;
                    case "predictiveTrue":
                        specificBudgetAction = "You want some new, energy efficient light bulbs.";
                        break;
                }
                break;
            case "Personal Care":
                switch (type) {
                    case "transactionExpense":
                        specificBudgetAction = "You bought a lot of Personal Care items at the store.";
                        break;
                    case "predictiveFalse":
                        specificBudgetAction = "You want to get a fancy new bath towel.";
                        break;
                    case "howMuchIsLeftTransaction":
                        specificBudgetAction = "You want to buy shower supplies.";
                        break;
                    case "predictiveTrue":
                        specificBudgetAction = "You want a new electric toothbrush.";
                        break;
                }
                break;
            case "Phone":
                switch (type) {
                    case "transactionExpense":
                        specificBudgetAction = "You just paid your phone bill.";
                        break;
                    case "predictiveFalse":
                        specificBudgetAction = "You want to buy a new phone.";
                        break;
                    case "howMuchIsLeftTransaction":
                        specificBudgetAction = "You want to buy a new phone charger.";
                        break;
                    case "predictiveTrue":
                        specificBudgetAction = "You want to change phone companies.";
                        break;
                }
                break;
            case "Rent":
                switch (type) {
                    case "transactionExpense":
                        specificBudgetAction = "You just paid your rent.";
                        break;
                    case "predictiveFalse":
                        specificBudgetAction = "Your rent just went up.";
                        break;
                    case "howMuchIsLeftTransaction":
                        specificBudgetAction = "You just paid your rent.";
                        break;
                    case "predictiveTrue":
                        specificBudgetAction = "Your rent just went down.";
                        break;
                }
                break;
            case "Transportation":
                switch (type) {
                    case "transactionExpense":
                        specificBudgetAction = "You just bought gas.";
                        break;
                    case "predictiveFalse":
                        specificBudgetAction = "You want to get new seat covers for your car.";
                        break;
                    case "howMuchIsLeftTransaction":
                        specificBudgetAction = "You want to buy a new bike.";
                        break;
                    case "predictiveTrue":
                        specificBudgetAction = "You want to get a new coat of paint for your bike.";
                        break;
                }
                break;
            case "Utilities":
                switch (type) {
                    case "transactionExpense":
                        specificBudgetAction = "You just paid your Utilities bill.";
                        break;
                    case "predictiveFalse":
                        specificBudgetAction = "Your Utilities bill just went up.";
                        break;
                    case "howMuchIsLeftTransaction":
                        specificBudgetAction = "You just paid your Utilities bill.";
                        break;
                    case "predictiveTrue":
                        specificBudgetAction = "Your Utilities bill just went down.";
                        break;
                }
                break;
            case "Income":
                if (type.equals("transactionIncome")) {
                    specificBudgetAction = "A relative just sent you a birthday check!";
                    categoryString = categoryHolder;
                }
                break;
        }
       if(specificBudgetAction!=null){
           fullStr = timeContext+" "+specificBudgetAction+" "+instructionPhrase;
       }
       else{fullStr=timeContext+" "+instructionPhrase;}
    }

    public String getFullStr() {
        return fullStr;
    }

    public String getCategoryString() {
        return categoryString;
    }

    public String getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getInstructionPhrase() {
        return instructionPhrase;
    }

    public String getMoneyStr() {
        return moneyStr;
    }

    public String getSpecificBudgetAction() {
        return specificBudgetAction;
    }

    public String getTimeContext() {
        return timeContext;
    }

    public void setCategoryString(String categoryString) {
        this.categoryString = categoryString;
    }

    public void setFullStr(String fullStr) {
        this.fullStr = fullStr;
    }

    public void setInstructionPhrase(String instructionPhrase) {
        this.instructionPhrase = instructionPhrase;
    }

    public void setMoneyStr(String moneyStr) {
        this.moneyStr = moneyStr;
    }

    public void setSpecificBudgetAction(String specificBudgetAction) {
        this.specificBudgetAction = specificBudgetAction;
    }

    public void setTimeContext(String timeContext) {
        this.timeContext = timeContext;
    }
}
