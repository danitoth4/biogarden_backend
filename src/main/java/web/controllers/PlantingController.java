package web.controllers;

import Misc.Cache;
import model.*;
import model.repositories.ConcreteCropRepository;
import model.repositories.GardenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.errorhandling.GardenNotFoundException;

import java.util.*;

@RestController
public class PlantingController {

    private final GardenRepository gardenRepository;

    private final ConcreteCropRepository concreteCropRepository;

    public PlantingController(GardenRepository gardenRepository, ConcreteCropRepository concreteCropRepository)
    {
        this.gardenRepository = gardenRepository;
        this.concreteCropRepository = concreteCropRepository;
    }

    @GetMapping("/planting/{id}")
    public Collection<ConcreteCrop> getPlantedCrops(@PathVariable("id") int id, @RequestParam String zoom, @RequestParam String startX, @RequestParam String startY, @RequestParam String endX, @RequestParam String endY)
    {
        float zoomValue = Float.parseFloat(zoom);
        int x1 = Integer.parseInt(startX), y1 = Integer.parseInt(startY), x2 = Integer.parseInt(endX), y2 = Integer.parseInt(endY);
        return gardenRepository.findById(id).orElseThrow(() -> new GardenNotFoundException(id)).getPlantedCropsList(zoomValue, x1, y1, x2, y2);
    }

    @PostMapping("/planting/{id}")
    public ResponseEntity<Collection<ConcreteCrop>> postCrops(@RequestBody PlantingOperation newPlant, @PathVariable("id") int id, @RequestParam String zoom, @RequestParam String startX, @RequestParam String startY, @RequestParam String endX, @RequestParam String endY)
    {
        Garden garden = gardenRepository.findById(id).orElseThrow(() -> new GardenNotFoundException(id));
        float zoomValue = Float.parseFloat(zoom);
        int x1 = Integer.parseInt(startX), y1 = Integer.parseInt(startY), x2 = Integer.parseInt(endX), y2 = Integer.parseInt(endY);
        if(garden.plantCrop(newPlant, zoomValue))
        {
            gardenRepository.save(garden);
            Cache.tryStoreGardeninCache(garden.getId(), garden.getPlantedCrops());
            return new ResponseEntity<>(garden.getPlantedCropsList(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/planting/{id}")
    public ResponseEntity<Collection<ConcreteCrop>> deleteCrops(@RequestBody PlantingOperation deletedPlants, @PathVariable("id") int id,@RequestParam String zoom, @RequestParam String startX, @RequestParam String startY, @RequestParam String endX, @RequestParam String endY)
    {
        Garden garden = gardenRepository.findById(id).orElseThrow(() -> new GardenNotFoundException(id));
        float zoomValue = Float.parseFloat(zoom);
        int x1 = Integer.parseInt(startX), y1 = Integer.parseInt(startY), x2 = Integer.parseInt(endX), y2 = Integer.parseInt(endY);
        if(garden.deleteCrops(deletedPlants, zoomValue))
        {
            gardenRepository.save(garden);
            Cache.tryStoreGardeninCache(garden.getId(), garden.getPlantedCrops());
            return new ResponseEntity<>(garden.getPlantedCropsList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
