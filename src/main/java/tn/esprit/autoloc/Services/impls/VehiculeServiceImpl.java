package tn.esprit.autoloc.Services.impls;

import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.Services.IVehiculeService;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.AgenceRepository;
import tn.esprit.autoloc.repository.EquipementRepository;
import tn.esprit.autoloc.repository.VehiculeRepository;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;
import tn.esprit.autoloc.web.dto.VehiculeDto;
import tn.esprit.autoloc.web.dto.VehiculeEquipementCreationRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final AgenceRepository agenceRepository;
    private final EquipementRepository equipementRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public Vehicule addVehicule(Vehicule vehicule) {
        if (vehiculeRepository.existsByImmatriculation(vehicule.getImmatriculation())) {
            throw new IllegalArgumentException(
                    "Un véhicule avec l'immatriculation " + vehicule.getImmatriculation() + " existe déjà");
        }

        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public Vehicule addVehiculeWithEquipement(VehiculeEquipementCreationRequest request) {
        Vehicule vehicule = request.vehicule();
        Set<Equipement> equipements = new HashSet<>(request.equipements());
        if (vehiculeRepository.existsByImmatriculation(vehicule.getImmatriculation())) {
            throw new IllegalArgumentException(
                    "Un véhicule avec l'immatriculation " + vehicule.getImmatriculation() + " existe déjà");
        }

        Set<String> libelles = new HashSet<>();
        for (Equipement equipement : equipements) {
            if (equipement.getLibelle() == null || equipement.getLibelle().isBlank()) {
                throw new IllegalArgumentException("Le libellé de chaque équipement est obligatoire");
            }
            equipement.setLibelle(equipement.getLibelle().trim());
            if (!libelles.add(equipement.getLibelle())) {
                throw new IllegalArgumentException(
                        "Le libellé " + equipement.getLibelle() + " est présent plusieurs fois");
            }
            if (equipementRepository.existsByLibelle(equipement.getLibelle())) {
                throw new IllegalArgumentException(
                        "Un équipement avec le libellé " + equipement.getLibelle() + " existe déjà");
            }
        }
        vehicule.setEquipements(equipements);

        Agence agence = agenceRepository.findById(request.agenceId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Agence introuvable avec l'id " + request.agenceId()));

        vehicule.setAgence(agence);
        vehicule.getEquipements().forEach(equipement -> equipement.getVehicules().add(vehicule));
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> getAllVehicules() {
        return vehiculeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehicule> getVehiculeById(Long id) {
        return vehiculeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDto> getAllVehiculesDto() {
        return vehiculeMapper.toDto(vehiculeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehiculeDto> getVehiculeDtoById(Long id) {
        return vehiculeRepository.findById(id)
                .map(vehiculeMapper::toDto);
    }

    @Override
    public Vehicule modifierVehicule(Vehicule vehicule, Long id) {
        Vehicule existingVehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable avec l'id " + id));

        vehiculeRepository.findByImmatriculation(vehicule.getImmatriculation())
                .filter(found -> !found.getIdVehicule().equals(id))
                .ifPresent(found -> {
                    throw new IllegalArgumentException(
                            "Un véhicule avec l'immatriculation "
                                    + vehicule.getImmatriculation() + " existe déjà");
                });

        existingVehicule.setImmatriculation(vehicule.getImmatriculation());
        existingVehicule.setMarque(vehicule.getMarque());
        existingVehicule.setModele(vehicule.getModele());
        existingVehicule.setCategorie(vehicule.getCategorie());
        existingVehicule.setTarifJournalier(vehicule.getTarifJournalier());
        existingVehicule.setStatut(vehicule.getStatut());
        existingVehicule.setAgence(vehicule.getAgence());
        existingVehicule.setEquipements(vehicule.getEquipements());

        return vehiculeRepository.save(existingVehicule);
    }

    @Override
    public void supprimerVehicule(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable avec l'id " + id));

        vehiculeRepository.delete(vehicule);
    }

    @Override
    public Vehicule assignToAgence(Long vehiculeId, Long agenceId) {
        Vehicule vehicule = getVehiculeOrThrow(vehiculeId);
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new IllegalArgumentException("Agence introuvable avec l'id " + agenceId));

        vehicule.setAgence(agence);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule removeFromAgence(Long vehiculeId) {
        Vehicule vehicule = getVehiculeOrThrow(vehiculeId);

        vehicule.setAgence(null);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule changeAgence(Long vehiculeId, Long newAgenceId) {
        Vehicule vehicule = getVehiculeOrThrow(vehiculeId);
        Agence newAgence = agenceRepository.findById(newAgenceId)
                .orElseThrow(() -> new IllegalArgumentException("Agence introuvable avec l'id " + newAgenceId));

        vehicule.setAgence(newAgence);
        return vehiculeRepository.save(vehicule);
    }

    private Vehicule getVehiculeOrThrow(Long vehiculeId) {
        return vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Véhicule introuvable avec l'id " + vehiculeId));
    }

}
