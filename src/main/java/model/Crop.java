package model;

import java.util.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Crop {

    @XmlID
    private String id = UUID.randomUUID().toString();

    private String name;

    private String binomialName;

    private String Description;

    private SunType sunRequirement;

    private String sowingMethod;

    private Double spread;

    private Double rowSpacing;

    private Double height;



    @XmlIDREF
    private List<Crop> helps = new ArrayList<>();

    @XmlIDREF
    private List<Crop> helpedBy = new ArrayList<>();

    @XmlIDREF
    private List<Crop> avoid = new ArrayList<>();

    public Crop()
    {}

    public String getId() {
        return id;
    }

    public  void setId(String id) {
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

    public Double getSpread() {
        return spread;
    }

    public void setSpread(Double spread) {
        this.spread = spread;
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