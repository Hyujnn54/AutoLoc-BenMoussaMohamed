package tn.esprit.autoloc.Services;

import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeEquipementCreationRequest;
import tn.esprit.autoloc.web.dto.VehiculeDto;

import java.util.List;
import java.util.Optional;

public interface IVehiculeService {

    Vehicule addVehicule(Vehicule vehicule);

    Vehicule addVehiculeWithEquipement(VehiculeEquipementCreationRequest request);

    List<Vehicule> getAllVehicules();

    Optional<Vehicule> getVehiculeById(Long id);

    List<VehiculeDto> getAllVehiculesDto();

    Optional<VehiculeDto> getVehiculeDtoById(Long id);

    Vehicule modifierVehicule(Vehicule vehicule, Long id);

    void supprimerVehicule(Long id);

    Vehicule assignToAgence(Long vehiculeId, Long agenceId);

    Vehicule removeFromAgence(Long vehiculeId);

    Vehicule changeAgence(Long vehiculeId, Long newAgenceId);
}
