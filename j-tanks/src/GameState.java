/*** In The Name of Allah ***/

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

/**
 * This class holds the state of game and all of its elements.
 * This class also handles user inputs, which affect the game state.
 *
 * @author Seyed Mohammad Ghaffarian
 */
public class GameState
{

    public OwnTank ownTank;
    public boolean gameOver;
    public boolean win;

    private int[][] map;
    private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT, keyPressed;
    private int mouseX, mouseY;
    private long lastFired;
    private KeyHandler keyHandler;
    private MouseHandler mouseHandler;

    private Vector<Tank> tanks;
    private Vector<Bullet> bullets;
    private Vector<Wall> walls;
    private Vector<Ground> ground;
    private Vector<Tree> trees;
    private Vector<Bonus> bonuses;

    private Data data = new Data();

    //This constructor sets the initial settings for the game.
    public GameState(int[][] map, int scale)
    {
        tanks = new Vector<>();
        walls = new Vector<>();
        ground = new Vector<>();
        trees = new Vector<>();
        bonuses = new Vector<>();

        int position;
        if (Network.isServer)
            position = 0;
        else
            position = 200;

        addInitalWallsAndTrees(map, scale);

        //Creating the own tank
        Vector<Weapon> weapons = new Vector<>();
        weapons.add(new Weapon(80, 1, Info.weapon1Damage, Info.cannonPipePath, Info.weapon1Display, Info.cannonPath));
        weapons.add(new Weapon(250, 1, Info.weapon2Damage, Info.machineGunPipePath, Info.weapon2Display, Info.machineGunPath));
        ownTank = new OwnTank(Info.tankGlobalPositionX + position, Info.tankGlobalPositionY, 100, 3, weapons, Info.ownTankPath,
                Info.ownTankMiddleX, Info.ownTankMiddleY);
        ownTank.setServer(Network.isServer);
        tanks.add(ownTank);
        ownTank.timer();

        handleMotion(false, false, false, false);

        gameOver = false;
        //
        keyUP = false;
        keyDOWN = false;
        keyRIGHT = false;
        keyLEFT = false;
        //
        mouseX = 0;
        mouseY = 0;
        //
        keyHandler = new KeyHandler();
        mouseHandler = new MouseHandler();
    }

