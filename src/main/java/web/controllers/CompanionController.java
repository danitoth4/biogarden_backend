package web.controllers;

import model.Companion;
import model.repositories.CropRepository;

import java.util.*;

import model.Crop;
import org.hibernate.sql.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import web.Util.UpdateMethod;
import web.errorhandling.CropNotFoundException;

@RestController("/companions")
public class CompanionController
{

    private final CropRepository repository;

    private static HashSet<Companion> companions;

    private static final Logger log = LoggerFactory.getLogger(CompanionController.class);

    public CompanionController(CropRepository cropRepository)
    {
        repository = cropRepository;
    }

    private static void initializeCompanions(CropRepository repo)
    {
        if (companions == null || companions.isEmpty())
        {
            companions = new HashSet<>();

            boolean warning = false;

            for (Crop crop : repo.findAll())
            {
                for (Crop c : crop.getAvoids())
                {
                    if (!createAndAddCompanion(false, crop, c))
                        warning = true;

                }
                for (Crop c : crop.getHelps())
                {
                    if (!createAndAddCompanion(true, crop, c))
                        warning = true;
                }
            }
            if (warning)
                log.warn("multiple relationships between same entities");
        }
    }

    private static boolean createAndAddCompanion(boolean isPositive, Crop first, Crop second)
    {
        Companion newCompanion = new Companion();
        newCompanion.setCropId1(first.getId());
        newCompanion.setCropId2(second.getId());
        newCompanion.setPositive(isPositive);
        return createAndAddCompanion(newCompanion);

    }

    private static boolean createAndAddCompanion(Companion newCompanion)
    {
        return companions.add(newCompanion);
    }

    private void updateCompanions(List<Companion> updatedEntries, UpdateMethod method, CropRepository repo)
    {
        initializeCompanions(repository);
        for (Companion c : updatedEntries)
        {
            Crop crop1 = repo.findById(c.getCropId1()).orElseThrow(() -> new CropNotFoundException(c.getCropId1()));
            Crop crop2 = repo.findById(c.getCropId2()).orElseThrow(() -> new CropNotFoundException(c.getCropId2()));
            if (method == UpdateMethod.ADDED)
            {
                if (c.getPositive())
                {
                    crop1.getHelps().add(crop2);
                } else
                {
                    crop1.getAvoids().add(crop2);
                }
                createAndAddCompanion(c);

            }
            if (method == UpdateMethod.DELETED)
            {
                if (c.getPositive())
                {
                    crop1.getHelps().remove(crop2);
                } else
                {
                    crop1.getAvoids().remove(crop2);
                }
                companions.remove(c);
            }
            repository.save(crop1);
        }
    }


    @GetMapping("/companions")
    public List<Companion> getCompanions()
    {
        initializeCompanions(repository);
        return new ArrayList<Companion>(companions);
    }

    @GetMapping("/companions/{id}")
    public List<Companion> getCompanionsForCrop(@PathVariable("id") int id)
    {
        initializeCompanions(repository);
        List<Companion> toreturn = new ArrayList<>();
        for(Companion c : companions)
        {
            if(c.getCropId1().equals(id))
            {
                toreturn.add(c);
            } else if(c.getCropId2().equals(id))
            {
                int temp = c.getCropId1();
                c.setCropId1(c.getCropId2());
                c.setCropId2(temp);
                toreturn.add(c);
            }
        }
        return toreturn;
    }

    @PostMapping("/companions")
    public void PostCompanions(@RequestBody List<Companion> newCompanions)
    {
        updateCompanions(newCompanions, UpdateMethod.ADDED, repository);
    }

    @DeleteMapping("/companions")
    public void DeleteCompanions(@RequestBody List<Companion> deletedCompanions)
    {
        updateCompanions(deletedCompanions, UpdateMethod.DELETED, repository);
    }


}
