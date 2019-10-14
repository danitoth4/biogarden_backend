package model.repositories;

import model.Crop;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CropRepository extends CrudRepository<Crop, Integer> {
    List<Crop> findAllByUserId(String userId);
}
