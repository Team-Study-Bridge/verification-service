package org.example.verificationservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.verificationservice.event.VerificationRequestMessage;
import org.example.verificationservice.event.VerificationResponseMessage;
import org.example.verificationservice.portone.PortOneClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class VerificationFunctionConfig {

    private final PortOneClient portOneClient;

    @Bean
    public Function<Flux<VerificationRequestMessage>, Flux<VerificationResponseMessage>> verify() {
        return flux -> flux.flatMap(request ->
                portOneClient.verifyPayment(request.impUid())
                        .map(rsp -> {
                            int expected = request.amount();
                            int actual = rsp.response().amount();

                            boolean isValid = expected == actual;
                            String reason = isValid ? null :
                                    String.format("결제 금액 불일치: 예상=%d, 실제=%d", expected, actual);

                            log.info("[검증 결과] id={}, isValid={}, reason={}", request.purchaseId(), isValid, reason);
                            return new VerificationResponseMessage(request.purchaseId(), isValid, reason);
                        })
                        .onErrorResume(e -> {
                            String errorMessage = "PortOne 통신 실패: " + e.getMessage();
                            log.error("[검증 예외 발생] id={}, reason={}", request.purchaseId(), errorMessage);
                            return Mono.just(new VerificationResponseMessage(request.purchaseId(), false, errorMessage));
                        })
        );
    }
}
