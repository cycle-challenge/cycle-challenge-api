package com.yeohangttukttak.api.domain.visit.dao;

import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitSearchResult {

    private Visit visit;

    private Travel travel;

    private Place place;

    private Double distance;

}
