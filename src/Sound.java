import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class: Sound
 * Coder: Toby Thomas
 * Teacher: Ms. Shaw | ICS4U
 * Date: 05/25/15
 * Description: This class creates an audio player that plays music while the program is running. It plays two
 * different tracks, depending on the current state of the program. If the player is navigating the main menu,
 * the first track is selected. If the player is in battle, the second track is selected. Finally, the Sound Class
 * features a stop method that stops the current track.
 */

public class Sound {

    //Declare audio variables
    AudioPlayer MGP = AudioPlayer.player;
    AudioStream BGM;
    AudioData MD;
    ContinuousAudioDataStream loop = null;


    //Constructor plays a selected track, determined by int i
    public Sound(int i) {
        try {
            switch (i) {
                case (0):
                    BGM = new AudioStream(new FileInputStream("src/music/darkworld.wav")); //Used in main menu
                    break;
                case (1):
                    BGM = new AudioStream(new FileInputStream("src/music/boss.wav")); //Used in battle
                    break;
            }
            MD = BGM.getData();
            loop = new ContinuousAudioDataStream(MD);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MGP.start(loop); //Start music
    }


    //Stop method stops the music
    public void stop() {
        MGP.stop(loop);
    }


}
