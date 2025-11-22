/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarc.edu.my.teddybite.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 *
 * @author ASUS
 */
public final class ValidationMethod
{

    public static final int EXIT_OPT = 0;

    // Any validation trigger == true
    public static boolean validateOptionRange(int option, int minOption, int maxOption)
    {
        if (option < minOption || option > maxOption) {
            ColorCode.setTextRed();
            System.out.println("Invalid Option. Please enter option within " + minOption + " and " + maxOption);
            ColorCode.setTextBlack();
            System.out.println();
            return true;
        }

        return false;
    }

    public static boolean validateName(String name)
    {
        for (char ch : name.toCharArray()) {
            if (!Character.isAlphabetic(ch) || Character.isSpaceChar(ch)) {
                ColorCode.setTextRed();
                System.out.println("Invalid name, please enter ALPHABET only");
                ColorCode.setTextBlack();
                System.out.println();
                return true;
            }
        }
        return false;
    }

    public static boolean validateGender(char gender)
    {
        if (gender != 'F' && gender != 'M') {
            ColorCode.setTextRed();
            System.out.println("Invalid gender, please enter 'F' for female or 'M' for male.");
            ColorCode.setTextBlack();
            System.out.println();
            return true;
        }
        return false;
    }

    public static boolean validateDate(int year, int month, int day)
    {

        if (year < LocalDate.EPOCH.getYear()) {
            ColorCode.setTextRed();
            System.out.println("Invalid date, please enter a valid date");
            ColorCode.setTextBlack();
            System.out.println();
            return true;
        }

        String dateToParse = String.format("%04d%02d%02d", year, month, day);

        try {
            LocalDate.parse(dateToParse, DateTimeFormatter.BASIC_ISO_DATE);
            // return false work here too
        } catch (DateTimeParseException ex) {
            ColorCode.setTextRed();
            System.out.println("Invalid date, please enter a valid date");
            ColorCode.setTextBlack();
            System.out.println();
            return true;
        }

        return false;
    }

    public static boolean validateContactNo(String contactNo)
    {
        for (char ch : contactNo.toCharArray()) {
            if (!Character.isDigit(ch)) {
                ColorCode.setTextRed();
                System.out.println("Invalid contact number, please enter NUMBER only");
                ColorCode.setTextBlack();
                System.out.println();
                return true;
            }
        }
        return false;
    }

    public static boolean validateAccNo(String accNo)
    {
        for (char ch : accNo.toCharArray()) {
            if (!Character.isDigit(ch)) {
                ColorCode.setTextRed();
                System.out.println("Invalid account number, please enter NUMBER only");
                ColorCode.setTextBlack();
                System.out.println();
                return true;
            }
        }
        return false;
    }

    public static boolean validateEmail(String email)
    {
        String emailRegex = "^(?=.{1,23}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        if (!Pattern.matches(emailRegex, email)) {
            ColorCode.setTextRed();
            System.out.println("Invalid email, please enter a valid email.");
            ColorCode.setTextBlack();
            System.out.println();
            return true;
        }

        return false;

    }

    public static boolean validateYesNo(char yesno)
    {
        if (yesno != 'Y' && yesno != 'N') {
            ColorCode.setTextRed();
            System.out.println("Invalid input, please enter 'Y' or 'N' only.");
            ColorCode.setTextBlack();
            System.out.println();
            return true;
        }

        return false;
    }

    public static boolean validateItemQuantity(int itemQty)
    {
        if (itemQty <= 0) {
            ColorCode.setTextRed();
            System.out.println("Invalid quantity, please enter a valid quantity.");
            ColorCode.setTextBlack();
            System.out.println();
            return true;
        }
        return false;
    }

    public static void printNumericInputError()
    {
        ColorCode.setTextRed();
        System.out.println("Invalid input, please enter NUMBER only.");
        ColorCode.setTextBlack();
        System.out.println();

    }

}
