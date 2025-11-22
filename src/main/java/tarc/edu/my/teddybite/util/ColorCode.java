/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarc.edu.my.teddybite.util;

/**
 *
 * @author CHEONG JAU CHUN
 */
public final class ColorCode
{

    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\033[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    public static final void setTextBlack()
    {
        System.out.print(BLACK);
    }

    public static final void setTextRed()
    {
        System.out.print(RED);
    }

    public static final void setTextGreen()
    {
        System.out.print(GREEN);
    }

    public static final void setTextYellow()
    {
        System.out.print(YELLOW);
    }

    public static final void setTextBlue()
    {
        System.out.print(BLUE);
    }

    public static final void setTextPurple()
    {
        System.out.print(PURPLE);
    }

    public static final void setTextCyan()
    {
        System.out.print(CYAN);
    }

    public static final void setTextWhite()
    {
        System.out.print(WHITE);
    }

}
