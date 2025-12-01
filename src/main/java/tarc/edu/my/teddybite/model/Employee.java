package tarc.edu.my.teddybite.model;

/**
 *
 * @author Ethel
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import tarc.edu.my.teddybite.util.ColorCode;
import tarc.edu.my.teddybite.util.ValidationMethod;

/**
 *
 * |author Ethel
 */
public class Employee extends Person
{

    /* Data Fields */
    private char gender;
    private String employeeID;
    private String empPosition;
    private double empSalary;

    private static final char EMP_CODE = 'E';
    private static final String EMP_FILEPATH = "Employee.txt";

    /* Employee - No-arg Constructor */
    public Employee()
    {
        // Prevent all null pointer reference
        this("", LocalDate.EPOCH, "", "", "", Character.MIN_VALUE);
    }

    /* Employee - Parameterized Constructor */
    public Employee(String name, LocalDate DOB, String contactNo, String email, String empPosition, char gender)
    {
        super(name, DOB, contactNo, email);
        this.employeeID = getNewEmployeeID();
        this.empPosition = empPosition;

        empSalary = switch (empPosition) {
            case "Manager" ->
                4500f;
            case "Cashier" ->
                3800f;
            case "Kitchen Crew" ->
                3500f;
            default ->
                3000f;
        };
        this.gender = gender;
    }

    /* Getters / Setters */
    // EmployeeID
    public String getEmployeeID()
    {
        return employeeID;
    }

    public void setEmployeeID(String employeeID)
    {
        this.employeeID = employeeID;
    }

    // Employee Position
    public String getEmpPosition()
    {
        return empPosition;
    }

    public void setEmpPosition(String empPosition)
    {
        this.empPosition = empPosition;
    }

    // Employee Salary
    public double getEmpSalary()
    {
        return empSalary;
    }

    public void setEmpSalary(double empSalary)
    {
        this.empSalary = empSalary;
    }

    // Employee Gender
    public double getGender()
    {
        return gender;
    }

    public void setGender(char gender)
    {
        this.gender = gender;
    }

    private static int countNoOfEmployee()
    {
        int lines = 0;
        try {
            BufferedReader fr = new BufferedReader(new FileReader(EMP_FILEPATH));
            while (fr.readLine() != null) {
                lines++;
            }
            fr.close();
        } catch (IOException ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + EMP_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }
        return lines;

    }

    private static String getNewEmployeeID()
    {
        return EMP_CODE + String.format("%04d", countNoOfEmployee() + 1);
    }

