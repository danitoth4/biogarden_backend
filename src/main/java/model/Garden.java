package model;

import java.awt.dnd.InvalidDnDOperationException;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class Garden
{

    private GardenType gardenType;

    private SunType sunType;

    private Double length;

    private Double width;

    public boolean isOverlapping(Point2D.Double topleft1, Point2D.Double bottomright1, Point2D.Double topleft2, Point2D.Double bottomright2 )
    {
        if (topleft1.getY() < bottomright2.getY() || bottomright1.getY() > topleft2.getY())
            return false;
        if(topleft1.getX() > bottomright2.getX() || bottomright1.getX() < topleft2.getX())
            return false;
        return true;
    }
}