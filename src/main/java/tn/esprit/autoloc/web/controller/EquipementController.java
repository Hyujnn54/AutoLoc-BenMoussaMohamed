package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.Services.IEquipementService;
import tn.esprit.autoloc.domain.Equipement;

import java.util.List;

@Tag(name = "Equipement", description = "Operations related to equipment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/equipements")
public class EquipementController {

    private final IEquipementService equipementService;

    @Operation(summary = "Ajouter un nouvel équipement")
    @PostMapping("/add")
    public ResponseEntity<Equipement> addEquipement(@RequestBody Equipement equipement) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(equipementService.addEquipement(equipement));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Récupérer tous les équipements")
    @GetMapping
    public ResponseEntity<List<Equipement>> getAllEquipements() {
        return ResponseEntity.ok(equipementService.getAllEquipements());
    }

    @Operation(summary = "Récupérer un équipement par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<Equipement> getEquipementById(@PathVariable Long id) {
        return equipementService.getEquipementById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Modifier un équipement par son ID")
    @PutMapping("/{id}")
    public ResponseEntity<Equipement> modifierEquipement(@RequestBody Equipement equipement,
                                                         @PathVariable Long id) {
        try {
            return ResponseEntity.ok(equipementService.modifierEquipement(equipement, id));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer un équipement par son ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEquipement(@PathVariable Long id) {
        try {
            equipementService.supprimerEquipement(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Affecter un équipement à un véhicule")
    @PutMapping("/{equipementId}/vehicule/{vehiculeId}")
    public ResponseEntity<Equipement> assignToVehicule(@PathVariable Long equipementId,
                                                       @PathVariable Long vehiculeId) {
        try {
            return ResponseEntity.ok(equipementService.assignToVehicule(equipementId, vehiculeId));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
