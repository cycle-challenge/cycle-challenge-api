package com.yeohangttukttak.api.domain.travel.dto;

import com.yeohangttukttak.api.domain.travel.entity.Visibility;
import com.yeohangttukttak.api.domain.visit.dto.VisitCreateDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TravelCreateDto {

    @NotBlank
    private String name;

    private Visibility visibility;

    List<VisitCreateDto> visits;

}