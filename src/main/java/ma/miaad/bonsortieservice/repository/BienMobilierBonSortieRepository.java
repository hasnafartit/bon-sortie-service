package ma.miaad.bonsortieservice.repository;

import ma.miaad.bonsortieservice.entities.BienMobilierBonSortie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BienMobilierBonSortieRepository extends JpaRepository<BienMobilierBonSortie,Long> {
}
