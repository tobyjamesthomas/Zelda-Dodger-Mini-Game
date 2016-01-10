import javax.swing.*;

/**
 * Class: ZeldaDodger
 * Coder: Toby Thomas
 * Teacher: Ms. Shaw | ICS4U
 * Date: 05/25/15
 * Description: The purpose of this class is to test the Game class. ZeldaDodger creates a JFrame and adds Game to the frame.
 */

public class ZeldaDodger extends JFrame { //ZeldaDodger creates a JFrame


    //Default constructor that creates a JFrame and adds Game to the frame
    public ZeldaDodger() {

        add(new Game()); //Implements and adds Game to frame

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 348);
        setLocationRelativeTo(null);
        setTitle("Zelda Dodger");
        setResizable(false); //The size of the frame cannot be changed by the player
        setVisible(true);
    }


    //Runs ZeldaDodger
    public static void main(String[] args) { new ZeldaDodger(); }


}