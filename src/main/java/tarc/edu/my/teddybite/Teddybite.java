/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package tarc.edu.my.teddybite;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Scanner;
import tarc.edu.my.teddybite.model.Customer;
import tarc.edu.my.teddybite.model.Employee;
import tarc.edu.my.teddybite.model.Item;
import tarc.edu.my.teddybite.model.Menu;
import tarc.edu.my.teddybite.model.Order;
import tarc.edu.my.teddybite.util.ColorCode;
import tarc.edu.my.teddybite.util.ValidationMethod;

/**
 *
 * @author ASUS
 */
public class Teddybite
{

    public static void main(String[] args)
    {

        boolean isInteger;
        int optionChoice;

        System.out.print("""
             *****                   ****
           *      *****************       *
           *                              *
             *                          *
            *      O             O       *
           *              @               *
            *            ---             *
             ****************************
            *  **    ***  ****     ***   *
           ****  ***    ***    ***   **** *
           ********************************
          *                                *
            *******************************
                         """);
        System.out.println("Welcome to TeddyBite ");
        System.out.println();
        ColorCode.setTextBlack();

        try (Scanner in = new Scanner(System.in)) {
            do {
                isInteger = true;
                optionChoice = 0;
                do {

                    System.out.println("""
                                       0. Exit
                                       1. Log in
                                       2. Sign up""");
                    System.out.print("Enter an Option >> ");
                    try {
                        isInteger = in.hasNextInt();
                        optionChoice = in.nextInt();
                    } catch (Exception ex) {
                        ValidationMethod.printNumericInputError();
                    } finally {
                        in.nextLine();
                    }

                } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, ValidationMethod.EXIT_OPT, 2));

                cls();
                switch (optionChoice) {
                    case 1 -> {
                        loginFunction(in);
                    }
                    case 2 -> {
                        Customer.addCustomer();
                    }
                }

                pressEnterToContinue(in);

            } while (optionChoice != ValidationMethod.EXIT_OPT);
        }

        // If choice == 3
        ColorCode.setTextGreen();
        System.out.println("Have a nice day...");
        ColorCode.setTextBlack();

        System.exit(0);
    }

    private static void loginFunction(Scanner in)
    {

        boolean isInteger = true;
        int optionChoice = 0;
        do {
            cls();
            do {

                System.out.print("""
                           Continue as:
                           0. Return to Main Menu
                           1. Employee
                           2. Customer
                             """);
                System.out.print("Enter an Option >> ");

                try {
                    isInteger = in.hasNextInt();
                    optionChoice = in.nextInt();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();

                } finally {
                    in.nextLine();
                }
            } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, ValidationMethod.EXIT_OPT, 2));

            cls();
            switch (optionChoice) {
                case 1 -> {

                    String name;
                    System.out.print("Enter Employee ID : ");
                    name = in.nextLine();
                    if (Employee.foundEmployee(name)) {
                        System.out.println("Login Successful");
                        pressEnterToContinue(in);
                        processEmployeeLogin(in);
                    } else {
                        ColorCode.setTextRed();
                        System.out.println("This ID is invalid.");
                        ColorCode.setTextBlack();
                        System.out.println();
                    }

                }
                case 2 -> {
                    String name;
                    System.out.print("Enter your NAME : ");
                    name = in.nextLine();
                    if (Customer.foundCustomer(name)) {
                        Customer currentCustomer = Customer.getCustomer(name);
                        System.out.println("Login Successful");
                        pressEnterToContinue(in);
                        processOrderMenu(in, currentCustomer);
                    } else {
                        ColorCode.setTextRed();
                        System.out.println("This name is not registered.");
                        ColorCode.setTextBlack();
                        System.out.println();
                    }
                }
            }
            pressEnterToContinue(in);
        } while (optionChoice != ValidationMethod.EXIT_OPT);

        // End of method
    }

    private static void processEmployeeLogin(Scanner in)
    {
        int optionChoice = 0;
        boolean isInteger = false;
        do {
            cls();

            do {
                System.out.println("\nEmployee Menu");
                System.out.println("===============");
                System.out.println("0. Return to Main Menu");
                System.out.println("1. Staff Modification");
                System.out.println("2. Item Modification");
                System.out.println("3. Report");
                System.out.print("Enter an Option >> ");

                try {
                    isInteger = in.hasNextInt();
                    optionChoice = in.nextInt();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();

                } finally {
                    in.nextLine();
                }
            } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, ValidationMethod.EXIT_OPT, 3));

            cls();
            switch (optionChoice) {
                case 1 -> {
                    processStaffModification(in);
                }
                case 2 -> {
                    processItemModification(in);
                }
                case 3 -> {
                    Employee.employeeSalaryReport();
                }
            }
            pressEnterToContinue(in);
        } while (optionChoice != ValidationMethod.EXIT_OPT);
    }

    private static void processStaffModification(Scanner in)
    {
        int optionChoice = 0;
        boolean isInteger = false;
        do {
            cls();
            do {
                System.out.println("\nStaff Modification");
                System.out.println("=================");
                System.out.println("0. Return to Employee Menu");
                System.out.println("1. Add Employee");
                System.out.println("2. Search Employee");
                System.out.println("3. Delete An Employee");
                System.out.println("4. Delete All Employee");
                System.out.println("5. Modify An Employee");
                System.out.print("Enter an Option >> ");

                try {
                    isInteger = in.hasNextInt();
                    optionChoice = in.nextInt();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();
                } finally {
                    in.nextLine();
                }
            } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, ValidationMethod.EXIT_OPT, 5));

            cls();
            switch (optionChoice) {
                case 1 -> {
                    Employee.addEmployee();
                }
                case 2 -> {
                    Employee.searchEmployee();
                }
                case 3 -> {
                    Employee.deleteEmployee();
                }
                case 4 -> {
                    Employee.deleteAllEmployee();
                }
                case 5 -> {
                    Employee.modifyEmployee();
                }
            }
            pressEnterToContinue(in);
        } while (optionChoice != ValidationMethod.EXIT_OPT);

    }

    private static void processItemModification(Scanner in)
    {
        int optionChoice = 0;
        boolean isInteger = false;

        do {
            do {
                System.out.println("\nItem Modification");
                System.out.println("=================");
                System.out.println("0. Return to Employee Menu");
                System.out.println("1. Add Item");
                System.out.println("2. Search Item");
                System.out.println("3. Delete An Item");
                System.out.println("4. Delete All Item");
                System.out.println("5. Modify An Item");
                System.out.print("Enter an Option >> ");

                try {
                    isInteger = in.hasNextInt();
                    optionChoice = in.nextInt();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();
                } finally {
                    in.nextLine();
                }
            } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, ValidationMethod.EXIT_OPT, 5));

            cls();
            switch (optionChoice) {
                case 1 -> {
                    Item.addItem();
                }
                case 2 -> {
                    Item.searchItem();
                }
                case 3 -> {
                    Item.deleteItem();
                }
                case 4 -> {
                    Item.deleteAllItem();
                }
                case 5 -> {
                    Item.modifyItem();
                }
            }
            pressEnterToContinue(in);
        } while (optionChoice != ValidationMethod.EXIT_OPT);

    }

    private static void processOrderMenu(Scanner in, Customer currentCustomer)
    {
        int optionChoice = 0;
        boolean isInteger = false;
        char continueOption = 'N';
        Order currentOrder = new Order();
        Menu currentMenu = new Menu();
        do {
            cls();
            do {
                System.out.println("\nOrder Menu");
                System.out.println("==========");
                System.out.println("0. Return to Main Menu");
                System.out.println("1. Display Menu");
                System.out.println("2. Order Food");
                System.out.println("3. Remove Order");
                System.out.println("4. Display Order");
                System.out.println("5. Make Payment");
                System.out.println("6. Deposit Account");
                System.out.print("Enter an Option >> ");

                try {
                    isInteger = in.hasNextInt();
                    optionChoice = in.nextInt();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();
                } finally {
                    in.nextLine();
                }
            } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, ValidationMethod.EXIT_OPT, 6));

            cls();
            switch (optionChoice) {
                case 0 -> {
                    do {
                        System.out.print("Confirm return to main menu ? (Y = Yes, N = No) : ");
                        continueOption = in.nextLine().toUpperCase().charAt(0);
                    } while (ValidationMethod.validateYesNo(continueOption));

                }
                case 1 -> {
                    processMenu(in, currentMenu);
                }
                case 2 -> {
                    currentOrder.orderItem(currentMenu.getMenuHashList());
                }
                case 3 -> {
                    currentOrder.removeOrder();
                }
                case 4 -> {
                    currentOrder.displayOrder();
                }
                case 5 -> {
                    processMakePayment(in, currentOrder.orderList, currentCustomer);
                }
                case 6 -> {
                    String accNo;
                    boolean isDouble = false;
                    System.out.print("Enter your Bank Account Number : ");
                    accNo = in.nextLine();
                    if (accNo.equals(currentCustomer.getBankAcc())) {
                        double amount;

                        do {
                            System.out.println("Enter the amount to deposit");
                            try {
                                isDouble = in.hasNextDouble();
                                currentCustomer.depositBankAccount(in.nextDouble());
                            } catch (Exception ex) {
                                ValidationMethod.printNumericInputError();
                            } finally {
                                in.nextLine();
                            }
                        } while (!isDouble);

                    }

                }
            }
            pressEnterToContinue(in);
        } while (continueOption != 'Y');
    }

    public static void processMenu(Scanner in, Menu currentMenu)
    {
        int optionChoice = 0;
        boolean isInteger = false;
        do {
            do {
                System.out.println("\nMenu");
                System.out.println("==========");
                System.out.println("0. Return to Order Menu");
                System.out.println("1. Display All Menu");
                System.out.println("2. Display Burger Menu");
                System.out.println("3. Display Fried Chicken Menu");
                System.out.println("4. Display Beverages Menu");
                System.out.println("5. Display Desserts Menu");
                System.out.println("6. Display Condiments Menu");
                System.out.print("Enter an Option >> ");

                try {
                    isInteger = in.hasNextInt();
                    optionChoice = in.nextInt();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();
                } finally {
                    in.nextLine();
                }
            } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, ValidationMethod.EXIT_OPT, 6));

            switch (optionChoice) {
                case 1 -> {
                    currentMenu.displayAllMenu();
                }
                case 2 -> {
                    currentMenu.displayBurgers();
                }
                case 3 -> {
                    currentMenu.displayFriedChicken();
                }
                case 4 -> {
                    currentMenu.displayBeverages();
                }
                case 5 -> {
                    currentMenu.displayDesserts();
                }
                case 6 -> {
                    currentMenu.displayCondiments();
                }
            }
            pressEnterToContinue(in);
        } while (optionChoice != ValidationMethod.EXIT_OPT);
    }

    private static void processMakePayment(Scanner in, HashMap<Item, Integer> orderList, Customer currenCustomer)
    {
        int optionChoice = 0;
        boolean isInteger = false;
        char continueOption;

        do {
            System.out.println("Payment Menu");
            System.out.println("============");
            System.out.println("0. Continue to order");
            System.out.println("1. Pay by Credit Card");
            System.out.println("2. Pay by Cash");
            System.out.print("Enter an Option >> ");

            try {
                isInteger = in.hasNextInt();
                optionChoice = in.nextInt();
            } catch (Exception ex) {
                ValidationMethod.printNumericInputError();
            } finally {
                in.nextLine();
            }
        } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, ValidationMethod.EXIT_OPT, 2));

        switch (optionChoice) {
            case 1 -> {
                do {
                    System.out.print("Confrim payment ? (Y = Yes, N = No) : ");
                    continueOption = in.nextLine().toUpperCase().charAt(0);
                } while (ValidationMethod.validateYesNo(continueOption));

                if (continueOption == 'Y') {
                    String accNo;
                    CreditPayment currentPayment = new CreditPayment();
                    currentPayment.calculatePayment(orderList);

                    System.out.print("Enter your Bank Account Number : ");
                    accNo = in.nextLine();
                    if (accNo.equals(currenCustomer.getBankAcc())) {
                        if (currenCustomer.withdrawBankAccount(currentPayment.getPaymentAmount())) {
                            System.out.println(currentPayment.toString());
                            ColorCode.setTextGreen();
                            System.out.println("Order Successful.");
                            ColorCode.setTextBlack();
                        } else {
                            System.out.println("Payment Discarded.");
                        }
                    } else {
                        ColorCode.setTextRed();
                        System.out.println("Invalid Bank Account.");
                        ColorCode.setTextBlack();
                        System.out.println();
                    }
                }
            }

            case 2 -> {
                do {
                    System.out.print("Confrim payment ? (Y = Yes, N = No) : ");
                    continueOption = in.nextLine().toUpperCase().charAt(0);
                } while (ValidationMethod.validateYesNo(continueOption));

                if (continueOption == 'Y') {
                    CashPayment currentPayment = new CashPayment();
                    currentPayment.calculatePayment(orderList);
                    System.out.println(currentPayment.toString());
                    ColorCode.setTextGreen();
                    System.out.println("Order Successful. Proceed to counter to pay.");
                    ColorCode.setTextBlack();
                    System.out.println();
                }

            }
        }
        pressEnterToContinue(in);

    }

    private static void cls()
    {
        try {
            Robot rb = new Robot();
            rb.keyPress(KeyEvent.VK_CONTROL);
            rb.keyPress(KeyEvent.VK_L);
            rb.keyRelease(KeyEvent.VK_L);
            rb.keyRelease(KeyEvent.VK_CONTROL);
            Thread.sleep(500);
        } catch (AWTException | InterruptedException ex) {
        }
    }

    private static void pressEnterToContinue(Scanner in)
    {
        System.out.print("Press enter key to continue...");
        in.nextLine();
    }

}
