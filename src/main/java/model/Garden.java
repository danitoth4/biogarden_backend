package model;

import Misc.Cache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import Misc.Grid;
import web.errorhandling.GardenException;


@Entity
public class Garden
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private Integer length;

    private Integer width;

    private ArrayList<GardenContent> gardenContents = new ArrayList<>();

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
        GardenContent def = new GardenContent(l ,w, "Default");
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

    public ArrayList<GardenContent> getGardenContents()
    {
        return gardenContents;
    }

    public void setGardenContent(ArrayList<GardenContent> gardenContents)
    {
        this.gardenContents = gardenContents;
    }

}