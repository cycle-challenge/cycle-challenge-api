package com.yeohangttukttak.api.domain.travel.dto;
import com.yeohangttukttak.api.domain.place.dto.LocationDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class TravelFindNearbyRequest {

    @Valid
    private LocationDTO location;

    @NotNull
    @Range(min = 3000, max = 50000)
    private Integer radius;

}
