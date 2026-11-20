package tn.esprit.autoloc.Services;

import tn.esprit.autoloc.domain.Equipement;

import java.util.List;
import java.util.Optional;

public interface IEquipementService {

    Equipement addEquipement(Equipement equipement);

    List<Equipement> getAllEquipements();

    Optional<Equipement> getEquipementById(Long id);

    Equipement modifierEquipement(Equipement equipement, Long id);

    void supprimerEquipement(Long id);

    Equipement assignToVehicule(Long equipementId, Long vehiculeId);

    Equipement removeFromVehicule(Long equipementId, Long vehiculeId);

    Equipement changeVehiculeEquipement(Long oldEquipementId, Long newEquipementId, Long vehiculeId);
}
