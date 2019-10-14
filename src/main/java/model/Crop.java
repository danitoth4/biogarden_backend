package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.*;

import static model.CropType.*;

@Entity
@Table( name = "Crops")
public class Crop {


    public static ArrayList<CropType> cropCylcle = new ArrayList<>(Arrays.asList(LEAF, FRUIT, ROOT, LEGUMES));

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Integer id;

    private String name;

    private String binomialName;

    @Length(max = 1000)
    private String Description;

    private String sowingMethod;

    private int diameter;

    private Float rowSpacing;

    private Float height;

    private String imageUrl;

    private CropType type;

    private String userId;

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

    public Crop()
    {

    }

    public Integer getId() {
        return id;
    }

    public  void setId(Integer id) {
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

    public String getSowingMethod() {
        return sowingMethod;
    }

    public void setSowingMethod(String sowingMethod) {
        this.sowingMethod = sowingMethod;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
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

    public CropType getType()
    {
        return type;
    }

    public void setType(CropType type)
    {
        this.type = type;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
}