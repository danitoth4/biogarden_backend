package model;

import java.awt.dnd.InvalidDnDOperationException;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class Garden {

    public GardenType gardenType;

    public SunType sunType;

    public Double length;

    public Double width;

    public HashMap<Point2D.Double, Plantable> plantables;

    public int plant(Plantable plantable, Point2D.Double point)
    {
        for(Map.Entry<Point2D.Double, Plantable> entry : plantables.entrySet())
        {
            if(isOverlapping(entry.getKey(), new Point2D.Double(entry.getKey().getX() + entry.getValue().getWidth(),entry.getKey().getY() + entry.getValue().getLength()), point, new Point2D.Double(point.getX() + plantable.getWidth(),point.getY() + plantable.getLength())))
                throw new InvalidDnDOperationException("the crop is overlapping with an existing crop");
        }
        plantables.put(point, plantable);
        return plantable.plant(null);
    }

    public boolean isOverlapping(Point2D.Double topleft1, Point2D.Double bottomright1, Point2D.Double topleft2, Point2D.Double bottomright2 )
    {
        if (topleft1.getY() < bottomright2.getY() || bottomright1.getY() > topleft2.getY())
            return false;
        if(topleft1.getX() > bottomright2.getX() || bottomright1.getX() < topleft2.getX())
            return false;
        return true;
    }
    }