API Documentation (Draft)
로그인 (Login) – POST /auth/sign-in

요청 본문 파라미터:

username (string, 필수): 사용자 로그인 ID

password (string, 필수): 사용자 비밀번호

응답 예시 (JSON):

{
"resMsg": "정상처리",
"resCode": 200,
"body": {
"id": "user123",
"name": "홍길동",
"role": "ROLE_USER",
"expirationTime": 1701638400000
}
}


설명: 제공된 사용자 정보로 인증을 시도하여 JWT 액세스 토큰과 리프레시 토큰을 발급합니다. 성공 시 토큰은 HTTP Only 쿠키로 설정되며, 응답 본문 body에 사용자 ID, 이름, 권한, 토큰만료시간 등을 포함한 정보를 반환합니다. 잘못된 자격 증명(아이디 또는 비밀번호가 틀린 경우)일 경우 resCode가 에러 코드를 가지며 resMsg에 오류 사유를 전달합니다 (예: "아이디 또는 비밀번호가 잘못되었습니다.", resCode: 500).

토큰 재발급 (Access Token Refresh) – GET /auth/verify

요청 파라미터: (없음) – 쿠키에 저장된 Authorization (액세스 토큰) 및 refreshToken 필요. 별도의 요청 본문이나 쿼리 파라미터는 없음.

응답 예시 (JSON):

{
"resMsg": "정상처리",
"resCode": 200,
"body": {
"id": "user123",
"name": "홍길동",
"role": "ROLE_USER",
"expirationTime": 1701642000000
}
}


설명: 만료된 액세스 토큰을 검증하고, 유효한 리프레시 토큰이 있을 경우 새로운 액세스 토큰을 발급합니다. 새로운 토큰이 발급되면 쿠키의 Authorization 값이 갱신되며, 응답의 body에는 사용자 정보와 새로운 토큰 만료 시간이 담깁니다. 액세스 토큰이 아직 유효한 경우 토큰 재발급 없이 현재 사용자 정보를 반환합니다. 유효한 토큰 없이는 JwtException에 의해 처리되며 resCode에 토큰 오류 코드(예: 205 만료된 토큰)가 설정된 응답이 반환됩니다.

상품 목록 조회 (Product List) – GET /item/list

요청 쿼리 파라미터:

searchType (string, 선택): 검색 유형 (예: 제목, 작성자 등 검색 기준)

keyword (string, 선택): 검색어 키워드

page (integer, 선택): 페이지 번호 (기본값 0, 0부터 시작)

pageSize (integer, 선택): 페이지 당 상품 수 (기본값 10)
※ 특정 카테고리의 상품만 조회하려면 /item/{category}/list 형식의 Endpoint를 사용하고, 위와 동일한 쿼리 파라미터를 적용합니다.

응답 예시 (JSON):

{
"resMsg": "정상처리",
"resCode": 200,
"body": {
"content": [
{ "id": 101, "name": "상품A", "dcrt": 0, "price": 12000, "filePath": "/images/itemA.jpg" },
{ "id": 102, "name": "상품B", "dcrt": 10, "price": 18000, "filePath": "/images/itemB.jpg" },
...
],
"page": 0,
"size": 10,
"totalElements": 45,
"totalPages": 5
}
}


설명: 상품 목록을 페이징 처리하여 조회합니다. 검색 유형과 키워드를 제공하면 해당 조건으로 상품을 필터링할 수 있습니다. dcrt 필드는 할인율(%)(없으면 0)이며, filePath는 상품 이미지 경로를 나타냅니다. 응답의 body에는 상품 목록 (content)과 페이지 정보(page, size, totalElements, totalPages)가 포함됩니다. 별도의 인증 없이 누구나 상품 목록을 조회할 수 있습니다.

상품 상세 조회 (Product Detail) – GET /item/view/{id}

요청 경로 파라미터:

id (integer, 필수): 조회할 상품의 고유 ID

응답 예시 (JSON):

{
"resMsg": "정상처리",
"resCode": 200,
"body": {
"id": 123,
"title": "예시 상품 제목",
"author": "홍길동",
"publisher": "예시 출판사",
"filePath": "/images/item123.jpg",
"price": 15000,
"stockCount": 7,
"state": "Y",
"comment": "상품 소개 코멘트...",
"information": "상세 설명 내용...",
"weight": 500
}
}


설명: 상품 ID에 해당하는 상세 정보를 조회합니다. title(제목), author(저자), publisher(출판사), 가격 및 재고(stockCount), 상품 상태(state: 활성화 여부, Y/N), 상품 설명(information) 등의 정보를 반환합니다. 인증이 필요 없이 상품 상세 정보 조회가 가능하며, 잘못된 ID를 요청하면 빈 결과 또는 오류 메시지를 반환합니다.

장바구니 담기 (Add to Cart) – POST /cart/add

요청 본문 파라미터:

list (array<object>, 필수): 장바구니에 담을 상품 목록 (객체 배열)

