package edu.hope.cs.bilancioandroid;

import org.junit.Test;

import edu.hope.cs.bilancioandroid.Model.Scenario;

import static org.junit.Assert.assertEquals;

public class ScenarioTest {
    private Scenario scenario0 = new Scenario("0.0.0.00000000");
    private Scenario scenario1 = new Scenario("1.1.1.11111111");
    private Scenario scenario2 = new Scenario("2.2.2.22222222");
    private Scenario scenario3 = new Scenario("3.3.3.33333333");
    private Scenario scenario4 = new Scenario("4.4.4.44444444");
    private Scenario scenario5 = new Scenario("5.5.5.55555555");
    private Scenario scenario6 = new Scenario("6.6.6.66666666");
    private Scenario scenario7 = new Scenario("7.7.7.77777777");
    private Scenario scenario8 = new Scenario("8.8.8.88888888");
    private Scenario scenario9 = new Scenario("9.9.9.99999999");

    @Test
    public void checkTimeFrame(){
        assertEquals("It's the beginning of the month." ,scenario0.getTimeContext());
        assertEquals("It's the middle of the month." ,scenario1.getTimeContext());
        assertEquals("It's the end of the month." ,scenario2.getTimeContext());
        assertEquals("It's the beginning of the month." ,scenario3.getTimeContext());
        assertEquals("It's the middle of the month." ,scenario4.getTimeContext());
        assertEquals("It's the end of the month." ,scenario5.getTimeContext());
        assertEquals("It's the beginning of the month." ,scenario6.getTimeContext());
        assertEquals("It's the middle of the month." ,scenario7.getTimeContext());
        assertEquals("It's the end of the month." ,scenario8.getTimeContext());
        assertEquals("It's the beginning of the month." ,scenario9.getTimeContext());
    }

    @Test
    public void categoryTest(){
        assertEquals("Clothing" ,scenario0.getCategoryString());
        assertEquals("Education" ,scenario1.getCategoryString());
        assertEquals("Entertainment" ,scenario2.getCategoryString());
        assertEquals("Food" ,scenario3.getCategoryString());
        assertEquals("Home Care" ,scenario4.getCategoryString());
        assertEquals("Personal Care" ,scenario5.getCategoryString());
        assertEquals("Phone" ,scenario6.getCategoryString());
        assertEquals("Rent" ,scenario7.getCategoryString());
        assertEquals("Transportation" ,scenario8.getCategoryString());
        assertEquals("Utilities" ,scenario9.getCategoryString());
    }

    @Test
    public void typeTest(){
        assertEquals("adjustmentPositive" ,scenario0.getType());
        assertEquals("adjustmentNegative" ,scenario1.getType());
        assertEquals("adjustmentIncomePositive" ,scenario2.getType());
        assertEquals("adjustmentIncomeNegative" ,scenario3.getType());
        assertEquals("transactionExpense" ,scenario4.getType());
        assertEquals("transactionIncome" ,scenario5.getType());
        assertEquals("predictiveTrue" ,scenario6.getType());
        assertEquals("predictiveFalse" ,scenario7.getType());
        assertEquals("howMuchIsLeftTransaction" ,scenario8.getType());
        assertEquals("predictiveFalse" ,scenario9.getType());
    }

    @Test
    public void specificActionTest(){
        assertEquals(null ,scenario0.getSpecificBudgetAction());
        assertEquals(null ,scenario1.getSpecificBudgetAction());
        assertEquals(null ,scenario2.getSpecificBudgetAction());
        assertEquals(null ,scenario3.getSpecificBudgetAction());
        assertEquals("You bought a pack of batteries at the store." ,scenario4.getSpecificBudgetAction());
        assertEquals("A relative just sent you a birthday check!" ,scenario5.getSpecificBudgetAction());
        assertEquals("You want to change phone companies." ,scenario6.getSpecificBudgetAction());
        assertEquals("Your rent just went up." ,scenario7.getSpecificBudgetAction());
        assertEquals("You want to buy a new bike." ,scenario8.getSpecificBudgetAction());
        assertEquals("Your Utilities bill just went up." ,scenario9.getSpecificBudgetAction());
    }