    /**
     * The method which updates the game state.
     */
    public void update()
    {
        ownTank.setPipeAngle(mouseX, mouseY);
        if (keyUP && keyDOWN)
            keyUP = keyDOWN = false;
        if (keyLEFT && keyRIGHT)
            keyLEFT = keyRIGHT = false;
        if (keyLEFT || keyRIGHT || keyUP || keyDOWN) handleMotion(keyUP, keyDOWN, keyLEFT, keyRIGHT);

        Point difPoint;
        Info.yDif = 0;
        Info.xDif = 0;
        if (ownTank.getLocX() >= (Info.mainFrameWidth / 2 - 150) &&
                ownTank.getLocX() <= Info.map[0].length * Info.unitWidth - (Info.mainFrameWidth / 2 - 30))
        {
            difPoint = ownTank.tankDif(ownTank.getRelativeX(), ownTank.getRelativeY());
            Info.yDif = 0;
            ownTank.setRelativeX((ownTank.getRelativeX() - difPoint.x));
        }
        if ((ownTank.getLocY() > (Info.mainFrameHeight / 2 - 140) &&
                ownTank.getLocY() < Info.map[0].length * Info.unitHeight - (Info.mainFrameHeight / 2)))
        {
            difPoint = ownTank.tankDif(ownTank.getRelativeX(), ownTank.getRelativeY());
            if (!(ownTank.getLocX() > (Info.mainFrameWidth / 2 - 150) &&
                    ownTank.getLocX() <= Info.map[0].length * Info.unitWidth - (Info.mainFrameWidth / 2 - 30)))
            {
                Info.xDif = 0;
            }
            ownTank.setRelativeY(ownTank.getRelativeY() - difPoint.y);
        }
        for (int i = 0; i < tanks.size(); i++)
        {
            updateTanksStat(tanks.get(i));
            bullets = tanks.get(i).getBullets();
            for (int j = 0 ; j < bullets.size() ; j++)
                updateBulletsStat(bullets.get(j));
        }

        if (Network.mPlayer)
        {
            Network.lastSent = (new Date()).getTime();
            if (Network.turn == 0)
            {
                saveData(getBullets(), getWalls(), getTanks(), getBonuses());
                try
                {
                    Network.sendData(data);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                Network.turn = 1;
                if (ownTank.getHealth() <= 0)
                {
                    Network.mPlayer = false;
                    Network.turn = 0;
                }
                if (ownTank.isWin())
                {
//                    Network.mPlayer = false;
                    Network.turn = 0;
                }
            }
            if (Network.turn == 1)
            {
                try
                {
                    boolean temp = true;
                    data = Network.getData();
                    for (int i = 0; i < bonuses.size(); i++)
                    {
                        for (DataQuantom dataQuantom : data.getBonuses())
                        {
                            if (bonuses.get(i).getAbsolutePos().equals(dataQuantom.getAbsolutePos()))
                            {
                                temp = false;
                                bonuses.get(i).setBonusType(dataQuantom.getState());
                            }
                        }
                        if (temp)
                        {
                            bonuses.remove(i);
                            i--;
                        }
                        temp = true;
                    }
                    for (int i = 0; i < walls.size(); i++)
                    {
                        for (DataQuantom dataQuantom : data.getWalls())
                        {
                            if (walls.get(i) instanceof WeakWall)
                                if (walls.get(i).getAbsolutePos().equals(dataQuantom.getAbsolutePos()) &&
                                        ((WeakWall) walls.get(i)).getState() <= dataQuantom.getState())
                                {
                                    ((WeakWall) walls.get(i)).setState(dataQuantom.getState());
                                    temp = false;
                                }
                        }
                        if (temp && walls.get(i) instanceof WeakWall)
                        {
                            walls.remove(i);
                            i--;
                        }
                        temp = true;
                    }
                    tanks = data.getTanks();
                    if (((OwnTank)tanks.get(tanks.size()-1)).isWin())
                    {
                        win=true;
                        gameOver=true;
                    }
                    ((OwnTank)tanks.get(tanks.size()-1)).changeOnlyRelativeX((tanks.get(tanks.size()-1)).getLocX()-Info.tankGlobalPositionX + Info.tankRelativePositionX);
                    ((OwnTank)tanks.get(tanks.size()-1)).changeOnlyRelativeY((tanks.get(tanks.size()-1)).getLocY()-Info.tankGlobalPositionY + Info.tankRelativePositionY);
                    if (tanks.get(tanks.size() - 1).getHealth() <= 0)
                    {
                        System.out.println("lost");
                        Network.mPlayer = false;
                        tanks.remove(tanks.get(tanks.size() - 1));
                    }
                    tanks.add(ownTank);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                Network.turn = 0;
            }
        }
    }


    public KeyListener getKeyListener()
    {
        return keyHandler;
    }

    public MouseListener getMouseListener()
    {
        return mouseHandler;
    }

    public MouseMotionListener getMouseMotionListener()
    {
        return mouseHandler;
    }

    /**
     * The keyboard handler.
     */
    class KeyHandler extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    keyUP = true;
                    break;
                case KeyEvent.VK_DOWN:
                    keyDOWN = true;
                    break;
                case KeyEvent.VK_LEFT:
                    keyLEFT = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    keyRIGHT = true;
                    break;
                case 192:
                    String code = JOptionPane.showInputDialog("Enter the cheat code", null);
                    if (code.equals("moreammo"))
                        ownTank.getActiveWeapon().setAmmo(ownTank.getActiveWeapon().getAmmo() + 50);
                    else if (code.equals("asperin"))
                        ownTank.setHealth(ownTank.getMaxHealth());
                    else if (code.equals("morelife"))
                        ownTank.setNumOfLives(ownTank.getNumOfLives() + 1);
                    break;
                case KeyEvent.VK_ESCAPE:
                    gameOver = true;
                    break;
            }
            keyPressed=true;
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    keyUP = false;
                    break;
                case KeyEvent.VK_DOWN:
                    keyDOWN = false;
                    break;
                case KeyEvent.VK_LEFT:
                    keyLEFT = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    keyRIGHT = false;
                    break;
            }
            keyPressed=false;
        }

    }

    /**
     * The mouse handler.
     */
    class MouseHandler extends MouseAdapter
    {

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getButton() == 1)
            {
                ownTank.fire();
            } else if (e.getButton() == 3)
                ownTank.changeWeapon();
        }
