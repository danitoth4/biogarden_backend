package web.controllers;

import model.repositories.GardenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import model.*;
import web.errorhandling.GardenNotFoundException;

@RestController
public class GardenController
{
    private final GardenRepository repository;

    public GardenController(GardenRepository gardenRepository)
    {
        this.repository = gardenRepository;
    }

    @GetMapping("/garden/{id}")
    public Garden getGarden(@PathVariable("id") int id)
    {
        Garden garden =  repository.findById(id).orElseThrow(() -> new GardenNotFoundException(id));
        return garden;
    }


    @PostMapping("/garden")
    @ResponseStatus(HttpStatus.CREATED)
    public Garden postGarden(@RequestBody Garden g)
    {
        Garden garden = new Garden(g.getLength(), g.getWidth());
        repository.save(garden);
        return garden;
    }

    /*
    @PutMapping("/garden")
    public Garden putGarden(@RequestBody Garden garden)
    {
        return new Garden();
    }*/
}