package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "ConcreteCrops")
public class ConcreteCrop
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "impactedCrop", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Recommendation> recommendations = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "impacterCrop", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Recommendation> impactedRecommendations = new ArrayList<>();


    public ConcreteCrop()
    {

    }
    /**
     * Initializes a new ConcreteCrop instance.
     *
     * @param type The abstract type of the crop.
     */
    public ConcreteCrop(Crop type)
    {
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
        short prefValue = 0;
        for(Recommendation recommendation : recommendations)
        {
            prefValue += recommendation.value;
        }
        return prefValue;
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
            rec = createCompanionRecommendation(crop, false);
        }
        else if(cropType.getHelpedBy().stream().anyMatch(a -> a.getId().equals(crop.getCropType().getId())))
        {
            rec = createCompanionRecommendation(crop, true);
        }
        if(rec != null)
        {
            crop.recommendations.add(rec);
            recommendations.add(rec);
        }
    }

    private CompanionRecommendation createCompanionRecommendation(ConcreteCrop other, boolean isPositive)
    {
        CompanionRecommendation rec = new CompanionRecommendation();
        rec.value = isPositive ? 6 : -6;
        rec.impactedCrop = this;
        rec.impacterCrop = other;
        rec.otherCropId = other.getCropTypeId();
        rec.reason = String.format("%s is a %s companion for %s", other.getCropType().getName(), isPositive ? "good" : "bad", this.getCropType().getName());
        return rec;
    }

    void addRotationRecommendation(ConcreteCrop crop, int contentId)
    {
        if(crop == null)
            return;
        //
        RotationRecommendation rec = (RotationRecommendation)recommendations.stream().filter(r -> r.impacterCrop.id.equals(crop.getId())).findFirst().orElse(null);
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
                rec.impactedCrop = this;
                rec.impacterCrop = crop;
                rec.otherContentId = contentId;
                rec.reason = "";
            }
        }
        else if( (Crop.cropCylcle.indexOf(this.cropType.getType()) - 1 == Crop.cropCylcle.indexOf(crop.getCropType().getType())) || ( Crop.cropCylcle.indexOf(this.cropType.getType()) == 0 && Crop.cropCylcle.indexOf(crop.getCropType().getType()) == Crop.cropCylcle.size() - 1 ))
        {
            rec.value += 1;
            if (isNew)
            {
                rec.impactedCrop = this;
                rec.impacterCrop = crop;
                rec.otherContentId = contentId;
                rec.reason = "";
            }
        }
        if(isNew && rec.value != 0)
        {
            crop.recommendations.add(rec);
            recommendations.add(rec);
        }

    }

}