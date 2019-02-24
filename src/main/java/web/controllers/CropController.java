package web.controllers;

import model.ModelManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import model.*;
import java.util.ArrayList;

@RestController
public class CropController
{
    @RequestMapping("/crop")
    public ArrayList<Crop> getCrops()
    {
        ArrayList<Crop> crops = new ArrayList<Crop>();
        return crops;
    }

    @RequestMapping("/crop/{name}")
    public Crop getCrop(@PathVariable("name") String name)
    {
        return ModelManager.getInstance().getData(name);
    }
}