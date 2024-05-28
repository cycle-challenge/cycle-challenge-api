package com.yeohangttukttak.api.domain.member.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.dto.MemberAuthDTO;
import com.yeohangttukttak.api.domain.member.dto.SocialSignInRequestDto;
import com.yeohangttukttak.api.domain.member.dto.TokenHeader;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class GoogleSignInService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberRepository memberRepository;

    private final Map<String, RSAKey> rsaKeyMap = new HashMap<>();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public MemberAuthDTO call(SocialSignInRequestDto signInRequestDto) throws IOException {

        if (rsaKeyMap.isEmpty()) getRSAKeys();
        parseIdToken(signInRequestDto.getIdToken());

        Member member = memberRepository.findByEmail(signInRequestDto.getEmail())
                .orElseGet(() -> {
                    Member newMember = Member.fromProfile(signInRequestDto);
                    memberRepository.save(newMember);
                    return newMember;
                });

        Instant now = Instant.now();

        JwtToken accessToken = JwtToken.issueAccessToken(signInRequestDto.getEmail(), now);
        JwtToken refreshToken = JwtToken.issueRefreshToken(signInRequestDto.getEmail(), now);

        refreshTokenRepository.save(member.getId(), refreshToken.getToken(), JwtToken.refreshTokenTTL);

        return new MemberAuthDTO(accessToken, refreshToken);

    }

    private void parseIdToken(String idToken) {
        try {

            String[] parts = idToken.split("\\.");

            TokenHeader header = objectMapper.readValue(
                    Base64.getUrlDecoder().decode(parts[0]), TokenHeader.class);

            RSAKey rsaKey = rsaKeyMap.get(header.getKid());

            if (rsaKey == null) throw new ApiException(ApiErrorCode.INVALID_AUTHORIZATION);

            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(rsaKey.toPublicKey());
            sig.update((parts[0] + "." + parts[1]).getBytes(StandardCharsets.UTF_8));

            byte[] signatureBytes = Base64.getUrlDecoder().decode(parts[2]);

            if (!sig.verify(signatureBytes)) throw new ApiException(ApiErrorCode.INVALID_AUTHORIZATION);


        } catch ( NoSuchAlgorithmException | JOSEException | InvalidKeyException | SignatureException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void getRSAKeys() {
        try {

            JWKSet jwkSet = JWKSet.load(
                    new URL("https://www.googleapis.com/oauth2/v3/certs"));

            for (JWK jwk : jwkSet.getKeys()) {
                if (jwk instanceof RSAKey rsaKey) {
                    rsaKeyMap.put(rsaKey.getKeyID(), rsaKey);
                }
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
