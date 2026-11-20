package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.Services.IVehiculeService;

import java.util.List;

@Tag(name = "VehiculeMohamed", description = "Operations related to Vehicule")
@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Ajouter un nouveau véhicule", description = "Ajoute un nouveau véhicule à la base de données.")
    @PostMapping("/add")
    public ResponseEntity<Vehicule> addVehicule(@RequestBody Vehicule vehicule) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.addVehicule(vehicule));
    }

    @Operation(summary = "Récupérer tous les véhicules", description = "Récupère la liste de tous les véhicules existants.")
    @GetMapping
    public ResponseEntity<List<Vehicule>> getAllVehicules() {
        return ResponseEntity.ok(vehiculeService.getAllVehicules());
    }

    @Operation(summary = "Récupérer un véhicule par son ID", description = "Récupère les détails d'un véhicule existant en utilisant son ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Vehicule> getVehiculeById(@PathVariable Long id) {
        return vehiculeService.getVehiculeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @Operation(summary = "Modifier un véhicule par son ID", description = "Modifie les détails d'un véhicule existant en utilisant son ID.")
    @PutMapping("/{id}")
    public ResponseEntity<Vehicule> modifierVehicule(@RequestBody Vehicule vehicule, @PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.modifierVehicule(vehicule, id));
    }

    @Operation(summary = "Affecter un véhicule à une agence",
            description = "Affecte un véhicule existant à une agence existante.")
    @PutMapping("/{vehiculeId}/agence/{agenceId}")
    public ResponseEntity<Vehicule> assignToAgence(@PathVariable Long vehiculeId,
                                                    @PathVariable Long agenceId) {
        try {
            return ResponseEntity.ok(vehiculeService.assignToAgence(vehiculeId, agenceId));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Retirer un véhicule de son agence",
            description = "Retire l'agence associée au véhicule sans supprimer le véhicule.")
    @DeleteMapping("/{vehiculeId}/agence")
    public ResponseEntity<Vehicule> removeFromAgence(@PathVariable Long vehiculeId) {
        try {
            return ResponseEntity.ok(vehiculeService.removeFromAgence(vehiculeId));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Changer l'agence d'un véhicule",
            description = "Affecte un véhicule à une autre agence existante.")
    @PutMapping("/{vehiculeId}/agence/change/{newAgenceId}")
    public ResponseEntity<Vehicule> changeAgence(@PathVariable Long vehiculeId,
                                                   @PathVariable Long newAgenceId) {
        try {
            return ResponseEntity.ok(vehiculeService.changeAgence(vehiculeId, newAgenceId));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer un véhicule par son ID", description = "Supprime un véhicule existant en utilisant son ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerVehicule(@PathVariable Long id) {
        try {
            vehiculeService.supprimerVehicule(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
