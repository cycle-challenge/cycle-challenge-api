package com.yeohangttukttak.api.domain.travel.dto;

import com.yeohangttukttak.api.domain.visit.dto.VisitDTO;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import lombok.Data;

import java.util.List;

@Data
public class TravelDaySummaryDTO {

    private int dayOfTravel;

    private List<VisitDTO> visits;

    private TravelDayBoundDTO bound;

    public TravelDaySummaryDTO(int dayOfTravel, List<Visit> visits,
                               TravelDayBoundDTO bound) {

        this.dayOfTravel = dayOfTravel;
        this.visits = visits.stream().map(VisitDTO::new).toList();
        this.bound = bound;

    }


}
