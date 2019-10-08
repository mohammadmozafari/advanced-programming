package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class is used for drawing some common components with common properties.
 *
 * @author Mohammad Mozafari
 */
public class CustomComponents
{
    /**
     * This static methods draws a JButton with given fields.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param name
     * @param path
     * @param released
     * @param pressed
     * @return
     */
    public static JButton createButton(int x, int y, int width, int height, String name, String path, Color released, Color pressed)
    {
        JButton button = new JButton(new ImageIcon(path));
        button.setName(name);
        button.setLocation(x, y);
        button.setSize(width, height);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(released);
        button.setBorderPainted(false);
        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                ((JButton)e.getSource()).setBackground(pressed);
                button.setVisible(false);
                button.setVisible(true);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                ((JButton)e.getSource()).setBackground(released);
                button.setVisible(false);
                button.setVisible(true);
            }
        });
        return button;
    }

    /**
     * This static method draws a JLabel with given fields.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param text
     * @param font
     * @param foreground
     * @return
     */
    public static JLabel createLabel(int x, int y, int width, int height, String text, Font font, Color foreground)
    {
        JLabel label = new JLabel(text);
        label.setLocation(x, y);
        label.setSize(width, height);
        label.setFont(font);
        label.setForeground(foreground);
        return label;
    }
}
