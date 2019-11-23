package model.repositories;

import model.Companion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanionRepository extends JpaRepository<Companion, String>
{
}
