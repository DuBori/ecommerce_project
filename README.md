https://duboribu.com/
# 🛒 Ecommerce Project

전자상거래 백엔드 프로젝트입니다.  
회원가입, 상품 조회, 장바구니, 주문 및 결제까지 쇼핑몰의 핵심 기능을 구현했습니다.  
초기에는 온프레미스 배포 환경으로 구성했으며, 현재는 AWS 기반의 클라우드 인프라(RDS, S3, EC2)로 전환 중입니다.  
CI/CD는 GitHub Actions 기반 자동화 배포를 구성했고, 구조적 리팩토링 및 문서화를 진행 중입니다.

---

## 🛠 사용 기술 스택

- **Backend**: Java 17, Spring Boot 3.x, Spring Security, JPA (Hibernate), QueryDSL
- **Database**: MySQL (RDS)
- **Infra**: Docker, Nginx, AWS EC2 / RDS / Route53 / SecurityGroup
- **DevOps**: GitHub Actions (CI/CD), Docker Compose
- **ETC**: JWT, RESTful API, Lombok, Validation

---

## 📦 주요 기능

| 기능           | 설명 |
|----------------|------|
| 회원가입/로그인 | 비밀번호 암호화, JWT 발급 및 인증 필터 구현 |
| 상품 관리       | 등록, 조회, 수정, 삭제 기능 |
| 검색 기능       | QueryDSL 기반 복합 검색 (이름, 카테고리, 가격 범위) |
| 장바구니        | 세션 기반 장바구니 구현 |
| 주문 처리       | 주문 → 재고 차감 → 상태 변경 로직 |
| 관리자 기능     | 상품/회원/주문 관리 일부 구현 (확장 가능 구조) |

---

## 🧱 프로젝트 구조

- 도메인 기준 폴더 구분:
  - `admin/` : 관리자 기능 (상품/회원/주문 등)
  - `front/` : 사용자 페이지 (상품 조회, 장바구니 등)
  - `warehouse/` : 물류 로직
  - `auth/` : JWT 및 로그인 인증 관련 처리
  - `entity/` : JPA 엔티티 정의 (Order, Product, Cart, etc)
  - `config/` : Spring 설정 파일 (Security, CORS 등)
  - `repository/` : JPA/QueryDSL 기반 저장소
  - `common/` : 공통 예외, 공통 응답, 로깅 등 유틸리티

---

## 🧠 기술적 핵심 구현 포인트

- **QueryDSL 동적 복합 검색**  
  `BooleanBuilder` + `null safe where절` 조합으로 상품명, 카테고리, 가격범위 동시 검색을 유연하게 처리함  
  → 쿼리 가독성과 재사용성 개선

- **JPA 성능 최적화 (N+1 해결)**  
  `fetch join`, `@BatchSize`를 통해 연관된 Entity를 효율적으로 로딩  
  → 주문 목록, 상품 리스트 등에서 쿼리 수 대폭 감소

- **Spring Security + JWT 기반 인증 처리**  
  `JwtAuthenticationFilter`를 커스터마이징해 로그인 시 JWT 토큰 발급 → 요청마다 인증 필터를 통해 사용자 검증  
  → 무상태 인증 구조, API 인증 처리 일관성 확보

- **AOP 기반 로깅 및 예외 처리**  
  `@Aspect`로 요청-응답 로깅, `@ControllerAdvice`로 예외 발생 시 일관된 JSON 응답 구조 제공  
  → 장애 추적 가능성 확보, 코드 일관성 유지

---

QueryDSL
복합 검색 기능 (상품명 + 카테고리 + 가격 범위)을 BooleanBuilder와 where절 null 무시 방식으로 구현하여 가독성과 재사용성 향상.

N+1 문제 해결
JPA에서 fetch join과 @BatchSize를 통해 쿼리 수 감소 및 성능 최적화.

Spring Security + JWT
커스텀 JwtAuthenticationFilter를 통해 로그인 후 JWT 토큰을 발급하고, 요청마다 인증 처리 수행.

AOP + Global Exception
AOP를 통해 공통 로깅 처리, @ControllerAdvice로 일관된 예외 응답 처리.

---
## 📊 ERD (Entity Relationship Diagram)
<img width="1095" height="989" alt="image" src="https://github.com/user-attachments/assets/f7270c51-dad2-4823-81b5-14397905b10d" />


