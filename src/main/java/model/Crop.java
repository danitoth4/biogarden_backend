package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.*;

import static model.CropType.*;

@Entity
@Table( name = "Crops")
public class Crop
{


    public static ArrayList<CropType> cropCylcle = new ArrayList<>(Arrays.asList(LEAF, FRUIT, ROOT, LEGUMES));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    private String name;

    @Length(max = 1000)
    private String description;

    private int diameter;

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

    public Crop(Crop other)
    {
        this.name = other.name;
        this.description = other.description;
        this.diameter = other.diameter;
        this.imageUrl = other.imageUrl;
        this.type = other.type;
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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }


    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
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