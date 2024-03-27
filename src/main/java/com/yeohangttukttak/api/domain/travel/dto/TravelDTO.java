package com.yeohangttukttak.api.domain.travel.dto;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.member.dto.MemberDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.travel.entity.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
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

    public TravelDTO(Travel travel) {
        this.id = travel.getId();
        this.name = travel.getName();
        this.motivation = travel.getMotivation();
        this.accompanyType = travel.getAccompanyType();
        this.transportType = travel.getTransportType();
        this.startedOn = travel.getPeriod().getStartedOn();
        this.endedOn = travel.getPeriod().getEndedOn();
        this.seasons = travel.getPeriod().getSeasons();

        this.thumbnail = new ImageDTO(travel.getThumbnail());
        this.member = new MemberDTO(travel.getMember());
    }

}
