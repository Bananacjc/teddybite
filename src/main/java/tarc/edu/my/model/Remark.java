package tarc.edu.my.model;

/**
 *
 * @author ShaoRong
 */
public interface Remark
{

    String EXTRA_LECTUCE = "Extra lectuce";
    String EXTRA_CHEESE = "Extra cheese";
    String EXTRA_SAUCE = "Extra Sauce";
    String LESS_ICE = "Less ice";
    String NO_ICE = "No ice";

    public void setExtraCheese(int quantity, int check);

    public void setExtraSauce(int quantity, int check);

    public void setExtraLectuce(int quantity, int check);

    public void setLessIce(int quantity);

    public void setNoIce(int quantity);

}
