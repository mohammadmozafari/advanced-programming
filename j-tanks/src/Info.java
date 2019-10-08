import java.awt.*;
import java.util.HashMap;

/**
 * This class is used for holding constant values across the program. They are final and static so they can be accessed anywhere.
 * This class centeralizes the info used in the code so it can be changed easily and quickly.
 *
 * @author Mohammad Mozafari
 */
public class Info
{
    public static HashMap<String,Image> photos;
    //Own Tank
    public final static int ownTankWidth = 80;
    public final static int ownTankHeight = 90;
    public final static int ownTankPipeLocX = 24;
    public final static int ownTankPipeLocY = -15;
    public final static int ownTankMiddleX = 40;
    public final static int ownTankMiddleY = 45;
    //Enemy 1
    public final static int moveless1MiddleX = 49;
    public final static int moveless1MiddleY = 47;
    public final static int moveless1PipeLocX = 6;
    public final static int moveless1PipeLocY = -6;
    //Enemy 2
    public final static int moveless2MiddleX = 34;
    public final static int moveless2MiddleY = 34;
    public final static int moveless2PipeLocX = 1;
    public final static int moveless2PipeLocY = -18;
    //Enemy 3
    public final static int moving1MiddleX = 30;
    public final static int moving1MiddleY = 30;
    public final static int moving1PipeLocX = 16;
    public final static int moving1PipeLocY = -28;
    //Enemy 4
    public final static int moving2MiddleX = 33;
    public final static int moving2MiddleY = 44;
    public final static int moving2PipeLocX = 21;
    public final static int moving2PipeLocY = -11;


    public final static int unitWidth = 100;
    public final static int unitHeight = 100;


    public final static String ownTankPath = "src/images/myTank/tankBody.png";
    public final static String cannonPath = "src/images/myTank/cannon.png";
    public final static String machineGunPath = "src/images/myTank/machineGunBullets.png";
    public final static String cannonPipePath = "src/images/myTank/cannon_L";
    public final static String machineGunPipePath = "src/images/myTank/machineGun_L";
    public final static String weapon1Display = "src/images/myTank/weapon1.png";
    public final static String weapon2Display = "src/images/myTank/weapon2.png";
    public final static String heartPath = "src/images/myTank/heart.png";
    public final static String cursorPath = "src/images/cursor.png";

    public final static String cannonShell = "src/images/Bonus/cannonShells.png";
    public final static String machineGunCart = "src/images/Bonus/machineGunCart.png";
    public final static String aidKit = "src/images/Bonus/medical-kit.png";
    public final static String upgrade = "src/images/Bonus/star.png";
    public final static String endingPosition = "src/images/Bonus/endingPosition.png";
    public static String mapLocation = "src/maps/map11.map";
    protected static int Quest = 11 ;

    public final static int weapon1Damage = 13;
    public final static int weapon2Damage = 8;

    public final static int mainFrameHeight = 720;
    public final static int mainFrameWidth = mainFrameHeight * 16 / 9;

    public final static int speedConstant = 100;

    public static int [][] map ;
    public static void setMap(int[][] map)
    {
        Info.map = map;
    }

    public static int xDif;
    public static int yDif;
    public static int tankGlobalPositionX;
    public static int tankGlobalPositionY;
    public static int tankRelativePositionX;
    public static int tankRelativePositionY;

    public static int movingEnemy1;
    public static int movingEnemy2;
    public static int movelessEnemy1;
    public static int movelessEnemy2;
}
