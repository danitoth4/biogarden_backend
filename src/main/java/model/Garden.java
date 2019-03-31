package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.awt.*;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;


@Entity
public class Garden
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

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

        for(int i = 0; i < length / cellSize; ++i)
        {
            for (int j = 0; j < width / cellSize; ++j)
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

    public GardenType getGardenType()
    {
        return gardenType;
    }

    public void setGardenType(GardenType gardenType)
    {
        this.gardenType = gardenType;
    }

    public SunType getSunType()
    {
        return sunType;
    }

    public void setSunType(SunType sunType)
    {
        this.sunType = sunType;
    }

    public Integer getLength()
    {
        return length;
    }

    public void setLength(Integer length)
    {
        this.length = length;
    }

    public Integer getWidth()
    {
        return width;
    }

    public void setWidth(Integer width)
    {
        this.width = width;
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