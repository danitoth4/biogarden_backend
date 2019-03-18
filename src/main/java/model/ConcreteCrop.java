package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.awt.*;
import java.util.*;


@Entity
@Table( name = "ConcreteCrops")
public class ConcreteCrop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Short id;

    private Integer length;

    private Integer width;

    private Short preferenceValue;

    private Point startPoint;

    private final Crop cropType;

    /**
     * Initializes a new ConcreteCrop instance.
     * @param type The abstract type of the crop.
     */
    public ConcreteCrop(Crop type)
    {
        cropType = type;
    }

}