package ma.miaad.bonsortieservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ma.miaad.bonsortieservice.model.BienMobilier;

import javax.persistence.*;

@Entity @Data @NoArgsConstructor @AllArgsConstructor @ToString
public class BienMobilierBonSortie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;
   // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long idBS;
    private Long reference;
    private Long qantiteBS;
    // @Transient
  //  private BienMobilier bienMobilier;
}
