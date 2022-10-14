package ma.miaad.bonsortieservice.repository;

import ma.miaad.bonsortieservice.entities.BonSortie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BonSortieRepository extends JpaRepository<BonSortie,Long> {
}
