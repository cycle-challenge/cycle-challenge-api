package com.yeohangttukttak.api.controller;

import com.yeohangttukttak.api.domain.place.Place;
import com.yeohangttukttak.api.domain.place.PlaceDTO;
import com.yeohangttukttak.api.domain.travel.Travel;
import com.yeohangttukttak.api.domain.travel.TravelDTO;
import com.yeohangttukttak.api.domain.travel.Visit;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.*;

@Data
public class VisitSearchDTO {

    private List<TravelDTO> travels;
    private List<PlaceDTO> places;

    public VisitSearchDTO (List<Visit> visits) {

        this.places = visits.stream()
                .map(Visit::getPlace).distinct().map(PlaceDTO::new).toList();

        this.travels = visits.stream()
                .collect(groupingBy(Visit::getTravel, mapping(Visit::getPlace, toList())))
                .entrySet().stream()
                .sorted(comparingByKey(comparing(Travel::getId)))
                .map(entry -> new TravelDTO(entry.getKey(), getFirstElement(entry.getValue(), Place::getFiles)))
                .toList();

    }

    private static <T, R> R getFirstElement(List<T> list, Function<T, List<R>> mapper) {
        return list.stream()
                .map(mapper)
                .filter(files -> !files.isEmpty())
                .findFirst()
                .map(files -> files.get(0))
                .orElse(null); // Consider handling this case more gracefully
    }
}
