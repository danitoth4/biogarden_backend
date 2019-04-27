package model;

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
    private Short id;

    private Integer length;

    private Integer width;

    private Short preferenceValue;

    private Point startPoint;

    private Point endPoint;

    @ManyToOne
    private final Crop cropType;

    /**
     * Initializes a new ConcreteCrop instance.
     *
     * @param type The abstract type of the crop.
     */
    public ConcreteCrop(Crop type)
    {
        cropType = type;
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
}