package model.repositories;

import model.GardenContent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GardenContentRepository extends CrudRepository<GardenContent, Integer>
{
    List<GardenContent> findAllByUserId(String userId);
}
