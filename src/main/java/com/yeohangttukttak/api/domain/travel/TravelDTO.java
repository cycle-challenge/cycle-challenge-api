package com.yeohangttukttak.api.domain.travel;

import com.yeohangttukttak.api.domain.file.ImageDTO;
import com.yeohangttukttak.api.domain.member.MemberDTO;
import com.yeohangttukttak.api.domain.file.File;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TravelDTO {

    private Long id;

    private String name;

    private Motivation motivation;

    private AccompanyType accompanyType;

    private LocalDate startedOn;

    private LocalDate endedOn;

    private ImageDTO image;

    private MemberDTO member;

    public TravelDTO(Travel travel, File file) {
        this.id = travel.getId();
        this.name = travel.getName();
        this.motivation = travel.getMotivation();
        this.accompanyType = travel.getAccompanyType();
        this.startedOn = travel.getPeriod().getStartedOn();
        this.endedOn = travel.getPeriod().getEndedOn();
        this.image = new ImageDTO(file);
        this.member = new MemberDTO(travel.getMember());
    }

}
