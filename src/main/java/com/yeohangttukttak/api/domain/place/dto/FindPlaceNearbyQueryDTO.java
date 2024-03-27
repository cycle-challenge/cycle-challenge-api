package com.yeohangttukttak.api.domain.place.dto;

import com.yeohangttukttak.api.domain.place.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindPlaceNearbyQueryDTO {

    private Place place;

    private Double distance;

}
