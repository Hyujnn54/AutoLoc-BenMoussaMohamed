package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {

    @Mapping(source = "tarifJournalier", target = "tarif")
    @Mapping(source = "agence.nom", target = "nomAgence")
    VehiculeDto toDto(Vehicule vehicule);

    List<VehiculeDto> toDto(List<Vehicule> vehicules);
}
