package com.yeohangttukttak.api.domain.travel.dto;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.member.dto.MemberDTO;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.entity.AccompanyType;
import com.yeohangttukttak.api.domain.travel.entity.Motivation;
import com.yeohangttukttak.api.domain.travel.entity.TransportType;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TravelDTO {

    private Long id;

    private String name;

    private Motivation motivation;

    private AccompanyType accompanyType;

    private TransportType transportType;

    private LocalDate startedOn;

    private LocalDate endedOn;

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

        this.thumbnail = new ImageDTO(travel.getThumbnail());
        this.member = new MemberDTO(travel.getMember());
    }

}
