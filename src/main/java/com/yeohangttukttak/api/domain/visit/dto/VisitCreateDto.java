package com.yeohangttukttak.api.domain.visit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class VisitCreateDto {

    @NotNull
    private Long placeId;

}
