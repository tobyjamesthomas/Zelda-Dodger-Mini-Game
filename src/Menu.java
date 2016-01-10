import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Class: Menu
 * Coder: Toby Thomas
 * Teacher: Ms. Shaw | ICS4U
 * Date: 05/25/15
 * Description: This class creates and activates the menu system of the game. The menu system is composed of a main (title screen) menu, main menu sub-pages
 * (How To Play, Highscores and Credits), and a pause menu (which is accessible mid game). Upon creation, the Menu class loads all the menu's pages and sets
 * the current screen to the main menu. Afterwards, several set methods are used to update the current page to the appropriate graphic. Finally, the keyPressed
 * method is activated upon a KeyEvent and allows the player to navigate between menu options.
 */

public class Menu {

    //Declare main and pause menu variables, as well as current page
    private ArrayList<ImageIcon> mainMenu = new ArrayList<ImageIcon>(); //Stores main menu graphics
    private ArrayList<ImageIcon> pauseMenu = new ArrayList<ImageIcon>(); //Stores pause menu graphics
    public int menuSelection = 0; //Current main menu selection
    public int pauseSelection = 0; //Current pause menu selection
    public String current; //Current page

    //Declare activity variables
    private Timer timer;
    public boolean pause = true;

    //Declare graphic variables
    private Image image; //Current graphic
    ImageIcon howTo = new ImageIcon(this.getClass().getResource("images/menu/howto.gif")); //How To Play graphic
    ImageIcon highScore = new ImageIcon(this.getClass().getResource("images/menu/highscores.png")); //Highscores graphic
    ImageIcon credits = new ImageIcon(this.getClass().getResource("images/menu/credits.png")); //Credits graphic
    ImageIcon gameOver = new ImageIcon(this.getClass().getResource("images/menu/gameover.png")); //Gameover graphic


    //Default constructor that loads all menu graphics and sets current screen to main menu
    public Menu() {
        loadScreens();
        menuScreen();
    }


    //Get Helper method that returns current graphic
    public Image getImage() { return image; }


    //loadScreens method adds main and pause menu graphics to their respective ArrayLists
    public void loadScreens() {
        for (int i = 0; i < 4; i++) {
            mainMenu.add(new ImageIcon(this.getClass().getResource("images/menu/m" + i + ".png")));
        }
        for (int i = 0; i < 2; i++) {
            pauseMenu.add(new ImageIcon(this.getClass().getResource("images/menu/p" + i + ".png")));
        }
    }


    //menuScreen method sets current screen to main menu
    public void menuScreen() {
        current = "main";
        menuSelection = 0;
        image = mainMenu.get(menuSelection).getImage();
    }


    //pauseScreen method sets current screen to pause menu
    public void pauseScreen() {
        current = "pause";
        pauseSelection = 0;
        image = pauseMenu.get(pauseSelection).getImage();
    }


    //howToScreen method sets current screen to the How To Play page
    public void howToScreen() {
        current = "howto";
        image = howTo.getImage();
    }


    //highScoreScreen method sets current screen to the Highscores page
    public void highScoreScreen() {
        current = "highscore";
        image = highScore.getImage();
    }


    //creditsScreen method sets current screen to the Credits page
    public void creditsScreen() {
        current = "credits";
        image = credits.getImage();
    }


    //gameOverScreen sets current screen to the Game Over page
    public void gameOverScreen() {
        current = "gameover";
        image = gameOver.getImage();
        timer = new Timer(2000, new ActionListener() { //A Timer is used to return player to the main menu screen after the Game Over page is displayed
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                menuScreen();
            }
        });
        timer.setRepeats(false); //Timer is set to play only once
        timer.start();
    }


    /**
     * The keyPressed method activates every time the player interacts with the menu pages.
     * Possible keys are the up arrow, down arrow and enter keys.
     * The up arrow and down arrow keys are used to navigation within the main and pause menu screens,
     * while the enter key is used to select an option or return to a previous screen.
     */
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode(); //key stores the key that has been pressed

        if (key == KeyEvent.VK_UP) { //if the player pressed the up arrow key
            if (current.equals("main")) { //if player is navigating through the main menu screen
                if (menuSelection == 0) {
                    menuSelection = mainMenu.size() - 1;
                } else menuSelection -= 1;
                image = mainMenu.get(menuSelection).getImage(); //updates the appropriate selection's graphic
            }
            if (current.equals("pause")) { //if player is navigating the pause menu screen
                if (pauseSelection == 0) {
                    pauseSelection = pauseMenu.size() - 1;
                } else pauseSelection -= 1;
                image = pauseMenu.get(pauseSelection).getImage(); //updates the appropriate selection's graphic
            }
        }

        if (key == KeyEvent.VK_DOWN) { //if the player pressed the down arrow key
            if (current.equals("main")) { //if player is navigating through the main menu screen
                if (menuSelection == mainMenu.size()-1) {
                    menuSelection = 0;
                } else menuSelection += 1;
                image = mainMenu.get(menuSelection).getImage(); //updates the appropriate selection's graphic
            }
            if (current.equals("pause")) { //if player is navigating the pause menu screen
                if (pauseSelection == pauseMenu.size()-1) {
                    pauseSelection = 0;
                } else pauseSelection += 1;
                image = pauseMenu.get(pauseSelection).getImage(); //updates the appropriate selection's graphic
            }
        }

        if (key == KeyEvent.VK_ENTER) { //if the player pressed the enter key
            if (current.equals("main")) { //if player is navigating through the main menu screen
                switch (menuSelection) {
                    case (0): //the player selected New Game
                        pause = false;
                        break;
                    case (1): //the player selected How To Play
                        howToScreen();
                        break;
                    case (2): //the player selected Highscores
                        highScoreScreen();
                        break;
                    case (3): //the player selected Credits
                        creditsScreen();
                        break;
                }
            } else if (current.equals("pause")) {
                switch (pauseSelection) { //if player is navigating through the main menu screen
                    case (0): //the player selected Continue (current game)
                        pause = false;
                        break;
                    case (1): //the player selected Exit (current game)
                        gameOverScreen();
                        break;
                }
            } else if (current.equals("howto") || current.equals("highscore") || current.equals("credits") || current.equals("gameover")) { //if player is on a main menu sub-page
                menuScreen(); //return to main menu screen
            }
        }
    }


}
