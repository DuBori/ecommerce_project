package com.duboribu.ecommerce.batch.reader;

import com.duboribu.ecommerce.batch.dto.CrawledBookDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@StepScope
public class KyoboBookItemReader implements ItemReader<CrawledBookDto> {

    // 교보문고 베스트셀러 URL: https://store.kyobobook.co.kr/bestseller/online/daily/domestic/{categoryCode}?page={page}
    private static final String BASE_URL = "https://store.kyobobook.co.kr/bestseller/online/daily/domestic";
    
    // 교보문고 카테고리 코드 매핑 (URL 경로 기준)
    private static final Map<String, String> CATEGORY_MAP = new LinkedHashMap<>();
    
    static {
        CATEGORY_MAP.put("소설", "01");
        CATEGORY_MAP.put("시/에세이", "02");
        CATEGORY_MAP.put("인문", "03");
        CATEGORY_MAP.put("가정/육아", "04");
        CATEGORY_MAP.put("요리", "05");
        CATEGORY_MAP.put("건강", "06");
        CATEGORY_MAP.put("취미/실용/스포츠", "07");
        CATEGORY_MAP.put("경제/경영", "08");
        CATEGORY_MAP.put("자기계발", "09");
        CATEGORY_MAP.put("정치/사회", "10");
        CATEGORY_MAP.put("역사/문화", "11");
        CATEGORY_MAP.put("종교", "12");
        CATEGORY_MAP.put("예술/대중문화", "13");
        CATEGORY_MAP.put("컴퓨터/IT", "19");
    }

    @Value("#{jobParameters['page'] ?: '1'}")
    private String page;

    private List<CrawledBookDto> crawledBooks;
    private int currentIndex = 0;

    @Override
    public CrawledBookDto read() {
        if (crawledBooks == null) {
            crawledBooks = crawlAllCategories();
            log.info("크롤링 완료 - 총 {}개 책 수집 (page: {})", crawledBooks.size(), page);
        }

        if (currentIndex < crawledBooks.size()) {
            return crawledBooks.get(currentIndex++);
        }
        
        // 배치 완료 후 초기화
        crawledBooks = null;
        currentIndex = 0;
        return null;
    }

    private List<CrawledBookDto> crawlAllCategories() {
        List<CrawledBookDto> allBooks = new ArrayList<>();

        for (Map.Entry<String, String> entry : CATEGORY_MAP.entrySet()) {
            String categoryName = entry.getKey();
            String categoryCode = entry.getValue();

            try {
                log.info("카테고리 크롤링 시작: {} (코드: {}, 페이지: {})", categoryName, categoryCode, page);
                
                // URL 구성: https://store.kyobobook.co.kr/bestseller/online/daily/domestic/01?page=1
                String url = BASE_URL + "/" + categoryCode + "?page=" + page;
                log.info("크롤링 URL: {}", url);
                
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                        .header("Referer", "https://store.kyobobook.co.kr/")
                        .timeout(15000)
                        .get();

                // 상품 리스트 파싱 (실제 HTML 구조에 맞게 선택자 수정 필요)
                Elements bookElements = doc.select("li[class*=prod], div[class*=prod], ul[class*=list] > li");
                
                log.info("발견된 요소 수: {}", bookElements.size());
                
                int count = 0;
                for (Element book : bookElements) {
                    if (count >= 3) break;

                    CrawledBookDto dto = parseBookElement(book, categoryCode, categoryName);
                    if (dto != null && dto.getTitle() != null && !dto.getTitle().isEmpty()) {
                        allBooks.add(dto);
                        count++;
                        log.info("책 파싱 완료: {} - {}", categoryName, dto.getTitle());
                    }
                }

                // 크롤링 예의 - 1초 대기
                Thread.sleep(1000);

            } catch (Exception e) {
                log.error("크롤링 실패 - 카테고리: {}, 오류: {}", categoryName, e.getMessage());
            }
        }

        return allBooks;
    }

    private CrawledBookDto parseBookElement(Element book, String categoryCode, String categoryName) {
        try {
            // 제목 (여러 선택자 시도)
            String title = selectText(book, 
                "span.prod_name", 
                "a.prod_info span.title",
                "[class*=name]",
                "[class*=title]",
                "a[class*=prod] span"
            );
            
            // 저자
            String author = selectText(book,
                "span.author",
                "span.prod_author",
                "[class*=author]",
                "span.info_author"
            );
            
            // 출판사
            String publisher = selectText(book,
                "span.publisher",
                "span.prod_publish",
                "[class*=publish]",
                "span.info_publish"
            );
            
            // 가격
            String priceText = selectText(book,
                "span.price",
                "span.sale_price",
                "span.prod_price",
                "[class*=price]"
            );
            priceText = priceText.replaceAll("[^0-9]", "");
            int price = 0;
            if (!priceText.isEmpty()) {
                // 첫 번째 숫자 그룹만 추출 (정가와 할인가가 붙어있는 경우)
                String firstNumber = priceText.length() > 10 ? priceText.substring(0, priceText.length() / 2) : priceText;
                price = Integer.parseInt(firstNumber);
            }
            
            // 이미지
           /* String imageUrl = book.select("img").attr("src");
            if (imageUrl.isEmpty()) {
                imageUrl = book.select("img").attr("data-src");
            }
            if (imageUrl.isEmpty()) {
                imageUrl = book.select("img").attr("data-lazy-src");
            }*/

            // 빈 제목이면 건너뜀
            if (title.isEmpty()) {
                return null;
            }

            return CrawledBookDto.builder()
                    .title(truncate(title, 100))
                    .author(truncate(author, 50))
                    .publisher(truncate(publisher, 50))
                    .price(price)
                    .categoryCode(categoryCode)
                    .categoryName(categoryName)
                    .comment(title + " - " + author)
                    .information("출판사: " + publisher)
                    .build();

        } catch (Exception e) {
            log.warn("책 파싱 실패: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 여러 선택자를 시도하여 첫 번째로 매칭되는 텍스트 반환
     */
    private String selectText(Element element, String... selectors) {
        for (String selector : selectors) {
            Element found = element.selectFirst(selector);
            if (found != null && !found.text().trim().isEmpty()) {
                return found.text().trim();
            }
        }
        return "";
    }
    
    /**
     * 문자열 길이 제한
     */
    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength) : str;
    }
}
