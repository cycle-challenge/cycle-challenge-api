package com.yeohangttukttak.api.domain.travel.dto;

import com.yeohangttukttak.api.domain.travel.entity.ReportReason;
import lombok.Data;

@Data
public class ReportCreateDto {

    private ReportReason reason;

    private String description;

}