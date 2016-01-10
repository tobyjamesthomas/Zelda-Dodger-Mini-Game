import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Class: Link
 * Coder: Toby Thomas
 * Teacher: Ms. Shaw | ICS4U
 * Date: 05/25/15
 * Description: This class creates a Link object. Link is the character that the player controls in the game. Link has the ability to move and attack.
 * Upon creation, Link has 3 hearts and loses these hearts when he is attacked by Octoroks or Rocks. When attacked, Link enters recovery mode, during
 * which he cannot be harmed for 2 seconds. When Link has no remaining hearts, he dies and the game is over. To control his movements, life and damage
 * from enemies, the Link class has multiple methods, including get and set methods, move, keyPressed and keyReleased, getLife, damaged and recover.
 * The get and set methods return and edit Link's instance variables, respectively. The keyPressed and keyReleased methods recognize KeyEvents by the
 * player and allow Link to move and attack. The getLife, damage and recover methods update Link's life. Finally, Link features pause, unpause and die
 * methods that edit Link's velocity and activity.
 */

public class Link {

    //Declare position and movement variables
    private int x;
    private int y;
    private int dx;
    private int dy;
    public char direction = 'd';

    //Declare activity variables
    private Timer timer;
    private boolean attacking = false;
    private boolean corrected = false; //Used to correct Link's positions when attacking
    public boolean paused = false;
    public boolean recovering = false;
    public float opacity = 1f;

    //Declare life-related variables
    private int life = 6;
    public ArrayList<Heart> hearts = new ArrayList<Heart>();
    public boolean dead = false;

    //Declare graphic variables
    private Image image; //Current graphic
    ImageIcon stillD = new ImageIcon(this.getClass().getResource("images/link/stillD.gif"));
    ImageIcon stillU = new ImageIcon(this.getClass().getResource("images/link/stillU.gif"));
    ImageIcon stillL = new ImageIcon(this.getClass().getResource("images/link/stillL.gif"));
    ImageIcon stillR = new ImageIcon(this.getClass().getResource("images/link/stillR.gif"));
    ImageIcon walkD = new ImageIcon(this.getClass().getResource("images/link/walkD.gif"));
    ImageIcon walkU = new ImageIcon(this.getClass().getResource("images/link/walkU.gif"));
    ImageIcon walkL = new ImageIcon(this.getClass().getResource("images/link/walkL.gif"));
    ImageIcon walkR = new ImageIcon(this.getClass().getResource("images/link/walkR.gif"));
    ImageIcon swordD = new ImageIcon(this.getClass().getResource("images/link/swordD.gif"));
    ImageIcon swordU = new ImageIcon(this.getClass().getResource("images/link/swordU.gif"));
    ImageIcon swordL = new ImageIcon(this.getClass().getResource("images/link/swordL.gif"));
    ImageIcon swordR = new ImageIcon(this.getClass().getResource("images/link/swordR.gif"));
    ImageIcon death = new ImageIcon(this.getClass().getResource("images/link/linkDeath.gif"));


    //Default constructor that sets Link's life to 3 hearts
    public Link() {
        image = stillD.getImage();
        for (int i = 0; i < 3; i++) {
            hearts.add(new Heart());
        }
    }


    //Move method that changes Link's position
    public void move() {
        x += dx;
        y += dy;
    }


    //Get Helper methods that return Link's position, graphic properties, and attacking
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Image getImage() {
        return image;
    }
    public int getWidth() {
        return image.getWidth(null);
    }
    public int getHeight() {
        return image.getHeight(null);
    }
    public boolean getAttacking() { return attacking; }


