package ma.miaad.bonsortieservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class BonSortie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateBS;
    private String nomDemandeur;
    private String statut;
    @OneToMany
    private List<BienMobilierBonSortie> bienSorties;
}
