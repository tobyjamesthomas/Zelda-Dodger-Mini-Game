/**
 * Class: HighScore
 * Coder: Toby Thomas
 * Teacher: Ms. Shaw | ICS4U
 * Date: 05/25/15
 * Description: This class creates a HighScore, an object that saves the name and score of each player. A HighScore is created every time the game
 * is played, completed (meaning Link dies), and a unique name is inputted by the player. When the game exits, all new HighScores are written and
 * saved to a text file, where they will be read and reused the next time the game opens.
 */

public class HighScore {

    //Declare player name and score variables
    String name;
    int score;


    //Constructor that creates new HighScore
    public HighScore(String n, int s) {
        name = n;
        score = s;
    }


    //toString method that saves the HighScore's player name and score in a String
    @Override
    public String toString() { return name + ", " + score; }


}
