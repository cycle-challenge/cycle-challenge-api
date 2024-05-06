package com.yeohangttukttak.api.domain.place.service;

import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceFindByGooglePlaceIdService {

    private final PlaceRepository placeRepository;

    public Place call(String googlePlaceId) {

        return placeRepository.findByGooglePlaceId(googlePlaceId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.PLACE_NOT_FOUND));

    }

}
