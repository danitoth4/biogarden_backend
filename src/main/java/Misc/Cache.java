package Misc;

import java.awt.*;
import java.util.HashMap;

public class Cache
{
    private static HashMap<Integer, HashMap<Point, String>> gardenCache;

    public static HashMap<Point, String> getCachedInstance(int id)
    {
        if(gardenCache == null)
            gardenCache = new HashMap<>();
        return gardenCache.get(id);
    }

    public static boolean tryStoreGardeninCache(int id, HashMap<Point, String> map)
    {
        if(hasFreeSpace() && id != 0)
        {
            gardenCache.put(id, map);
            return true;
        }
        return false;
    }

    private static boolean hasFreeSpace()
    {
        return true;
    }
}
