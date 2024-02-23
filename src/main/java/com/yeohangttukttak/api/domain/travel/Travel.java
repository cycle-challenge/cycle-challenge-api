package com.yeohangttukttak.api.domain.travel;

import com.yeohangttukttak.api.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
public class Travel extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private TravelPeriod period;

    @Enumerated(EnumType.STRING)
    private AccompanyType accompanyType;

    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    @Enumerated(EnumType.STRING)
    private TravelMotivation motivation;

}
