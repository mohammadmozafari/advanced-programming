import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * all sounds across the game is played in this class
 * @author soroush mortazavi
 */
public class SoundMaker
{
    public static Clip clip;
    public static Clip inGameSound;

    public static void play(String url)
    {
//        try
//        {
//           // File soundFile = new File(url);
//         //   AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
//            if (url.equals("src/Sounds/inGame.wav") || url.equals("src/Sounds/mainPage.wav"))
//            {
//               // inGameSound = AudioSystem.getClip();
//               // inGameSound.open(audioIn);
//               // inGameSound.start();
//            }
//            else
//            {
//                //clip = AudioSystem.getClip();
//               // clip.open(audioIn);
//                //clip.start();
//            }
//        } catch (LineUnavailableException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        } catch (UnsupportedAudioFileException e)
//        {
//            e.printStackTrace();
//        }
    }
}
