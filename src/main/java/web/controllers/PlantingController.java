package web.controllers;

import Misc.Cache;
import model.*;
import model.repositories.ConcreteCropRepository;
import model.repositories.GardenContentRepository;
import model.repositories.GardenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.errorhandling.ContentNotFoundException;
import web.errorhandling.GardenNotFoundException;

import java.util.*;

@RestController
public class PlantingController {

    private final GardenContentRepository gardenContentRepository;

    private final ConcreteCropRepository concreteCropRepository;

    public PlantingController(GardenContentRepository gardenContentRepository, ConcreteCropRepository concreteCropRepository)
    {
        this.gardenContentRepository = gardenContentRepository;
        this.concreteCropRepository = concreteCropRepository;
    }

    @GetMapping("/planting/{contentId}")
    public Collection<ConcreteCrop> getPlantedCrops(@PathVariable("contentId") int id, @RequestParam String zoom, @RequestParam String startX, @RequestParam String startY, @RequestParam String endX, @RequestParam String endY)
    {
        float zoomValue = Float.parseFloat(zoom);
        int x1 = Integer.parseInt(startX), y1 = Integer.parseInt(startY), x2 = Integer.parseInt(endX), y2 = Integer.parseInt(endY);
        return gardenContentRepository.findById(id).orElseThrow(() -> new ContentNotFoundException(id)).getPlantedCropsList(zoomValue, x1, y1, x2, y2);
    }

    @PostMapping("/planting/{contentId}")
    public ResponseEntity<Collection<ConcreteCrop>> postCrops(@RequestBody PlantingOperation newPlant, @PathVariable("contentId") int id, @RequestParam String zoom, @RequestParam String startX, @RequestParam String startY, @RequestParam String endX, @RequestParam String endY)
    {
        GardenContent gardenContent = gardenContentRepository.findById(id).orElseThrow(() -> new ContentNotFoundException(id));
        float zoomValue = Float.parseFloat(zoom);
        int x1 = Integer.parseInt(startX), y1 = Integer.parseInt(startY), x2 = Integer.parseInt(endX), y2 = Integer.parseInt(endY);
        if(gardenContent.plantCrop(newPlant, zoomValue))
        {
            gardenContentRepository.save(gardenContent);
            Cache.tryStoreGardeninCache(id, gardenContent.plantedCrops);
            return new ResponseEntity<>(gardenContent.getPlantedCropsList(zoomValue, x1, y1, x2, y2), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/planting/{id}")
    public ResponseEntity<Collection<ConcreteCrop>> deleteCrops(@RequestBody PlantingOperation deletedPlants, @PathVariable("id") int id,@RequestParam String zoom, @RequestParam String startX, @RequestParam String startY, @RequestParam String endX, @RequestParam String endY)
    {
        GardenContent gardenContent = gardenContentRepository.findById(id).orElseThrow(() -> new ContentNotFoundException(id));
        float zoomValue = Float.parseFloat(zoom);
        int x1 = Integer.parseInt(startX), y1 = Integer.parseInt(startY), x2 = Integer.parseInt(endX), y2 = Integer.parseInt(endY);
        if(gardenContent.deleteCrops(deletedPlants, zoomValue))
        {
            gardenContentRepository.save(gardenContent);
            Cache.tryStoreGardeninCache(id, gardenContent.plantedCrops);
            return new ResponseEntity<>(gardenContent.getPlantedCropsList(zoomValue, x1, y1, x2, y2), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
