package com.yeohangttukttak.api.domain.travel.entity;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.interfaces.Bookmarkable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter
@NoArgsConstructor(access = PROTECTED)
public class Travel extends BaseEntity implements Bookmarkable {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "travel_id")
    private Long id;

    private String name;

    @Embedded
    private TravelPeriod period;

    @Enumerated(EnumType.STRING)
    private AccompanyType accompanyType;

    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    @Enumerated(EnumType.STRING)
    private Motivation motivation;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
    private List<Visit> visits = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "thumbnail_id")
    private Image thumbnail;

    @Builder
    public Travel(Long id, String name,
                  TravelPeriod period,
                  AccompanyType accompanyType,
                  TransportType transportType,
                  Motivation motivation,
                  Member member,
                  Visibility visibility,
                  Image thumbnail) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.accompanyType = accompanyType;
        this.transportType = transportType;
        this.motivation = motivation;
        this.member = member;
        this.visibility = visibility;
        this.thumbnail = thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public void setPeriod(TravelPeriod period) {
        this.period = period;
    }

    public void setVisits(List<Visit> visits) {
        Image previewImage = null;

        for (Visit visit : visits) {
            visit.setTravel(this);  // 각 Visit에 대한 Travel 참조 설정
            Place place = visit.getPlace();
            if (previewImage == null && !place.getImages().isEmpty()) {
                previewImage = place.getImages().get(0);
            }
            this.visits.add(visit);
        }

        this.setThumbnail(previewImage);
    }

    public void deleteVisit(Visit visit) {
        boolean isRemoved = this.visits.remove(visit);

        System.out.println(isRemoved);
    }


}
