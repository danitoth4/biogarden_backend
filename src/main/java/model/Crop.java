package model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.*;

@Entity
@Table( name = "Crops")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property= "id")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Short id;

    private String name;

    private String binomialName;

    private String Description;

    private SunType sunRequirement;

    private String sowingMethod;

    private Float diameter;

    private Float rowSpacing;

    private Float height;

    private String imageUrl;


    @ManyToMany
    @JsonIgnore
    private Set<Crop> helps = new HashSet<>();

    @ManyToMany(mappedBy = "helps")
    @JsonIgnore
    private Set<Crop> helpedBy = new HashSet<>();

    @ManyToMany
    @JsonIgnore
    private Set<Crop> avoids = new HashSet<>();

    @ManyToMany(mappedBy = "avoids")
    @JsonIgnore
    private Set<Crop> avoidedBy = new HashSet<>();


    //private Set<Integer> avoidedList;

    public Crop()
    {

    }


    public Short getId() {
        return id;
    }

    public  void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBinomialName() {
        return binomialName;
    }

    public void setBinomialName(String binomialName) {
        this.binomialName = binomialName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public SunType getSunRequirement() {
        return sunRequirement;
    }

    public void setSunRequirement(SunType sunRequirement) {
        this.sunRequirement = sunRequirement;
    }

    public String getSowingMethod() {
        return sowingMethod;
    }

    public void setSowingMethod(String sowingMethod) {
        this.sowingMethod = sowingMethod;
    }

    public Float getDiameter() {
        return diameter;
    }

    public void setDiameter(Float diameter) {
        this.diameter = diameter;
    }

    public Float getRowSpacing() {
        return rowSpacing;
    }

    public void setRowSpacing(Float rowSpacing) {
        this.rowSpacing = rowSpacing;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Set<Crop> getHelps() {
        return helps;
    }

    public void setHelps(Set<Crop> helps) {
        this.helps = helps;
    }

    public Set<Crop> getHelpedBy() {
        return helpedBy;
    }

    public void setHelpedBy(Set<Crop> helpedBy) {
        this.helpedBy = helpedBy;
    }

    public Set<Crop> getAvoids() { return avoids;}

    public void setAvoids(Set<Crop> avoids) {
        this.avoids = avoids;
    }


    public Set<Crop> getAvoidedBy()
    {
        return avoidedBy;
    }

    public void setAvoidedBy(Set<Crop> avoidedBy)
    {
        this.avoidedBy = avoidedBy;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}