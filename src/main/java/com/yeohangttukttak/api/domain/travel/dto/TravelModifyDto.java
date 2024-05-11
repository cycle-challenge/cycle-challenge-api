package com.yeohangttukttak.api.domain.travel.dto;

import com.yeohangttukttak.api.domain.travel.entity.Visibility;
import com.yeohangttukttak.api.domain.visit.dto.VisitCreateDto;
import com.yeohangttukttak.api.domain.visit.dto.VisitModifyDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TravelModifyDto {

    @NotBlank
    private String name;

    private LocalDate startedOn;

    private LocalDate endedOn;

    private Visibility visibility;

    List<VisitModifyDto> visits;

}