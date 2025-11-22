/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarc.edu.my.teddybite.payment;

/**
 *
 * @author CHEONG JAU CHUN
 */
public class CreditPayment extends Payment
{

    public CreditPayment()
    {
        this(0f);
    }

    public CreditPayment(double paymentAmount)
    {
        super(paymentAmount);
    }

    @Override
    public String toString()
    {
        String strPaymentType = "Payment Type : " + this.getClass().getSimpleName();
        return super.toString() + strPaymentType;
    }

}
