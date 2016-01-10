import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class: Rock
 * Coder: Toby Thomas
 * Teacher: Ms. Shaw | ICS4U
 * Date: 05/25/15
 * Description: This class creates a Rock object, a projectile that is shot from an Octorok and damages Link, if touched. Rock is created by an Octorok, and
 * inherits its position and direction. When the Shoot method is called, the Rock's velocity is assigned, to which it is animated in the Game class. Finally,
 * Rock features pause, unpause and terminate methods that edit the Rock's velocity and activity.
 */

public class Rock {

    //Declare position and movement variables
    private int x;
    private int y;
    private int dx; //Change in x
    private int dy; //Change in y
    private int dxTemp;
    private int dyTemp;
    private char direction;

    //Declare activity variables
    private Timer timer;
    public boolean live = true;

    //Declare graphic variables
    private Image image; //Current graphic
    ImageIcon rock = new ImageIcon(this.getClass().getResource("images/rock/rock.gif"));
    ImageIcon blank = new ImageIcon(this.getClass().getResource("images/rock/rockBroken.gif"));


    //Constructor that inherits an Octorok's position and direction
    public Rock(Octorok octorok) {
        image = rock.getImage();
        x = octorok.getX();
        y = octorok.getY();
        direction = octorok.getDirection();
    }


    //Game method that changes Rock's position
    public void move() {
        x += dx;
        y += dy;
    }


    //Get Helper methods that return the Rock's position and graphic properties
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return image.getWidth(null);
    }
    public int getHeight() { return image.getHeight(null); }
    public Image getImage() {
        return image;
    }


    //Set Helper methods that set the Rock's position
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }


    //Shoot method that sets the Rock's velocity, depending on its direction
    public void shoot() {
        switch (direction) {
            case ('d'):
                dx = 0;
                dy = 2;
                break;
            case ('u'):
                dx = 0;
                dy = -2;
                break;
            case ('l'):
                dx = -2;
                dy = 0;
                break;
            case ('r'):
                dx = 2;
                dy = 0;
                break;
        }
    }


    //Pause method that sets the Rock's velocity to 0 when called
    public void pause() {
        dxTemp = dx; //Velocity is stored temporarily, until Rock is played again
        dyTemp = dy;
        dx = 0; //Velocity is set to 0
        dy = 0;
    }


    //Unpause method that resets the Rock's velocity to prior value
    public void unpause() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { //Timer is used to delay the Rock's unpause
                dx = dxTemp; //Velocity is reset from stored values
                dy = dyTemp;
            }
        });
        timer.setRepeats(false); //Timer is set to play only once
        timer.start();
    }


    //Terminate method removes the Rock from game
    public void terminate() {
        image = blank.getImage(); //Graphic is a blank image
        live = false; //While false, no damage is done to Link
    }
}
