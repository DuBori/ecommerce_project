# 🛒 Ecommerce Project

전자상거래 백엔드 프로젝트입니다. 회원 가입부터 상품 조회, 장바구니, 주문 및 결제까지 쇼핑몰의 주요 기능을 구현하였습니다.  
클라우드가 아닌 **온프레미스 환경**에서 직접 서버를 운영하며 배포까지 진행했습니다.

---

## 🛠️ 사용 기술 스택

- **Backend**: Java 17, Spring Boot 3.x, Spring Security, JPA (Hibernate), QueryDSL
- **Database**: MySQL, Redis (세션/캐싱)
- **Infra**: Docker, Nginx, GitHub Actions, Ubuntu On-Premise Server
- **ETC**: JWT, RESTful API, Lombok, Validation

---

## 📦 주요 기능

| 기능 | 설명 |
|------|------|
| 회원가입/로그인 | 비밀번호 암호화, JWT 기반 인증 처리 |
| 상품 관리 | 상품 등록, 조회, 수정, 삭제 |
| 검색 기능 | QueryDSL을 활용한 동적 검색 (이름, 카테고리, 가격 범위) |
| 장바구니 | 세션 기반으로 장바구니 데이터 관리 |
| 주문 처리 | 주문 → 재고 차감, 주문 상태 변경 |
| 관리자 기능 | 상품/회원/주문 관리 일부 구현 (확장 가능) |

---

## 🧱 프로젝트 구조

```bash
src/
├── config/         # Spring Security, JWT 설정 등
├── controller/     # API 요청 처리
├── domain/         # Entity, Enum 등 도메인 구성
│   ├── member/
│   ├── product/
│   └── order/
├── repository/     # JPA/QueryDSL Repository
├── service/        # 비즈니스 로직 처리
├── dto/            # 요청/응답 DTO
└── util/           # 유틸성 클래스 (토큰, 로깅 등)
