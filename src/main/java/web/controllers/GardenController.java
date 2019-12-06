package web.controllers;

import com.google.common.collect.Lists;
import model.repositories.GardenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import model.*;
import org.springframework.web.client.HttpClientErrorException;
import web.errorhandling.GardenNotFoundException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
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
    public List<Garden> getGardens(@AuthenticationPrincipal Jwt jwt)
    {
        return Lists.newArrayList(repository.findGardensByUserId(jwt.getSubject()));
    }

    @GetMapping("/garden/{id}")
    public ResponseEntity<Garden> getGarden(@PathVariable("id") int id, @AuthenticationPrincipal Jwt jwt)
    {
        Garden g =  repository.findByIdAndUserId(id, jwt.getSubject()).orElseThrow(() -> new GardenNotFoundException(id));
        return new ResponseEntity<>(g, HttpStatus.OK);
    }

    @PutMapping("/garden/{id}")
    public ResponseEntity<Garden> putGarden(@PathVariable("id") int id, @Valid @RequestBody Garden garden, @AuthenticationPrincipal Jwt jwt)
    {
        Garden g = repository.findByIdAndUserId(id, jwt.getSubject()).orElseThrow(() -> new GardenNotFoundException(id));
        g.setName(garden.getName());
        try
        {
            return new ResponseEntity<>(repository.save(g), HttpStatus.OK);
        }
        catch (ConstraintViolationException ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/garden")
    public ResponseEntity<Garden> postGarden(@Valid @RequestBody Garden g, @AuthenticationPrincipal Jwt jwt)
    {
        Garden garden = new Garden(g.getLength(), g.getWidth(), jwt.getSubject());
        garden.setName(g.getName());
        repository.save(garden);
        return new ResponseEntity<>(garden, HttpStatus.CREATED);
    }

    @DeleteMapping("/garden/{id}")
    public ResponseEntity deleteGarden(@PathVariable("id") int id, @AuthenticationPrincipal Jwt jwt)
    {
        Garden g = repository.findByIdAndUserId(id, jwt.getSubject()).orElseThrow(() -> new GardenNotFoundException(id));
        repository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}