package model;

import java.util.Map;

public interface Plantable {

    public int plant(Map<Direction , ConcreteCrop> neighbours);

    public double getWidth();

    public double getLength();
}