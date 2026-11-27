package tn.esprit.autoloc.web.dto;

import java.math.BigDecimal;

public record VehiculeDto(
        String immatriculation,
        String marque,
        String modele,
        BigDecimal tarif,
        String nomAgence
) {
}
