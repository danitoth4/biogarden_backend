package web.controllers;

import model.Companion;
import model.repositories.CropRepository;
import  java.util.*;
import model.Crop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

public class CompanionController {

    private final CropRepository repository;

    private static HashSet<Companion> companions;

    private static final Logger log = LoggerFactory.getLogger(CompanionController.class);

    private static void initializeCompanions(CropRepository repo) {
        if (companions == null || companions.isEmpty()) {
            companions = new HashSet<>();

            boolean warning = false;

            for (Crop crop : repo.findAll()) {
                for (Crop c : crop.getAvoids()) {
                    if(!createAndAddCompanion(false, crop, c))
                        warning = true;

                }
                for (Crop c : crop.getHelpedBy()) {
                    if(!createAndAddCompanion(true, crop, c))
                        warning = true;
                }
            }
            if(warning)
                log.warn("multiple relationships between same entities");
        }
    }

        private static boolean createAndAddCompanion(boolean isPositive, Crop first, Crop second)
        {
            Companion newCompanion = new Companion();
            newCompanion.setCropId1(first.getId());
            newCompanion.setCropId2(second.getId());
            newCompanion.setPositive(isPositive);
            if(!companions.contains(newCompanion)) {
                companions.add(newCompanion);
                return true;
            }
            else
            {
                return false;
            }

        }



    public CompanionController(CropRepository cropRepository) {
        repository = cropRepository;
    }

    @GetMapping("/companions")
    public List<Companion> getCompanions()
    {
        initializeCompanions(repository);
        return new ArrayList<Companion>(companions);
    }


}
