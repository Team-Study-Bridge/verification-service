package org.example.verificationservice.event;

public record VerificationRequestMessage(
        Long purchaseId,
        String impUid,
        int amount // 결제된 금액
) {}
