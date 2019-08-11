package web.controllers;

import com.google.common.collect.Lists;
import model.repositories.GardenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import model.*;
import web.errorhandling.GardenNotFoundException;
import java.util.List;

@RestController
public class GardenController
{
    private final GardenRepository repository;

    public GardenController(GardenRepository gardenRepository)
    {
        this.repository = gardenRepository;
    }

    @GetMapping("/garden")
    public List<Garden> getGardens()
    {
        return Lists.newArrayList(repository.findAll());
    }

    @GetMapping("/garden/{id}")
    public Garden getGarden(@PathVariable("id") int id)
    {
        return repository.findById(id).orElseThrow(() -> new GardenNotFoundException(id));
    }

    @PutMapping("/garden/{id}")
    public Garden putGarden(@PathVariable("id") int id, @RequestBody Garden garden)
    {
        return repository.findById(id)
                .map(g -> {
                    g.setName(garden.getName());
                    return repository.save(g);
                }).orElseThrow(() -> new GardenNotFoundException(id));
    }

    @PostMapping("/garden")
    @ResponseStatus(HttpStatus.CREATED)
    public Garden postGarden(@RequestBody Garden g)
    {
        Garden garden = new Garden(g.getLength(), g.getWidth());
        garden.setName(g.getName());
        repository.save(garden);
        return garden;
    }

    @DeleteMapping("/garden/{id}")
    public void deleteGarden(@PathVariable("id") int id)
    {
        repository.deleteById(id);
    }
}