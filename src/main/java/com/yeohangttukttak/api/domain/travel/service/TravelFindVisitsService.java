package com.yeohangttukttak.api.domain.travel.service;

import com.yeohangttukttak.api.domain.travel.dto.TravelDayBoundDTO;
import com.yeohangttukttak.api.domain.travel.dto.TravelDaySummaryDTO;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@RequiredArgsConstructor
@Service
public class TravelFindVisitsService {

    private final VisitRepository visitRepository;

    public List<TravelDaySummaryDTO> find(Long id) {
        List<Visit> visits = visitRepository.findByTravel(id);

        Map<Integer, List<Visit>> groupedVisits = visits.stream()
                .collect(Collectors.groupingBy(Visit::getDayOfTravel));

        return groupedVisits.entrySet().stream()
                .map(this::convertToDTO)
                .toList();
    }

    private TravelDaySummaryDTO convertToDTO(Entry<Integer, List<Visit>> entry) {
        List<Visit> visits = entry.getValue().stream().sorted(comparing(Visit::getOrderOfVisit)).toList();
        TravelDayBoundDTO travelDayBoundDTO = calculateEnvelopeForVisits(visits);
        return new TravelDaySummaryDTO(entry.getKey(), visits, travelDayBoundDTO);
    }


    private TravelDayBoundDTO calculateEnvelopeForVisits(List<Visit> visits) {
        Envelope entireEnvelope = new Envelope();
        List<Envelope> partialEnvelopes = new ArrayList<>();

        List<Point> points = visits.stream()
                .map(visit -> visit.getPlace().getLocation().getPoint())
                .toList();

        // 전체 Envelope을 계산
        points.forEach(point -> entireEnvelope.expandToInclude(point.getCoordinate()));

        // 각 점을 순회하면서 부분 Envelopes 계산
        points.forEach(currentPoint -> {
            Envelope bufferEnvelope = currentPoint.buffer(0.01).getEnvelopeInternal();
            Envelope partialEnvelope = new Envelope();

            points.stream()
                    .map(nextPoint -> nextPoint.buffer(0.01).getEnvelopeInternal())
                    .filter(nextEnvelop -> nextEnvelop.intersects(bufferEnvelope))
                    .forEach(partialEnvelope::expandToInclude);

            partialEnvelopes.add(partialEnvelope);
        });

        return new TravelDayBoundDTO(entireEnvelope, partialEnvelopes);
    }
}
