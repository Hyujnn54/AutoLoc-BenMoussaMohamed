package tn.esprit.autoloc.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;

import java.util.List;

public record VehiculeEquipementCreationRequest(
        @NotNull @Valid Vehicule vehicule,
        @NotEmpty @Valid List<Equipement> equipements,
        @NotNull Long agenceId
) {
}
