package com.yeohangttukttak.api.domain.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenHeader {

    String kid;

    String alg;

    String typ;

}