package web.controllers;

import com.google.common.collect.Lists;
import model.repositories.CropRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import model.*;
import web.errorhandling.CropNotFoundException;
import java.util.List;

@RestController
public class CropController {

    private final CropRepository repository;

    public CropController(CropRepository cropRepository) {
        repository = cropRepository;
    }


    @RequestMapping("/crop")
    public List<Crop> getCrops(@AuthenticationPrincipal Jwt jwt)
    {
        Iterable<Crop> crops = repository.findAll();//ByUserId(jwt.getSubject());
        return Lists.newArrayList(crops);
    }

    @RequestMapping("/crop/{id}")
    public Crop getCrop(@PathVariable("id") int id, @AuthenticationPrincipal Jwt jwt)
    {
        return repository.findById(id).orElseThrow(() -> new CropNotFoundException(id));
    }

    @PutMapping("/crop/{id}")
    public ResponseEntity<Crop> putCrop(@RequestBody Crop crop, @PathVariable int id, @AuthenticationPrincipal Jwt jwt)
    {
        Crop updatedCrop = repository.findByIdAndUserId(id, jwt.getSubject()).orElse(null);
        if(updatedCrop == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        updatedCrop = crop;
        updatedCrop.setId(id);
        updatedCrop.setUserId(jwt.getSubject());
        return new ResponseEntity<>(repository.save(updatedCrop), HttpStatus.OK);
    }

    @PostMapping("/crop")
    public Crop postCrop(@RequestBody Crop crop, @AuthenticationPrincipal Jwt jwt)
    {
        crop.setUserId(jwt.getSubject());
        return repository.save(crop);
    }


    @DeleteMapping("/crop/{id}")
    public ResponseEntity deleteCrop(@PathVariable int id, @AuthenticationPrincipal Jwt jwt)
    {
        Crop deletedCrop = repository.findByIdAndUserId(id, jwt.getSubject()).orElse(null);
        if(deletedCrop == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        repository.delete(deletedCrop);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}