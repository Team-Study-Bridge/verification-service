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
        return flux -> flux
                .flatMap(request ->
                    portOneClient.verifyPayment(request.impUid())
                            .map(rsp -> {
                                boolean isValid = rsp.response().amount() == request.amount();
                                return new VerificationResponseMessage(request.purchaseId(), isValid, null); // PortOneClient는 Mono 반환
                            })
                            .onErrorResume(e -> {
                                log.error("[검증 예외 발생] id: {}, 원인: {}", request.purchaseId(), e.getMessage());
                                return Mono.just(new VerificationResponseMessage(request.purchaseId(), false, e.getMessage()));
                            })
                );
    }

}
