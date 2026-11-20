package tn.esprit.autoloc.Services.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Services.IEquipementService;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.EquipementRepository;
import tn.esprit.autoloc.repository.VehiculeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final EquipementRepository equipementRepository;
    private final VehiculeRepository vehiculeRepository;

    @Override
    public Equipement addEquipement(Equipement equipement) {
        if (equipementRepository.existsByLibelle(equipement.getLibelle())) {
            throw new IllegalArgumentException(
                    "Un équipement avec le libellé " + equipement.getLibelle() + " existe déjà");
        }
        return equipementRepository.save(equipement);
    }

    @Override
    public List<Equipement> getAllEquipements() {
        return equipementRepository.findAll();
    }

    @Override
    public Optional<Equipement> getEquipementById(Long id) {
        return equipementRepository.findById(id);
    }

    @Override
    public Equipement modifierEquipement(Equipement equipement, Long id) {
        Equipement existingEquipement = equipementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Équipement introuvable avec l'id " + id));

        equipementRepository.findByLibelle(equipement.getLibelle())
                .filter(found -> !found.getIdEquipement().equals(id))
                .ifPresent(found -> {
                    throw new IllegalArgumentException(
                            "Un équipement avec le libellé " + equipement.getLibelle() + " existe déjà");
                });

        existingEquipement.setLibelle(equipement.getLibelle());
        return equipementRepository.save(existingEquipement);
    }

    @Override
    public void supprimerEquipement(Long id) {
        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Équipement introuvable avec l'id " + id));
        equipementRepository.delete(equipement);
    }

    @Override
    public Equipement assignToVehicule(Long equipementId, Long vehiculeId) {
        Equipement equipement = equipementRepository.findById(equipementId)
                .orElseThrow(() -> new IllegalArgumentException("Équipement introuvable avec l'id " + equipementId));
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable avec l'id " + vehiculeId));

        vehicule.getEquipements().add(equipement);
        equipement.getVehicules().add(vehicule);
        vehiculeRepository.save(vehicule);
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement removeFromVehicule(Long equipementId, Long vehiculeId) {
        Equipement equipement = equipementRepository.findById(equipementId)
                .orElseThrow(() -> new IllegalArgumentException("Équipement introuvable avec l'id " + equipementId));
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable avec l'id " + vehiculeId));

        if (!vehicule.getEquipements().remove(equipement)) {
            throw new IllegalArgumentException("L'équipement n'est pas affecté à ce véhicule");
        }

        equipement.getVehicules().remove(vehicule);
        vehiculeRepository.save(vehicule);
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement changeVehiculeEquipement(Long oldEquipementId, Long newEquipementId, Long vehiculeId) {
        Equipement oldEquipement = equipementRepository.findById(oldEquipementId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Équipement introuvable avec l'id " + oldEquipementId));
        Equipement newEquipement = equipementRepository.findById(newEquipementId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Équipement introuvable avec l'id " + newEquipementId));
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable avec l'id " + vehiculeId));

        if (!vehicule.getEquipements().remove(oldEquipement)) {
            throw new IllegalArgumentException("L'ancien équipement n'est pas affecté à ce véhicule");
        }

        oldEquipement.getVehicules().remove(vehicule);
        vehicule.getEquipements().add(newEquipement);
        newEquipement.getVehicules().add(vehicule);
        vehiculeRepository.save(vehicule);
        equipementRepository.save(oldEquipement);
        return equipementRepository.save(newEquipement);
    }
}
