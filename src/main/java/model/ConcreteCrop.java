package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "ConcreteCrops")
public class ConcreteCrop
{
    @Id
    @JsonProperty("id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private GardenContent gardenContent;

    private Short preferenceValue;

    private int startX;

    private int startY;

    private int endX;

    private int endY;

    //we need the fk so we only need to send this to the client
    @Column(name = "CROP_TYPE_ID", insertable = false, updatable = false)
    private Integer cropTypeId;

    @ManyToOne
    @JoinColumn(name = "CROP_TYPE_ID")
    @JsonIgnore
    private Crop cropType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "impactedCrop", cascade = CascadeType.ALL)
    public List<CompanionRecommendation> companionRecommendations = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "impactedCrop", cascade = CascadeType.ALL)
    public List<RotationRecommendation> rotationRecommendations = new ArrayList<>();

    public ConcreteCrop()
    {
        if(id == null)
        id = java.util.UUID.randomUUID().toString();
    }

    /**
     * Initializes a new ConcreteCrop instance.
     *
     * @param type The abstract type of the crop.
     */
    public ConcreteCrop(Crop type)
    {
        id = java.util.UUID.randomUUID().toString();
        cropType = type;
    }

    public GardenContent getGarden()
    {
        return gardenContent;
    }

    public void setGarden(GardenContent gardenContent)
    {
        this.gardenContent = gardenContent;
    }


    public String getId()
    {
        return id;
    }

    public void  setId(String id)
    {
        this.id = id;
    }

    public Integer getCropTypeId()
    {
        return cropTypeId;
    }

    public void setCropTypeId(Integer cropTypeId)
    {
        this.cropTypeId = cropTypeId;
    }

    public int getStartX()
    {
        return startX;
    }

    public void setStartX(int startX)
    {
        this.startX = startX;
    }

    public int getStartY()
    {
        return startY;
    }

    public void setStartY(int startY)
    {
        this.startY = startY;
    }

    public int getEndX()
    {
        return endX;
    }

    public void setEndX(int endX)
    {
        this.endX = endX;
    }

    public int getEndY()
    {
        return endY;
    }

    public void setEndY(int endY)
    {
        this.endY = endY;
    }

    public Short getPreferenceValue()
    {
        return preferenceValue;
    }

    public Crop getCropType()
    {
        return cropType;
    }

    void addCompanionRecommendation(ConcreteCrop crop)
    {
        CompanionRecommendation rec = null;
        if(cropType.getAvoids().stream().anyMatch(a -> a.getId().equals(crop.getCropType().getId())))
        {
            rec = new CompanionRecommendation();
            rec.value = -6;
            rec.impacterId = crop.id;
            rec.otherCropId = crop.getCropTypeId();
            //lets build a stringo
            rec.reason = "";
        }
        else if(cropType.getHelpedBy().stream().anyMatch(a -> a.getId().equals(crop.getCropType().getId())))
        {
            rec = new CompanionRecommendation();
            rec.value = 6;
            rec.impacterId = crop.id;
            rec.otherCropId = crop.getCropTypeId();
            //lets build a stringo
            rec.reason = "";
        }
        if(rec != null)
        {
            companionRecommendations.add(rec);
        }
    }

    void addRotationRecommendation(ConcreteCrop crop, int contentId)
    {
        if(crop == null)
            return;
        RotationRecommendation rec = rotationRecommendations.stream().filter(r -> r.impacterId.equals(crop.getId())).findFirst().orElse(null);
        boolean isNew = false;
        if(rec == null)
        {
            isNew = true;
            rec = new RotationRecommendation();
        }
        if(this.cropType.getType() == crop.cropType.getType())
        {
            rec.value += -1;
            if(isNew)
            {
                rec.impacterId = crop.getId();
                rec.otherContentId = contentId;
                rec.reason = "";
            }
        }
        else if( (Crop.cropCylcle.indexOf(this.cropType.getType()) - 1 == Crop.cropCylcle.indexOf(crop.getCropType().getType())) || ( Crop.cropCylcle.indexOf(this.cropType.getType()) == 0 && Crop.cropCylcle.indexOf(crop.getCropType().getType()) == Crop.cropCylcle.size() - 1 ))
        {
            rec.value += 1;
            if (isNew)
            {
                rec.impacterId = crop.getId();
                rec.otherContentId = contentId;
                rec.reason = "";
            }
        }
        if(isNew && rec.value != 0)
        {
            rotationRecommendations.add(rec);
        }

    }

}