import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class: Octorok
 * Coder: Toby Thomas
 * Teacher: Ms. Shaw | ICS4U
 * Date: 05/25/15
 * Description: This class creates an Octorok object, an enemy that attacks Link. Octorok spawns around Link when created in the Game class, not on him
 * to avoid unfair damage. When spawned, Octoroks begin moving by the move and play methods. The play method is what determines the Octorok's direction
 * and velocity, as the move method is what changes the Octoroks position. In addition to damaging Link by touching him, Octoroks shoot Rock projectiles
 * that are also harmful to Link. Octoroks shoot at a rate of 1 Rock every 2.5 seconds. Finally, Octorok features pause, unpause and terminate methods
 * that edit the Octorok and its Rock's velocities and activity.
 */

public class Octorok {

    //Declare position and movement variables
    private int x;
    private int y;
    private int dx;
    private int dy;
    public char direction = 'd';
    private int random;

    //Declare activity variables
    private Timer walkTimer;
    private Timer shootTimer;
    private boolean dead = false;

    //Declare and assign new projectile (Rock)
    public Rock rock = new Rock(this);

    //Declare graphic variables
    private Image image; //Current graphic
    ImageIcon walkD = new ImageIcon(this.getClass().getResource("images/octorok/ocD.gif"));
    ImageIcon walkU = new ImageIcon(this.getClass().getResource("images/octorok/ocU.gif"));
    ImageIcon walkL = new ImageIcon(this.getClass().getResource("images/octorok/ocL.gif"));
    ImageIcon walkR = new ImageIcon(this.getClass().getResource("images/octorok/ocR.gif"));


    //Constructor that inherits Link's position and spawns an Octorok around him
    public Octorok(Link link) {
        image = walkD.getImage();
        x = (int) (Math.random() * (372 - getWidth()) + 64);
        y = (int) (Math.random() * (208 - getHeight()) + 60);

        //If Octorok's position is on Link, reset position (to avoid unfair damage)
        while (link.getX() < getX() + getWidth() && link.getY() < getY() + getHeight()
                && getX() < link.getX() + link.getWidth() && getY() < link.getY() + link.getHeight()) {
            x = (int) ((Math.random() * (372 - getWidth())) + 64);
            y = (int) ((Math.random() * (208 - getHeight())) + 60);
        }
    }


    //Game method that changes Octorok's position
    public void move() {
        x += dx;
        y += dy;
    }


    //Get Helper methods that return the Octorok's position, direction and graphic properties, as well as the Octorok itself
    public int getX() { return x; }
    public int getY() { return y; }
    public char getDirection() { return direction; }
    public Image getImage() { return image; }
    public int getWidth() { return image.getWidth(null); }
    public int getHeight() { return image.getHeight(null); }
    public Octorok getOctorok() { return this; }


    //Set Helper methods that set the Octorok's position and activity
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setDead(boolean dead) { this.dead = dead; }


    //Play method that changes the Octorok's direction, velocity, and appropriate graphic, every second
    public void play() {
        if (!dead) {
            walkTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    random = (int) (Math.random() * 4); //Math.random is used to randomly generate a new direction
                    switch (random) {
                        case (0):
                            direction = 'd';
                            image = walkD.getImage();
                            dx = 0;
                            dy = 1;
                            break;
                        case (1):
                            direction = 'u';
                            image = walkU.getImage();
                            dx = 0;
                            dy = -1;
                            break;
                        case (2):
                            direction = 'l';
                            image = walkL.getImage();
                            dx = -1;
                            dy = 0;
                            break;
                        case (3):
                            direction = 'r';
                            image = walkR.getImage();
                            dx = 1;
                            dy = 0;
                            break;
                    }
                }
            });
            walkTimer.setRepeats(true); //walkTimer is set to repeat
            walkTimer.start();
        }
    }


    //Shoot method declares and shoots a new Rock every 2.5 seconds
    public void shoot() {
        shootTimer = new Timer(2500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                rock = new Rock(getOctorok());
                rock.shoot();
            }
        });
        shootTimer.setRepeats(true); //shootTimer is set to repeat
        shootTimer.start();
    }


    //Pause method that sets the Octorok's velocity and activity to 0 when called
    public void pause() {
        walkTimer.stop();
        dx = 0;
        dy = 0;
        shootTimer.stop();
        rock.pause(); //Pauses Rock
    }


    //Unpause method recalls play and shoot methods, as well as unpauses the Octorok's current Rock
    public void unpause() {
        play();
        rock.unpause();
        shoot();
    }


    //Die Helper method sets Octorok's dead to true
    public void die() {
        setDead(true);
    }

}
