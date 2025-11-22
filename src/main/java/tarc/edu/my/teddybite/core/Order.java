package tarc.edu.my.teddybite.core;

/**
 *
 * @author ShaoRong
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import tarc.edu.my.teddybite.util.ColorCode;
import tarc.edu.my.teddybite.util.ValidationMethod;

public class Order
{

    public HashMap<Item, Integer> orderList = new HashMap<>();
    private String orderID;
    private LocalDate orderDate;
    private static int noOfOrder = 0;
    private static final char ORDER_CODE = 'O';

    public Order()
    {
        this(LocalDate.now());
    }

    public Order(LocalDate orderDate)
    {
        orderID = ORDER_CODE + "" + String.format("%04d", noOfOrder + 1);
        this.orderDate = orderDate;
    }

    public String getOrderID()
    {
        return orderID;
    }

    public void setOrderID(String orderID)
    {
        this.orderID = orderID;
    }

    public String getorderDate()
    {
        return orderDate.toString();
    }

    public LocalDate getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate)
    {
        this.orderDate = orderDate;
    }

    public void setOrderDate(int year, int month, int day)
    {
        orderDate = LocalDate.of(year, month, day);
    }

    @Override
    public String toString()
    {
        return String.format("Order ID : " + orderID + "\t\tDate: " + orderDate);
    }

    public void orderItem(HashSet<Item> menuList)
    {
        String code;
        boolean codeFound = false;
        boolean isInteger = false;
        char otherRemark;
        int itemQty = 0;
        String checkCategory;
        Item orderItem = new Item();
        char contOrder;
        Scanner in = new Scanner(System.in);

        do {
            do {

                System.out.print("Enter item CODE to order : ");
                code = in.nextLine();

                for (Item item : menuList) {
                    if (item.getitemCode().equalsIgnoreCase(code)) {
                        orderItem = item;
                        codeFound = true;
                        break;
                    }
                }
                if (codeFound == false) {
                    ColorCode.setTextRed();
                    System.out.println("The Item code does not exist !\n");
                    ColorCode.setTextBlack();
                    System.out.println();
                }

            } while (codeFound == false);

            do {
                System.out.print("Enter the QUANTITY to order : ");
                try {
                    isInteger = in.hasNextInt();
                    itemQty = in.nextInt();
                } catch (Exception ex) {
                    ValidationMethod.printNumericInputError();
                } finally {
                    in.nextLine();
                }
            } while (ValidationMethod.validateItemQuantity(itemQty)); // Add stock if needed

            checkCategory = orderItem.getitemCategory();

            if (checkCategory.equals("Burger") || checkCategory.equals("Beverages")) {
                char checkRemark;
                int checkStatus = 0;
                int remarkOption = 0;

                do {
                    System.out.print("Do you require any remark ? (Y = Yes, N = No) : ");
                    checkRemark = in.nextLine().toUpperCase().charAt(0);

                } while (ValidationMethod.validateYesNo(checkRemark));

                switch (checkRemark) {
                    case 'Y' -> {
                        do {
                            if (checkCategory.equals("Burger")) {

                                do {
                                    System.out.println("\n[Remark]");
                                    System.out.println("1. Extra Lectuce");
                                    System.out.println("2. Extra Sauce");
                                    System.out.println("3. Extra Cheese");
                                    System.out.print("Enter an option for remark >> ");

                                    try {
                                        isInteger = in.hasNextInt();
                                        remarkOption = in.nextInt();
                                    } catch (Exception ex) {
                                        ValidationMethod.printNumericInputError();
                                    } finally {
                                        in.nextLine();
                                    }

                                } while (!isInteger || ValidationMethod.validateOptionRange(remarkOption, 1, 3));

                                switch (remarkOption) {
                                    case 1 ->
                                        orderItem.setExtraLectuce(itemQty, checkStatus);
                                    case 2 ->
                                        orderItem.setExtraSauce(itemQty, checkStatus);
                                    case 3 ->
                                        orderItem.setExtraCheese(itemQty, checkStatus);
                                }
                            } else if (checkCategory.equals("Beverages")) {
                                do {
                                    System.out.println("\n[Remark]");
                                    System.out.println("1. Less Ice");
                                    System.out.println("2. No Ice");
                                    System.out.print("Enter an option for remark >> ");

                                    try {
                                        isInteger = in.hasNextInt();
                                        remarkOption = in.nextInt();
                                    } catch (Exception ex) {
                                        ValidationMethod.printNumericInputError();
                                    } finally {
                                        in.nextLine();
                                    }

                                } while (!isInteger || ValidationMethod.validateOptionRange(remarkOption, 1, 2));

                                switch (remarkOption) {
                                    case 1 ->
                                        orderItem.setLessIce(itemQty);
                                    case 2 ->
                                        orderItem.setNoIce(itemQty);
                                }
                            }

                            do {
                                System.out.print("Do you need others remark? (Y/N) >> ");
                                otherRemark = in.nextLine().toUpperCase().charAt(0);
                            } while (ValidationMethod.validateYesNo(otherRemark));
                        } while (otherRemark == 'Y');
                    }

                }

            }

            if (orderList.containsKey(orderItem)) {
                int newQty = orderList.get(orderItem) + itemQty;
                orderList.replace(orderItem, newQty);
            } else {
                orderList.put(orderItem, itemQty);
            }
            ColorCode.setTextGreen();
            System.out.println(itemQty + " x " + orderItem.getitemName() + " has successfully added to your order list.");
            ColorCode.setTextBlack();

            do {
                System.out.print("\nDo you want to continue order ? (Y/N) >> ");
                contOrder = in.nextLine().charAt(0);
            } while (ValidationMethod.validateYesNo(contOrder));

        } while (contOrder == 'Y');
        System.out.println();

    }

    public void removeOrder()
    {
        Scanner in = new Scanner(System.in);
        String code;
        boolean codeFound = false;
        boolean isInteger = false;
        char contRemove;
        int removeOpt = 0;

        if (orderList.isEmpty()) {
            ColorCode.setTextRed();
            System.out.println("You haven't order anything yet.");
            ColorCode.setTextBlack();
            System.out.println();
            return;
        }

        do {
            System.out.println("\n[ Remove ]");
            System.out.println("0. Back to Item Modification Menu");
            System.out.println("1. Remove all item in order list");
            System.out.println("2. Remove all the same item in order list");
            System.out.print("Enter an option to remove : ");

            try {
                isInteger = in.hasNextInt();
                removeOpt = in.nextInt();
            } catch (Exception ex) {
                ValidationMethod.printNumericInputError();
            } finally {
                in.nextLine();
            }
        } while (!isInteger || ValidationMethod.validateOptionRange(removeOpt, ValidationMethod.EXIT_OPT, 2));

        switch (removeOpt) {
            case 1 -> {
                orderList.clear();
                ColorCode.setTextGreen();
                System.out.println("Your order list is empty now.");
                ColorCode.setTextBlack();
            }
            case 2 -> {
                do {
                    codeFound = false;
                    System.out.print("\nEnter the item code to remove: ");
                    code = in.nextLine();

                    for (Item item : orderList.keySet()) {
                        if (item.getitemCode().equalsIgnoreCase(code)) {
                            orderList.remove(item);
                            codeFound = true;
                            ColorCode.setTextGreen();
                            System.out.println("The order item has been removed.");
                            ColorCode.setTextBlack();
                            break;

                        }
                    }
                    if (codeFound == false) {
                        ColorCode.setTextRed();
                        System.out.println("The Item does not exist in the order list !\n");
                        ColorCode.setTextBlack();
                    }

                    do {
                        System.out.print("Do you want to continue remove order item ? (Y/N) >> ");
                        contRemove = in.nextLine().toUpperCase().charAt(0);
                    } while (ValidationMethod.validateYesNo(contRemove));
                } while (contRemove == 'Y');
                System.out.println();
            }
        }
    }

    public void displayOrder()
    {

        if (orderList.isEmpty()) {
            ColorCode.setTextRed();
            System.out.println("You haven't order anything yet.");
            ColorCode.setTextBlack();
            System.out.println();
            return;
        }

        System.out.println("Order ID : " + orderID);
        sortOrder(orderList);
        System.out.println();
    }

    private void sortOrder(HashMap<Item, Integer> items)
    {
        int count = 1;
        List<Item> sortedKeys = new ArrayList<>(items.keySet());
        Collections.sort(sortedKeys);

        for (Item i : sortedKeys) {
            System.out.printf("%d. %-2s\t%-20s\tQty: %d\n", count, i.getitemCode(), i.getitemName(), orderList.get(i));
            if (!i.getRemark().equals("")) {
                ColorCode.setTextYellow();
                System.out.println("Remark : " + i.getRemark());
                ColorCode.setTextBlack();
            }
            count++;
        }
    }

}