    //Set Helper methods that set Link's position
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }


    /**
     * The keyPressed method moves Link and activates every time the player is playing the game.
     * The keys that move Link include the arrow keys and space bar.
     * The arrow keys are used to navigate Link within the game, and the space bar makes Link
     * swing his sword. When Link's attack is activated, his position remains constant. However,
     * to compensate for the change in his graphic size, Link's position is temporarily corrected
     * while he is attacking. Finally, Link cannot attack while he is recovering from damage.
     */
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode(); //key stores the key that has been pressed

        if (key == KeyEvent.VK_LEFT && !attacking) { //if the player pressed the left key and Link is not attacking
            dx = -2;
            image = walkL.getImage();
            direction = 'l'; //direction is left
        }

        if (key == KeyEvent.VK_RIGHT && !attacking) { //if the player pressed the right key and Link is not attacking
            dx = 2;
            image = walkR.getImage();
            direction = 'r'; //direction is right
        }

        if (key == KeyEvent.VK_UP && !attacking) { //if the player pressed the up key and Link is not attacking
            dy = -2;
            image = walkU.getImage();
            direction = 'u'; //direction is up
        }

        if (key == KeyEvent.VK_DOWN && !attacking) { //if the player pressed the down key and Link is not attacking
            dy = 2;
            image = walkD.getImage();
            direction = 'd'; //direction is down
        }

        if (key == KeyEvent.VK_SPACE && !recovering) { //if the player pressed the space bar and Link is not recovering
            attacking = true; //Link is attacking
            switch (direction) { //depending on direction, image updates to appropriate attacking graphic
                case ('l'):
                    image = swordL.getImage();
                    while (!corrected) { //corrects position
                        x -= 20;
                        y -= 15;
                        corrected = true; //inhibits multiple corrections if space bar is not released
                    }
                    break;
                case ('r'):
                    image = swordR.getImage();
                    while (!corrected) { //corrects position
                        x -= 5;
                        y -= 15;
                        corrected = true;
                    }
                    break;
                case ('u'):
                    image = swordU.getImage();
                    while (!corrected) { //corrects position
                        x -= 22;
                        y -= 15;
                        corrected = true;
                    }
                    break;
                case ('d'):
                    image = swordD.getImage();
                    while (!corrected) { //corrects position
                        x -= 15;
                        y -= 1;
                        corrected = true;
                    }
                    break;
            }
            dx = 0; //disable movement
            dy = 0;
        }

    }

    /**
     * Opposite to the keyPressed method, the keyReleased method stops Link's movement and
     * activates every time the player releases a button mid-game.
     * The keyReleased method's primary function is to react appropriately when Link stops
     * moving. This includes updating Link's graphic and returning Link to his position
     * prior to attacking.
     */
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode(); //key stores the key that has been pressed

        if (key == KeyEvent.VK_LEFT && !attacking) { //if the player released the left key and Link is not attacking
            dx = 0; //set velocity to 0
            image = stillL.getImage();
            direction = 'l';
        }

        if (key == KeyEvent.VK_RIGHT && !attacking) { //if the player released the right key and Link is not attacking
            dx = 0; //set velocity to 0
            image = stillR.getImage();
            direction = 'r';
        }

        if (key == KeyEvent.VK_UP && !attacking) { //if the player released the up key and Link is not attacking
            dy = 0; //set velocity to 0
            image = stillU.getImage();
            direction = 'u';
        }

        if (key == KeyEvent.VK_DOWN && !attacking) { //if the player released the down key and Link is not attacking
            dy = 0; //set velocity to 0
            image = stillD.getImage();
            direction = 'd';
        }

        if (key == KeyEvent.VK_SPACE) { //if the player released the space bar
            attacking = false; //Link is not attacking

            switch (direction) { //depending on direction, image updates to appropriate still graphic
                case ('l'):
                    image = stillL.getImage();
                    if (!recovering) { //corrects position
                        x += 20;
                        y += 15;
                    }
                    corrected = false; //position is no longer corrected
                    break;
                case ('r'):
                    image = stillR.getImage();
                    if (!recovering) { //corrects position
                        x += 5;
                        y += 15;
                    }
                    corrected = false; //position is no longer corrected
                    break;
                case ('u'):
                    image = stillU.getImage();
                    if (!recovering) { //corrects position
                        x += 22;
                        y += 15;
                    }
                    corrected = false; //position is no longer corrected
                    break;
                case ('d'):
                    image = stillD.getImage();
                    if (!recovering) { //corrects position
                        x += 15;
                        y += 1;
                    }
                    corrected = false; //position is no longer corrected
                    break;
            }
        }
    }


    //getLife method updates Link's hearts based on its remaining life
    public void getLife() {
        switch (life) {
            case (6): //max health
                for (int i = 0; i < 3; i++) {
                    hearts.set(i, new Heart()); //all hearts are full
                }
                break;
            case (5):
                hearts.set(2, new Heart(1)); //last heart is half full
                break;
            case (4):
                hearts.set(2, new Heart(0)); //last heart is empty
                break;
            case (3):
                hearts.set(1, new Heart(1)); //second heart is half full
                break;
            case (2):
                hearts.set(1, new Heart(0)); //second heart is empty
                break;
            case (1):
                hearts.set(0, new Heart(1)); //first heart is half full
                break;
            case (0): //no health
                for (int i = 0; i < 3; i++) {
                    hearts.set(i, new Heart(0)); //all hearts are empty
                }
                break;
        }
    }


    //damaged method decreases Link's life every time it is called
    public void damaged() {
        if (life > 1) { //does not reach 1
            life -= 1; //decreases life until 1
        } else { //when life is 1
            life -= 1; //life becomes 0
            die(); //Link dies
        }
        getLife(); //updates Link's hearts
    }


    //The recover method is called when Link is damaged. It gives him and the player 2 seconds before he can be damaged again
    public void recover() {
        if (!dead) opacity = 0.5f; //sets Link's opacity to half, allowing the player to know when he is recovering
        recovering = true; //Link is recovering
        attacking = false; //Link cannot attack while recovering
        timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                opacity = 1f; //Link's opacity is normalized when 2 seconds is reached
                recovering = false; //Link is no longer recovering
            }
        });
        timer.setRepeats(false); //Timer is set to play only once
        timer.start();
    }



    //Pause method that sets the Link's velocity to 0 when called
    public void pause() {
        paused = true; //Link is paused
        dx = 0;
        dy = 0;
    }


    //Unpause method that delays Link's ability to move when called. This gives the player time to regain control of Link
    public void unpause() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                paused = false; //Link is no longer paused when 1 second is reached
            }
        });
        timer.setRepeats(false); //Timer is set to play only once
        timer.start();
    }


    //Die method disables Links movement, as well as update his graphic to his dying animation
    public void die() {
        y -= 35;
        death.getImage().flush(); //resets Link's animation
        image = death.getImage();
        dx = 0; //sets velocity to 0
        dy = 0;
        dead = true; //Link is dead
    }


}
