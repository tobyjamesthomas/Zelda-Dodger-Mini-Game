import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.*;

/**
 * Class: Game
 * Coder: Toby Thomas
 * Teacher: Ms. Shaw | ICS4U
 * Date: 05/25/15
 * Description: This class creates an interactive JPanel, in which the game is played. The Game class controls the logic and GUI of the game, including the
 * Menu, Sound, Link, Heart, Octorok, Rock and HighScore classes. It does this using multiple methods that paint the GUI, detect ActionEvents and KeyEvents,
 * react to these events in an appropriate manner, create, maintain and end new games, detect and manage collisions between objects and their surroundings,
 * and finally, save highscores using a user and file IO.
 */

public class Game extends JPanel implements ActionListener { //Game returns an interactive JPanel

    //Declare the game's timer, menu, Link and enemies variables
    private Timer timer;
    private Menu menu;
    private Link link;
    private ArrayList<Octorok> ocs;

    //Declare the game's setting variables
    private int wave = 0; //determines how many enemies should spawn
    private int killCount = 0; //tracks the number of kills in a game
    private boolean gameOver = false; //tracks whether player is mid-game or not

    //Declare the game's highscores variable
    private ArrayList<HighScore> highscores;

    //Import the game's background
    ImageIcon bg = new ImageIcon(this.getClass().getResource("images/bg.jpg"));

    //Declare the game's music variable
    private Sound music;
    private boolean sound = true;


