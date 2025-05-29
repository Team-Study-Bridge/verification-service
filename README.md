# 💳 verification-service

MSA 환경에서 **결제 내역 검증**을 담당하는 마이크로서비스입니다.

[![codecov](https://codecov.io/gh/Team-Study-Bridge/verification-service/branch/develop/graph/badge.svg)](https://app.codecov.io/gh/Team-Study-Bridge/verification-service/branch/develop)

---

## 🚀 주요 기술 스택
- **Spring Boot 3.4**
- **WebFlux + R2DBC** – 비동기 논블로킹 처리
- **RabbitMQ (Spring Cloud Stream)** – 이벤트 기반 아키텍처
- **PortOne 결제 검증 API 연동**
- **JaCoCo + GitHub Actions + Codecov** – 테스트 커버리지 자동화

---

## 🧩 주요 기능
- RabbitMQ 기반 **결제 검증 요청 메시지 수신**
- PortOne API를 통한 **결제 내역 검증 처리**
- Reactor 기반 **비동기 검증 로직**
- 검증 결과 메시지를 **RabbitMQ 이벤트로 송신**

---

## ✅ 테스트 및 커버리지

- 모든 테스트는 **GitHub Actions**에서 자동으로 실행됩니다.
- 테스트 실행 후, `JaCoCo`가 커버리지 리포트를 생성하고, `Codecov`에 업로드됩니다.
- PR 생성 시, Codecov 봇이 **커버리지 변화**를 분석하고 댓글로 알려줍니다.

### 🎯 커버리지 기준
| 항목         | 기준 (Target) | 허용 오차 (Threshold) |
|--------------|---------------|------------------------|
| 전체 커버리지 | 80% 이상      | ±1%                    |
| 변경된 코드   | 70% 이상      | ±1%                    |

📌 기준 미달 시 PR이 실패 처리되며, 기존 기준보다 감소할 경우 경고가 발생합니다.