package model;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CropMap
{
    private HashMap<Point, ConcreteCrop> concreteCropMap = new HashMap<>();

    public Set<ConcreteCrop> getCropsOnSpace(int x1, int x2, int y1, int y2)
    {
        Set<ConcreteCrop> set = new HashSet<>();
        for(int i = x1; i < x2; ++i)
            for(int j = y1; j < y2; ++j)
            {
                ConcreteCrop cc = concreteCropMap.get(new Point(i, j));
                if(cc != null)
                    set.add(cc);
            }
        return set;
    }

    public HashMap<Point, ConcreteCrop> getConcreteCropMap()
    {
        return concreteCropMap;
    }

    public void setConcreteCropMap(HashMap<Point, ConcreteCrop> concreteCropMap)
    {
        this.concreteCropMap = concreteCropMap;
    }
}
