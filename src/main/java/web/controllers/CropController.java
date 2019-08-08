package web.controllers;

import com.google.common.collect.Lists;
import model.repositories.CropRepository;
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
    public List<Crop> getCrops()
    {
        Iterable<Crop> crops = repository.findAll();
        return Lists.newArrayList(crops);
    }

    @RequestMapping("/crop/{id}")
    public Crop getCrop(@PathVariable("id") Short id)
    {
        return repository.findById(id).orElseThrow(() -> new CropNotFoundException(id));
    }

    @PutMapping("/crop/{id}")
    public Crop putCrop(@RequestBody Crop crop, @PathVariable Short id)
    {

        return repository.findById(id)
                .map(c -> {
                    c.setName(crop.getName());
                    c.setBinomialName(crop.getBinomialName());
                    c.setDescription(crop.getDescription());
                    c.setSowingMethod(crop.getSowingMethod());
                    c.setDiameter(crop.getDiameter());
                    c.setRowSpacing(crop.getRowSpacing());
                    c.setHeight(crop.getHeight());
                    return repository.save(c);
                })
                .orElseGet(() -> {
                    crop.setId(id);
                    return repository.save(crop);
                });
    }

    @PostMapping("/crop")
    public Crop postCrop(@RequestBody Crop crop)
    {
        return repository.save(crop);
    }


    @DeleteMapping("/crop/{id}")
    public void deleteCrop(@PathVariable Short id)
    {
        CompanionController compController = new CompanionController(this.repository);
        List<Companion> comps =  compController.getCompanions();
        comps.removeIf(cmp ->  !cmp.getCropId1().equals(id) && !cmp.getCropId2().equals(id));
        compController.DeleteCompanions(comps);
        repository.deleteById(id);
    }
}