package com.yeohangttukttak.api.service.visit;

import com.yeohangttukttak.api.domain.place.PlaceDTO;
import com.yeohangttukttak.api.domain.travel.TravelDTO;
import lombok.Data;

import java.util.List;

@Data
public class VisitSearchDTO {

    private List<TravelDTO> travels;
    private List<PlaceDTO> places;

    public VisitSearchDTO (List<TravelDTO> travels, List<PlaceDTO> places) {
        this.travels = travels;
        this.places = places;
    }

}
