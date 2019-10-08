package game.mapMaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @auther seyyed soroush mortazavi moghaddam
 */
public class DrawPanel extends JPanel
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
    //14 : bounce
    //15 : endingPosition

    private int scaleH;
    private int scaleV;
    public String brush = "";
    private final int PIXELS_H = 720;
    private final int PIXELS_V = (PIXELS_H * 4) / 5;
    private int[][] cells;

    public DrawPanel(int scaleH)
    {
        this.scaleH = scaleH;
        scaleV = scaleH;
        cells = new int[scaleH][scaleV];
        for (int i = 0; i < scaleH; i++)
        {
            for (int j = 0; j < scaleV; j++)
            {
                cells[i][j] = 0;
            }
        }
        setPreferredSize(new Dimension(PIXELS_H, PIXELS_V));
        setMaximumSize(new Dimension(PIXELS_H, PIXELS_V));

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                fillCell(e.getPoint());
            }

        });
        addMouseMotionListener(new MouseMotionListener()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                    fillCell(e.getPoint());
            }

            @Override
            public void mouseMoved(MouseEvent e)
            {

            }
        });
    }
    private int intEquivalent(String str)
    {
        switch (str)
        {
            case "udw1":
                return 1;
            case "udw2":
                return 2;
            case "dw":
                return 3;
            case "me1":
                return 4;
            case "me2":
                return 5;
            case "mle1":
                return 6;
            case "mle2":
                return 7;
            case "tp":
                return 8;
            case "d":
                return 0;
            case "t":
                return 10;
            case "trk":
                return 11;
            case "cs":
                return 12;
            case "mgc":
                return 13;
            case "b":
                return 14;
            case "eP":
                return 15;
            default:
                return 0;
        }
    }

    /**
     * this method returns the color equivalent of each integer entry
     * @param input is the int value of the square in the map editors panel
     * @return a color representing the value of the cell in the graphical interface of the map editor's panel
     */
    private Color ColorEquivalentOf(int input)
    {
        Color color  = new Color(10,100,10);
        Color color1 = new Color(150,0,100);
        Color color2 = new Color(100,20,20);
        switch (input)
        {
            case 1 :
                return Color.BLACK;
            case 2 :
                return Color.darkGray;
            case 3 :
                return Color.lightGray;
            case 4 :
                return Color.red;
            case 5 :
                return Color.pink;
            case 6 :
                return Color.yellow;
            case 7 :
                return Color.orange;
            case 8 :
                return Color.green;
            case 0 :
                return Color.BLACK;
            case 10 :
                return color;
            case 11 :
                return Color.cyan;
            case 12 :
                return Color.magenta;
            case 13 :
            return Color.blue;
            case 14 :
                return color1;
            case 15 :
                return color2;
            default:
                return Color.white;
        }
    }

    /**
     * this method finds the tank's starting position on the [][] map and deletes it
     * in case the starting point isn't placed more than one !
     */
    private void deleteTank()
    {
        for (int k = 0; k < scaleH; k++)
        {
            for (int p = 0; p < scaleV; p++)
            {
                if (cells[k][p] == 8)
                {
                    cells[k][p] = 0;
                }
            }
        }
    }

    /**
     * this method gets every point on the map editor where the mouse has been clicked on
     * and then adds the selected item to the int[][] map
     * this [][] map is later used to fill the graphical interface of the map editor
     * @param point is the place where the mouse was clicked on
     */
    private void fillCell(Point point)
    {
        if (point.getX() > 0 && point.getY() > 0)
        {
            int i;
            for (i = 0; i <= scaleH * (PIXELS_H / scaleH); i += PIXELS_H / scaleH)
            {
                if (i > point.getX())
                {
                    i -= PIXELS_H / scaleH;
                    int j;
                    for (j = 0; j <= scaleV * (PIXELS_V / scaleV); j += PIXELS_V / scaleV)
                    {
                        if (j > point.getY())
                        {
                            j -= PIXELS_V / scaleV;
                            if (brush.equals("tp"))
                            {
                                deleteTank();
                            }
                            cells[(j / (PIXELS_V / scaleV))][(i / (PIXELS_H / scaleH))] = intEquivalent(brush);
                            update();
                            break;
                        }
                    }
                    break;
                }

            }
        }
    }
    private void update()
    {
        repaint();
        //the below lines prints the map in the console (kept just in case)
//        for (int i = 0; i < scaleH; i++)
//        {
//            for (int j = 0; j < scaleV; j++)
//            {
//                System.out.print(cells[i][j]);
//            }
//            System.out.println(" : " + i);
//        }
//        System.out.println("__________");
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        int count = 0;
        for (int i = 0; i <= scaleV * (PIXELS_V / scaleV); i += PIXELS_V / scaleV)
        {
            g.drawLine(0, i, scaleH * (PIXELS_H / scaleH), i);
            count = i;
        }
        for (int j = 0; j <= scaleH * (PIXELS_H / scaleH); j += PIXELS_H / scaleH)
        {
            g.drawLine(j, 0, j, count);
        }
        for (int i = 0; i < scaleV * (PIXELS_V / scaleV); i += PIXELS_V / scaleV)
        {
            for (int j = 0; j < scaleH * (PIXELS_H / scaleH); j += PIXELS_H / scaleH)
            {
                if (cells[i/(PIXELS_V / scaleV)][j/(PIXELS_H / scaleH)]!=0)
                {
                    g.setColor(ColorEquivalentOf(cells[i/(PIXELS_V / scaleV)][j/(PIXELS_H / scaleH)]));
                    g.fillRect(j,i,PIXELS_H / scaleH,PIXELS_V / scaleV);
                }
            }

        }

    }

    public String getBrush()
    {
        return brush;
    }

    public void setBrush(String brush)
    {
        this.brush = brush;
    }

    public int[][] getCells()
    {
        return cells;
    }

    public void setCells(int[][] cells,int scale)
    {
        scaleH=scale;
        scaleV=scale;
        this.cells = cells;
        repaint();
    }
}
