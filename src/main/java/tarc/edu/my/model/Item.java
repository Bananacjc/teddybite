package tarc.edu.my.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import tarc.edu.my.teddybite.util.ColorCode;
import tarc.edu.my.teddybite.util.ValidationMethod;

/**
 *
 * @author Ethel / ShaoRong
 */
public class Item implements Remark, Cloneable, Comparable<Item>
{

    private String itemCode;
    private String itemName;
    private double itemPrice;
    private String itemRemark;
    private String itemCategory;

    private static final char ITEM_CODE = 'I';
    private static final char BURGER_CODE = 'B';
    private static final char FRIED_CODE = 'F';
    private static final char BEVERAGE_CODE = 'W';
    private static final char DESSERT_CODE = 'D';
    private static final char CONDIMENT_CODE = 'C';
    private static final String ITEM_FILEPATH = "MenuItem.txt";

    public Item()
    {
        this("", "", 0.0f, "");
    }

    public Item(String itemCode, String itemName, double itemPrice, String itemCategory)
    {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        itemRemark = "";
        this.itemCategory = itemCategory;
    }

    public String getitemCode()
    {
        return itemCode;
    }

    public String getitemName()
    {
        return itemName;
    }

    public double getitemPrice()
    {
        return itemPrice;
    }

    public void setitemCode(String code)
    {
        this.itemCode = code;
    }

    public void setitemName(String name)
    {
        this.itemName = name;
    }

    public void setitemPrice(double price)
    {
        this.itemPrice = price;
    }

    public String getitemCategory()
    {
        return itemCategory;
    }

    public void setitemCategory(String itemCategory)
    {
        this.itemCategory = itemCategory;
    }

    public String getRemark()
    {
        return itemRemark;
    }

    private static String getNewItemCode(String itemCategory)
    {
        char categoryCode;
        int count = countNoOfItem();

        categoryCode
                = switch (itemCategory) {
            case "Burger" ->
                BURGER_CODE;
            case "Fried Chicken" ->
                FRIED_CODE;
            case "Beverages" ->
                BEVERAGE_CODE;
            case "Desserts" ->
                DESSERT_CODE;
            case "Condiments" ->
                CONDIMENT_CODE;
            default ->
                ITEM_CODE;
        };

        return ITEM_CODE + "" + categoryCode + "" + String.format("%04d", count + 1);

    }

    private static int countNoOfItem()
    {
        int lines = 0;
        try {
            BufferedReader fr = new BufferedReader(new FileReader(ITEM_FILEPATH));
            while (fr.readLine() != null) {
                lines++;
            }
            fr.close();

        } catch (IOException ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + ITEM_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }

        return lines;
    }

    @Override
    public void setExtraSauce(int quantity, int check)
    {
        if (check == 0) {
            if (itemRemark.equals("")) {
                itemRemark = itemRemark.concat(quantity + " x " + EXTRA_SAUCE);
            } else {
                itemRemark = itemRemark.concat(", " + quantity + " x " + EXTRA_SAUCE);
            }
        } else {
            itemRemark = itemRemark.concat(" & " + EXTRA_SAUCE);
        }

    }

    @Override
    public void setExtraCheese(int quantity, int check)
    {
        if (check == 0) {
            if (itemRemark.equals("")) {
                itemRemark = itemRemark.concat(quantity + " x " + EXTRA_CHEESE);
            } else {
                itemRemark = itemRemark.concat(", " + quantity + " x " + EXTRA_CHEESE);
            }
        } else {
            itemRemark = itemRemark.concat(" & " + EXTRA_CHEESE);
        }
    }

    @Override
    public void setExtraLectuce(int quantity, int check)
    {
        if (check == 0) {
            if (itemRemark.equals("")) {
                itemRemark = itemRemark.concat(quantity + " x " + EXTRA_LECTUCE);
            } else {
                itemRemark = itemRemark.concat(", " + quantity + " x " + EXTRA_LECTUCE);
            }
        } else {
            itemRemark = itemRemark.concat(" & " + EXTRA_LECTUCE);
        }
    }

    @Override
    public void setLessIce(int quantity)
    {
        if (itemRemark.equals("")) {
            itemRemark = itemRemark.concat(quantity + " x " + LESS_ICE);
        }
    }

