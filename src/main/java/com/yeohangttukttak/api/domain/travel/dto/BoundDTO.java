package com.yeohangttukttak.api.domain.travel.dto;

import com.yeohangttukttak.api.domain.place.dto.LocationDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Envelope;


@Data
@AllArgsConstructor
public class BoundDTO {

    private LocationDTO southwest;

    private LocationDTO northeast;

    public BoundDTO(Envelope envelope) {
        Location southwest = new Location(envelope.getMinY(), envelope.getMinX());
        Location northeast = new Location(envelope.getMaxY(), envelope.getMaxX());

        this.southwest = new LocationDTO(southwest);
        this.northeast = new LocationDTO(northeast);
    }

}