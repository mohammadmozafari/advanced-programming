import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * all the progress of sending and receiving data on multi player is done in this class
 * this class has an boolean indicating if the game is multi player or not.
 * @author mohammad mozafary
 */
public class Network
{
    public static boolean mPlayer = false;
    public static int turn;
    public static boolean isServer, firstConnection = true;
    public static Socket socket;
    public static long lastSent = 0;

    public static void startServer(JFrame frame)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(13028);
            System.out.println("waiting");
            socket = serverSocket.accept();
            mPlayer = true;
            System.out.println("connected");
            frame.dispose();
            turn = 0;
            Main.startGame();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void connectToOthers(JFrame frame)
    {
        String ip = null;
//        if (!mPlayer)
            ip = JOptionPane.showInputDialog(null, "please enter the ip of the Host", "127.0.0.1");
        try
        {
            socket = new Socket(ip, 13028);
            mPlayer = true;
            System.out.println("connected");
            Main.startGame();
            frame.dispose();
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "no such host to connect to");
        }
    }

    public static void sendData(Data data) throws IOException
    {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(data);
        oos.flush();
    }

    public static Data getData() throws IOException
    {
        Data data = null;
        ObjectInputStream in = null;
        try
        {
            in = new ObjectInputStream(socket.getInputStream());
            data = (Data) in.readObject();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        if (data != null)
        {
            //System.out.println("read");
        }
        return data;
    }

    public static Tank getLittleData()
    {
        ObjectInputStream in = null;
        Tank t = null;
        try
        {
            in = new ObjectInputStream(socket.getInputStream());
            t = (Tank) in.readObject();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        if (t != null)
        {
            //System.out.println("read");
        }
        return t;
    }
}
