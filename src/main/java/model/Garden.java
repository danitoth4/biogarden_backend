package model;

import Misc.Cache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Misc.Grid;
import model.repositories.ConcreteCropRepository;
import web.errorhandling.GardenException;


@Entity
public class Garden
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private Integer length;

    private Integer width;

    @JsonProperty("plantedCrops")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "garden", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ConcreteCrop> plantedCropsList = new ArrayList<>();

    @JsonIgnore
    @Transient
    private HashMap<Point, String> plantedCrops;

    /**
     *
     */
    public Garden()
    {
    }

    /**
     *
     * @param l
     * @param w
     */
    public Garden(int l, int w)
    {
        length = l;
        width = w;

        initialize();
    }

    private void setUpMap()
    {

    }

    public int getId()
    {
        return id;
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

    public void setPlantedCropsList(ArrayList<ConcreteCrop> plantedCropsList)
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

    public Integer getLength()
    {
        return length;
    }

    public Integer getWidth()
    {
        return width;
    }

    /**
     * This function sets up the HashMap in which the cells of the garden are mapped to the crops that are on them.
     */
    private void initialize()
    {
        //first we check if the map is null
        if(plantedCrops == null)
        {
            //try to get the value from the cache so it doesn't have to be created
            plantedCrops = Cache.getCachedInstance(this.id);

            if(plantedCrops == null)
            {
                if(this.length != null && this.width != null)
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

                    for (int i = 0; i < width; ++i)
                    {
                        for (int j = 0; j < length; ++j)
                        {
                            Point p = new Point(i, j);
                            if(!plantedCrops.containsKey(p))
                            {
                                plantedCrops.put(p, null);
                            }
                        }
                    }
                }
                else
                {
                    throw new GardenException("Length and width must be set");
                }
            }
        }
    }

    public Collection<ConcreteCrop> selectDistinct(Collection<ConcreteCrop> crops)
    {
        crops.stream().distinct().
        for(ConcreteCrop cc : crops)
        {

        }
        return crops;
    }

    public Collection<ConcreteCrop> getPlantedCropsList(float zoom, int x1, int y1, int x2, int y2)
    {
        List<ConcreteCrop> zoomedList = plantedCropsList.stream().filter(cc -> (cc.getStartX() >= x1 && cc.getStartY() >= y1 && cc.getStartX() < x2 && cc.getStartY() < y2) ||
                cc.getEndX() > x1 && cc.getEndY() > y1 && cc.getEndX() <= x2 && cc.getEndY() <= y2).collect(Collectors.toList());
        zoomedList.forEach(cc -> {
            cc.setStartX(Math.round((cc.getStartX() - x1) / zoom));
            cc.setStartY(Math.round((cc.getStartY() - y1) / zoom));
            cc.setEndX(Math.round((cc.getEndX() - x1) / zoom));
            cc.setEndY(Math.round((cc.getEndY() - y1 ) / zoom));
            if(cc.getStartX() == cc.getEndX())
                cc.setEndX(cc.getStartX() + 1);
            if(cc.getStartY() == cc.getEndY())
                cc.setEndY(cc.getEndY() + 1);
            cc.setId(null);
        });
        return selectDistinct(zoomedList);
        /*initialize();
        List<ConcreteCrop> zoomedList = new ArrayList<>();
        //Stream<Map.Entry<Point, Integer>> stream = plantedCrops.entrySet().stream();
        int i = 0, j = 0;
        double dx0 = x1, dx1 = x1 + zoom, dy0 = y1, dy1 = y1 + zoom;
        while (dx1 <= x2)
        {
            while(dy1 <= y2)
            {

                LinkedList<String> containedIds = new LinkedList<>();
                for(int x = (int)Math.round(dx0); x < Math.round(dx1); ++x)
                {
                    for(int y = (int)Math.round(dy0); y < Math.round(dy1); ++y)
                    {
                        String id = plantedCrops.get(new Point(x,y));
                        if(id != null)
                        containedIds.add(id);
                    }
                }
                Stream stream = plantedCropsList.stream().filter(c -> containedIds.contains(c.getId()));
                if(stream.count() > 0)
                {
                    ConcreteCrop cc = new ConcreteCrop();
                    cc.setStartX(i);
                    cc.setStartY(j);

                    if(stream.distinct().count() > 1)
                    {

                    }
                }
                j++; dy0 = dy1; dy1 += zoom;
            }
            i++; dx0 = dx1; dx1 += zoom;
        }*/

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
                    for(int x_index = plantedCrop.getStartX(); x_index < plantedCrop.getEndX(); ++x_index)
                    {
                        for(int y_index = plantedCrop.getStartY(); y_index < plantedCrop.getEndY(); ++y_index)
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