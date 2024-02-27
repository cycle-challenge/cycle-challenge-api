package com.yeohangttukttak.api.converter;

import com.yeohangttukttak.api.domain.place.Location;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;

public class StringToLocationConverter implements Converter<String, Location> {

    @Override
    public Location convert(String source) {
        double[] tokens = Arrays.stream(source.split(","))
                .map(String::trim)
                .mapToDouble(Double::parseDouble)
                .toArray();

        if (tokens.length != 2)
            throw new IllegalArgumentException("입력 값은 콤마(,)로 구분된 위도와 경도 값을 포함해야 합니다.");

        return new Location(tokens[0], tokens[1]);
    }

}
