package model;

import java.awt.geom.Point2D;
import java.util.*;

public class ConcreteCrop {

    private double length;

    private double width;

    private int preferenceValue;

    private Point2D coordinates;

    private final Crop cropType;

    public ConcreteCrop(Crop type)
    {
        cropType = type;
    }

}