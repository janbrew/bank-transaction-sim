package net.jsf.brain;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import net.jsf.exceptions.*;

public class Bank {
    private static ArrayList<ArrayList<Long>> bankAccounts = new ArrayList<>();
    private static final String currency = "$";
    private static long accountNumber = 0;

    public Bank(int number) throws AccountNotFound {
        accountNumber = number;

        try {
            accessAccount();
        }
        catch (NoSuchElementException error) {
            throw new AccountNotFound("Account number does not exist", error);
        }
    }

    public ArrayList<Long> accessAccount() {
        return bankAccounts.stream().filter(acc -> acc.get(0) == accountNumber).findFirst().get();
    }

    public void depositCash(long cashAmount) throws DepositLimitReached {
        ArrayList<Long> account = accessAccount();

        if (cashAmount > 10000) {
            throw new DepositLimitReached("The max amount to deposit is $ 100,000", null);
        }
        else {
            account.set(1, account.get(1) + cashAmount);
        }
    }

    public void withdrawCash(long cashAmount) throws InsufficientAccountBalance {
        ArrayList<Long> account = accessAccount();

        if (account.get(1) < cashAmount) {
            throw new InsufficientAccountBalance("Account balance should be greater than the withdraw amount", null);
        }
        else if (account.get(1) - cashAmount < 500) {
            throw new InsufficientAccountBalance("Final account balance should not be less than the 500$ threshold", null);
        }
        account.set(1, account.get(1) - cashAmount);
    }

    public long seeBalance() {
        ArrayList<Long> account = accessAccount();

        return account.get(1);
    }
    
    public int calculateInterest() {
        ArrayList<Long> account = accessAccount();

        int interest = 0;

        if (account.get(1) >= 500) {
            interest = (int) Math.ceil(account.get(1) * 0.001);
        }

        return interest;
    }

    public static void generateAccount() {
        long accountNumber = 111111111;
        long accountBalance = 400;

        for (int i = 0; i < 10; i++) {
            ArrayList<Long> generatedAccount = new ArrayList<Long>();

            generatedAccount.add(accountNumber);
            generatedAccount.add(accountBalance);

            bankAccounts.add(generatedAccount);

            accountNumber += 111111111;
            accountBalance += 100;
        }
    }

    public static String formatAccountNumber() {
        String number =  String.valueOf(accountNumber);
        StringBuilder stringNum = new StringBuilder();

        int counter = 0;

        for (int i = 0; i < number.length(); i++) {
            if (counter == 3) {
                stringNum.append(" ");
                counter = 0;
            }
            stringNum.append(number.charAt(i));
            counter++;
        }

        return stringNum.toString().stripTrailing();
    }

    public static String formatAccountNumber(String condition) {
        StringBuilder formatNum = new StringBuilder();
        if (condition.equals("censored")) {
            String num = formatAccountNumber();

            formatNum.append(num.charAt(0));
            formatNum.append(num.substring(1, 10).replaceAll("[0-9]", "X"));
            formatNum.append(num.charAt(10));
        }

        return formatNum.toString();
    }
   
    public static String formatAccountBalance(long balance) {
        return String.format("%s %,d", currency, balance);
    }

    public static ArrayList<ArrayList<Long>> getBankAccounts() {
        return bankAccounts;
    }
}
