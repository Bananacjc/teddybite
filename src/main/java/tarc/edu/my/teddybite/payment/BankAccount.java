package tarc.edu.my.teddybite.payment;

import tarc.edu.my.teddybite.util.ColorCode;

public class BankAccount
{

    private String accNo;
    private double balance;
    private static double minBalance = 100f;

    public BankAccount()
    {
        this("");
    }

    public String getAccNo()
    {
        return accNo;
    }

    public BankAccount(String ccNo)
    {
        this.accNo = ccNo;
        this.balance = 10000f;
    }

    public boolean withdrawBalance(double amount)
    {
        if (amount > balance || ((balance - amount) <= minBalance)) {
            ColorCode.setTextRed();
            System.out.println("Insufficient Balance, withdraw failed.");
            ColorCode.setTextBlack();
            return false;
        } else {
            balance -= amount;
            ColorCode.setTextGreen();
            System.out.println("Withdraw Successful.");
            System.out.println("Updated Balance : RM " + String.format("%.2f", balance));
            ColorCode.setTextBlack();
            return true;
        }

    }

    public void depositBalance(double amount)
    {
        balance += amount;
        ColorCode.setTextGreen();
        System.out.println("Deposit Successful");
        System.out.println("Updated Balance : RM " + String.format("%.2f", balance));
    }

}
