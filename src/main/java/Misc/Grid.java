package Misc;

import java.awt.*;

public class Grid
{
    public static boolean isOverlapping(Point topleft1, Point bottomright1, Point topleft2, Point bottomright2)
    {
        if (topleft1.getY() >= bottomright2.getY() || bottomright1.getY() <= topleft2.getY())
            return false;
        if (topleft1.getX() >= bottomright2.getX() || bottomright1.getX() <= topleft2.getX())
            return false;
        return true;
    }

    public static boolean isOverlapping(int topleft1X, int topleft1Y, int bottomright1X, int bottomright1Y, int topleft2X, int topleft2Y, int bottomright2X, int bottomright2Y)
    {
        if (topleft1Y >= bottomright2Y || bottomright1Y <= topleft2Y)
            return false;
        if (topleft1X >= bottomright2X || bottomright1X <= topleft2X)
            return false;
        return true;
    }
}
