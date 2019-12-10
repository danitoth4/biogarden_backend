package model;

//import Misc.Cache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private CropMap plantedCrops;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Garden garden;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "after", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private GardenContent before;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    public CropMap getPlantedCrops()
    {
        return plantedCrops;
    }

    public void setPlantedCrops(CropMap plantedCrops)
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
            //setting up the map manually. if the list loaded from the db already contains elements then fill them
            plantedCrops = new CropMap();
            for(ConcreteCrop cc : plantedCropsList)
            {
                addCropToMap(cc);
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
                plantedCrops.getConcreteCropMap().put(new Point(i, j), cc);
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
            cc.setId(0);
        });
        return zoomedList.stream().distinct().collect(Collectors.toList());
    }

    public boolean plantCrop(PlantingOperation po)
    {
        initialize();
        if (!validatePosition(po))
        {
            return false;
        }
        //the user wants to plant somewhere thats already taken
        if (!plantedCrops.getCropsOnSpace(po.getX1(), po.getX2(), po.getY1(), po.getY2()).isEmpty())
            return false;
        try
        {
            //length and width of the planting
            int x = po.getX2() - po.getX1();
            int y = po.getY2() - po.getY1();

            // get width and length of the crop based on rotation. If the crop is rotated its legnth becomes its width and vice versa
            int width = po.isRotated() ? po.getCrop().getLength() : po.getCrop().getWidth();
            int length = po.isRotated() ? po.getCrop().getWidth() : po.getCrop().getLength();

            //how many times does it fit there in x direction and y direction
            int i = x / width;
            int j = y / length;
            for (int k = 0; k < i; ++k)
            {
                for (int l = 0; l < j; ++l)
                {
                    createCropAndRecommendations(po, k, l, width, length);
                }
            }
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    private void createCropAndRecommendations(PlantingOperation po, int k, int l, int width, int length)
    {
        ConcreteCrop plantedCrop = new ConcreteCrop(po.getCrop());
        plantedCrop.setGardenContent(this);
        plantedCrop.setStartX(po.getX1() + k * width);
        plantedCrop.setStartY(po.getY1() + l * length);
        plantedCrop.setEndX(plantedCrop.getStartX() + width);
        plantedCrop.setEndY(plantedCrop.getStartY() + length);
        plantedCrop.setCropTypeId(plantedCrop.getCropType().getId());
        if(k * width - Crop.radius == po.getX1() || (k + 1)* width + Crop.radius >= po.getX2()
                || l * length - Crop.radius == po.getY1() || (l + 1) * length + Crop.radius >= po.getY2())
                for(ConcreteCrop cc : plantedCrops.getCropsOnSpace(
                        plantedCrop.getStartX() - Crop.radius,
                        plantedCrop.getEndX() + Crop.radius,
                        plantedCrop.getStartY() - Crop.radius,
                        plantedCrop.getEndY() + Crop.radius))
                {
                    cc.addCompanionRecommendation(plantedCrop);
                    plantedCrop.addCompanionRecommendation(cc);
                }
        plantedCropsList.add(plantedCrop);
        addCropToMap(plantedCrop);
        addRotationRecommendations(plantedCrop);
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
            for(int j = startY; j <= endY; ++j)
            {
                Point p = new Point(i, j);
                ConcreteCrop onBefore = last.plantedCrops.getConcreteCropMap().get(p);
                ConcreteCrop onCurrently = current.plantedCrops.getConcreteCropMap().get(p);
                if(onCurrently != null && onBefore != null)
                {
                    onCurrently.addRotationRecommendation(onBefore, last.id);
                }
            }
        }
    }

    /**
     * organizes the the coordinates in the right order and validates that they are on the grid
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
        initialize();
        for(ConcreteCrop cc : plantedCrops.getCropsOnSpace(po.getX1(), po.getX2(), po.getY1(), po.getY2()))
        {
            plantedCropsList.remove(cc);
        }
    }
}