    @Override
    public void setNoIce(int quantity)
    {
        if (itemRemark.equals("")) {
            itemRemark = itemRemark.concat(quantity + " x " + NO_ICE);
        }
    }

    @Override
    public String toString()
    {
        return String.format("%s\t%-10s\t%.2f", itemCode, itemName, itemPrice);
    }

    @Override
    public int compareTo(Item other)
    {
        return this.itemCode.compareToIgnoreCase(((Item) other).itemCode);
    }

    @Override
    public Item clone() throws CloneNotSupportedException
    {
        Item temp = (Item) super.clone();
        temp.itemCode = itemCode;
        temp.itemName = itemName;
        temp.itemPrice = itemPrice;
        temp.itemRemark = itemRemark;
        return temp;
    }

    public static void addItem()
    {

        String newItemCategory = "";
        double newItemPrice = 0f;
        String newItemName;

        Scanner in = new Scanner(System.in);
        boolean isInteger = false;
        int optionChoice = 0;
        char continueChoice;

        do {
            do {
                System.out.println("1. Burger");
                System.out.println("2. Fried Chicken ");
                System.out.println("3. Beverages");
                System.out.println("4. Desserts");
                System.out.println("5. Condiments");
                System.out.print("Choose Item CATEGORY : ");

                try {
                    isInteger = in.hasNextInt();
                    optionChoice = in.nextInt();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();
                } finally {
                    in.nextLine();
                }

            } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, 1, 5));

            switch (optionChoice) {
                case 1 ->
                    newItemCategory = "Burger";
                case 2 ->
                    newItemCategory = "Fried Chicken";
                case 3 ->
                    newItemCategory = "Beverages";
                case 4 ->
                    newItemCategory = "Desserts";
                case 5 ->
                    newItemCategory = "Condiments";
            }

            System.out.print("Enter item NAME : ");
            newItemName = in.nextLine();

            do {
                System.out.print("Enter item PRICE : ");

                try {
                    isInteger = in.hasNextDouble();
                    newItemPrice = in.nextDouble();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();
                } finally {
                    in.nextLine();
                }
            } while (!isInteger);

            do {
                System.out.print("Add the new Item ? (Y = Yes, N = No) : ");
                continueChoice = in.nextLine().toUpperCase().charAt(0);
            } while (ValidationMethod.validateYesNo(continueChoice));

