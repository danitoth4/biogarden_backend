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
        Garden garden = ModelManager.getInstance().getCurrentGarden();
        return garden.getPlantedCrops().values();
    }

    @PostMapping("/planting")
    public ResponseEntity<List<ConcreteCrop>> postCrops(@RequestBody PlantingOperation newPlant)
    {
        if(!newPlant.getMethod().equals(UpdateMethod.ADDED))
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    @DeleteMapping("/planting")
    public ResponseEntity<List<ConcreteCrop>> deleteCrops(@RequestBody PlantingOperation deletedPlants)
    {
        if(!deletedPlants.getMethod().equals(UpdateMethod.DELETED))
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
}
