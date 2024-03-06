package com.yeohangttukttak.api.global.config.converter;

import com.yeohangttukttak.api.domain.place.dto.LocationDTO;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;

public class StringToLocationDTOConverter implements Converter<String, LocationDTO> {

    @Override
    public LocationDTO convert(String source) {
        double[] tokens = Arrays.stream(source.split(","))
                .map(String::trim)
                .mapToDouble(Double::parseDouble)
                .toArray();

        if (tokens.length != 2)
            throw new IllegalArgumentException("입력 값은 콤마(,)로 구분된 위도와 경도 값을 포함해야 합니다.");

        return new LocationDTO(tokens[0], tokens[1]);
    }

}
