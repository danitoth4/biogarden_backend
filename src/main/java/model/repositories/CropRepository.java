package model.repositories;

import model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CropRepository extends JpaRepository<Crop, Integer>
{
    List<Crop> findAllByUserId(String userId);

    Optional<Crop> findByIdAndUserId(Integer id, String userId);
}
