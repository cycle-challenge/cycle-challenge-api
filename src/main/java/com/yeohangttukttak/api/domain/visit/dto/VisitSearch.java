package com.yeohangttukttak.api.domain.visit.dto;

import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.travel.entity.AccompanyType;
import com.yeohangttukttak.api.domain.travel.entity.Motivation;
import com.yeohangttukttak.api.domain.travel.entity.TransportType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class VisitSearch {

    private Location location;

    private int radius;

    private List<AgeGroup> ageGroups;

    private List<Motivation> motivations;

    private List<AccompanyType> accompanyTypes;

    private List<TransportType> transportTypes;

    @Builder
    public VisitSearch(Location location,
                       int radius,
                       List<AgeGroup> ageGroups,
                       List<Motivation> motivations,
                       List<AccompanyType> accompanyTypes,
                       List<TransportType> transportTypes) {
        this.location = location;
        this.radius = radius;
        this.ageGroups = ageGroups;
        this.motivations = motivations;
        this.accompanyTypes = accompanyTypes;
        this.transportTypes = transportTypes;
    }
}
