package web.controllers;

import com.google.common.collect.Lists;
import model.Companion;
import model.Crop;
import model.repositories.CompanionRepository;

import java.util.*;

import model.repositories.CropRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import web.errorhandling.CropNotFoundException;

@RestController("/companions")
public class CompanionController
{

    private final CompanionRepository repository;

    private final CropRepository cropRepository;

    public CompanionController(CompanionRepository companionRepository, CropRepository cropRepository)
    {
        this.repository = companionRepository;
        this.cropRepository = cropRepository;
    }
    
    @GetMapping("/companions")
    public List<Companion> getCompanions(@AuthenticationPrincipal Jwt jwt)
    {
        Iterable<Companion> companions = repository.findAll();
        return Lists.newArrayList(companions);
    }

    @GetMapping("/companions/{id}")
    public ResponseEntity<Set<Companion>> getCompanionsForCrop(@PathVariable("id") int id, @AuthenticationPrincipal Jwt jwt)
    {
        Crop crop = cropRepository.findByIdAndUserId(id, jwt.getSubject()).orElseThrow(() -> new CropNotFoundException(id));
        return new ResponseEntity<>(crop.getImpactedBy(), HttpStatus.OK);
    }

    @PostMapping("/companions")
    public ResponseEntity<Companion> PostCompanion(@RequestBody Companion newCompanion, @AuthenticationPrincipal Jwt jwt)
    {
        return new ResponseEntity<>(repository.save(newCompanion), HttpStatus.CREATED);
    }

    @DeleteMapping("/companions/{id}")
    public ResponseEntity DeleteCompanions(@PathVariable("id") int id, @AuthenticationPrincipal Jwt jwt)
    {
        Companion deleted = repository.findById(id).orElse(null);
        if(deleted == null || !deleted.getImpacted().getUserId().equals(jwt.getSubject()))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        repository.delete(deleted);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
