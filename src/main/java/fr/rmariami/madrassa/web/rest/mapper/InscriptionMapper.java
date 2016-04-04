package fr.rmariami.madrassa.web.rest.mapper;

import fr.rmariami.madrassa.domain.*;
import fr.rmariami.madrassa.web.rest.dto.InscriptionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Inscription and its DTO InscriptionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface InscriptionMapper {

    @Mapping(source = "classRoom.id", target = "classRoomId")
    @Mapping(source = "classRoom.code", target = "classRoomCode")
    @Mapping(source = "scholar.id", target = "scholarId")
    @Mapping(source = "scholar.name", target = "scholarName")
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.firstName", target = "authorName")
    InscriptionDTO inscriptionToInscriptionDTO(Inscription inscription);

    List<InscriptionDTO> inscriptionsToInscriptionDTOs(List<Inscription> inscriptions);

    @Mapping(source = "classRoomId", target = "classRoom")
    @Mapping(source = "scholarId", target = "scholar")
    @Mapping(source = "authorId", target = "author")
    @Mapping(target = "wishss", ignore = true)
    Inscription inscriptionDTOToInscription(InscriptionDTO inscriptionDTO);

    List<Inscription> inscriptionDTOsToInscriptions(List<InscriptionDTO> inscriptionDTOs);

    default ClassRoom classRoomFromId(Long id) {
        if (id == null) {
            return null;
        }
        ClassRoom classRoom = new ClassRoom();
        classRoom.setId(id);
        return classRoom;
    }

    default Scholar scholarFromId(Long id) {
        if (id == null) {
            return null;
        }
        Scholar scholar = new Scholar();
        scholar.setId(id);
        return scholar;
    }
}
