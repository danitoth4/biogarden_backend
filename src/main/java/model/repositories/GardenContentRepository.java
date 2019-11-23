package model.repositories;

import model.GardenContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GardenContentRepository extends JpaRepository<GardenContent, Integer>
{
    List<GardenContent> findAllByUserId(String userId);

    Optional<GardenContent> findByIdAndUserId(Integer id, String userId);
}
