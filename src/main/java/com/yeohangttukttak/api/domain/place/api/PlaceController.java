package com.yeohangttukttak.api.domain.place.api;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Block;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.dao.PlaceReviewRepository;
import com.yeohangttukttak.api.domain.place.dao.PlaceSuggestionRepository;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceReviewCreateDto;
import com.yeohangttukttak.api.domain.place.dto.PlaceReviewDto;
import com.yeohangttukttak.api.domain.place.dto.PlaceReviewReportDto;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.entity.PlaceReview;
import com.yeohangttukttak.api.domain.place.entity.PlaceSuggestion;
import com.yeohangttukttak.api.domain.place.service.PlaceFindBookmarkedService;
import com.yeohangttukttak.api.domain.travel.dao.TravelRepository;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.common.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;

import static java.lang.String.format;
import static java.util.Comparator.comparing;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceReviewRepository placeReviewRepository;
    private final PlaceSuggestionRepository placeSuggestionRepository;
    private final PlaceRepository placeRepository;
    private final TravelRepository travelRepository;
    private final MemberRepository memberRepository;
    private final PlaceFindBookmarkedService placeBookmarkFindService;

    @PostMapping("/{id}/reviews")
    @Transactional
    public ResponseEntity<Void> createReview(
            HttpServletRequest request,
            @PathVariable("id") Long placeId,
            @RequestBody PlaceReviewCreateDto body) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        Member member = memberRepository.findByEmail(accessToken.getEmail()).orElseThrow(() ->
                new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        Place place = placeRepository.find(placeId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.PLACE_NOT_FOUND));

        Long id = placeReviewRepository.save(
                new PlaceReview(body.getRating(), body.getWantsToRevisit(), body.getComment(), place, member));

        URI entityUri = URI.create(format("/api/v1/places/%d/comments/%d", place.getId(), id));

        return ResponseEntity.created(entityUri).build();
    }

    @GetMapping("/autocomplete")
    public ApiResponse<List<PlaceSuggestion>> autoComplete(@RequestParam("query") String query) {
        return new ApiResponse<>(placeSuggestionRepository.search(query));
    }

    @GetMapping("/{id}/images")
    public ApiResponse<List<ImageDTO>> getPlaceImages(
            @PathVariable("id") Long id,
            @ModelAttribute PageSearch search) {
        Place place = placeRepository.find(id)
                .orElseThrow(() -> new ApiException(ApiErrorCode.PLACE_NOT_FOUND));

        return new ApiResponse<>(place.getImages().stream()
                .map(ImageDTO::new)
                .toList());
    }

    @GetMapping("/bookmarked")
    public ApiResponse<List<PlaceDTO>> findBookmarkedPlace(HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        List<Place> places = placeBookmarkFindService.call(accessToken.getEmail());
        return new ApiResponse<>(places.stream().map(PlaceDTO::new).toList());
    }

    @GetMapping("/{id}")
    public ApiResponse<PlaceDTO> find(@PathVariable("id") Long id) {
        Place place = placeRepository.find(id)
                .orElseThrow(() -> new ApiException(ApiErrorCode.PLACE_NOT_FOUND));

        PlaceReviewReportDto review = placeReviewRepository.createReports(List.of(id)).stream().findFirst()
                .orElseThrow(() -> new ApiException(ApiErrorCode.PLACE_NOT_FOUND));

        return new ApiResponse<>(new PlaceDTO(place, review));
    }

    @GetMapping("/{id}/reviews")
    public ApiResponse<List<PlaceReviewDto>> findReviews(HttpServletRequest request,
                                                         @PathVariable("id") Long id) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        Member member = memberRepository.findByEmail(accessToken.getEmail())
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        List<Member> blockMembers = member.getBlocks().stream().map(Block::getBlocked).toList();

        Place place = placeRepository.find(id)
                .orElseThrow(() -> new ApiException(ApiErrorCode.PLACE_NOT_FOUND));

        List<PlaceReviewDto> reviews = placeReviewRepository.findByPlace(place.getId()).stream()
                .filter((review) -> !blockMembers.contains(review.getMember()))
                .map(PlaceReviewDto::new)
                .sorted(comparing(PlaceReviewDto::getCreatedAt).reversed())
                .toList();

        return new ApiResponse<>(reviews);
    }

    @GetMapping("/reviews/my")
    public ApiResponse<List<PlaceReviewDto>> findMyReviews(HttpServletRequest request) {

        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        Member member = memberRepository.findByEmail(accessToken.getEmail())
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        List<PlaceReviewDto> reviews = placeReviewRepository.findByMember(member.getId())
                .stream()
                .map(PlaceReviewDto::new)
                .sorted(comparing(PlaceReviewDto::getCreatedAt).reversed())
                .toList();

        return new ApiResponse<>(reviews);
    }

    @Transactional
    @DeleteMapping("/reviews/{id}")
    public ApiResponse<Void> deleteReviews(HttpServletRequest request, @PathVariable("id") Long id) {

        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        Member member = memberRepository.findByEmail(accessToken.getEmail())
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        PlaceReview review = placeReviewRepository.find(id)
                .orElseThrow(() -> new ApiException(ApiErrorCode.PLACE_NOT_FOUND));

        if (!member.equals(review.getMember())) {
            throw new ApiException(ApiErrorCode.PERMISSION_DENIED);
        }

        placeReviewRepository.delete(review);

        return new ApiResponse<>(null);
    }
    @GetMapping("/{id}/travels")
    public ApiResponse<List<TravelDTO>> findTravels(@PathVariable("id") Long id) {
        Place place = placeRepository.find(id)
                .orElseThrow(() -> new ApiException(ApiErrorCode.PLACE_NOT_FOUND));

        List<TravelDTO> travels = travelRepository.findAllByPlace(place.getId()).stream()
                .map(TravelDTO::new)
                .toList();

        return new ApiResponse<>(travels);
    }

}
