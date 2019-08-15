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
    public Collection<ConcreteCrop> getPlantedCrops(@PathVariable("id") int id, @RequestParam String zoomS)
    {
        double zoom = Double.parseDouble(zoomS);
        return gardenRepository.findById(id).orElseThrow(() -> new GardenNotFoundException(id)).getPlantedCropsList(zoom);
    }

    @PostMapping("/planting/{id}")
    public ResponseEntity<Collection<ConcreteCrop>> postCrops(@RequestBody PlantingOperation newPlant, @PathVariable("id") int id, @RequestParam String zoomS)
    {
        Garden garden = gardenRepository.findById(id).orElseThrow(() -> new GardenNotFoundException(id));
        double zoom = Double.parseDouble(zoomS);
        if(garden.plantCrop(newPlant, zoom))
        {
            gardenRepository.save(garden);
            Cache.tryStoreGardeninCache(garden.getId(), garden.getPlantedCrops());
            return new ResponseEntity<>(garden.getPlantedCropsList(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/planting/{id}")
    public ResponseEntity<Collection<ConcreteCrop>> deleteCrops(@RequestBody PlantingOperation deletedPlants, @PathVariable("id") int id, @RequestParam String zoomS)
    {
        Garden garden = gardenRepository.findById(id).orElseThrow(() -> new GardenNotFoundException(id));
        double zoom = Double.parseDouble(zoomS);
        if(garden.deleteCrops(deletedPlants, zoom))
        {
            gardenRepository.save(garden);
            Cache.tryStoreGardeninCache(garden.getId(), garden.getPlantedCrops());
            return new ResponseEntity<>(garden.getPlantedCropsList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
