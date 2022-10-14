package ma.miaad.bonsortieservice.feign;

import ma.miaad.bonsortieservice.model.BienMobilier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "BIENMOBILIER-SERVICE")
public interface BienMobilierRestClient {
    @GetMapping("/bienMobiliers/view1/{reference}")
    BienMobilier getBienMobilierByReference(@PathVariable(name = "reference") Long reference);
    @PutMapping(path = "/bienMobiliers/retairerQuantite/{reference}/{quantite}")
    public void retairerQuantite(@PathVariable(name = "reference") Long reference, @PathVariable(name = "quantite") Long quantite);
}
