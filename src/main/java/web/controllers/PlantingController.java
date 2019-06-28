package web.controllers;

import model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.Util.UpdateMethod;
import java.util.*;

@RestController
public class PlantingController {

    @GetMapping("/planting")
    public Collection<ConcreteCrop> getPlantedCrops()
    {
        return ModelManager.getInstance().getCurrentGarden().getPlantedCropsList();
    }

    @PostMapping("/planting")
    public ResponseEntity<Collection<ConcreteCrop>> postCrops(@RequestBody PlantingOperation newPlant)
    {
        if(ModelManager.getInstance().getCurrentGarden().plantCrop(newPlant))
        {
            return new ResponseEntity<>(ModelManager.getInstance().getCurrentGarden().getPlantedCrops().values(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/planting")
    public ResponseEntity<Collection<ConcreteCrop>> deleteCrops(@RequestBody PlantingOperation deletedPlants)
    {
        if(ModelManager.getInstance().getCurrentGarden().deleteCrops(deletedPlants))
        {
            return new ResponseEntity<>(ModelManager.getInstance().getCurrentGarden().getPlantedCrops().values(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
