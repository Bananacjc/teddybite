/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarc.edu.my.teddybite.payment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import tarc.edu.my.teddybite.util.ColorCode;
import tarc.edu.my.teddybite.util.ColorCode;
import tarc.edu.my.teddybite.core.Item;

/**
 *
 * @author CHEONG JAU CHUN
 */
public abstract class Payment
{

    /* Data Fields */
    private String paymentID;
    private double paymentAmount;
    private static double totalPaymentAmount;
    private static int totalNoOfPaymentToday = 0;
    private static final char PAYMENT_CODE = 'P';
    private static final String PAY_FILEPATH = "Payment.txt";

    /* Payment - No-arg Constructors */
    public Payment()
    {
        // Prevent all null pointer reference
        this(0f);
    }

    /* Payment - Parameterized Constructor */
    public Payment(double paymentAmount)
    {
        paymentID = PAYMENT_CODE + "" + String.format("%04d", totalNoOfPaymentToday + 1);
        this.paymentAmount = paymentAmount;
    }

    /* Getters */
    // PaymentID
    public String getPaymentID()
    {
        return paymentID;
    }

    // PaymentAmount
    public double getPaymentAmount()
    {
        return paymentAmount;
    }

    /* Setters */
    // PaymentID
    public void setPaymentID(String paymentID)
    {
        this.paymentID = paymentID;
    }

    // PaymentAmount
    public void setPaymentAmount(double paymentAmount)
    {
        this.paymentAmount = paymentAmount;
    }

    @Override
    public String toString()
    {
        String strPaymentID = "Payment ID : " + paymentID + "\n";
        String strPaymentAmount = "Payment Amount : RM " + String.format("%.2f", paymentAmount) + "\n";
        return strPaymentID + strPaymentAmount;
    }

    public void calculatePayment(HashMap<Item, Integer> orderList)
    {
        for (Item item : orderList.keySet()) {
            paymentAmount += item.getitemPrice() * orderList.get(item);
        }

        totalPaymentAmount += paymentAmount;

    }

    public void writePayment()
    {
        totalNoOfPaymentToday++;
        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(PAY_FILEPATH, true));
            fw.write(Integer.toString(totalNoOfPaymentToday) + '|');
            fw.write(Double.toString(paymentAmount) + "\n");
            fw.close();
        } catch (Exception ex) {
            ColorCode.setTextRed();
            System.out.println("Error Occurred on " + PAY_FILEPATH + ". Please contact Admin.");
            ColorCode.setTextBlack();
            System.out.println();
        }
    }

}