    @Test
    public void moneyStrTest(){
        assertEquals("$0.00" ,scenario0.getMoneyStr());
        assertEquals("$111111.11" ,scenario1.getMoneyStr());
        assertEquals("$222222.22" ,scenario2.getMoneyStr());
        assertEquals("$333333.33" ,scenario3.getMoneyStr());
        assertEquals("$444444.44" ,scenario4.getMoneyStr());
        assertEquals("$555555.55" ,scenario5.getMoneyStr());
        assertEquals("$666666.66" ,scenario6.getMoneyStr());
        assertEquals("$777777.77" ,scenario7.getMoneyStr());
        assertEquals("$888888.88" ,scenario8.getMoneyStr());
        assertEquals("$999999.99" ,scenario9.getMoneyStr());
    }

    @Test
    public void amountTest(){
        assertEquals(0.00 ,scenario0.getAmount(), 5);
        assertEquals(111111.11 ,scenario1.getAmount(), 5);
        assertEquals(222222.22 ,scenario2.getAmount(), 5);
        assertEquals(333333.33 ,scenario3.getAmount(), 5);
        assertEquals(444444.44 ,scenario4.getAmount(), 5);
        assertEquals(555555.55 ,scenario5.getAmount(), 5);
        assertEquals(666666.66 ,scenario6.getAmount(), 5);
        assertEquals(777777.77 ,scenario7.getAmount(), 5);
        assertEquals(888888.88 ,scenario8.getAmount(), 5);
        assertEquals(999999.99 ,scenario9.getAmount(), 5);
    }

    @Test
    public void instructionPhraseTest(){
        assertEquals("Adjust your budgets to account for a "+scenario0.getMoneyStr()+" decrease in your "+scenario0.getCategoryString()+" budget." ,scenario0.getInstructionPhrase());
        assertEquals("Adjust your budgets to account for a " + scenario1.getMoneyStr() + " increase in your "+ scenario1.getCategoryString() +" budget." ,scenario1.getInstructionPhrase());
        assertEquals("Adjust your budgets to account for a " + scenario2.getMoneyStr() + " increase in income." ,scenario2.getInstructionPhrase());
        assertEquals("Adjust your budgets to account for a " + scenario3.getMoneyStr() + " decrease in income." ,scenario3.getInstructionPhrase());
        assertEquals("You spent "+scenario4.getMoneyStr()+".  Enter this transaction." ,scenario4.getInstructionPhrase());
        assertEquals("You got "+scenario5.getMoneyStr()+".  Enter this income." ,scenario5.getInstructionPhrase());
        assertEquals("It cost "+scenario6.getMoneyStr()+".  Can you afford this?" ,scenario6.getInstructionPhrase());
        assertEquals("It cost "+scenario7.getMoneyStr()+".  Can you afford this?" ,scenario7.getInstructionPhrase());
        assertEquals("It will cost "+scenario8.getMoneyStr()+". If you make this purchase, how much would you have left in your "+scenario8.getCategoryString()+" budget?" ,scenario8.getInstructionPhrase());
        assertEquals("It cost "+scenario9.getMoneyStr()+".  Can you afford this?" ,scenario9.getInstructionPhrase());
    }

    @Test
    public void fullStringTest(){
        assertEquals(scenario0.getTimeContext()+" "+scenario0.getInstructionPhrase() ,scenario0.getFullStr());
        assertEquals(scenario1.getTimeContext()+" "+scenario1.getInstructionPhrase() ,scenario1.getFullStr());
        assertEquals(scenario2.getTimeContext()+" "+scenario2.getInstructionPhrase() ,scenario2.getFullStr());
        assertEquals(scenario3.getTimeContext()+" "+scenario3.getInstructionPhrase() ,scenario3.getFullStr());
        assertEquals(scenario4.getTimeContext()+" "+scenario4.getSpecificBudgetAction()+" "+scenario4.getInstructionPhrase() ,scenario4.getFullStr());
        assertEquals(scenario5.getTimeContext()+" "+scenario5.getSpecificBudgetAction()+" "+scenario5.getInstructionPhrase() ,scenario5.getFullStr());
        assertEquals(scenario6.getTimeContext()+" "+scenario6.getSpecificBudgetAction()+" "+scenario6.getInstructionPhrase() ,scenario6.getFullStr());
        assertEquals(scenario7.getTimeContext()+" "+scenario7.getSpecificBudgetAction()+" "+scenario7.getInstructionPhrase() ,scenario7.getFullStr());
        assertEquals(scenario8.getTimeContext()+" "+scenario8.getSpecificBudgetAction()+" "+scenario8.getInstructionPhrase() ,scenario8.getFullStr());
        assertEquals(scenario9.getTimeContext()+" "+scenario9.getSpecificBudgetAction()+" "+scenario9.getInstructionPhrase() ,scenario9.getFullStr());
    }
}