productId (integer, 필수): 장바구니에 추가할 상품 ID

quantity (integer, 필수): 해당 상품 수량
예시 요청:

{
"list": [
{ "productId": 101, "quantity": 2 },
{ "productId": 305, "quantity": 1 }
]
}


응답: 성공 시 HTTP 200 OK 상태 코드와 본문이 없는 응답을 반환합니다 (empty body).

설명: 현재 로그인한 사용자의 장바구니에 지정한 상품들을 추가합니다. 반드시 사용자 인증(로그인)이 된 상태에서만 호출 가능하며, 서버는 요청의 JWT 토큰으로 사용자 ID를 확인합니다. 요청 본문은 한 번에 여러 상품을 담을 수 있는 배열 구조로 되어 있습니다. 정상 처리되면 특별한 내용 없이 200 OK 응답을 반환하며, 클라이언트는 필요에 따라 별도의 장바구니 목록 조회 API를 통해 최신 장바구니 상태를 확인할 수 있습니다.

주문 생성 (Create Order) – POST /orderApi/create

요청 본문 파라미터:

impUid (string, 필수): 결제 승인 고유번호 (PG사에서 전달된 결제 거래 ID)

merchantUid (string, 필수): 주문 고유번호 (상점에서 생성한 주문 번호)

userName (string, 필수): 주문자 이름

userId (string, 필수): 주문자 회원 ID

buyerAddr (string, 필수): 수령인 주소

phone (string, 필수): 연락처 전화번호

email (string, 필수): 이메일 주소

cart (string, 선택): 문자열 "cart" – 장바구니 기반 주문인지 여부를 표시 ("cart"인 경우 주문 완료 후 해당 사용자의 장바구니를 비웁니다. 직접 구매인 경우 생략 또는 빈값)

productItems (array<object>, 필수): 주문 상품 목록

productId (integer, 필수): 주문한 상품 ID

quantity (integer, 필수): 구매 수량

productName (string, 필수): 상품 이름 (레코드 용도)

productPrice (string, 필수): 상품 가격 (레코드 용도)
예시 요청:

{
"impUid": "IMP_123456789",
"merchantUid": "ORD20251203-00000005",
"userName": "홍길동",
"userId": "user123",
"buyerAddr": "서울특별시 ...",
"phone": "010-1234-5678",
"email": "user123@example.com",
"cart": "cart",
"productItems": [
{ "productId": 101, "quantity": 2, "productName": "상품A", "productPrice": "12000" },
{ "productId": 305, "quantity": 1, "productName": "상품B", "productPrice": "18000" }
]
}


응답 예시 (JSON):

{
"resMsg": "정상처리",
"resCode": 200,
"body": "ORD20251203-00000005"
}


설명: 결제 완료 후 주문 정보를 서버에 전송하여 새로운 주문을 생성합니다. 요청 본문에는 결제승인 ID(impUid)와 주문번호(merchantUid) 및 배송/연락처 정보, 상품 목록을 포함해야 합니다. 서버는 결제 정보를 검증하고 주문 내역(주문, 주문상품, 배송 정보 등)을 생성합니다. cart 필드가 "cart"로 지정된 경우, 주문 생성 후 해당 사용자의 장바구니 내용을 비웁니다. 응답의 body에는 생성된 주문의 고유 주문번호(merchantUid)가 반환되며, 이를 통해 추후 주문 조회를 할 수 있습니다. (오류 발생 시 resCode가 500 등의 에러 코드로 반환되고, 메시지에 오류 원인이 담깁니다.)

주문 상세 조회 (Order Detail) – GET /order/receipt/{orderId}

요청 경로 파라미터:

orderId (string, 필수): 조회할 주문의 고유 주문번호 (merchantUid 형식, 예: ORD20251203-00000005)

응답 예시 (JSON):

{
"resMsg": "정상처리",
"resCode": 200,
"body": {
"orderId": 15,
"totalNetPrice": 42000,
"foReceiptOrderItemList": [
{ "itemId": 101, "itemName": "상품A", "itemNetPrice": 12000, "itemCount": 2 },
{ "itemId": 305, "itemName": "상품B", "itemNetPrice": 18000, "itemCount": 1 }
]
}
}


설명: 주문 완료 후 발급된 주문번호로 주문 상세 내역을 조회합니다. 응답 body에는 주문의 내부 식별자(orderId), 총 결제금액(totalNetPrice), 그리고 foReceiptOrderItemList 배열로 각 상품별 주문 수량 및 금액이 포함됩니다 (itemNetPrice는 해당 상품의 단위 가격, itemCount는 수량). 이 API는 보안을 위해 인증된 사용자만 접근 가능하며, 일반적으로 본인 계정의 주문내역만 조회할 수 있습니다. 주문번호에 해당하는 주문이 없거나 접근 권한이 없는 경우 적절한 오류 메시지 또는 빈 결과가 반환됩니다.