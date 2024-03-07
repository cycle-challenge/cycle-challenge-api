package com.yeohangttukttak.api.domain.visit.dto;

import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.travel.entity.AccompanyType;
import com.yeohangttukttak.api.domain.travel.entity.Motivation;
import com.yeohangttukttak.api.domain.travel.entity.Season;
import com.yeohangttukttak.api.domain.travel.entity.TransportType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class VisitSearch {

    private Location location;

    private int radius;

    private Set<AgeGroup> ageGroups;

    private Set<Motivation> motivations;

    private Set<AccompanyType> accompanyTypes;

    private Set<TransportType> transportTypes;

    private Set<Season> seasons;

    @Builder
    public VisitSearch(Location location,
                       int radius,
                       Set<AgeGroup> ageGroups,
                       Set<Motivation> motivations,
                       Set<AccompanyType> accompanyTypes,
                       Set<TransportType> transportTypes,
                       Set<Season> seasons) {
        this.location = location;
        this.radius = radius;
        this.ageGroups = ageGroups;
        this.motivations = motivations;
        this.accompanyTypes = accompanyTypes;
        this.transportTypes = transportTypes;
        this.seasons = seasons;
    }
}
