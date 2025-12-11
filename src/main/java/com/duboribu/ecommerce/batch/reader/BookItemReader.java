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
public class BookItemReader implements ItemReader<CrawledBookDto> {

    // 알라딘으로 변경
    private static final String BASE_URL = "https://www.aladin.co.kr/shop/common/wbest.aspx?BestType=Bestseller";
    
    // 알라딘으로 변경
    private static final Map<String, String> CATEGORY_MAP = new LinkedHashMap<>();
    
    static {
        CATEGORY_MAP.put("건강", "55890");
        CATEGORY_MAP.put("경제경영", "170");
        CATEGORY_MAP.put("고전", "2105");
        CATEGORY_MAP.put("과학", "987");
        CATEGORY_MAP.put("달력/기타", "4395");
        CATEGORY_MAP.put("대학교재/전문서적", "8257");
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
                String url = BASE_URL + "&" + categoryCode;
                log.info("크롤링 URL: {}", url);
                
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                        .header("Referer", "https://store.kyobobook.co.kr/")
                        .timeout(15000)
                        .get();

                // 상품 리스트: div.flex > div.flex.items-top (각 책 아이템)
                Elements bookElements = doc.select("div.ss_book_box");
                
                log.info("발견된 책 요소 수: {}", bookElements.size());
                
                int count = 0;
                for (Element book : bookElements) {
                    if (count >= 3) break;

                    CrawledBookDto dto = parseBookElement(book, categoryCode, categoryName);
                    if (dto != null && dto.getTitle() != null && !dto.getTitle().isEmpty()) {
                        allBooks.add(dto);
                        count++;
                        log.info("책 파싱 완료: {} - {} ({}원)", categoryName, dto.getTitle(), dto.getPrice());
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
            // 제목: a.prod_link.line-clamp-2.font-medium
            String title = book.select("a.bo3").text();
            
            // 저자/출판사 정보: div.line-clamp-2.flex 안의 텍스트 (최서영 · 북로망스 · 2025.10.01)
            String authorPublisherInfo = book.select("div.ss_book_list ul li a").text();
            String author = "";
            String publisher = "";
            
            if (!authorPublisherInfo.isEmpty()) {
                Elements selectA = book.select("div.ss_book_list ul li a");
                int lastIndex = selectA.size() - 1;

                List<String> authors = new ArrayList<>();
                for (int i = 0; i < lastIndex; i++) {
                    authors.add(selectA.get(i).text());
                }
                author = String.join(", ", authors);
                publisher = selectA.get(lastIndex).text();
            }
            
            // 가격: span.font-bold 다음의 숫자 (17,550)
            // 할인가 우선, 없으면 정가
            String priceText = "";
            Element priceElement = book.selectFirst("span.ss_p2");
            if (priceElement != null) {
                priceText = priceElement.text();
            }
            
            // 가격 숫자만 추출
            int price = 0;
            if (!priceText.isEmpty()) {
                String numericPrice = priceText.replaceAll("[^0-9]", "");
                if (!numericPrice.isEmpty()) {
                    price = Integer.parseInt(numericPrice);
                }
            }
            
            // 책 소개: p.prod_introduction
            String introduction = book.select("span.ss_f_g2").text();

            // 빈 제목이면 건너뜀
            if (title.isEmpty()) {
                return null;
            }

            return CrawledBookDto.builder()
                    .title(truncate(title, 100))
                    .author(truncate(author, 50))
                    .publisher(truncate(publisher, 50))
                    .price(price)
                    /*.filePath(imageUrl)*/
                    .categoryCode(categoryCode)
                    .categoryName(categoryName)
                    .comment(truncate(introduction, 500))
                    .information("저자: " + author + ", 출판사: " + publisher)
                    .build();

        } catch (Exception e) {
            log.warn("책 파싱 실패: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 문자열 길이 제한
     */
    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength) : str;
    }
}
