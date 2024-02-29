package com.yeohangttukttak.api.domain.place.entity;

import lombok.Getter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@Getter
public class Location {

    private final Double latitude;

    private final Double longitude;

    private final Point point;

    // 클래스 레벨의 정적 변수로 선언해 한 번만 초기화 한다.
    private static final GeometryFactory factory = new GeometryFactory(
            new PrecisionModel(), 4326);

    public Location (Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.point = factory.createPoint(new Coordinate(longitude, latitude));
    }

    public Location (Point point) {
        this.point = point;
        this.latitude = point.getCoordinate().y;
        this.longitude = point.getCoordinate().x;
    }

}
