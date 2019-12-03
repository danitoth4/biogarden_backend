package web.controllers;

import Misc.Cache;
import model.*;
import model.repositories.ConcreteCropRepository;
import model.repositories.CropRepository;
import model.repositories.GardenContentRepository;
import model.repositories.GardenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import web.errorhandling.ContentNotFoundException;
import web.errorhandling.CropNotFoundException;
import web.errorhandling.GardenNotFoundException;

import java.util.*;

@RestController
public class PlantingController {

    private final GardenContentRepository gardenContentRepository;

    private final ConcreteCropRepository concreteCropRepository;

    private final CropRepository cropRepository;

    public PlantingController(GardenContentRepository gardenContentRepository, ConcreteCropRepository concreteCropRepository, CropRepository cropRepository)
    {
        this.gardenContentRepository = gardenContentRepository;
        this.concreteCropRepository = concreteCropRepository;
        this.cropRepository = cropRepository;
    }

    @GetMapping("/planting/{contentId}")
    public Collection<ConcreteCrop> getPlantedCrops(@PathVariable("contentId") int id, @RequestParam String zoom, @RequestParam String startX, @RequestParam String startY, @RequestParam String endX, @RequestParam String endY, @AuthenticationPrincipal Jwt jwt)
    {
        float zoomValue = Float.parseFloat(zoom);
        int x1 = Integer.parseInt(startX), y1 = Integer.parseInt(startY), x2 = Integer.parseInt(endX), y2 = Integer.parseInt(endY);
        return gardenContentRepository.findByIdAndUserId(id, jwt.getSubject()).orElseThrow(() -> new ContentNotFoundException(id)).getPlantedCropsList(zoomValue, x1, y1, x2, y2);
    }

    @PostMapping("/planting/{contentId}")
    public ResponseEntity<Collection<ConcreteCrop>> postCrops(@RequestBody PlantingOperation newPlant, @PathVariable("contentId") int id, @RequestParam String zoom, @RequestParam String startX, @RequestParam String startY, @RequestParam String endX, @RequestParam String endY, @AuthenticationPrincipal Jwt jwt)
    {
        Crop newCrop = cropRepository.findById(newPlant.getCropId()).orElseThrow(() -> new CropNotFoundException(newPlant.getCropId()));
        newPlant.setCrop(newCrop);
        GardenContent gardenContent = gardenContentRepository.findByIdAndUserId(id, jwt.getSubject()).orElseThrow(() -> new ContentNotFoundException(id));
        float zoomValue = Float.parseFloat(zoom);
        int x1 = Integer.parseInt(startX), y1 = Integer.parseInt(startY), x2 = Integer.parseInt(endX), y2 = Integer.parseInt(endY);
        if(gardenContent.plantCrop(newPlant, zoomValue))
        {
            gardenContentRepository.save(gardenContent);
            Cache.tryStoreGardeninCache(id, gardenContent.getPlantedCrops());
            return new ResponseEntity<>(gardenContent.getPlantedCropsList(zoomValue, x1, y1, x2, y2), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/planting/{id}")
    public ResponseEntity<Collection<ConcreteCrop>> deleteCrops(@RequestBody PlantingOperation deletedPlants, @PathVariable("id") int id,@RequestParam String zoom, @RequestParam String startX, @RequestParam String startY, @RequestParam String endX, @RequestParam String endY, @AuthenticationPrincipal Jwt jwt)
    {
        GardenContent gardenContent = gardenContentRepository.findByIdAndUserId(id, jwt.getSubject()).orElseThrow(() -> new ContentNotFoundException(id));
        float zoomValue = Float.parseFloat(zoom);
        int x1 = Integer.parseInt(startX), y1 = Integer.parseInt(startY), x2 = Integer.parseInt(endX), y2 = Integer.parseInt(endY);
        gardenContent.deleteCrops(deletedPlants, zoomValue);
        gardenContentRepository.save(gardenContent);
        return new ResponseEntity<>(gardenContent.getPlantedCropsList(zoomValue, x1, y1, x2, y2), HttpStatus.OK);
    }
}
