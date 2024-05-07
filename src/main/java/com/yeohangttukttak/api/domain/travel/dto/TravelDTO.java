package com.yeohangttukttak.api.domain.travel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.member.dto.MemberDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.entity.*;
import com.yeohangttukttak.api.domain.visit.dto.VisitDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TravelDTO {

    private Long id;

    private String name;

    private Motivation motivation;

    private AccompanyType accompanyType;

    private TransportType transportType;

    private LocalDate startedOn;

    private LocalDate endedOn;

    private Set<Season> seasons;

    private ImageDTO thumbnail;

    private MemberDTO member;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<PlaceDTO> places;
    
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<VisitDTO> visits;

    public TravelDTO(Travel travel) {
        this.id = travel.getId();
        this.name = travel.getName();
        this.motivation = travel.getMotivation();
        this.accompanyType = travel.getAccompanyType();
        this.transportType = travel.getTransportType();

        if (travel.getPeriod() != null) {
            this.startedOn = travel.getPeriod().getStartedOn();
            this.endedOn = travel.getPeriod().getEndedOn();
            this.seasons = travel.getPeriod().getSeasons();
        }

        if (travel.getThumbnail() != null) {
            this.thumbnail = new ImageDTO(travel.getThumbnail());
        }
        this.member = new MemberDTO(travel.getMember());
    }

    public TravelDTO(Travel travel, List<Place> places) {
        this(travel);
        this.places = places.stream().map(PlaceDTO::new).toList();
    }
}
