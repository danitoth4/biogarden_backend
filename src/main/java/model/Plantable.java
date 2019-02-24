package model;

import java.util.Map;

public interface Plantable {

    public int plant(Map<Direction ,PlantedCrop> neighbours);

    public double getWidth();

    public double getLength();
}