package model;

import Misc.Cache;
import Misc.Grid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import web.errorhandling.GardenException;

import javax.persistence.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class GardenContent
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    String name;

    @JsonProperty("plantedCrops")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gardenContent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<ConcreteCrop> plantedCropsList = new ArrayList<>();

    @JsonIgnore
    @Transient
    public HashMap<Point, String> plantedCrops;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Garden garden;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "after")
    public GardenContent before;

    @OneToOne(fetch = FetchType.LAZY)
    public GardenContent after;

    public GardenContent(Garden garden, String name)
    {
        this.garden = garden;
        this.name = name;
    }

    void initialize()
    {
        //first we check if the map is null
        if(plantedCrops == null)
        {
            //try to get the value from the cache so it doesn't have to be created
            plantedCrops = Cache.getCachedInstance(this.id);

            if(plantedCrops == null)
            {

                //setting up the map manually. if the list loaded from the db already contains elements then fill them

                plantedCrops = new HashMap<>();

                for(ConcreteCrop cc : plantedCropsList)
                {
                    for(int i = cc.getStartX(); i < cc.getEndX(); ++i )
                    {
                        for(int j = cc.getStartY(); j < cc.getEndY(); ++j)
                        {
                            plantedCrops.put(new Point(i, j), cc.getId());
                        }
                    }
                }
                for (int i = 0; i < garden.getWidth(); ++i)
                {
                    for (int j = 0; j < garden.getLength(); ++j)
                    {
                        Point p = new Point(i, j);
                        if(!plantedCrops.containsKey(p))
                        {
                            plantedCrops.put(p, null);
                        }
                    }
                }
            }
        }
    }

    public Collection<ConcreteCrop> getPlantedCropsList(float zoom, int x1, int y1, int x2, int y2)
    {
        List<ConcreteCrop> zoomedList = plantedCropsList.stream().filter(cc -> (cc.getEndX() > x1 && cc.getEndY() > y1 && cc.getStartX() < x2 && cc.getStartY() < y2)).collect(Collectors.toList());
        zoomedList.forEach(cc -> {
            cc.setStartX(Math.round((cc.getStartX() - x1) / zoom));
            cc.setStartY(Math.round((cc.getStartY() - y1) / zoom));
            cc.setEndX(Math.round((cc.getEndX() - x1) / zoom));
            cc.setEndY(Math.round((cc.getEndY() - y1) / zoom));
            if (cc.getStartX() == cc.getEndX())
                cc.setEndX(cc.getStartX() + 1);
            if (cc.getStartY() == cc.getEndY())
                cc.setEndY(cc.getEndY() + 1);
            cc.setId(null);
        });
        return zoomedList.stream().distinct().collect(Collectors.toList());
    }

    public boolean plantCrop(PlantingOperation po, double zoom)
    {
        initialize();
        if (!validatePosition(po))
        {
            return false;
        }
        for (ConcreteCrop cc : plantedCropsList)
        {
            if (cc != null && Grid.isOverlapping(cc.getStartX(), cc.getStartY(), cc.getEndX(), cc.getEndY(), po.getX1(), po.getY1(), po.getX2(), po.getY2()))
                return false;
        }

        try
        {
            if (po.getCrop() == null)
            {
                return false;
            }
            //checking if the plant fits there at least once
            int x = po.getX2() - po.getX1();
            int y = po.getY2() - po.getY1();
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
                    plantedCrop.setGarden(this);
                    plantedCrop.setStartX(po.getX1() + k * dm);
                    plantedCrop.setStartY(po.getY1() + l * dm);
                    plantedCrop.setEndX(plantedCrop.getStartX() + dm);
                    plantedCrop.setEndY(plantedCrop.getStartY() + dm);
                    plantedCrop.setCropTypeId(plantedCrop.getCropType().getId());
                    plantedCropsList.add(plantedCrop);
                    for (int x_index = plantedCrop.getStartX(); x_index < plantedCrop.getEndX(); ++x_index)
                    {
                        for (int y_index = plantedCrop.getStartY(); y_index < plantedCrop.getEndY(); ++y_index)
                            plantedCrops.put(new Point(plantedCrop.getStartX(), plantedCrop.getStartY()), plantedCrop.getId());
                    }
                }
            }
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * organizes the the coordinates in the right order and validates if they are on the grid
     *
     * @param po The planting operation
     * @return true if the operation is valid
     */
    private boolean validatePosition(PlantingOperation po)
    {
        if (po.getX1() > po.getX2())
        {
            int temp = po.getX1();
            po.setX1(po.getX2());
            po.setX2(temp);
        }
        if (po.getY1() > po.getY2())
        {
            int temp = po.getY1();
            po.setY1(po.getY2());
            po.setY2(temp);
        }

        return !(!plantedCrops.containsKey(new Point(po.getX1(), po.getY1())) || !plantedCrops.containsKey(new Point(po.getX2() - 1, po.getY2() - 1)));
    }

    public boolean deleteCrops(PlantingOperation po, double zoom)
    {
        initialize();
        if (!validatePosition(po))
        {
            return false;
        }
        for (Map.Entry<Point, String> entry : plantedCrops.entrySet())
        {
            ConcreteCrop cc = plantedCropsList.stream().filter(c -> c.getId().equals(entry.getValue())).findAny().orElse(null);
            if (cc != null && Grid.isOverlapping(cc.getStartX(), cc.getStartY(), cc.getEndX(), cc.getEndY(), po.getX1(), po.getY1(), po.getX2(), po.getY2()))
            {
                plantedCropsList.remove(cc);
                plantedCrops.replace(entry.getKey(), null);

            }
        }
        return true;
    }

}
