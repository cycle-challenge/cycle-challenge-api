package com.yeohangttukttak.api.domain.place.dto;

import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.global.config.validator.ValidLatitude;
import com.yeohangttukttak.api.global.config.validator.ValidLongitude;
import lombok.Data;

@Data
public class LocationDTO {

    @ValidLatitude
    private double latitude;

    @ValidLongitude
    private double longitude;

    public LocationDTO (Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public LocationDTO (Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
