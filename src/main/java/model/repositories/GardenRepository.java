package model.repositories;


import model.Garden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GardenRepository extends JpaRepository<Garden, Integer>
{
    List<Garden> findGardensByUserId(String userId);

    Optional<Garden> findByIdAndUserId(Integer id, String userId);
}
