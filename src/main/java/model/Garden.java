package model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.*;


@Entity
public class Garden
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Min(value = 1, message = "Garden length must be a positive value")
    @Max(value = 2000, message = "This version only supports gardens that are not longer than 100m")
    private Integer length;

    @Min(value = 1, message = "Garden width must be a positive value")
    @Max(value = 2000, message = "This version only supports gardens that are not wider than 100m")
    private Integer width;

    private String userId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "garden", cascade = CascadeType.ALL)
    private List<GardenContent> gardenContents = new ArrayList<>();

    /**
     *
     */
    public Garden()
    {
    }

    /**
     * @param l Length of the garden
     * @param w Width of the garden
     * @param userId Id of the user who owns the garden
     */
    public Garden(int l, int w, String userId)
    {
        length = l;
        width = w;
        this.userId = userId;
        GardenContent def = new GardenContent(this, "Default");
        this.gardenContents.add(def);
    }

    //region Getters and Setters
    public int getId()
    {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getLength()
    {
        return length;
    }

    public Integer getWidth()
    {
        return width;
    }

    public List<GardenContent> getGardenContents()
    {
        return gardenContents;
    }

    public void setGardenContent(List<GardenContent> gardenContents)
    {
        this.gardenContents = gardenContents;
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
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Garden garden = (Garden) o;
        return id == garden.id &&
                Objects.equals(name, garden.name) &&
                length.equals(garden.length) &&
                width.equals(garden.width) &&
                userId.equals(garden.userId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, length, width, userId);
    }
}