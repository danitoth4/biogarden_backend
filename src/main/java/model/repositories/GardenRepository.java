package model.repositories;


import model.Garden;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GardenRepository extends CrudRepository<Garden, Integer> {

    List<Garden> findGardensByUserId(String userId);
}
