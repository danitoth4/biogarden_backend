package model;

import java.util.*;

public class PlantedCrop extends Crop implements Plantable {

    private double length;

    private double width;

    private int preferenceValue;

    private Map<Direction, PlantedCrop> neighbours = new HashMap<>();

    public PlantedCrop(Crop base)
    {
        this.setId(base.getId());
        this.setName(base.getName());
        this.setBinomialName(base.getBinomialName());
        this.setDescription(base.getDescription());
        this.setSunRequirement(base.getSunRequirement());
        this.setSowingMethod(base.getSowingMethod());
        this.setSpread(base.getSpread());
        this.setHeight(base.getHeight());
        this.setRowSpacing(base.getRowSpacing());
        this.setAvoid(base.getAvoid());
        this.setHelps(base.getHelps());
        this.setHelpedBy(base.getHelpedBy());
    }

    @Override
    public int plant(Map<Direction, PlantedCrop> neighbours) {
        this.setNeighbours(neighbours);
        for(Direction d : Direction.values())
        {
            if(this.getAvoid().contains(neighbours.get(d)))
            {
                //trouble
                preferenceValue--;
                continue;
            }
            if(this.getHelpedBy().contains(neighbours.get(d)))
            {
                preferenceValue++;
                //happy
            }
            if(this.getHelps().contains(neighbours.get(d)))
            {
                //signal to other crop
            }
        }
        return preferenceValue;
    }

    public double getArea()
    {
        return width * length;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getPreferenceValue() {
        return preferenceValue;
    }

    public Map<Direction, PlantedCrop> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Map<Direction, PlantedCrop> neighbours) {
        this.neighbours = neighbours;
    }



}