    public static void addEmployee()
    {
        String newEmpName;
        char newGender;
        int newDOBYear = 0;
        int newDOBMonth = 0;
        int newDOBDay = 0;
        String newContactNo;
        String newEmail;
        String newEmpPosition = "";
        double newEmpSalary = 0f;

        boolean isInteger = true;

        int optionChoice = 0;
        char continueChoice = Character.MIN_VALUE;
        Scanner in = new Scanner(System.in);

        do {
            // Employee name
            do {
                System.out.print("Enter the Employee NAME : ");
                newEmpName = in.nextLine();
            } while (ValidationMethod.validateName(newEmpName));

            // Employee gender
            do {
                System.out.print("Enter the Employee GENDER (F = Female, M = Male) : ");
                newGender = in.nextLine().toUpperCase().charAt(0);
            } while (ValidationMethod.validateGender(newGender));

            // Employee DOB
            do {
                System.out.println("Enter the Employee DATE OF BIRTH >> ");

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

                } catch (InputMismatchException ex) {
                    ValidationMethod.printNumericInputError();

                } finally {
                    in.nextLine();
                }

            } while (!isInteger || ValidationMethod.validateDate(newDOBYear, newDOBMonth, newDOBDay));

            // Employee Contact No
            do {
                System.out.print("Enter the employee CONTACT NUMBER : ");
                newContactNo = in.nextLine().toLowerCase();
            } while (ValidationMethod.validateContactNo(newContactNo));

            // Employee Email
            do {
                System.out.print("Enter the employee EMAIL : ");
                newEmail = in.nextLine().toLowerCase();
            } while (ValidationMethod.validateEmail(newEmail));

            // Employee Position
            do {
                System.out.println("Enter the employee POSITION >> ");
                System.out.println("1. Manager");
                System.out.println("2. Cashier");
                System.out.println("3. Kitchen Crew");
                System.out.println("4. Cleaner");
                System.out.print("Enter an option : ");
                try {
                    isInteger = in.hasNextInt();
                    optionChoice = in.nextInt();

                } catch (InputMismatchException ex) {
                    ValidationMethod.printNumericInputError();

                } finally {
                    in.nextLine();
                }

            } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, 1, 4));

            switch (optionChoice) {
                case 1 -> {
                    newEmpPosition = "Manager";
                    newEmpSalary = 4500f;
                }
                case 2 -> {
                    newEmpPosition = "Cashier";
                    newEmpSalary = 3800f;
                }
                case 3 -> {
                    newEmpPosition = "Kitchen Crew";
                    newEmpSalary = 3500f;
                }
                case 4 -> {
                    newEmpPosition = "Cleaner";
                    newEmpSalary = 3000f;
                }
            }

            do {
                System.out.print("Add the new Employee ? (Y = Yes, N = No) : ");
                continueChoice = in.nextLine().toUpperCase().charAt(0);
            } while (ValidationMethod.validateYesNo(continueChoice));

            switch (continueChoice) {
                case 'Y' -> {
                    Employee newEmp = new Employee(
                            newEmpName,
                            LocalDate.of(newDOBYear, newDOBMonth, newDOBDay),
                            newContactNo,
                            newEmail,
                            newEmpPosition,
                            newGender);
                    writeEmployee(newEmp);
                }
                case 'N' -> {
                    ColorCode.setTextRed();
                    System.out.println("Employee discarded...");
                    ColorCode.setTextBlack();
                    System.out.println();
                }
            }

            do {
                System.out.print("Do you want to continue to add ? (Y = Yes, N = No) : ");
                continueChoice = in.nextLine().toUpperCase().charAt(0);
            } while (ValidationMethod.validateYesNo(continueChoice));

        } while (continueChoice == 'Y');

    }

    public static void searchEmployee()
    {
        ArrayList<String> empList = readEmployee();
        boolean isInteger = true;
        int optionChoice = 0;
        char continueChoice;
        String search;
        boolean found;

        Scanner in = new Scanner(System.in);

        do {
            do {
                System.out.println("1. Search By Employee ID");
                System.out.println("2. Search By Employee Name");
                System.out.println("3. Search By Employee Position");
                System.out.print("Your Option : ");
                try {
                    isInteger = in.hasNextInt();
                    optionChoice = in.nextInt();
                } catch (InputMismatchException ex) {
                    ValidationMethod.printNumericInputError();
                } finally {
                    in.nextLine();
                }
            } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, 1, 3));

            switch (optionChoice) {
                case 1 -> {
                    found = false;
                    System.out.printf("Enter the Employee ID to Search : ");
                    search = in.nextLine();
                    for (String item : empList) {
                        String temp[] = item.split("\\|");

                        if (temp[0].equals(search)) {
                            found = true;
                            System.out.println("Employee Code : " + temp[0]);
                            System.out.println("Employee Position : " + temp[1]);
                            System.out.printf("Employee salary : RM %.2f\n", Double.valueOf(temp[2]));
                            System.out.println("Employee Gender : " + temp[3]);
                            System.out.println("Employee Date Joined : " + temp[4]);
                            System.out.println("Employee Name : " + temp[5]);
                            System.out.println("Employee Birthday : " + temp[6]);
                            System.out.println("Employee Contact Number : " + temp[7]);
                            System.out.println("Employee Email : " + temp[8]);
                            System.out.println("\n----------------------------------\n");
                        }

                    }
                    if (found == false) {
                        System.out.println("Unable to find the employee code entered");
                    }

                }
                case 2 -> {
                    found = false;
                    System.out.printf("Enter the Employee NAME to Search : ");
                    search = in.nextLine();
                    for (String item : empList) {
                        String temp[] = item.split("\\|");

                        if (temp[5].equals(search)) {
                            found = true;
                            System.out.println("Employee Code : " + temp[0]);
                            System.out.println("Employee Position : " + temp[1]);
                            System.out.printf("Employee salary : RM %.2f\n", Double.valueOf(temp[2]));
                            System.out.println("Employee Gender : " + temp[3]);
                            System.out.println("Employee Date Joined : " + temp[4]);
                            System.out.println("Employee Name : " + temp[5]);
                            System.out.println("Employee Birthday : " + temp[6]);
                            System.out.println("Employee Contact Number: " + temp[7]);
                            System.out.println("Employee Email : " + temp[8]);
                            System.out.println("\n----------------------------------\n");
                        }

                    }
                    if (found == false) {
                        System.out.println("Unable to find the name entered");
                    }
                }
                case 3 -> {
                    found = false;
                    System.out.print("1. Manager\n2. Cashier\n3. Kitchen Crew\n4. Cleaner \n");
                    System.out.print("Select Employee POSITION : ");
                    try {
                        int position_option = in.nextInt();
                        search = switch (position_option) {
                            case 1 ->
                                "Manager";
                            case 2 ->
                                "Cashier";
                            case 3 ->
                                "Kitchen Crew";
                            case 4 ->
                                "Cleaner";
                            default ->
                                "";

                        };

                        for (String item : empList) {
                            String temp[] = item.split("\\|");

                            if (temp[1].equals(search)) {
                                found = true;
                                System.out.println("Employee Code : " + temp[0]);
                                System.out.println("Employee Position : " + temp[1]);
                                System.out.printf("Employee salary : RM %.2f\n", Double.valueOf(temp[2]));
                                System.out.println("Employee Gender : " + temp[3]);
                                System.out.println("Employee Date Joined : " + temp[4]);
                                System.out.println("Employee Name : " + temp[5]);
                                System.out.println("Employee Birthday : " + temp[6]);
                                System.out.println("Employee Contact Number: " + temp[7]);
                                System.out.println("Employee Email : " + temp[8]);
                                System.out.println("\n----------------------------------\n");
                            }

                        }
                        if (found == false) {
                            System.out.println("Unable to find the category entered");
                        }
                    } catch (InputMismatchException e) {
                        ValidationMethod.printNumericInputError();
                    } finally {
                        in.nextLine();
                    }

                }
            }

            do {
                System.out.print("Continue to search ? (Y = Yes, N = No) : ");
                continueChoice = in.nextLine().toUpperCase().charAt(0);
            } while (ValidationMethod.validateYesNo(continueChoice));
        } while (continueChoice == 'Y');

    }

    public static void modifyEmployee()
    {
        Scanner in = new Scanner(System.in);
        ArrayList<String> empList = readEmployee();

        int empOption = 0;
        String empID;
        int found = -1;
        char YN;
        boolean isInteger = true;
        int positionOption = 0;

        System.out.print("Enter Employee ID to modify : ");
        empID = in.nextLine();
        int count = 0;

        for (String s : empList) {
            String detail[] = s.split("\\|", 2);
            if (detail[0].equals(empID)) {
                found = count;
            }
            count++;
        }

        if (found == -1) {
            System.out.println("Employee ID not found");
            return;
        }
        String emp[] = empList.get(found).split("\\|");
        do {
            boolean errorOccur = true;

            do {
                System.out.println("Current Value");
                System.out.println("-------------------");
                System.out.println("Employee Name           : " + emp[5]);
                System.out.println("Employee Position       : " + emp[1]);
                System.out.println("Employee Salary         : RM " + Double.valueOf(emp[2]));
                System.out.println("Employee Contact Number : " + emp[7]);
                System.out.println("Employee Email : " + emp[8]);
                System.out.println("Employee Date Joined    : " + emp[4]);

                System.out.println("\nEnter the option that you want to modify");
                System.out.println("============================================");
                System.out.println("1. Employee Name");
                System.out.println("2. Employee Position");
                System.out.println("3. Employee Salary");
                System.out.println("4. Employee Contact Number");
                System.out.println("5. Employee Year Joined");
                System.out.println("6. Employee Email");
                System.out.print("option ? ");

                try {
                    isInteger = in.hasNextInt();
                    empOption = in.nextInt();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();
                } finally {

                    in.nextLine();
                }
            } while (!isInteger || ValidationMethod.validateOptionRange(empOption, 1, 6));

            while (errorOccur) {
                errorOccur = false;
                switch (empOption) {
                    case 1 -> {
                        System.out.print("Enter new Employee NAME : ");
                        String name = in.nextLine();
                        if (!ValidationMethod.validateName(name)) {
                            emp[5] = name;
                        } else {
                            errorOccur = true;
                        }
                    }
                    case 2 -> {
                        do {
                            System.out.print("1. Manager\n2. Cashier\n3. Kitchen Crew\n4. Cleaner \n");
                            System.out.print("Select new Employee POSITION : ");
                            try {
                                isInteger = in.hasNextInt();
                                positionOption = in.nextInt();
                            } catch (InputMismatchException e) {
                                ValidationMethod.printNumericInputError();
                            } finally {
                                in.nextLine();
                            }
                        } while (!isInteger || ValidationMethod.validateOptionRange(positionOption, 1, 4));

                        switch (positionOption) {
                            case 1:
                                emp[1] = "Manager";
                                break;
                            case 2:
                                emp[1] = "Cashier";
                                break;
                            case 3:
                                emp[1] = "Kitchen Crew";
                                break;
                            case 4:
                                emp[1] = "Cleaner";
                                break;
                            default:
                                System.out.println("Invalid Input.");
                        }
                    }
                    case 3 -> {
                        try {
                            System.out.print("Enter the new SALARY : ");
                            emp[2] = String.valueOf(in.nextDouble());
                        } catch (NumberFormatException E) {

                            ValidationMethod.printNumericInputError();
                            errorOccur = true;
                        } finally {
                            in.nextLine();
                        }
                    }

                    case 4 -> {

                        System.out.print("Enter the new CONTACT NUMBER : ");
                        String contact = in.nextLine();
                        if (!ValidationMethod.validateContactNo(contact)) {
                            emp[7] = contact;
                        } else {
                            errorOccur = true;
                        }
                    }

                    case 5 -> {
                        int newDOBYear = 0, newDOBMonth = 0, newDOBDay = 0;

                        do {

                            System.out.print("Enter the new DATE JOINED >> ");
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

                            } catch (InputMismatchException ex) {
                                ValidationMethod.printNumericInputError();

                            } finally {
                                in.nextLine();
                            }

                        } while (!isInteger || ValidationMethod.validateDate(newDOBYear, newDOBMonth, newDOBDay));
                        emp[4] = LocalDate.of(newDOBYear, newDOBMonth, newDOBDay).toString();
                    }
                    case 6 -> {
                        System.out.print("Enter the new EMAIL : ");
                        String email = in.nextLine();
                        if (!ValidationMethod.validateEmail(email)) {
                            emp[8] = email;
                        } else {
                            errorOccur = true;
                        }
                    }
                    default -> {
                        System.out.println("INVALID OPTION ");
                    }
                }
            }
            do {
                System.out.print("Continue modify the current employee detail again ? (Y = Yes, N = No) : ");
                YN = in.nextLine().toUpperCase().charAt(0);

            } while (ValidationMethod.validateYesNo(YN));
        } while (YN == 'Y');

        do {
            System.out.print("Save the modification ? (Y = Yes, N = No) : ");
            YN = in.nextLine().toUpperCase().charAt(0);
        } while (ValidationMethod.validateYesNo(YN));

        switch (YN) {
            case 'Y' -> {
                String mod = emp[0] + "|" + emp[1] + "|" + emp[2] + "|" + emp[3] + "|" + emp[4] + "|" + emp[5] + "|" + emp[6] + "|" + emp[7] + '|' + emp[8];
                empList.set(found, mod);

                writeEmployee(empList);
                ColorCode.setTextGreen();
                System.out.println("Modification successful.");
                ColorCode.setTextBlack();
                System.out.println();
            }
            case 'N' -> {
                ColorCode.setTextRed();
                System.out.println("Modification discarded.");
                ColorCode.setTextBlack();
                System.out.println();
            }
        }

    }

    public static void deleteAllEmployee()
    {
        char continueChoice;
        try (Scanner in = new Scanner(System.in)) {
            do {
                System.out.println("Confirm to delete all Employee ? (Y = Yes, N = No) ");
                ColorCode.setTextRed();
                System.out.println("**NOTE** this changes is irreversible");
                ColorCode.setTextBlack();
                System.out.print(">>> ");
                continueChoice = in.nextLine().toUpperCase().charAt(0);
            } while (ValidationMethod.validateYesNo(continueChoice));

            switch (continueChoice) {
                case 'Y' -> {
                    try {
                        BufferedWriter fw = new BufferedWriter(new FileWriter(EMP_FILEPATH, false));
                        fw.close();
                        ColorCode.setTextGreen();
                        System.out.println("All Content in " + EMP_FILEPATH + " Deleted.");
                        ColorCode.setTextBlack();
                        System.out.println();
                        fw.close();
                    } catch (IOException ex) {
                        ColorCode.setTextRed();
                        System.out.println("Error Occurred on " + EMP_FILEPATH + ". Please contact Admin.");
                        ColorCode.setTextBlack();
                        System.out.println();
                    }
                }
                case 'N' -> {
                    ColorCode.setTextGreen();
                    System.out.println("Changes Discarded.");
                    ColorCode.setTextBlack();
                    System.out.println();
                }
            }
        }
    }

    public static void deleteEmployee()
    {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter Employee ID to Remove the Employe : ");
        String id = in.nextLine();

        ArrayList<String> list = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(EMP_FILEPATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String sp[] = line.split("\\|", 2);
                if (!id.equals(sp[0])) {
                    list.add(line);
                } else {
                    found = true;
                }
            }
            reader.close();
        } catch (IOException e) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + EMP_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }
        if (found == true) {
            writeEmployee(list);
            ColorCode.setTextGreen();
            System.out.println("Employee removed successfully.");
            ColorCode.setTextBlack();
            System.out.println();
        } else {
            System.out.println("Employee not found");
        }

    }

    private static ArrayList<String> readEmployee()
    {
        String textLine;
        ArrayList<String> empList = new ArrayList<>();
        try {
            BufferedReader fr = new BufferedReader(new FileReader(EMP_FILEPATH));
            while (fr.ready()) {
                textLine = fr.readLine();
                empList.add(textLine);

            }
            fr.close();
        } catch (IOException ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + EMP_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }

        return empList;
    }

    private static void writeEmployee(ArrayList<String> list)
    {

        try {
            FileWriter fw = new FileWriter(EMP_FILEPATH, false);
            for (String s : list) {
                fw.write(s);
                fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + EMP_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }
    }

    private static void writeEmployee(Employee newEmp)
    {
        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(EMP_FILEPATH, true));
            fw.write(newEmp.employeeID + '|');
            fw.write(newEmp.empPosition + '|');
            fw.write(Double.toString(newEmp.empSalary) + '|');
            fw.write(Character.toString(newEmp.gender) + '|');
            fw.write(newEmp.getDateJoined() + '|');
            fw.write(newEmp.getName() + '|');
            fw.write(newEmp.getDOB() + '|');
            fw.write(newEmp.getContactNo() + '|');
            fw.write(newEmp.getEmail() + "\n");
            fw.close();
        } catch (IOException ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + EMP_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }
    }

    public static boolean foundEmployee(String id)
    {
        ArrayList<String> empList = readEmployee();

        for (String s : empList) {
            String s1[] = s.split("\\|");

            if (s1[0].equals(id)) {
                return true;
            }
        }

        return false;
    }

    public static void employeeSalaryReport()
    {

        Scanner scan = new Scanner(System.in);
        int opt;
        do {
            ArrayList<String> empList = readEmployee();
            System.out.println("1. Ascending order for salary report");
            System.out.println("2. Descending order for salary report");
            System.out.println("0. Back");
            System.out.print("Option ? ");
            opt = scan.nextInt();
            scan.nextLine();
            if (opt == 0) {
                break;
            }

            if (opt == 1 || opt == 2) {
                System.out.printf("Salary report\n-------------\n");
                System.out.printf("%-5s %-15s%15s%9s\n", "ID", "Name", "Position", "Salary");
            }
            if (opt == 1) {
                printReportAscending(empList, 1, 0);
            } else if (opt == 2) {
                printReportDescending(empList, 1, 0);
            }

            if (opt < 0 || opt > 2) {
                System.out.println("Invalid range have entered Please try again");
            }

            System.out.printf("\n\n\nDo you want to access to Salary Report Again Y = Yes Otheres = No\nAgain ?");

            char again = scan.nextLine().charAt(0);
            if (again == 'Y' || again == 'y') {
                opt = -1;
            } else {
                break;
            }
        } while (opt < 0 || opt > 2);
    }

    private static void printReportAscending(ArrayList<String> empList, int no, double ttl)
    {
        if (empList.size() == 1) {
            String temp[] = empList.get(0).split("\\|");
            System.out.printf("%-5s %-15s%15s%9.2f\n", temp[0], temp[5], temp[1], Double.valueOf(temp[2]));
            System.out.printf("%-5s %-15s%15s%9.2f\n", "", "", "TOTAL : ", ttl += Double.valueOf(temp[2]));
            return;
        }
        double salary[] = new double[empList.size()];

        for (int i = 0; i < empList.size(); i++) {
            String temp[] = empList.get(i).split("\\|");
            salary[i] = Double.valueOf(temp[2]);
        }

        //start compare
        int small = 0;
        for (int i = 1; i < empList.size(); i++) {
            if (salary[i - 1] > salary[i]) {
                small = i;
            }
        }
        String temp[] = empList.get(small).split("\\|");
        System.out.printf("%-5s %-15s%15s%9.2f\n", temp[0], temp[5], temp[1], Double.valueOf(temp[2]));
        ttl += Double.valueOf(temp[2]);
        empList.remove(small);
        printReportAscending(empList, no + 1, ttl);

    }

    private static void printReportDescending(ArrayList<String> empList, int no, double ttl)
    {
        if (empList.size() == 1) {
            String temp[] = empList.get(0).split("\\|");
            System.out.printf("%-5s %-15s%15s%9.2f\n", temp[0], temp[5], temp[1], Double.valueOf(temp[2]));
            System.out.printf("%-5s %-15s%15s%9.2f\n", "", "", "TOTAL : ", ttl += Double.valueOf(temp[2]));
            return;
        }
        double salary[] = new double[empList.size()];

        for (int i = 0; i < empList.size(); i++) {
            String temp[] = empList.get(i).split("\\|");
            salary[i] = Double.valueOf(temp[2]);
        }

        //start compare
        int large = 0;
        for (int i = 1; i < empList.size(); i++) {
            if (salary[i - 1] < salary[i]) {
                large = i;
            }
        }
        String temp[] = empList.get(large).split("\\|");
        System.out.printf("%-5s %-15s%15s%9.2f\n", temp[0], temp[5], temp[1], Double.valueOf(temp[2]));
        ttl += Double.valueOf(temp[2]);
        empList.remove(large);
        printReportDescending(empList, no + 1, ttl);

    }

}
