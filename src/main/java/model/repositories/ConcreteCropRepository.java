package model.repositories;

import model.ConcreteCrop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcreteCropRepository extends JpaRepository<ConcreteCrop, String>
{
}
