package com.yeohangttukttak.api.domain.member.api;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yeohangttukttak.api.domain.member.dao.BlockRepository;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dto.*;
import com.yeohangttukttak.api.domain.member.entity.*;
import com.yeohangttukttak.api.domain.member.service.*;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberAuthRenewService authRenewService;
    private final MemberFindProfileService findProfileService;

    private final GoogleSignInService googleSignInService;
    private final GoogleRevokeService googleRevokeService;

    private final AppleSignInService appleSignInService;
    private final AppleRevokeService appleRevokeService;

    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;

    @GetMapping("/profile")
    public ApiResponse<MemberProfileDTO> findProfile(HttpServletRequest request) {

        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        return new ApiResponse<>(findProfileService.findByEmail(accessToken.getEmail()));

    }

    @PostMapping("/auth/renew")
    public ResponseEntity<ApiResponse<MemberAuthDTO>> renew(@RequestBody MemberAuthRenewRequest body) {
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(new ApiResponse<>(authRenewService.renew(body.getRefreshToken(), body.getEmail())));
    }

    @GetMapping("/sign-in/google")
    public ResponseEntity<Void> signInGoogle(@RequestParam("code") String code) throws JsonProcessingException {

        MemberAuthDTO authDTO = googleSignInService.call(code);

        // 앱의 Custom Scheme 으로 Redirection
        String redirectUri = UriComponentsBuilder.fromUriString("com.yeohaeng.ttukttak.app:/")
                .queryParam("status", "success")
                .queryParam("access-token", authDTO.getAccessToken())
                .queryParam("refresh-token", authDTO.getRefreshToken())
                .encode().toUriString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUri)
                .build();
    }

    @PostMapping("/sign-in/apple")
    public ResponseEntity<Void> signInApple(@RequestParam("code") String code) throws JsonProcessingException {

        MemberAuthDTO authDTO = appleSignInService.call(code);

        // 앱의 Custom Scheme 으로 Redirection
        String redirectUri = UriComponentsBuilder.fromUriString("com.yeohaeng.ttukttak.app:/")
                .queryParam("status", "success")
                .queryParam("access-token", authDTO.getAccessToken())
                .queryParam("refresh-token", authDTO.getRefreshToken())
                .encode().toUriString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUri)
                .build();
    }

    @Transactional
    @DeleteMapping("/")
    public ApiResponse<Void> deleteUser(HttpServletRequest request) {

        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        Member member = memberRepository.findByEmail(accessToken.getEmail())
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        switch (member.getAuthType()) {
            case APPLE -> appleRevokeService.call(member.getRefreshToken());
            case GOOGLE -> googleRevokeService.call(member.getRefreshToken());
            default -> throw new ApiException(ApiErrorCode.INTERNAL_SERVER_ERROR);
        }

        memberRepository.delete(member);

        return new ApiResponse<>(null);

    }

    @Transactional
    @PostMapping("/blocks/{id}")
    public ResponseEntity<ApiResponse<Void>> blockMember(HttpServletRequest request,
                                       @PathVariable("id") Long id) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        Member member = memberRepository.findByEmail(accessToken.getEmail())
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        Member targetMember = memberRepository.find(id)
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        // 1. 자기 자신을 차단하는 경우를 검사한다.
        if (member.equals(targetMember)) {
            throw new ApiException(ApiErrorCode.INVALID_BLOCK_MEMBER);
        }

        // 2. 이미 차단된 이력이 있는지 검사한다.
        blockRepository.find(new BlockId(member.getId(), targetMember.getId()))
                .ifPresent((block) -> {
                    throw new ApiException(ApiErrorCode.MEMBER_ALREADY_BLOCKED);
                });

        // 3. 사용자를 차단한다.
        Block block = new Block(member, targetMember);
        blockRepository.save(block);

        URI uri = URI.create(String.format("/api/v1/members/blocks/%s", id));

        return ResponseEntity.created(uri)
                .body(new ApiResponse<>(null));
    }

    @GetMapping("/blocks")
    public ApiResponse<List<MemberDTO>> findBlockedMembers(HttpServletRequest request) {

        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        Member member = memberRepository.findByEmail(accessToken.getEmail())
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        List<MemberDTO> memberDTOS = member.getBlocks().stream()
                .map(Block::getBlocked)
                .map(MemberDTO::new)
                .toList();

        return new ApiResponse<>(memberDTOS);

    }


    @Transactional
    @DeleteMapping("/blocks/{id}")
    public ApiResponse<Void> unblockMember(HttpServletRequest request,
                                           @PathVariable("id") Long id) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        Member member = memberRepository.findByEmail(accessToken.getEmail())
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        // 1. 이미 차단된 이력이 있는지 검사한다.
        Block block = blockRepository.find(new BlockId(member.getId(), id))
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        // 2. 차단 이력을 삭제한다.
        blockRepository.delete(block);

        return new ApiResponse<>(null);
    }

}
