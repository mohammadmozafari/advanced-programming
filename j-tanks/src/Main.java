/*** In The Name of Allah ***/

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;

/**
 * Program start.
 *
 * @author mohammad mozafary and soroush mortazavi
 */
public class Main
{
    private static GameFrame frame;
    private static Image img;
    private static Cursor cursor;

    public static void main(String[] args)
    {
        Menu menu = new Menu();
    }
    public static void startGame()
    {
        // Initialize the global thread-pool
        ThreadPool.init();

        // Show the game menu ...

        // After the player clicks 'PLAY' ...s

        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                img = Toolkit.getDefaultToolkit().getImage(Info.cursorPath);
                cursor = Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(16, 16), "Aim");
                try
                {
                    Info.setMap(mapReader(Info.mapLocation));
                } catch (IOException e)
                {
                    JOptionPane.showMessageDialog(null,"map not found !","mapNotFound",JOptionPane.ERROR_MESSAGE);
                }
                frame = new GameFrame("Game of tanks");
                frame.setLocationRelativeTo(null); // put frame at center of screen
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.initBufferStrategy();
                frame.setCursor(cursor);
                // Create and execute the game-loop
                GameLoop game = new GameLoop(frame);
                game.init(Info.map,Info.map[0].length);
                ThreadPool.execute(game);
                // and the game starts ...
            }

            /**
             * this method is called once to read the map information from it's file
             * @param mapPath is map's file location
             * @return an 2d array of digits holding map information
             * @throws IOException
             */
            private int[][] mapReader(String mapPath) throws IOException
            {
                FileReader fileReader = null;
                try
                {
                    fileReader = new FileReader(mapPath);
                } catch (FileNotFoundException e1)
                {
                    e1.printStackTrace();
                }
                assert fileReader != null;
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = null;
                int scale = 0;
                line = bufferedReader.readLine();
                char someChar = ',';
                int count = 0;

                for (int i = 0; i < line.length(); i++)
                {
                    if (line.charAt(i) == someChar)
                        count++;
                }
                scale = count;
                int[][] cells = new int[scale][scale];
                int i = 0;
                fileReader.close();
                fileReader = new FileReader(mapPath);
                bufferedReader = new BufferedReader(fileReader);
                while ((line = bufferedReader.readLine()) != null)
                {
                    for (int j = 0; j < scale; j++)
                    {
                        cells[i][j] = Integer.parseInt(line.split(",")[j]);
                    }
                    i++;
                }
                fileReader.close();
                return cells;
            }
        });
    }

}
