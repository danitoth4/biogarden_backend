package model;

import Misc.Cache;
import Misc.Grid;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private int id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gardenContent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ConcreteCrop> plantedCropsList = new ArrayList<>();

    @JsonIgnore
    @Transient
    private HashMap<Point, String> plantedCrops;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Garden garden;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "after")
    private GardenContent before;

    @OneToOne(fetch = FetchType.LAZY)
    private GardenContent after;

    private String userId;

    public GardenContent(Garden garden, String name)
    {
        this.garden = garden;
        this.name = name;
        this.userId = garden.getUserId();
    }

    public GardenContent()
    {}

    //region Getters and Setters
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<ConcreteCrop> getPlantedCropsList()
    {
        return plantedCropsList;
    }

    public void setPlantedCropsList(List<ConcreteCrop> plantedCropsList)
    {
        this.plantedCropsList = plantedCropsList;
    }

    public HashMap<Point, String> getPlantedCrops()
    {
        return plantedCrops;
    }

    public void setPlantedCrops(HashMap<Point, String> plantedCrops)
    {
        this.plantedCrops = plantedCrops;
    }

    public Garden getGarden()
    {
        return garden;
    }

    public void setGarden(Garden garden)
    {
        this.garden = garden;
    }

    public GardenContent getBefore()
    {
        return before;
    }

    public void setBefore(GardenContent before)
    {
        this.before = before;
    }

    public GardenContent getAfter()
    {
        return after;
    }

    public void setAfter(GardenContent after)
    {
        this.after = after;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    //endregion


    private void initialize()
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
                    addCropToMap(cc);
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

    private void addCropToMap(ConcreteCrop cc)
    {
        if(plantedCrops == null)
            return;
        for(int i = cc.getStartX(); i < cc.getEndX(); ++i )
        {
            for(int j = cc.getStartY(); j < cc.getEndY(); ++j)
            {
                plantedCrops.put(new Point(i, j), cc.getId());
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
            int dm = po.getCrop().getDiameter();
            if (dm > x || dm > y)
            {
                return false;
            }
            //how many times does it fit there in x direction and y direction
            int i = x / dm;
            int j = y / dm;
            for (int k = 0; k < i; ++k)
            {
                for (int l = 0; l < j; ++l)
                {
                    ConcreteCrop plantedCrop = new ConcreteCrop(po.getCrop());
                    plantedCrop.setGardenContent(this);
                    plantedCrop.setStartX(po.getX1() + k * dm);
                    plantedCrop.setStartY(po.getY1() + l * dm);
                    plantedCrop.setEndX(plantedCrop.getStartX() + dm);
                    plantedCrop.setEndY(plantedCrop.getStartY() + dm);
                    plantedCrop.setCropTypeId(plantedCrop.getCropType().getId());
                    if(k == 0 || k == i - 1 || l == 0 ||l == j - 1)
                    plantedCropsList.stream().parallel().forEach(cc -> {
                        if(Grid.isOverlapping(cc.getStartX(), cc.getStartY(), cc.getEndX(), cc.getEndY(), plantedCrop.getStartX() - dm, plantedCrop.getStartY() - dm, plantedCrop.getEndX() + dm, plantedCrop.getEndY() + dm))
                        {
                            cc.addCompanionRecommendation(plantedCrop);
                            plantedCrop.addCompanionRecommendation(cc);
                        }
                    });
                    plantedCropsList.add(plantedCrop);
                    addCropToMap(plantedCrop);
                    addRotationRecommendations(plantedCrop);
                }
            }
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    private void addRotationRecommendations(ConcreteCrop cc)
    {
        if(this.before != null)
        {
            before.initialize();
            addRotationRecommendation(before, this, cc.getStartX(), cc.getStartY(), cc.getEndX(), cc.getEndY());
        }
        if(this.after != null)
        {
            after.initialize();
            addRotationRecommendation(this, after, cc.getStartX(), cc.getStartY(), cc.getEndX(), cc.getEndY());
        }
    }

    private static void addRotationRecommendation(GardenContent last, GardenContent current, int startX, int startY, int endX, int endY)
    {
        for(int i = startX; i <= endX; ++i)
        {
            for(int j = startY; j <= endY; ++i)
            {
                Point p = new Point(i, j);
                String onBeforeId = last.plantedCrops.get(p);
                String onCurrentlyId = current.plantedCrops.get(p);
                if(onBeforeId != null && onCurrentlyId != null)
                {
                    ConcreteCrop onCurrently = current.plantedCropsList.stream().filter(cc -> cc.getId().equals(onCurrentlyId)).findFirst().orElse(null);
                    ConcreteCrop onBefore = last.plantedCropsList.stream().filter(cc -> cc.getId().equals(onBeforeId)).findFirst().orElse(null);
                    if(onCurrently != null)
                    {
                        onCurrently.addRotationRecommendation(onBefore, last.id);
                    }
                }
            }
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
        return (po.getX1() >= 0 && po.getY1() >= 0 && po.getX2() <= this.garden.getWidth() && po.getY2() <= this.garden.getLength());
    }

    public void deleteCrops(PlantingOperation po, double zoom)
    {
        if (!validatePosition(po))
        {
            return;
        }
        for(ConcreteCrop cc : plantedCropsList.stream().filter(c -> Grid.isOverlapping(c.getStartX(), c.getStartY(), c.getEndX(), c.getEndY(), po.getX1(), po.getY1(), po.getX2(), po.getY2())).collect(Collectors.toList()))
        {
            plantedCropsList.remove(cc);
        }
    }

}
