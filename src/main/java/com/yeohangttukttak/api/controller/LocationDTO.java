package com.yeohangttukttak.api.controller;

import com.yeohangttukttak.api.domain.place.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationDTO {

    private double latitude;

    private double longitude;

    public LocationDTO (Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

}
