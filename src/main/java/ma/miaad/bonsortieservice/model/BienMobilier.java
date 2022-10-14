package ma.miaad.bonsortieservice.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;


@Data
public class BienMobilier {
   private Long reference;
   private Long stockMinimum;
   private Long idFamille;
   private String nomFamille;
   private Long idEmplacement;
   private String nomEmplacement;
   // private Long idBonEntre;
   private Long idFournisseur;
   private String nomFournisseur;
   private  Long quantite;
   private Long codeBarre;
   private String documentLiee;
}
