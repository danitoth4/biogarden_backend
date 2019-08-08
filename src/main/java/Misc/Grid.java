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
}
