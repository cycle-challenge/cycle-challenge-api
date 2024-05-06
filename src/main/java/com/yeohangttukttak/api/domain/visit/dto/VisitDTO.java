package com.yeohangttukttak.api.domain.visit.dto;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import lombok.Data;

import java.util.List;

@Data
public class VisitDTO {

    private Long id;

    private Integer dayOfTravel;

    private Integer orderOfVisit;

    private PlaceDTO place;

    private List<ImageDTO> images;

    public VisitDTO(Visit visit) {
        id = visit.getId();
        dayOfTravel = visit.getDayOfTravel();
        orderOfVisit = visit.getOrderOfVisit();
        place = new PlaceDTO(visit.getPlace());
        images = visit.getImages().stream()
                .map(ImageDTO::new).toList();
    }

    public VisitDTO(Visit visit, PlaceDTO place) {
        this(visit);
        this.place = place;
    }

}