            switch (continueChoice) {
                case 'Y' -> {
                    Item newItem = new Item(
                            getNewItemCode(newItemCategory),
                            newItemName,
                            newItemPrice,
                            newItemCategory);
                    writeItem(newItem);
                }
                case 'N' -> {
                    ColorCode.setTextRed();
                    System.out.println("Item discarded...");
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

    public static void deleteAllItem()
    {
        char continueChoice;

        Scanner in = new Scanner(System.in);
        do {
            System.out.println("Confirm to delete all Item ? (Y = Yes, N = No)");
            ColorCode.setTextRed();
            System.out.println("**NOTE** this changes is irreversible");
            ColorCode.setTextBlack();
            System.out.print(">>> ");
            continueChoice = in.nextLine().toUpperCase().charAt(0);
        } while (ValidationMethod.validateYesNo(continueChoice));

        switch (continueChoice) {
            case 'Y' -> {
                try {
                    BufferedWriter fw = new BufferedWriter(new FileWriter(ITEM_FILEPATH, false));
                    fw.close();
                    ColorCode.setTextGreen();
                    System.out.println("All Content in " + ITEM_FILEPATH + " Deleted.");
                    ColorCode.setTextBlack();
                    System.out.println();
                    fw.close();
                } catch (IOException ex) {
                    ColorCode.setTextRed();
                    System.out.println("Error Occurred on " + ITEM_FILEPATH + ". Please contact Admin.");
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

    public static void deleteItem()
    {
        String id;
        ArrayList<String> list = new ArrayList<>();
        boolean found = false;

        Scanner in = new Scanner(System.in);

        System.out.printf("Enter the Item Code to Remove an Item : ");
        id = in.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(ITEM_FILEPATH))) {
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

        } catch (IOException ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + ITEM_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }
        if (found == true) {
            writeItem(list);
            ColorCode.setTextGreen();
            System.out.println("Item removed successfully.");
            ColorCode.setTextBlack();
            System.out.println();
        } else {
            System.out.println("Item not found");
        }

    }

    public static void searchItem()
    {
        ArrayList<String> itemList = readItem();
        boolean isInteger = true;
        int optionChoice = 0;
        char continueChoice;
        String search;
        boolean found;

        Scanner in = new Scanner(System.in);

        do {
            do {
                System.out.println("1. Search By Item Code");
                System.out.println("2. Search By Item Name");
                System.out.println("3. Search By Item Category");
                System.out.printf("Your Option : ");
                try {
                    isInteger = in.hasNextInt();
                    optionChoice = in.nextInt();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();
                } finally {
                    in.nextLine();
                }
            } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, 1, 3));

            switch (optionChoice) {
                case 1 -> {
                    found = false;
                    System.out.printf("Enter the Item CODE to Search: ");
                    search = in.nextLine();
                    for (String item : itemList) {
                        String temp[] = item.split("\\|");

                        if (temp[0].equals(search)) {
                            found = true;
                            System.out.println("Item Code : " + temp[0]);
                            System.out.println("Item Category : " + temp[1]);
                            System.out.println("Item Name : " + temp[2]);
                            System.out.printf("Item Price : RM %.2f\n", Double.valueOf(temp[3]));
                            System.out.println("\n----------------------------------\n");
                        }

                    }
                    if (found == false) {
                        ColorCode.setTextRed();
                        System.out.println("Unable to find the item code entered");
                        ColorCode.setTextBlack();
                        System.out.println();
                    }
                }
                case 2 -> {
                    found = false;
                    System.out.printf("Enter the Item NAME to Search: ");
                    search = in.nextLine();
                    for (String item : itemList) {
                        String temp[] = item.split("\\|");

                        if (temp[2].equals(search)) {
                            found = true;
                            System.out.println("Item Code : " + temp[0]);
                            System.out.println("Item Category : " + temp[1]);
                            System.out.println("Item Name : " + temp[2]);
                            System.out.printf("Item Price : RM %.2f\n", Double.valueOf(temp[3]));
                            System.out.println("\n----------------------------------\n");
                        }

                    }
                    if (found == false) {
                        System.out.println("Unable to find the name entered");
                    }
                }
                case 3 -> {
                    found = false;
                    System.out.print("1.Burgers\n2. Fried Chicken\n3. Beverages\n4. Desserts\n5. Condiments\n");
                    System.out.print("Select Item CATEGORY : ");
                    try {
                        int category_option = in.nextInt();
                        search = switch (category_option) {
                            case 1 ->
                                "Burger";
                            case 2 ->
                                "Fried Chicken";
                            case 3 ->
                                "Beverages";
                            case 4 ->
                                "Desserts";
                            case 5 ->
                                "Condiments";
                            default ->
                                "";
                        };

                        for (String item : itemList) {
                            String temp[] = item.split("\\|");

                            if (temp[1].equals(search)) {
                                found = true;
                                System.out.println("Item Code : " + temp[0]);
                                System.out.println("Item Category : " + temp[1]);
                                System.out.println("Item Name : " + temp[2]);
                                System.out.printf("Item Price : RM %.2f\n", Double.valueOf(temp[3]));
                                System.out.println("\n----------------------------------\n");
                            }
                        }
                        if (found == false) {
                            System.out.println("Unable to find the name entered");
                        }
                    } catch (InputMismatchException ex) {
                        ValidationMethod.printNumericInputError();
                    } finally {
                        in.nextLine();
                    }

                }
            }
            do {
                System.out.println("Continue to search ? (Y = Yes, N = No) : ");
                continueChoice = in.nextLine().toUpperCase().charAt(0);
            } while (ValidationMethod.validateYesNo(continueChoice));
        } while (continueChoice == 'Y');

    }

    public static void modifyItem()
    {
        Scanner in = new Scanner(System.in);
        ArrayList<String> itemList = readItem();

        int itemOption;
        String itemCode;
        int found = -1;
        char YN;
        boolean isInteger = true;
        int optionChoice = -1;

        System.out.print("Enter Item code : ");
        itemCode = in.nextLine();
        int count = 0;

        for (String s : itemList) {
            String detail[] = s.split("\\|", 2);
            if (detail[0].equals(itemCode)) {
                found = count;
            }
            count++;
        }
        if (found == -1) {
            System.out.println("Item not found");
            return;
        }

        String itemDetails[] = itemList.get(found).split("\\|");

        do {
            do {
                System.out.println("Current Value");
                System.out.println("-------------------");
                System.out.println("Item Code            : " + itemDetails[0]);
                System.out.println("Item Category        : " + itemDetails[1]);
                System.out.println("Item Name            : " + itemDetails[2]);
                System.out.printf("Item Price           : RM %.2f\n", Double.valueOf(itemDetails[3]));

                System.out.println("Which part do you wanna modify");
                System.out.println("------------------------------");
                System.out.println("0. Confirm Modification");
                System.out.println("1. Item Category");
                System.out.println("2. Item Name");
                System.out.println("3. Item Price");
                System.out.print("Select an Option >> ");

                try {
                    isInteger = in.hasNextInt();
                    optionChoice = in.nextInt();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();
                } finally {
                    in.nextLine();
                }
            } while (!isInteger || ValidationMethod.validateOptionRange(optionChoice, ValidationMethod.EXIT_OPT, 3));

            switch (optionChoice) {
                case 1 -> {
                    System.out.println("1. Burgers \n2. Fried Chicken \n3. Beverages \n4. Desserts \n5. Condiments");
                    System.out.print("Select new Item Category : ");
                    try {
                        isInteger = in.hasNextInt();
                        optionChoice = in.nextInt();
                    } catch (InputMismatchException ex) {
                        ValidationMethod.printNumericInputError();
                    } finally {
                        in.nextLine();
                    }

                    itemDetails[1] = switch (optionChoice) {
                        case 1 ->
                            "Burger";
                        case 2 ->
                            "Fried Chicken";
                        case 3 ->
                            "Beverages";
                        case 4 ->
                            "Desserts";
                        case 5 ->
                            "Condiments";
                        default ->
                            "";
                    };
                }
                case 2 -> {
                    System.out.print("Enter new Item NAME : ");
                    String name = in.nextLine();
                    itemDetails[2] = name;
                }
                case 3 -> {
                    try {
                        System.out.print("Enter new Item Price : ");
                        itemDetails[3] = String.valueOf(in.nextDouble());
                    } catch (NumberFormatException ex) {
                        ValidationMethod.printNumericInputError();

                    } finally {
                        in.nextLine();
                    }
                }

            }

            do {
                System.out.print("Continue modify the current item detail again ? (Y = Yes, N = No) : ");
                YN = in.nextLine().toUpperCase().charAt(0);

            } while (ValidationMethod.validateYesNo(YN));

        } while (YN == 'Y');

        do {
            System.out.print("Save the modification ? (Y = Yes, N = No) : ");
            YN = in.nextLine().toUpperCase().charAt(0);
        } while (ValidationMethod.validateYesNo(YN));

        switch (YN) {
            case 'Y' -> {
                String mod = itemDetails[0] + '|' + itemDetails[1] + '|' + itemDetails[2] + '|' + itemDetails[3];
                itemList.set(found, mod);

                writeItem(itemList);
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

    private static ArrayList<String> readItem()
    {
        String textLine;
        ArrayList<String> itemList = new ArrayList<>();
        try {
            BufferedReader fr = new BufferedReader(new FileReader(ITEM_FILEPATH));
            while (fr.ready()) {
                textLine = fr.readLine();
                itemList.add(textLine);
            }
            fr.close();
        } catch (IOException ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + ITEM_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }

        return itemList;
    }

    private static void writeItem(Item newItem)
    {
        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(ITEM_FILEPATH, true));
            fw.write(newItem.itemCode + '|');
            fw.write(newItem.itemCategory + '|');
            fw.write(newItem.itemName + '|');
            fw.write(Double.toString(newItem.itemPrice) + "\n");
            fw.close();
        } catch (IOException ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + ITEM_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }
    }

    private static void writeItem(ArrayList<String> list)
    {
        try {
            FileWriter fw = new FileWriter(ITEM_FILEPATH, false);
            for (String s : list) {
                fw.write(s);
                fw.write("\n");
            }
            fw.close();
        } catch (IOException ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + ITEM_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }
    }

}
