package web.controllers;

import com.google.common.collect.Lists;
import model.Companion;
import model.repositories.CompanionRepository;
import model.repositories.CropRepository;

import java.util.*;

import model.Crop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import web.Util.UpdateMethod;
import web.errorhandling.CropNotFoundException;

@RestController("/companions")
public class CompanionController
{

    private final CompanionRepository repository;

    public CompanionController(CompanionRepository companionRepository)
    {
        this.repository = companionRepository;
    }
    
    @GetMapping("/companions")
    public List<Companion> getCompanions(@AuthenticationPrincipal Jwt jwt)
    {
        Iterable<Companion> companions = repository.findAll();
        return Lists.newArrayList(companions);
    }

    @GetMapping("/companions/{id}")
    public List<Companion> getCompanionsForCrop(@PathVariable("id") int id, @AuthenticationPrincipal Jwt jwt)
    {
        Iterable<Companion> companions = repository.findAll();
        return Lists.newArrayList(companions);
    }

    @PostMapping("/companions")
    public void PostCompanions(@RequestBody List<Companion> newCompanions, @AuthenticationPrincipal Jwt jwt)
    {
    }

    @DeleteMapping("/companions")
    public void DeleteCompanions(@RequestBody List<Companion> deletedCompanions, @AuthenticationPrincipal Jwt jwt)
    {
    }


}
