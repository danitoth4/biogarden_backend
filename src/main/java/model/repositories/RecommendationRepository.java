package model.repositories;

import model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, String>
{
}
