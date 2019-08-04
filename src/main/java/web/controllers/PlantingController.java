package web.controllers;

import model.*;
import model.repositories.GardenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.errorhandling.GardenNotFoundException;

import java.util.*;

@RestController
public class PlantingController {

    private final GardenRepository repository;

    public PlantingController(GardenRepository gardenRepository)
    {
        this.repository = gardenRepository;
    }

    @GetMapping("/planting/{id}")
    public Collection<ConcreteCrop> getPlantedCrops(@PathVariable("id") int id)
    {
        return repository.findById(id).orElseThrow(() -> new GardenNotFoundException(id)).getPlantedCropsList();
    }

    @PostMapping("/planting/{id}")
    public ResponseEntity<Collection<ConcreteCrop>> postCrops(@RequestBody PlantingOperation newPlant, @PathVariable("id") int id)
    {
        Garden garden = repository.findById(id).orElseThrow(() -> new GardenNotFoundException(id));
        if(garden.plantCrop(newPlant))
        {
            return new ResponseEntity<>(garden.getPlantedCrops().values(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/planting/{id}")
    public ResponseEntity<Collection<ConcreteCrop>> deleteCrops(@RequestBody PlantingOperation deletedPlants, @PathVariable("id") int id)
    {
        Garden garden = repository.findById(id).orElseThrow(() -> new GardenNotFoundException(id));
        if(garden.deleteCrops(deletedPlants))
        {
            return new ResponseEntity<>(garden.getPlantedCrops().values(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
