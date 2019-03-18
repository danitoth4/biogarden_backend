package model;

import javax.persistence.Entity;
import java.awt.*;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;


@Entity
public class Garden
{

    public static final int cellSize = 5;

    private GardenType gardenType;

    private SunType sunType;

    private Integer length;

    private Integer width;

    private HashMap<Point, ConcreteCrop> plantedCrops = new HashMap<>();

    public Garden(int l, int w)
    {
        length = l;
        width = w;

        for(int i = 0; i < length / 5; ++i)
        {
            for (int j = 0; j < width / 5; ++j)
            {
                plantedCrops.put(new Point(i, j), null);

            }
        }
    }

    public HashMap<Point, ConcreteCrop> getPlantedCrops() {
        return plantedCrops;
    }

    public void setPlantedCrops(HashMap<Point, ConcreteCrop> plantedCrops) {
        this.plantedCrops = plantedCrops;
    }




    /*
    public boolean isOverlapping(Point2D.Double topleft1, Point2D.Double bottomright1, Point2D.Double topleft2, Point2D.Double bottomright2 )
    {
        if (topleft1.getY() < bottomright2.getY() || bottomright1.getY() > topleft2.getY())
            return false;
        if(topleft1.getX() > bottomright2.getX() || bottomright1.getX() < topleft2.getX())
            return false;
        return true;
    }*/
}