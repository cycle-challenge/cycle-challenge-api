package com.yeohangttukttak.api.domain.member.dto;

import jdk.jfr.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class VerificationEmailSentEvent extends Event {

    private String email;

    private String code;

}
