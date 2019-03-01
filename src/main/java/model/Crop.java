package model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table( name = "Crops")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String binomialName;

    private String Description;

    private SunType sunRequirement;

    private String sowingMethod;

    private Double diameter;

    private Double rowSpacing;

    private Double height;


    private List<Crop> helps = new ArrayList<>();

    private List<Crop> helpedBy = new ArrayList<>();

    private List<Crop> avoid = new ArrayList<>();

    public Crop()
    {

    }

    public Long getId() {
        return id;
    }

    public  void setId(Long id) {
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

    public Double getDiameter() {
        return diameter;
    }

    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }

    public Double getRowSpacing() {
        return rowSpacing;
    }

    public void setRowSpacing(Double rowSpacing) {
        this.rowSpacing = rowSpacing;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public List<Crop> getHelps() {
        return helps;
    }

    public void setHelps(List<Crop> helps) {
        this.helps = helps;
    }

    public List<Crop> getHelpedBy() {
        return helpedBy;
    }

    public void setHelpedBy(List<Crop> helpedBy) {
        this.helpedBy = helpedBy;
    }

    public List<Crop> getAvoid() {
        return avoid;
    }

    public void setAvoid(List<Crop> avoid) {
        this.avoid = avoid;
    }

}