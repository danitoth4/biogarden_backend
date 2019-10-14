package model;

import javax.persistence.*;
import java.util.*;


@Entity
public class Garden
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private Integer length;

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
     * @param l
     * @param w
     */
    public Garden(int l, int w)
    {
        length = l;
        width = w;
        GardenContent def = new GardenContent(this, "Default", this.userId);
        this.gardenContents.add(def);
    }

    public int getId()
    {
        return id;
    }

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

}