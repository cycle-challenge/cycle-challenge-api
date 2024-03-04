package com.yeohangttukttak.api.domain.travel.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED) @Getter
public class TravelPeriod {

    private LocalDate startedOn;

    private LocalDate endedOn;

    public TravelPeriod(LocalDate startedOn, LocalDate endedOn) {
        if (endedOn.isBefore(startedOn)) {
            throw new IllegalArgumentException("종료 날짜는 시작 날짜 이후여야 합니다.");
        }

        this.startedOn = startedOn;
        this.endedOn = endedOn;
    }

    /**
     * 여행의 시작, 종류 일자를 일 단위로 계산 한다.
     * @return 여행 기간 (일 단위)
     */
    public int getDurationInDays() {
        Period period = Period.between(startedOn, endedOn);
        return period.getDays();
    }

    public Set<Season> getSeasons() {
        LocalDate curtDate = startedOn;

        Set<Season> seasons = EnumSet.noneOf(Season.class);

        while (curtDate.isBefore(endedOn)) {
            seasons.add(Season.valueOf(curtDate.getMonthValue()));
            curtDate = curtDate.plusMonths(1);
        }

        return seasons;
    }


}
