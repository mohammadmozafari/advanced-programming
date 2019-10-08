import java.awt.*;
import java.awt.geom.Area;

/**
 * this interface is for the objects who will collide with each other
 * their area and center will be used in order to collide them
 * @author mohammad mozafary
 */
public interface Crashable
{
    Area getArea();
    Point getCenter();
}
