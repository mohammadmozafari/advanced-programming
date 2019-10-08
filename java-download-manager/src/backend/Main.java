package backend;

public class Main
{
    public static void main(String[] args)
    {
        StoredData.setLookAndFeels();
        ActionCenter a = new ActionCenter();
        a.changeLookAndFeel();
        GuiManager.loadMainFrame();
    }
}
