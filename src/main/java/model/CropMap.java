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


    /**
     * This could be an improvement later...
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return
     */
    public ConcreteCrop getSummedCropForSpace(int x1, int x2, int y1, int y2)
    {
        Set<ConcreteCrop> all = this.getCropsOnSpace(x1, x2, y1, y2);
        if(all.size() == 0)
            return null;
        ConcreteCrop concreteCrop = new ConcreteCrop();
        for(ConcreteCrop cc : all)
        {
            if(concreteCrop.getCropType() == null)
                concreteCrop = cc;
            else
                if(!concreteCrop.getCropType().equals(cc.getCropType()) && !concreteCrop.getCropType().getName().equals("Mix"))
                {
                    Crop mix = new Crop();
                    mix.setDescription("This represents multiple crop types on an area");
                    mix.setName("Mix");
                    mix.setId(-1);
                    concreteCrop.setCropType(mix);
                }
                concreteCrop.setPreferenceValue(concreteCrop.getPreferenceValue() + cc.getPreferenceValue());
        }
        concreteCrop.setStartX(x1);
        concreteCrop.setStartY(y1);
        concreteCrop.setEndX(x2);
        concreteCrop.setEndY(y2);
        return concreteCrop;
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
