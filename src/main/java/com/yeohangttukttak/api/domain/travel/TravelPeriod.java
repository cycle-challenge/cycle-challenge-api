package com.yeohangttukttak.api.domain.travel;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

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


}
