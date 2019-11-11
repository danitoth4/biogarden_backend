package model.repositories;

import model.Crop;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CropRepository extends CrudRepository<Crop, Integer> {
    List<Crop> findAllByUserId(String userId);

    Optional<Crop> findByIdAndUserId(Integer id, String userId);
}
