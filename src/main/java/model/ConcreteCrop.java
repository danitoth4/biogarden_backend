package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class ConcreteCrop
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

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
    private List<Recommendation> recommendations = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "impacterCrop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommendation> impactedRecommendations = new ArrayList<>();


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

    //region Getters and Setters
    public GardenContent getGardenContent()
    {
        return gardenContent;
    }

    public void setGardenContent(GardenContent gardenContent)
    {
        this.gardenContent = gardenContent;
    }


    public int getId()
    {
        return id;
    }

    public void  setId(int id)
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
            prefValue += recommendation.getValue();
        }
        return prefValue;
    }

    public Crop getCropType()
    {
        return cropType;
    }

    public void setCropType(Crop cropType)
    {
        this.cropType = cropType;
    }
    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcreteCrop that = (ConcreteCrop) o;
        return id == that.id &&
                startX == that.startX &&
                startY == that.startY &&
                endX == that.endX &&
                endY == that.endY &&
                this.getPreferenceValue().equals(that.getPreferenceValue()) &&
                cropTypeId.equals(that.cropTypeId) &&
                cropType.equals(that.cropType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, preferenceValue, startX, startY, endX, endY, cropTypeId, cropType);
    }

    void addCompanionRecommendation(ConcreteCrop crop)
    {
        CompanionRecommendation rec = null;
        Companion companion = cropType.getImpactedBy().stream().filter(comp -> comp.getImpacting().getId().equals(crop.getCropType().getId())).findAny().orElse(null);
        if(companion != null)
        {
            rec = new CompanionRecommendation();
            rec.setValue(companion.getPositive() ? 6 : -6);
            rec.setImpactedCrop(this);
            rec.setImpacterCrop(crop);
            rec.setReason(String.format("%s is a %s companion for %s", crop.getCropType().getName(), companion.getPositive() ? "good" : "bad", this.getCropType().getName()));
            rec.setCompanion(companion);
        }
        if(rec != null)
        {
            recommendations.add(rec);
        }
    }

    void addRotationRecommendation(ConcreteCrop crop, int contentId)
    {
        if(crop == null)
            return;
        //
        RotationRecommendation rec = (RotationRecommendation)recommendations.stream().filter(r -> r.getImpacterCrop().getId() == crop.getId()).findFirst().orElse(null);
        boolean isNew = false;
        if(rec == null)
        {
            isNew = true;
            rec = new RotationRecommendation();
        }
        if(this.cropType.getType() == crop.cropType.getType())
        {
            rec.setValue(rec.getValue() - 1);
            if(isNew)
            {
                rec.setImpactedCrop(this);
                rec.setImpacterCrop(crop);
                rec.setReason("");
            }
        }
        else if( (Crop.cropCylcle.indexOf(this.cropType.getType()) - 1 == Crop.cropCylcle.indexOf(crop.getCropType().getType())) || ( Crop.cropCylcle.indexOf(this.cropType.getType()) == 0 && Crop.cropCylcle.indexOf(crop.getCropType().getType()) == Crop.cropCylcle.size() - 1 ))
        {
            rec.setValue(rec.getValue() + 1);
            if (isNew)
            {
                rec.setImpactedCrop(this);
                rec.setImpacterCrop(crop);
                rec.setReason("");
            }
        }
        if(isNew && rec.getValue() != 0)
        {
            crop.recommendations.add(rec);
            recommendations.add(rec);
        }
    }
}