    //Default constructor that creates JPanel, and adds KeyListner and components
    public Game() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK) ;
        setDoubleBuffered(true);

        menu = new Menu(); //Create new Menu
        getHighScores(); //Import highscores
        newGame(); //Create new game
        music = new Sound(0); //Starts main menu theme

        timer = new Timer(5, this); //Game's GUI updates every 5 ms
        timer.start();
    }


    //Paint method paints all graphics onto the panel
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(bg.getImage(), 0, 0, this); //paints background

        if (menu.pause) { //if the player is navigating the menu
            g2d.drawImage(menu.getImage(), 0, 0, this); //get and paint the menu's current screen
            if (menu.current.equals("highscore")) { //if the player is on the Highscores page
                g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); //set font and size
                FontMetrics fm = g.getFontMetrics(); //used to right align score
                g.setColor(Color.white); //set font colour
                for (int i = 0; i < 4; i++) { //Draw the first 4 highscores
                    g2d.drawString(highscores.get(i).name, 99, 141 + 30 * i);
                    String score = String.valueOf(highscores.get(i).score);
                    g2d.drawString(score, (403 - fm.stringWidth(score)), 141 + 30 * i);
                }
            }
        } else { //if the player is playing the game
            for (int i = 0; i < ocs.size(); i++) { //for all Octoroks and Rocks
                ocs.get(i).move(); //update Octoroks' positions
                adjustToSceneBinding(ocs.get(i)); //adjust Octoroks' positions to playing field
                ocs.get(i).rock.move(); //update Rocks' positions
                adjustToSceneBinding(ocs.get(i).rock); //adjust Rocks' positions to playing field
                g2d.drawImage(ocs.get(i).getImage(), ocs.get(i).getX(), ocs.get(i).getY(), this); //paint Octoroks at current positions
                g2d.drawImage(ocs.get(i).rock.getImage(), ocs.get(i).rock.getX(), ocs.get(i).rock.getY(), this); //paint Rocks at current positions
            }

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, link.opacity)); //set Graphics2D opacity to Link's opacity (50% when in recovery)
            g2d.drawImage(link.getImage(), link.getX(), link.getY(), this); //paint Link at current position
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1)); //set Graphics2d opacity to 100%

            for (int i = 0; i < link.hearts.size(); i++) { //for all of Link's hearts
                g2d.drawImage(link.hearts.get(i).getImage(), 10 + i * (link.hearts.get(i).getWidth() + 3), 10, this); //paint hearts in a row
            }

            g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); //set font and size
            FontMetrics fm = g.getFontMetrics(); //used to right align kill count
            String count = String.valueOf(killCount);
            g.setColor(Color.white); //set font colour
            g2d.drawString(count, (480 - fm.stringWidth(count)), 35); //Draw the player's kill count
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }


    //actionPerformed method activates when the player hits a key
    public void actionPerformed(ActionEvent e) {
        if (!menu.pause) { //if the player is not navigating the menu, the player is controlling Link
            link.move(); //update Link's position
            if (!link.dead) adjustToSceneBinding(link); //adjust Link's position to playing field
            if (!link.recovering) { //if Link is not recovering
                adjustToOctorokBinding(link); //check collision between Link and Octoroks
                adjustToRockBinding(link); //check collision between Link and Rocks
            }
        }
        repaint(); //repaint panel
    }


    //TAdapter class is within the Game class and activates when the player presses or released a key
    private class TAdapter extends KeyAdapter {

        //The keyReleased method activates when the player releases a key
        public void keyReleased(KeyEvent e) {
            if (!menu.pause && !link.dead && !link.paused) link.keyReleased(e); //Calls Link's keyReleased only when player means to control Link
        }

        //The keyPressed method activates when the player presses a key
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_P) { //if the player pressed the P key
                for (int i = 0; i < ocs.size(); i++) { //for all Octoroks and Rocks
                    if (!menu.pause) { //if the player is mid-game
                        ocs.get(i).pause(); //pause Octoroks
                        link.pause(); //pause Link
                        menu.pauseScreen(); //open pause menu screen
                    } else if (menu.pause) { //if the player is navigating the pause menu screen, exit the menu
                        ocs.get(i).unpause(); //unpause Octoroks and Rocks
                        link.unpause(); //unpause Link
                    }
                }
                if (!menu.pause) menu.pause = true; //if not paused, pause game
                else menu.pause = false; //if paused, unpause game
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER) { //if the player pressed the enter key
                if (menu.pause && menu.current.equals("pause") && menu.pauseSelection == 0) { //if the player selects the Continue option in the pause menu
                    for (int i = 0; i < ocs.size(); i++) { //for all Octoroks and Rocks
                        ocs.get(i).unpause(); //unpause Octoroks and Rocks
                        link.unpause(); //unpause Link
                    }
                    menu.pause = false; //game is no longer paused
                } else if (menu.pause && menu.current.equals("pause") && menu.pauseSelection == 1) { //if the player selects the Exit option in the pause menu
                    music.stop(); //Stops music
                    Timer musicTimer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            music = new Sound(0); //Starts main menu theme after 2 seconds
                        }
                    });
                    musicTimer.setRepeats(false);
                    musicTimer.start();
                } else if (menu.pause && menu.current.equals("main") && menu.menuSelection == 0) {
                    music.stop(); //Stops music
                    Timer musicTimer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            music = new Sound(1); //Starts battle theme after 1 second
                        }
                    });
                    musicTimer.setRepeats(false);
                    musicTimer.start();
                    newGame(); //if the player selects the New Game option in the main menu, start a new Game
                }
            }

            if (menu.pause) menu.keyPressed(e); //if the player is navigating the game's menu, call Menu's keyPressed method

            if (!menu.pause && !link.dead && !link.paused) link.keyPressed(e); //Calls Link's keyPressed only when the player means to control Link
        }

    }


    //AdjustToSceneBinding method detects a collision between Link and the playing field, and restricts Link from exiting playing field
    private void adjustToSceneBinding(Link link) {
        //Correct the X Boundaries.
        if (link.getX() < 64) { //if Link passes left boundary
            link.setX(64); //adjust Link's position to right of boundary
        } else if (link.getX() + link.getWidth() > 436) { //if Link passes right boundary
            link.setX(436 - link.getWidth()); //adjust Link's position to left of boundary
        }

        //Correct the Y Boundaries.
        if (link.getY() < 50) { //if Link passes upper boundary
            link.setY(50); //adjust Link's position to beneath the boundary
        } else if (link.getY() + link.getHeight() > 268) { //if Link passes the lower boundary
            link.setY(268 - link.getHeight()); //adjust Link's position to above the boundary
        }
    }


    //AdjustToSceneBinding method detects a collision between an Octorok and the playing field, and restricts the Octorok from exiting playing field
    private void adjustToSceneBinding(Octorok oc) {
        //Correct the X Boundaries.
        if (oc.getX() < 64) { //if Octorok passes left boundary
            oc.setX(64); //adjust Octorok's position to right of boundary
        } else if (oc.getX() + oc.getWidth() > 436) { //if Octorok passes right boundary
            oc.setX(436 - oc.getWidth()); //adjust Octorok's position to left of boundary
        }

        //Correct the Y Boundaries.
        if (oc.getY() < 60) { //if Octorok passes upper boundary
            oc.setY(60); //adjust Octorok's position to beneath the boundary
        } else if (oc.getY() + oc.getHeight() > 268) { //if Octorok passes the lower boundary
            oc.setY(268 - oc.getHeight()); //adjust Octorok's position to above the boundary
        }
    }


    //AdjustToSceneBinding method detects a collision between a Rock and the playing field, and restricts the Rock from exiting playing field
    private void adjustToSceneBinding(Rock r) {
        //Correct the X Boundaries.
        if (r.getX() < 64) { //if Rock passes left boundary
            r.setX(64); //adjust Rock's position to right of boundary
            r.terminate(); //terminate the Rock
        } else if (r.getX() + r.getWidth() > 436) { //if Rock passes right boundary
            r.setX(436 - r.getWidth()); //adjust Rock's position to left of boundary
            r.terminate(); //terminate the Rock
        }

        //Correct the Y Boundaries.
        if (r.getY() < 60) { //if Rock passes upper boundary
            r.setY(60); //adjust Rock's position to beneath the boundary
            r.terminate(); //terminate the Rock
        } else if (r.getY() + r.getHeight() > 268) { //if Rock passes the lower boundary
            r.setY(268 - r.getHeight()); //adjust Rock's position to above the boundary
            r.terminate(); //terminate the Rock
        }
    }


    //AdjustToOctorokBinding method detects a collision between Link and the Octoroks
    private void adjustToOctorokBinding(Link link) {
        for (int i = 0; i < ocs.size(); i++) { //for each Octorok
            if (link.getX() < ocs.get(i).getX() + ocs.get(i).getWidth() && link.getY() < ocs.get(i).getY() + ocs.get(i).getHeight()
                    && ocs.get(i).getX() < link.getX() + link.getWidth() && ocs.get(i).getY() < link.getY() + link.getHeight()) { //if collision occurs
                if (!link.getAttacking()) { //if Link is not attacking
                    link.damaged(); //Link is damaged
                    link.recover(); //Link enters recovery
                } else if (link.getAttacking() && ocs.get(i).direction == 'r' && link.direction == 'r' && ocs.get(i).getX() < link.getX()) { //if Link is attacking right and collision occurs on left
                    link.damaged(); //Link is damaged
                    link.recover(); //Link enters recovery
                } else if (link.getAttacking() && ocs.get(i).direction == 'd' && link.direction == 'd' && ocs.get(i).getY() < link.getY()) { //if Link is attacking down and collision occurs on top
                    link.damaged(); //Link is damaged
                    link.recover(); //Link enters recovery
                } else if (link.getAttacking() && ocs.get(i).direction == 'l' && link.direction == 'l' && ocs.get(i).getX() > link.getX()) { //if Link is attacking left and collision occurs on right
                    link.damaged(); //Link is damaged
                    link.recover(); //Link enters recovery
                } else if (link.getAttacking() && ocs.get(i).direction == 'u' && link.direction == 'u' && ocs.get(i).getY() > link.getY()) { //if Link is attacking up and collision occurs on bottom
                    link.damaged(); //Link is damaged
                    link.recover(); //Link enters recovery
                } else { //if Link is attacking
                    ocs.get(i).die(); //Octorok dies
                    ocs.remove(i); //Octorok is removed from ocs
                    killCount++; //The player's kill count increases
                }
            }
        }
        if (ocs.size() == 0) newWave(); //if all the Octoroks are dead, spawn more Octoroks
        if (!gameOver && link.dead && !menu.pause) gameOver(); //if Link is dead, call gameOver method
    }


    //AdjustToRockBinding method detects a collision between Link and the Rocks
    private void adjustToRockBinding(Link link) {
        for (int i = 0; i < ocs.size(); i++) { //for each Rock
            if (link.getX() < ocs.get(i).rock.getX() + ocs.get(i).rock.getWidth() && link.getY() < ocs.get(i).rock.getY() + ocs.get(i).rock.getHeight()
                    && ocs.get(i).rock.getX() < link.getX() + link.getWidth() && ocs.get(i).rock.getY() < link.getY() + link.getHeight() && ocs.get(i).rock.live) { //if collision occurs and Rock is active
                link.damaged(); //Link is damaged
                link.recover(); //Link enters recovery
                ocs.get(i).rock.terminate(); //Terminate Rock
            }
        }
    }


    //newGame method creates a new game
    private void newGame() {
        gameOver = false; //The player is mid-game
        link = new Link(); //Create a new Link
        ocs = new ArrayList<Octorok>(); //Create a new Octorok ArrayList
        wave = 0; //Reset wave to 0
        killCount = 0; //Reset kill count to 0
        newWave(); //Spawn Octoroks
    }


    //newWave method spawns new Octoroks
    private void newWave() {
        for (int i = 0; i < wave; i++) { //for the current wave
            ocs.add(new Octorok(link)); //Spawn a new Octorok
            ocs.get(i).play(); //Play Octorok's movement
            ocs.get(i).shoot(); //Play Octorok's shooting
            ocs.get(i).rock.terminate(); //Terminate current Rocks
        }
        wave++; //Increase next wave
    }


    //gameOver method ends a game when called
    private void gameOver() {
        gameOver = true; //The player is no longer playing a game
        Timer deathTimer = new Timer(1800, new ActionListener() { //Timer is used to animate the Menu's Game Over screen
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                music.stop(); //Stops music
                menu.pause = true; //The menu will activate after 1.8 seconds (length of Link's death animation)
                menu.gameOverScreen(); //Open the menu's Game Over screen
            }
        });
        deathTimer.setRepeats(false); //Timer is set to play only once
        deathTimer.start();
        Timer saveTimer = new Timer(3800, new ActionListener() { //Timer is used to create a new HighScore once the player returns to the main menu
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                music = new Sound(0); //Starts main menu theme
                String name = JOptionPane.showInputDialog("You killed " + killCount + " Octoroks. Save your score!", "Your name"); //Retrieve name from user
                while (name != null && name.length() > 10) { //If the player inputs a name that exceeds 10 characters, notify player and retrieve another name
                    name = JOptionPane.showInputDialog("You killed " + killCount + " Octoroks. Save your score! (Max 10 characters)", "Your name");
                }
                if (name == null || name.equals("") || name.equals("Your name"))
                    ; //If the player does not accept InputDialog, or does not input a unique name, Highscore is not saved
                else {
                    highscores.add(new HighScore(name, killCount)); //Create a new HighScore with name and score of the player
                    updateHighScores(); //Update the Highscores file
                }
            }
        });
        saveTimer.setRepeats(false); //Timer is set to play only once
        saveTimer.start();
    }


    //getHighScores method retrieves saved HighScores from the highScores.txt file, using a Scanner
    private void getHighScores() {
        highscores = new ArrayList<HighScore>(); //Creates a new ArrayList of HighScores

        try {

            Scanner in = new Scanner(new FileReader("src/highscores.txt")); //import highscores.txt file

            //Retrieves HighScores from the file and adds them to the ArrayList
            while (in.hasNext()) {
                String s = in.nextLine();
                String[] tokens = s.split(", ");
                highscores.add(new HighScore(tokens[0], Integer.parseInt(tokens[1])));
            }

            in.close();

        } catch (FileNotFoundException e) {

            System.out.println("Error: Cannot open file for reading");

        } catch (NoSuchElementException e) {

            System.out.println("Error: EOF encountered, file may be corrupt");

        }
    }


    //updateHighScores method saves the HighScores to the highscores.txt file, using a PrintWriter
    public void updateHighScores() {
        try {

            PrintWriter out = new PrintWriter(new FileWriter("src/highscores.txt")); //import highscores.txt file

            //Saves all HighScores from the ArrayList to the file
            for (int i = 0; i < highscores.size(); i++) {
                ArrayList<HighScore> sortedScores = sort(highscores); //Sorts the HighScores from highest to lowest, to ensure the top scorers are first
                out.println(sortedScores.get(i).toString());
            }

            out.close();

        } catch (FileNotFoundException e) {

            System.out.println("Error: Cannot open file for writing");

        } catch (IOException e) {

            System.out.println("Error: Cannot write to file");

        }
    }


    //Sort methods returns a sorted HighScore ArrayList, from greatest to smallest, using the Merge Sort method
    private ArrayList<HighScore> sort(ArrayList<HighScore> list) {
        for (int i = 0; i < list.size(); i++) {
            int smallest = i;
            for (int a = i; a < list.size(); a++) {
                if (list.get(a).score > list.get(smallest).score) {
                    swap(list, a, smallest);
                    continue;
                }
                else if (list.get(a).score == list.get(smallest).score) {
                    if (list.get(a).name.compareToIgnoreCase(list.get(smallest).name) < 0) {
                        swap(list, a, smallest);
                        continue;
                    }
                }
            }
        }
        return list;
    }


    //Swap method swaps two HighScores within an ArrayList
    private void swap(ArrayList<HighScore> list, int firstInd, int secondInd) {
        HighScore temp = list.set(firstInd, list.get(secondInd));
        list.set(secondInd, temp);
    }


}