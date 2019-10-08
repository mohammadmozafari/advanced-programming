package game.mapMaker.InGameMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * this class is just at early stage and should be modified for further usage
 * @auther seyyed soroush mortazavi moghaddam
 */
public class MapConstructor extends JPanel
{
    private JPanel mainPanel;
    private int[][] map;
    private int scale;
    private  int mapScaleX ;
    private  int mapScaleY ;

    public MapConstructor(int[][] map, int scale,int mapScale) throws HeadlessException
    {
//      JOptionPane.showMessageDialog(this,"Processing...","Process",JOptionPane.INFORMATION_MESSAGE);
        setLayout(new BorderLayout());
        mainPanel = new JPanel(new GridLayout(scale, scale));
        this.map = map;
        this.scale = scale;
        setVisible(true);
        mapScaleX=mapScale;
        mapScaleY = (mapScaleX * 4) / 5;
    }

    private String textureEquvalentOf(int input)
    {
        //the meaning of each int:
        //1 : unDestroyableWall1
        //2 : unDestroyableWall2
        //3 : destroyableWall
        //4 : movingEnemy1
        //5 : movingEnemy2
        //6 : movelessEnemy1
        //7 : movelessEnemy2
        //8 : tankPosition
        //0 : delete
        //10 : tree
        //11 : tankRepairKit
        //12 : cannonShells
        //13 : machineGunCartridges
        switch (input)
        {
            case 1:
                return "src/images/udw1.png";
            case 2:
                return "src/images/udw2.png";
            case 3:
                return "src/images/weakWall_L3.png";
            case 4:
                return "src/images/movingEnemy1.png";
            case 5:
                return "src/images/movingEnemy2.png";
            case 6:
                return "src/images/moveLessEnemy1.png";
            case 7:
                return "src/images/moveLessEnemy2.png";
            case 8:
                return "src/images/myTank/tankBody.png";
            case 0:
                return "src/images/ground.png";
            case 10:
                return "src/images/tree.png";
            case 11:
                return "src/images/repairKit.png";
            case 12:
                return "src/images/cannonShells.png";
            case 13:
                return "src/images/machineGunCartridges.png";
            default:
                return "src/images/ground.png";
        }
    }


    /**
     * this method previews a small version of the map in the map editor's panel
     * each cell is taken from the current map's filled cells
     * @param g is the brush which dose the painting on the map editors panel
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        for (int i = 0; i < scale; i++)
        {
            for (int j = 0; j < scale; j++)
            {
                Image img = null;
                try
                {
                    img = ImageIO.read(new File(textureEquvalentOf(map[i][j])));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                g.drawImage(img, mapScaleX * j, mapScaleY * i, mapScaleX, mapScaleY, null);
            }
        }
    }

    public void paintGameMap(Graphics2D g2d,Point point)
    {

    }
}
