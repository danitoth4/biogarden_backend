package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.*;

import static model.CropType.*;

@Entity
@Table( name = "Crops", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "user_id"})})
public class Crop
{

    public static ArrayList<CropType> cropCylcle = new ArrayList<>(Arrays.asList(LEAF, FRUIT, ROOT, LEGUMES));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Length(max = 1000)
    private String description;

    @Min(value = 1, message = "All crops must have at least 5cm diameter")
    private int diameter;

    @URL
    private String imageUrl;

    private CropType type;

    @NotBlank
    @Column(name = "user_id")
    private String userId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "impacting")
    @JsonIgnore
    private Set<Companion> impacts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "impacted")
    @JsonIgnore
    private Set<Companion> impactedBy = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cropType")
    @JsonIgnore
    private List<ConcreteCrop> concreteCrops = new LinkedList<>();

    public Crop()
    { }

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
        this.description = description;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crop crop = (Crop) o;
        return id == crop.id &&
                diameter == crop.diameter &&
                name.equals(crop.name) &&
                Objects.equals(description, crop.description) &&
                imageUrl.equals(crop.imageUrl) &&
                type == crop.type &&
                Objects.equals(userId, crop.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, diameter, imageUrl, type, userId);
    }
}