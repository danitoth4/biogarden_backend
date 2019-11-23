package Misc;

import java.awt.*;

public class Grid
{
    public static boolean isOverlapping(Point topLeft1, Point bottomRight1, Point topLeft2, Point bottomRight2)
    {
        if (topLeft1.getY() >= bottomRight2.getY() || bottomRight1.getY() <= topLeft2.getY())
            return false;
        if (topLeft1.getX() >= bottomRight2.getX() || bottomRight1.getX() <= topLeft2.getX())
            return false;
        return true;
    }

    public static boolean isOverlapping(int topLeft1X, int topLeft1Y, int bottomRight1X, int bottomRight1Y, int topLeft2X, int topLeft2Y, int bottomRight2X, int bottomRight2Y)
    {
        if (topLeft1Y >= bottomRight2Y || bottomRight1Y <= topLeft2Y)
            return false;
        if (topLeft1X >= bottomRight2X || bottomRight1X <= topLeft2X)
            return false;
        return true;
    }
}
