package ma.miaad.bonsortieservice.web;

import lombok.Data;
import ma.miaad.bonsortieservice.entities.BienMobilierBonSortie;
import ma.miaad.bonsortieservice.entities.BonSortie;
import ma.miaad.bonsortieservice.feign.BienMobilierRestClient;
import ma.miaad.bonsortieservice.model.BienMobilier;
import ma.miaad.bonsortieservice.repository.BienMobilierBonSortieRepository;
import ma.miaad.bonsortieservice.repository.BonSortieRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class BonSortieRestController {
    private BonSortieRepository bonSortieRepository;
    private BienMobilierRestClient bienMobilierRestClient;
    private BienMobilierBonSortieRepository bienMobilierBonSortieRepository;

    public BonSortieRestController(BonSortieRepository bonSortieRepository, BienMobilierRestClient bienMobilierRestClient, BienMobilierBonSortieRepository bienMobilierBonSortieRepository) {
        this.bonSortieRepository = bonSortieRepository;
        this.bienMobilierRestClient = bienMobilierRestClient;
        this.bienMobilierBonSortieRepository = bienMobilierBonSortieRepository;
    }

    @PostMapping(path = "/creationBonM")
    public void saveBienMobilier(@RequestBody Params params) {
        String nomDemandeur = params.nomDemandeur;
        String statut = params.statut;
        ArrayList<Long> references = params.getReferences();
        ArrayList<Long> qantiteBSs = params.getQantiteBSs();
        BonSortie bonSortie = bonSortieRepository.save(new BonSortie(null, new Date(), nomDemandeur, statut, null));
        List<BienMobilierBonSortie> bienMobilierBonSorties = new ArrayList<BienMobilierBonSortie>();
        int i = 0;
        for (Long r : references) {
            Long qantiteBS = qantiteBSs.get(i);
            BienMobilierBonSortie bienMobilierBonSortie = new BienMobilierBonSortie(null, bonSortie.getId(), bienMobilierRestClient.getBienMobilierByReference(r).getReference(), qantiteBS /*bienMobilierRestClient.getBienMobilierByReference(r).getQuantite(),bienMobilierRestClient.getBienMobilierByReference(r)*/);
            i += 1;
            bienMobilierBonSortieRepository.save(bienMobilierBonSortie);
            if (statut.equals("valide")) {
                bienMobilierRestClient.retairerQuantite(r, qantiteBS);
            }
            bienMobilierBonSorties.add(bienMobilierBonSortie);

        }
        bonSortie.setBienSorties(bienMobilierBonSorties);
        System.out.println("**********BonSortie***********" + bonSortie);
        Long id = bonSortieRepository.save(bonSortie).getId();
    }

    boolean verifierQuantiteBien(Long reference, Long quantite) {
        BienMobilier bienMobilier = bienMobilierRestClient.getBienMobilierByReference(reference);
        if (bienMobilier.getQuantite() >= quantite) {
            return true;
        }
        return false;
    }


    @PutMapping(path = "/sorties/valide/{id}")
    public boolean updateBonSortie(@PathVariable(name = "id") Long id) {
        boolean test = true;
        if (bonSortieRepository.existsById(id)) {
            BonSortie bonSortie = bonSortieRepository.findById(id).get();
            if (!(bonSortie.getStatut()).equals("valide")) {
                for (BienMobilierBonSortie mobilierBonSortie : bienMobilierBonSortieRepository.findAll()) {
                    if (mobilierBonSortie.getIdBS() == id) {
                        Long reference = mobilierBonSortie.getReference();
                        Long qantiteBs = mobilierBonSortie.getQantiteBS();
                        test = test & verifierQuantiteBien(reference, qantiteBs);
                    }
                }
                if (test == true) {
                    bienMobilierBonSortieRepository.findAll().forEach(bienMobilierBonSortie -> {
                        if (bienMobilierBonSortie.getIdBS() == id) {
                            Long reference = bienMobilierBonSortie.getReference();
                            Long qantiteBs = bienMobilierBonSortie.getQantiteBS();
                            bienMobilierRestClient.retairerQuantite(reference, qantiteBs);
                        }
                    });
                    bonSortie.setStatut("valide");
                    bonSortieRepository.save(bonSortie);
                    return true;
                }

            }

        }
        return false;
    }

    @GetMapping(path = "/sorties/{id}")
    public BonSortie getBonSortie(@PathVariable(name = "id") Long id) {
        if (bonSortieRepository.existsById(id)) {
            BonSortie bonSortie = bonSortieRepository.findById(id).get();
            List<BienMobilierBonSortie> bienMobilierBonSorties = new ArrayList<BienMobilierBonSortie>();
            bienMobilierBonSortieRepository.findAll().forEach(bienMobilierBonSortie -> {
                if (bienMobilierBonSortie.getIdBS() == id) {
                    // bienMobilierBonSortie.setBienMobilier(bienMobilierRestClient.getBienMobilierByReference(bienMobilierBonSortie.getReference()));
                    bienMobilierBonSorties.add(bienMobilierBonSortie);
                }
            });

            bonSortie.setBienSorties(bienMobilierBonSorties);
            return bonSortie;
        } else {
            return null;
        }
    }

    @GetMapping(path = "/sorties")
    public List<BonSortie> getBonSorties() {
        bonSortieRepository.findAll().forEach(bonSortie -> {
            List<BienMobilierBonSortie> bienMobilierBonSorties = new ArrayList<BienMobilierBonSortie>();
            bienMobilierBonSortieRepository.findAll().forEach(bienMobilierBonSortie -> {
                if (bienMobilierBonSortie.getIdBS() == bonSortie.getId()) {
                    //  bienMobilierBonSortie.setBienMobilier(bienMobilierRestClient.getBienMobilierByReference(bienMobilierBonSortie.getReference()));
                    bienMobilierBonSorties.add(bienMobilierBonSortie);
                }
            });
            bonSortie.setBienSorties(bienMobilierBonSorties);
        });
        return bonSortieRepository.findAll();
    }

    @GetMapping(path = "/sorties/getByBien/{reference}")
    public List<BonSortie> getBonSortiesByBien(@PathVariable(name = "reference") Long reference) {
        List<BonSortie> bonSorties = new ArrayList<BonSortie>();
        bienMobilierBonSortieRepository.findAll().forEach(bienMobilierBonSortie -> {
            if (bienMobilierBonSortie.getReference() == reference) {
                bonSorties.add(bonSortieRepository.findById(bienMobilierBonSortie.getIdBS()).get());
            }
        });
        return bonSorties;
    }

    @DeleteMapping(path = "/sorties/delete/{id}")
    void deleteBonSortie(@PathVariable(name = "id") Long id) {
        List<BienMobilierBonSortie> bienMobilierBonSorties = new ArrayList<BienMobilierBonSortie>();
        bienMobilierBonSortieRepository.findAll().forEach(bienMobilierBonSortie -> {
            if (bienMobilierBonSortie.getIdBS() == id) {
                bienMobilierBonSorties.add(bienMobilierBonSortie);
            }
        });
        if (bonSortieRepository.existsById(id)) {
            BonSortie bonSortie = bonSortieRepository.findById(id).get();
            bonSortieRepository.deleteById(id);
        }
        for (BienMobilierBonSortie bienMobilierBonSortie : bienMobilierBonSorties){
            Long idBMS = bienMobilierBonSortie.getId();
            bienMobilierBonSortieRepository.deleteById(idBMS);
        }

    }
}
@Data
class Params{
    String nomDemandeur;
    String statut;
    ArrayList<Long> references ;
    ArrayList<Long> qantiteBSs;
}