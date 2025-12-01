package tarc.edu.my.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import tarc.edu.my.teddybite.util.ColorCode;

/**
 *
 * @author ASUS
 */
public class Menu
{

    private static final String ITEM_FILEPATH = "MenuItem.txt";

    ArrayList<String> BurgerList = new ArrayList<>();
    ArrayList<String> ChickenList = new ArrayList<>();
    ArrayList<String> BeveragesList = new ArrayList<>();
    ArrayList<String> DessertsList = new ArrayList<>();
    ArrayList<String> CondimentsList = new ArrayList<>();

    public Menu()
    {
        try {
            BufferedReader bufferedR = new BufferedReader(new FileReader(ITEM_FILEPATH));
            String MenuLine;
            while ((MenuLine = bufferedR.readLine()) != null) {
                if (MenuLine.contains("Burger")) {
                    BurgerList.add(MenuLine);
                } else if (MenuLine.contains("Fried Chicken")) {
                    ChickenList.add(MenuLine);
                } else if (MenuLine.contains("Beverages")) {
                    BeveragesList.add(MenuLine);
                } else if (MenuLine.contains("Desserts")) {
                    DessertsList.add(MenuLine);
                } else if (MenuLine.contains("Condiments")) {
                    CondimentsList.add(MenuLine);
                }
            }
        } catch (IOException e) {
            System.out.println("Menu text cannot close");
        }
    }

    public void displayBurgers()
    {
        System.out.println("Burgers");
        System.out.println("=======");
        System.out.printf("%-10s %-20s %-10s \n", "ID", "Name", "Price");
        for (String burger : BurgerList) {
            String bs[] = burger.split("\\|");
            System.out.printf("%-10s %-20s RM %-10s \n", bs[0], bs[2], bs[3]);

        }
    }

    public void displayFriedChicken()
    {
        System.out.println("Fried Chicken");
        System.out.println("=============");
        System.out.printf("%-10s %-20s %-10s \n", "ID", "Name", "Price");
        for (String FChicken : ChickenList) {
            String fs[] = FChicken.split("\\|");
            System.out.printf("%-10s %-20s RM %-10s \n", fs[0], fs[2], fs[3]);
        }
    }

    public void displayBeverages()
    {
        System.out.println("Beverages");
        System.out.println("=========");
        System.out.printf("%-10s %-20s %-10s \n", "ID", "Name", "Price");
        for (String beverage : BeveragesList) {
            String bes[] = beverage.split("\\|");
            System.out.printf("%-10s %-20s RM %-10s \n", bes[0], bes[2], bes[3]);
        }
    }

    public void displayDesserts()
    {
        System.out.println("Desserts");
        System.out.println("========");
        System.out.printf("%-10s %-20s %-10s \n", "ID", "Name", "Price");
        for (String dessert : DessertsList) {
            String ds[] = dessert.split("\\|");
            System.out.printf("%-10s %-20s RM %-10s \n", ds[0], ds[2], ds[3]);
        }
    }

    public void displayCondiments()
    {
        System.out.println("Condiments");
        System.out.println("==========");
        System.out.printf("%-10s %-20s %-10s \n", "ID", "Name", "Price");
        for (String condiment : CondimentsList) {
            String ss[] = condiment.split("\\|");
            System.out.printf("%-10s %-20s RM %-10s \n", ss[0], ss[2], ss[3]);
        }
    }

    public void displayAllMenu()
    {
        System.out.println("------------------------------------------");
        System.out.println("|     M     M  EEEEE  N    N  U    U     |");
        System.out.println("|     M M M M  E      NN   N  U    U     |");
        System.out.println("|     M  M  M  EEEEE  N N  N  U    U     |");
        System.out.println("|     M     M  E      N   NN  U    U     |");
        System.out.println("|     M     M  EEEEE  N    N   UUUU      |");
        System.out.println("------------------------------------------\n");
        displayBurgers();
        displayFriedChicken();
        displayBeverages();
        displayDesserts();
        displayCondiments();
    }

    public HashSet<Item> getMenuHashList()
    {
        HashSet<Item> menuList = new HashSet<>();

        try {
            BufferedReader fr = new BufferedReader(new FileReader(ITEM_FILEPATH));
            String menuLine;

            while ((menuLine = fr.readLine()) != null) {
                String[] itemDetail = menuLine.split("\\|");
                Item menuItem = new Item(itemDetail[0], itemDetail[2], Double.parseDouble(itemDetail[3]), itemDetail[1]);
                menuList.add(menuItem);
            }

        } catch (IOException | NumberFormatException ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + ITEM_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }

        return menuList;

    }

}