//
//		@Override
//		public void mousePressed(MouseEvent e) {
//			mouseX = e.getX();
//			mouseY = e.getY();
//			mousePress = true;
//		}
//
//		@Override
//		public void mouseReleased(MouseEvent e) {
//			mousePress = false;
//		}

        @Override
        public void mouseDragged(MouseEvent e)
        {
            mouseX = e.getX();
            mouseY = e.getY();

            //if ((new Date()).getTime() - lastFired > 500)
            {
                lastFired = new Date().getTime();
                ownTank.fire();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e)
        {
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    public Vector<Bullet> getBullets()
    {
        return bullets;
    }

    //getters
    public Vector<Ground> getGround()
    {
        return ground;
    }

    public Vector<Wall> getWalls()
    {
        return walls;
    }

    public Vector<Tree> getTrees()
    {
        return trees;
    }

    public Vector<Tank> getTanks()
    {
        return tanks;
    }

    public Vector<Bonus> getBonuses()
    {
        return bonuses;
    }

    /**
     * This method checks to find out if there is any collision between shapes.
     * Bullets look for bullets and tanks and walls while tanks look for others tanks and walls.
     *
     * @param o the object to check its collision
     * @return true if there is a collision
     */
    public boolean checkCollision(Crashable o)
    {
        Area copy;
        Bullet bullet;
        Tank tank;
        if (o instanceof Tank)
        {
            Tank object = (Tank) o;
            for (int i = 0; i < tanks.size(); i++)
            {
                if (tanks.get(i) == object)
                    continue;
                if (Point.distance(object.getCenter().x, object.getCenter().y,
                        tanks.get(i).getCenter().x, tanks.get(i).getCenter().y) > 300)
                    continue;
                copy = (Area) object.getArea().clone();
                copy.intersect(tanks.get(i).getArea());
                if (!copy.isEmpty() && object != tanks.get(i))
                {
                    return true;
                }
            }
            for (int i = 0; i < walls.size(); i++)
            {
                if (Point.distance(object.getCenter().x, object.getCenter().y,
                        walls.get(i).getCenter().x, walls.get(i).getCenter().y) > 300)
                    continue;
                copy = (Area) object.getArea().clone();
                copy.intersect(walls.get(i).getArea());
                if (!copy.isEmpty())
                    return true;
            }
            if (object instanceof OwnTank)
                for (int i = 0; i < bonuses.size(); i++)
                {
                    if (Point.distance(object.getCenter().x, object.getCenter().y,
                            bonuses.get(i).getCenter().x,
                            bonuses.get(i).getCenter().y) > 300)
                        continue;
                    copy = (Area) object.getArea().clone();
                    copy.intersect(bonuses.get(i).getArea());
                    if (!copy.isEmpty())
                    {
                        if (bonuses.get(i).getBonusType()==5)
                        {
                            gameOver = true;
                            win = true;
                            ownTank.setWin(true);
                        }
                        bonuses.get(i).apply((OwnTank) object);
                        bonuses.remove(i);
                        return true;
                    }
                }
            return false;
        }
        if (o instanceof Bullet)
        {
            bullet = (Bullet) o;
            for (int i = 0; i < tanks.size(); i++)
            {
                if (Point.distance(bullet.getCenter().x, bullet.getCenter().y,
                        tanks.get(i).getCenter().x, tanks.get(i).getCenter().y) > 300)
                    continue;
                copy = (Area) bullet.getArea().clone();
                copy.intersect(tanks.get(i).getArea());
                if (!copy.isEmpty())
                {
                    tank = tanks.get(i);
                    tank.setHealth(tank.getHealth() - bullet.getDamage());
                    if (tank.getHealth() <= 0)
                    {
                        if (tank == ownTank)
                        {
                            gameOver = true;
                            win = false;
                            return true;
                        }
                        else
                        {
                            Bonus b = getLucky(new Point(tank.getLocX(), tank.getLocY()));
                            if (b != null)
                                bonuses.add(b);
                        }
                        tanks.remove(tank);
                        SoundMaker.play("src/Sounds/enemydestroyed.wav");
                    }
                    return true;
                }
            }
            for (int i = 0; i < walls.size(); i++)
            {
                if (Point.distance(bullet.getCenter().x, bullet.getCenter().y,
                        walls.get(i).getCenter().x, walls.get(i).getCenter().y) > 300)
                    continue;
                copy = (Area) bullet.getArea().clone();
                copy.intersect(walls.get(i).getArea());
                if (!copy.isEmpty())
                {
                    if (walls.get(i) instanceof WeakWall)
                    {
                        WeakWall weak = (WeakWall) walls.get(i);
                        weak.getShot(bullet.getDamage());
                        if (weak.isDestroyed())
                        {
                            Bonus b = getLucky(weak.position);
                            if (b != null)
                                bonuses.add(b);
                            walls.remove(weak);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Setting the initial position of objects in the game.
     * @param map the array containning the info
     * @param scale the horizontal and vertical scale
     */
    public void addInitalWallsAndTrees(int[][] map, int scale)
    {
        hashMapMaker();
        Wall wall;
        Tank tank;

        for (int i = 0; i < scale; i++)
        {
            for (int j = 0; j < scale; j++)
            {
                switch (map[j][i])
                {
                    case 1:
                        wall = new Wall(new Point(i * Info.unitWidth, j * Info.unitHeight), "src/images/udw1.png");
                        walls.add(wall);
                        break;
                    case 2:
                        wall = new Wall(new Point(i * Info.unitWidth, j * Info.unitHeight), "src/images/udw2.png");
                        walls.add(wall);
                        break;
                    case 3:
                        wall = new WeakWall(new Point(i * Info.unitWidth, j * Info.unitHeight), "src/images/weakWall_L3.png");
                        walls.add(wall);
                        break;
                    case 4:
                        tank = new MovingTank(i * Info.unitWidth, j * Info.unitHeight, Info.movingEnemy1,
                                "src/images/Enemies/movingEnemy1Body.png", "src/images/Enemies/movingEnemy1Pipe.png",
                                Info.moving1MiddleX, Info.moving1MiddleY, Info.moving1PipeLocX, Info.moving1PipeLocY, true);
                        tank.setTankType(3);
                        tanks.add(tank);
                        break;
                    case 5:
                        tank = new MovingTank(i * Info.unitWidth, j * Info.unitHeight, Info.movingEnemy2,
                                "src/images/Enemies/movingEnemy2Body.png", "src/images/Enemies/movingEnemy2Pipe.png",
                                Info.moving2MiddleX, Info.moving2MiddleY, Info.moving2PipeLocX, Info.moving2PipeLocY, false);
                        tank.setTankType(4);
                        tanks.add(tank);
                        break;
                    case 6:
                        tank = new Tank(i * Info.unitWidth, j * Info.unitHeight, Info.movelessEnemy1,
                                "src/images/Enemies/moveLessEnemy1Body.png", "src/images/Enemies/moveLessEnemy1Pipe.png",
                                Info.moveless1MiddleX, Info.moveless1MiddleY, Info.moveless1PipeLocX, Info.moveless1PipeLocY);
                        tank.setTankType(1);
                        tanks.add(tank);
                        break;
                    case 7:
                        tank = new Tank(i * Info.unitWidth, j * Info.unitHeight, Info.movelessEnemy2,
                                "src/images/Enemies/moveLessEnemy2Body.png", "src/images/Enemies/moveLessEnemy2Pipe.png",
                                Info.moveless2MiddleX, Info.moveless2MiddleY, Info.moveless2PipeLocX, Info.moveless2PipeLocY);
                        tank.setTankType(2);
                        tanks.add(tank);
                        break;
                    case 8:
                    {
                        Info.tankGlobalPositionX = i * Info.unitWidth;
                        Info.tankGlobalPositionY = j * Info.unitHeight;
                        Info.tankRelativePositionX = i * Info.unitWidth;
                        Info.tankRelativePositionY = j * Info.unitHeight;
                        break;
                    }
                    case 10:
                        trees.add(new Tree(new Point(i * Info.unitWidth, j * Info.unitHeight), "src/images/tree.png"));
                        break;
                    case 11:
                        bonuses.add(new Bonus(new Point(i * Info.unitWidth, j * Info.unitHeight), Info.aidKit, 3));
                        break;
                    case 12:
                        bonuses.add(new Bonus(new Point(i * Info.unitWidth, j * Info.unitHeight), Info.cannonShell, 2));
                        break;
                    case 13:
                        bonuses.add(new Bonus(new Point(i * Info.unitWidth, j * Info.unitHeight), Info.machineGunCart, 1));
                        break;
                    case 14:
                        bonuses.add(new Bonus(new Point(i * Info.unitWidth, j * Info.unitHeight), Info.upgrade, 4));
                        break;
                    case 15:
                        bonuses.add(new Bonus(new Point(i * Info.unitWidth, j * Info.unitHeight), Info.endingPosition, 5));
                        break;
                }
                ground.add(new Ground(new Point(i * Info.unitWidth, j * Info.unitHeight), "src/images/ground.png"));
            }
        }
    }

    /**
     * Fills the images in the global setting
     */
    private void hashMapMaker()
    {
        Image img = null;
        Info.photos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/photos.txt")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                try
                {
                    img = ImageIO.read(new File(line));
                    Info.photos.put(line, img);
                } catch (Exception e)
                {
                    if (!line.equals("//first line just for nothing !"))
                        System.out.println(line + " : not Found");
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method changes the position of the tank according to the key combination applied.
     *
     * @param up    true if up is pressed
     * @param down  true if down is pressed
     * @param left  true if left is pressed
     * @param right true if right is pressed
     */
    public void handleMotion(boolean up, boolean down, boolean left, boolean right)
    {
        int motionStep = 4;
        int rotationType = 0;
        int hor = 0, ver = 0;
        double tempAngle;

        if (left)
        {
            hor = -1;
            rotationType = 6;
        }
        if (right)
        {
            hor = 1;
            rotationType = 2;
        }
        if (up)
        {
            ver = -1;
            if (right)
                rotationType = 1;
            else if (left)
                rotationType = 7;
            else
                rotationType = 0;
        }
        if (down)
        {
            ver = 1;
            if (right)
                rotationType = 3;
            else if (left)
                rotationType = 5;
            else
                rotationType = 4;
        }

        if (ver != 0)
        {
            ownTank.setLocY(ownTank.getLocY() + ver * motionStep);
            ownTank.setRelativeY(ownTank.getRelativeY() + ver * motionStep);
        }
        if (hor != 0)
        {
            ownTank.setLocX(ownTank.getLocX() + hor * motionStep);
            ownTank.setRelativeX(ownTank.getRelativeX() + hor * motionStep);
        }
        ownTank.setLocX(Math.max(ownTank.getLocX(), 0));
        ownTank.setRelativeX(Math.max(ownTank.getRelativeX(), 0));

        ownTank.setLocX(Math.min(ownTank.getLocX(), Info.map[0].length * Info.unitWidth));
        ownTank.setRelativeX(Math.min(ownTank.getRelativeX(), Info.map[0].length * Info.unitWidth));

        ownTank.setLocY(Math.max(ownTank.getLocY(), 0));
        ownTank.setRelativeY(Math.max(ownTank.getRelativeY(), 0));

        ownTank.setLocY(Math.min(ownTank.getLocY(), Info.map[0].length * Info.unitHeight));
        ownTank.setRelativeY(Math.min(ownTank.getRelativeY(), Info.map[0].length * Info.unitHeight));

        Info.tankGlobalPositionX = ownTank.getLocX();
        Info.tankGlobalPositionY = ownTank.getLocY();
        if (checkCollision(ownTank))
        {
            if (ver != 0)
            {
                ownTank.setLocY(ownTank.getLocY() - ver * motionStep);
                ownTank.setRelativeY(ownTank.getRelativeY() - ver * motionStep);
            }
            if (hor != 0)
            {
                ownTank.setLocX(ownTank.getLocX() - hor * motionStep);
                ownTank.setRelativeX(ownTank.getRelativeX() - hor * motionStep);
            }
        }

        tempAngle = ownTank.getTankAngle();
        ownTank.rotate(rotationType);
//        if (checkCollision(ownTank))
//            ownTank.setTankAngle(tempAngle);

//        ownTank.setLocX(Math.max(ownTank.getLocX(), 0));
//        ownTank.setRelativeX(Math.max(ownTank.getRelativeX(), 0));
//
//        ownTank.setLocX(Math.min(ownTank.getLocX(), Info.map[0].length * Info.unitWidth));
//        ownTank.setRelativeX(Math.min(ownTank.getRelativeX(), Info.map[0].length * Info.unitWidth));
//
//        ownTank.setLocY(Math.max(ownTank.getLocY(), 0));
//        ownTank.setRelativeY(Math.max(ownTank.getRelativeY(), 0));
//
//        ownTank.setLocY(Math.min(ownTank.getLocY(), Info.map[0].length * Info.unitHeight));
//        ownTank.setRelativeY(Math.min(ownTank.getRelativeY(), Info.map[0].length * Info.unitHeight));

        Info.tankGlobalPositionX = ownTank.getLocX();
        Info.tankGlobalPositionY = ownTank.getLocY();
    }

    /**
     * There is a small chance that this method returns a bonus.
     * It is used to place lucky bonuses under destructable walls
     *
     * @param position the position of the bonus
     * @return null if there is no bonus
     */
    public Bonus getLucky(Point position)
    {
        int i;
        Random rand = new Random();
        i = rand.nextInt(30);
        if (i == 2)
            return new Bonus(position, Info.cannonShell, 2);
        else if (i == 1)
            return new Bonus(position, Info.machineGunCart, 1);
        else if (i == 3)
            return new Bonus(position, Info.aidKit, 3);
        else if (i == 4)
            return new Bonus(position, Info.upgrade, 4);
        return null;
    }

    /**
     * Every time this method is called the tanks take a step and fire if in the right place.
     */
    public void updateTanksStat(Tank tank)
    {
        if (tank instanceof OwnTank)
        {
            return;
        }
        if (tank.getLocX() - (Info.tankGlobalPositionX - Info.tankRelativePositionX) < -200 ||
                tank.getLocX() - (Info.tankGlobalPositionX - Info.tankRelativePositionX) > Info.mainFrameWidth + 200)
        {
            return;
        }
        if (tank.getLocY() - (Info.tankGlobalPositionY - Info.tankRelativePositionY) < -200 ||
                tank.getLocY() - (Info.tankGlobalPositionY - Info.tankRelativePositionY) > Info.mainFrameHeight + 200)
        {
            return;
        }

        if (tank instanceof MovingTank)
        {
            ((MovingTank) tank).move(2);
            if (checkCollision(tank))
                ((MovingTank) tank).changeDirection();
            tank.setPipeAngle(ownTank.getCenter().x, ownTank.getCenter().y);
            if (Network.mPlayer)
            {
                if (Point.distance(ownTank.getCenter().x, ownTank.getCenter().y, tank.getLocX(), tank.getLocY()) <=
                        Point.distance(tanks.get(tanks.size() - 2).getCenter().x, tanks.get(tanks.size() - 2).getCenter().y,
                                tank.getLocX(), tank.getLocY()))
                    tank.setPipeAngle(ownTank.getCenter().x, ownTank.getCenter().y);
                else
                    tank.setPipeAngle(tanks.get(tanks.size() - 2).getCenter().x, tanks.get(tanks.size() - 2).getCenter().y);
            }
        } else
        {
            tank.setPipeAngle(ownTank.getCenter().x, ownTank.getCenter().y);
            if (Network.mPlayer)
            {
                if (Point.distance(ownTank.getCenter().x, ownTank.getCenter().y, tank.getLocX(), tank.getLocY()) <=
                        Point.distance(tanks.get(tanks.size() - 2).getCenter().x, tanks.get(tanks.size() - 2).getCenter().y,
                                tank.getLocX(), tank.getLocY()))
                    tank.setPipeAngle(ownTank.getCenter().x, ownTank.getCenter().y);
                else
                    tank.setPipeAngle(tanks.get(tanks.size() - 2).getCenter().x, tanks.get(tanks.size() - 2).getCenter().y);
            }
//                Bullet b = tanks.get(i).fire();
//                if (b != null)
//                    bullets.add(b);
        }
        tank.fire();
    }

    /**
     * Every time this method is called the bullets move or get destoyed if they crash with something.
     */
    public void updateBulletsStat(Bullet bullet)
    {
        bullet.move();
        if (checkCollision(bullet))
        {
            bullets.remove(bullet);
            System.out.println("removed");
            return;
        }
    }

    /**
     * Saves the data to transfer to other player.
     * @param bullets bullets object
     * @param walls walls object
     * @param tanks tanks object
     * @param bonuses bonuses object
     */
    private void saveData(Vector<Bullet> bullets, Vector<Wall> walls, Vector<Tank> tanks, Vector<Bonus> bonuses)
    {
        data.setTanks(tanks);
        Vector<DataQuantom> bonusQ = new Vector<>();
        Vector<DataQuantom> wallQ = new Vector<>();
        for (Wall wall : walls)
        {
            if (wall instanceof WeakWall)
                wallQ.add(new DataQuantom(wall.getAbsolutePos(), ((WeakWall) wall).getState()));
        }
        for (Bonus bonus : bonuses)
        {
            bonusQ.add(new DataQuantom(bonus.getAbsolutePos(), bonus.getBonusType()));
        }
        data.setBonuses(bonusQ);
        data.setWalls(wallQ);
    }

    public void timer()
    {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                if (keyPressed)
                {
                    SoundMaker.play("src/Sounds/motor1.wav");
                }
            }
        }, 0, 300);
    }
}


