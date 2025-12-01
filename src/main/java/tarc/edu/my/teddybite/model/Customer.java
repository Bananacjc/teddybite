/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarc.edu.my.teddybite.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import tarc.edu.my.teddybite.util.ColorCode;
import tarc.edu.my.teddybite.util.ValidationMethod;
import tarc.edu.my.teddybite.BankAccount;

/**
 *
 * @author ASUS
 */
public final class Customer extends Person
{

    private BankAccount bankAcc;

    private static final String CUST_FILEPATH = "Customer.txt";

    /* Customer - No-arg Constructors */
    public Customer()
    {
        // Prevent any null pointer reference
        this("", LocalDate.now(), "", "", new BankAccount(""));
    }

    /* Customer - Parameterized Constructor */
    public Customer(String name, LocalDate DOB, String contactNo, String email, BankAccount bankAcc)
    {
        super(name, DOB, contactNo, email);
        this.bankAcc = bankAcc;
    }

    public boolean withdrawBankAccount(double amount)
    {
        return bankAcc.withdrawBalance(amount);
    }

    public void depositBankAccount(double amount)
    {
        bankAcc.depositBalance(amount);
    }

    public String getBankAcc()
    {
        return bankAcc.getAccNo();
    }

    public static void addCustomer()
    {
        String newName;
        int newDOBYear = 0, newDOBMonth = 0, newDOBDay = 0;
        String newContactNo;
        String newEmail;
        String newAccNo;
        boolean isInteger = false;
        char continueChoice;
        Scanner in = new Scanner(System.in);

        System.out.println("User Registration");
        System.out.println("=================");

        System.out.print("Enter your NAME : ");
        newName = in.nextLine();

        do {
            System.out.println("Enter your DATE OF BIRTH >> ");
            try {
                System.out.print("Enter YEAR : ");
                isInteger = in.hasNextInt();
                newDOBYear = in.nextInt();

                System.out.print("Enter MONTH : ");
                isInteger = in.hasNextInt();
                newDOBMonth = in.nextInt();

                System.out.print("Enter DAY : ");
                isInteger = in.hasNextInt();
                newDOBDay = in.nextInt();
            } catch (Exception ex) {
                ValidationMethod.printNumericInputError();
            } finally {
                in.nextLine();
            }

        } while (!isInteger || ValidationMethod.validateDate(newDOBYear, newDOBMonth, newDOBDay));

        do {
            System.out.print("Enter your CONTACT NUMBER : ");
            newContactNo = in.nextLine().toLowerCase();
        } while (ValidationMethod.validateContactNo(newContactNo));

        // Employee Email
        do {
            System.out.print("Enter your EMAIL : ");
            newEmail = in.nextLine().toLowerCase();
        } while (ValidationMethod.validateEmail(newEmail));

        do {
            System.out.print("Enter your BANK ACCOUNT NUMBER : ");
            newAccNo = in.nextLine();
        } while (ValidationMethod.validateAccNo(newAccNo));

        do {
            System.out.print("Confirm to register ? (Y = Yes, N = No) : ");
            continueChoice = in.nextLine().toUpperCase().charAt(0);
        } while (ValidationMethod.validateYesNo(continueChoice));

        switch (continueChoice) {
            case 'Y' -> {
                Customer newCust = new Customer(
                        newName,
                        LocalDate.of(newDOBYear, newDOBMonth, newDOBDay),
                        newContactNo,
                        newEmail,
                        new BankAccount(newAccNo));
                writeCustomer(newCust);
            }
            case 'N' -> {
                ColorCode.setTextRed();
                System.out.println("Registration discarded...");
                ColorCode.setTextBlack();
                System.out.println();
            }
        }

    }

    private static void writeCustomer(Customer newCust)
    {
        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(CUST_FILEPATH, true));
            fw.write(newCust.getName() + '|');
            fw.write(newCust.getDOB() + '|');

            fw.write(newCust.getContactNo() + '|');
            fw.write(newCust.getEmail() + '|');
            fw.write(newCust.bankAcc.getAccNo() + "\n");
            fw.close();

        } catch (IOException ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + CUST_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }
    }

    private static ArrayList<String> readCustomer()
    {
        String textLine;
        ArrayList<String> custList = new ArrayList<>();
        try {
            BufferedReader fr = new BufferedReader(new FileReader(CUST_FILEPATH));
            while (fr.ready()) {
                textLine = fr.readLine();
                custList.add(textLine);

            }
            fr.close();
        } catch (IOException ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + CUST_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }

        return custList;

    }

    public static boolean foundCustomer(String name)
    {
        ArrayList<String> custList = readCustomer();

        for (String s : custList) {
            String s1[] = s.split("\\|");

            if (s1[0].equals(name)) {
                return true;
            }
        }

        return false;
    }

    public static Customer getCustomer(String name)
    {
        ArrayList<String> custList = readCustomer();

        for (String s : custList) {
            String s1[] = s.split("\\|");

            if (s1[0].equals(name)) {
                String[] date = s1[1].split("-");
                return new Customer(
                        s1[0],
                        LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])),
                        s1[2],
                        s1[3],
                        new BankAccount(s1[4]));
            }
        }

        return null;
    }

}
