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
    public Collection<ConcreteCrop> getPlantedCrops(@PathVariable("id") int id)
    {
        return gardenRepository.findById(id).orElseThrow(() -> new GardenNotFoundException(id)).getPlantedCropsList();
    }

    @PostMapping("/planting/{id}")
    public ResponseEntity<Collection<ConcreteCrop>> postCrops(@RequestBody PlantingOperation newPlant, @PathVariable("id") int id)
    {
        Garden garden = gardenRepository.findById(id).orElseThrow(() -> new GardenNotFoundException(id));
        if(garden.plantCrop(newPlant, concreteCropRepository))
        {
            gardenRepository.save(garden);
            Cache.tryStoreGardeninCache(garden.getId(), garden.getPlantedCrops());
            return new ResponseEntity<>(garden.getPlantedCropsList(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/planting/{id}")
    public ResponseEntity<Collection<ConcreteCrop>> deleteCrops(@RequestBody PlantingOperation deletedPlants, @PathVariable("id") int id)
    {
        Garden garden = gardenRepository.findById(id).orElseThrow(() -> new GardenNotFoundException(id));
        if(garden.deleteCrops(deletedPlants))
        {
            gardenRepository.save(garden);
            Cache.tryStoreGardeninCache(garden.getId(), garden.getPlantedCrops());
            return new ResponseEntity<>(garden.getPlantedCropsList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
