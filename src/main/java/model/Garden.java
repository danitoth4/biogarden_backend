package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.h2.table.Plan;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.awt.*;
import java.util.ArrayList;
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

    @JsonProperty("plantedCrops")
    private ArrayList<ConcreteCrop> plantedCropsList = new ArrayList<>();

    @JsonIgnore
    private HashMap<Point, ConcreteCrop> plantedCrops = new HashMap<>();

    public ArrayList<ConcreteCrop> getPlantedCropsList()
    {
        return plantedCropsList;
    }

    public void setPlantedCropsList(ArrayList<ConcreteCrop> plantedCropsList)
    {
        this.plantedCropsList = plantedCropsList;
    }

    public HashMap<Point, ConcreteCrop> getPlantedCrops()
    {
        return plantedCrops;
    }

    public void setPlantedCrops(HashMap<Point, ConcreteCrop> plantedCrops)
    {
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

    public Garden(int l, int w)
    {
        length = l;
        width = w;

        for (int i = 0; i < length / cellSize; ++i)
        {
            for (int j = 0; j < width / cellSize; ++j)
            {
                plantedCrops.put(new Point(i, j), null);

            }
        }
    }

    public boolean plantCrop(PlantingOperation po)
    {
        if (!validatePosition(po))
        {
            return false;
        }
        for (ConcreteCrop cc : plantedCrops.values())
        {
            if (cc != null && isOverlapping(cc.getStartPoint(), cc.getEndPoint(), new Point(po.getX1(), po.getY1()), new Point(po.getX2(), po.getY2())))
                return false;
        }

        try
        {
            if (po.getCrop() == null)
            {
                return false;
            }
            //checking if the plant fits there at least once
            int x = (po.getX2() - po.getX1()) * cellSize;
            int y = (po.getY2() - po.getY1()) * cellSize;
            int dm = Math.round(po.getCrop().getDiameter());
            if (dm > x || dm > y)
            {
                return false;
            }
            //some black magic calculations idk who wrote this garbage
            int i = x / dm;
            int j = y / dm;
            for (int k = 0; k < i; ++k)
            {
                for (int l = 0; l < j; ++l)
                {
                    //maybe this works, i have no idea
                    ConcreteCrop plantedCrop = new ConcreteCrop(po.getCrop());
                    plantedCrop.setStartPoint(new Point(po.getX1() + k * (dm / cellSize), po.getY1() + l * (dm / cellSize)));
                    plantedCrop.setEndPoint(new Point(plantedCrop.getStartPoint().x + dm / cellSize, plantedCrop.getStartPoint().y + dm / cellSize));
                    plantedCropsList.add(plantedCrop);
                    for(int x_index = (int)plantedCrop.getStartPoint().getX(); x_index < (int)plantedCrop.getEndPoint().getX(); ++x_index)
                    {
                        for(int y_index = (int)plantedCrop.getStartPoint().getY(); y_index < (int)plantedCrop.getEndPoint().getY(); ++y_index)
                            plantedCrops.put(new Point(plantedCrop.getStartPoint()), plantedCrop);
                    }

                }
            }
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public boolean deleteCrops(PlantingOperation po)
    {
        if (!validatePosition(po))
        {
            return false;
        }
        for (Map.Entry<Point, ConcreteCrop> entry : plantedCrops.entrySet())
        {
            if (entry.getValue() != null && isOverlapping(entry.getValue().getStartPoint(), entry.getValue().getEndPoint(), new Point(po.getX1(), po.getY1()), new Point(po.getX2(), po.getY2())))
            {
                plantedCropsList.remove( entry.getValue() );
                plantedCrops.replace(entry.getKey(), null);
            }
        }
        return true;
    }


    private boolean isOverlapping(Point topleft1, Point bottomright1, Point topleft2, Point bottomright2)
    {
        if (topleft1.getY() >= bottomright2.getY() || bottomright1.getY() <= topleft2.getY())
            return false;
        if (topleft1.getX() >= bottomright2.getX() || bottomright1.getX() <= topleft2.getX())
            return false;
        return true;
    }


    /**
     * organizes the the coordinates in the right order and validates if they are on the grid
     * @param po The planting operation
     * @return true if the operation is valid
     */
    private boolean validatePosition(PlantingOperation po)
    {
        if(po.getX1() > po.getX2() )
        {
            int temp = po.getX1();
            po.setX1(po.getX2());
            po.setX2(temp);
        }
        if(po.getY1() > po.getY2() )
        {
            int temp = po.getY1();
            po.setY1(po.getY2());
            po.setY2(temp);
        }

        return !(!plantedCrops.containsKey(new Point(po.getX1(), po.getY1())) || !plantedCrops.containsKey(new Point(po.getX2() -1 , po.getY2() - 1)));
    }
}