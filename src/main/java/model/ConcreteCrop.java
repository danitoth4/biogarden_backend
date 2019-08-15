package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.awt.*;


@Entity
@Table(name = "ConcreteCrops")
public class ConcreteCrop
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private Garden garden;

    private Short preferenceValue;

    private Point startPoint;

    private Point endPoint;

    @Column(name = "CROP_TYPE_ID", insertable = false, updatable = false)
    private Integer cropTypeId;

    @ManyToOne
    @JoinColumn(name = "CROP_TYPE_ID")
    @JsonIgnore
    private Crop cropType;


    public ConcreteCrop(){}

    /**
     * Initializes a new ConcreteCrop instance.
     *
     * @param type The abstract type of the crop.
     */
    public ConcreteCrop(Crop type)
    {
        cropType = type;
    }

    public Garden getGarden()
    {
        return garden;
    }

    public void setGarden(Garden garden)
    {
        this.garden = garden;
    }


    public Integer getId()
    {
        return id;
    }

    public Integer getCropTypeId()
    {
        return cropTypeId;
    }

    public void setCropTypeId(Integer cropId)
    {
        this.cropTypeId = cropTypeId;
    }

    public Short getPreferenceValue()
    {
        return preferenceValue;
    }

    public Point getStartPoint()
    {
        return startPoint;
    }

    public void setStartPoint(Point startPoint)
    {
        this.startPoint = startPoint;
    }


    public Point getEndPoint()
    {
        return endPoint;
    }

    public void setEndPoint(Point endPoint)
    {
        this.endPoint = endPoint;
    }

    public Crop getCropType()
    {
        return cropType;
    }

    /*
    @Override
    public boolean equals(Object o)
    {
        if(o.getClass() != ConcreteCrop.class)
            return false;
        ConcreteCrop other = (ConcreteCrop)o;
        return (this.id.equals(other.id) && this.startPoint.equals(other.startPoint) && this.endPoint.equals(other.endPoint) && this.garden.eq)
    }*/
}