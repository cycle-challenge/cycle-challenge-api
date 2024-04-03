package com.yeohangttukttak.api.domain.travel.dto;

import lombok.Data;
import org.locationtech.jts.geom.Envelope;

import java.util.List;

@Data
public class TravelDayBoundDTO {

    private BoundDTO entire;

    private List<BoundDTO> visits;

    public TravelDayBoundDTO(Envelope entire, List<Envelope> envelopes) {
        this.entire = new BoundDTO(entire);
        this.visits = envelopes.stream().map(BoundDTO::new).toList();
    }

}
