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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "impacting")
    @JsonIgnore
    private Set<Companion> impacts = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "impacted")
    @JsonIgnore
    private Set<Companion> impactedBy = new HashSet<>();

    public Crop()
    {

    }

    //region Getters and Setters
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

    public Set<Companion> getImpacts()
    {
        return impacts;
    }

    public void setImpacts(Set<Companion> impacts)
    {
        this.impacts = impacts;
    }

    public boolean addToImpacts(Companion companion)
    {
        return this.impacts.add(companion);
    }

    public Set<Companion> getImpactedBy()
    {
        return impactedBy;
    }

    public void setImpactedBy(Set<Companion> impactedBy)
    {
        this.impactedBy = impactedBy;
    }

    public boolean addToImpactedBy(Companion companion)
    {
        return this.impactedBy.add(companion);
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    //endregion
}