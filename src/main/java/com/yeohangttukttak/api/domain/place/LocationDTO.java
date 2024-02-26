package com.yeohangttukttak.api.domain.place;

import com.yeohangttukttak.api.domain.place.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class LocationDTO {

    private double latitude;

    private double longitude;

    public LocationDTO (Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

}
