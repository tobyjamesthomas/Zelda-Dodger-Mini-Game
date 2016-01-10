import javax.swing.*;
import java.awt.*;

/**
 * Class: Heart
 * Coder: Toby Thomas
 * Teacher: Ms. Shaw | ICS4U
 * Date: 05/25/15
 * Description: This class creates a Heart object, a representation of Link's life. There are three types of Hearts: full, half and empty. Each type
 * has a respective graphic and is used depending on Link's remaining life. The Heart class features two constructors and two get helper methods.
 */

public class Heart {

    //Declare graphic variables
    private Image image; //Current graphic
    ImageIcon full = new ImageIcon(this.getClass().getResource("images/heart/fullH.gif"));
    ImageIcon half = new ImageIcon(this.getClass().getResource("images/heart/halfH.gif"));
    ImageIcon empty = new ImageIcon(this.getClass().getResource("images/heart/emptyH.gif"));


    //Default constructor that sets the Heart's graphic to full
    public Heart() { image = full.getImage(); }


    //Custom constructor that sets the Heart's graphic to either full, half or empty
    public Heart(int type) {
        switch (type) {
            case (2):
                image = full.getImage();
                break;
            case (1):
                image = half.getImage();
                break;
            case (0):
                image = empty.getImage();
                break;
        }
    }


    //Get Helper methods that return the Heart's graphic and width
    public Image getImage() {
        return image;
    }
    public int getWidth() { return image.getWidth(null); }


}
