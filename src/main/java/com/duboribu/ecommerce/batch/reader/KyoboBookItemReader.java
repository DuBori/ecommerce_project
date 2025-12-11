package com.duboribu.ecommerce.batch.reader;


import com.duboribu.ecommerce.batch.dto.CrawledBookDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class KyoboBookItemReader implements ItemReader<CrawledBookDto> {

    // 교보문고 카테고리별 베스트셀러 URL 매핑
    private static final Map<String, String> CATEGORY_MAP = new LinkedHashMap<>();
    
    static {
        CATEGORY_MAP.put("소설", "KOR01");
        CATEGORY_MAP.put("시/에세이", "KOR02");
        CATEGORY_MAP.put("인문", "KOR03");
        CATEGORY_MAP.put("경제/경영", "KOR09");
        CATEGORY_MAP.put("자기계발", "KOR10");
        CATEGORY_MAP.put("컴퓨터/IT", "KOR20");
    }

    private List<CrawledBookDto> crawledBooks;
    private int currentIndex = 0;

    @Override
    public CrawledBookDto read() {
        if (crawledBooks == null) {
            crawledBooks = crawlAllCategories();
            log.info("크롤링 완료 - 총 {}개 책 수집", crawledBooks.size());
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
                log.info("카테고리 크롤링 시작: {}", categoryName);
                
                // 교보문고 베스트셀러 페이지
                String url = "https://product.kyobobook.co.kr/bestseller/online?period=001&dsplDvsnCode=001&dsplTrgtDvsnCode=001&saleCmdtClstCode=" + categoryCode;
                
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                        .timeout(15000)
                        .get();

                // 상품 리스트 파싱 (교보문고 실제 HTML 구조에 맞게 수정 필요)
                Elements bookElements = doc.select("li.prod_item, div.prod_area");
                
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
            // 제목
            String title = book.select("span.prod_name, a.prod_info span.title").text();
            if (title.isEmpty()) {
                title = book.select("[class*=name], [class*=title]").first() != null 
                        ? book.select("[class*=name], [class*=title]").first().text() : "";
            }
            
            // 저자
            String author = book.select("span.author, span.prod_author").text();
            if (author.isEmpty()) {
                author = book.select("[class*=author]").text();
            }
            
            // 출판사
            String publisher = book.select("span.publisher, span.prod_publish").text();
            if (publisher.isEmpty()) {
                publisher = book.select("[class*=publish]").text();
            }
            
            // 가격
            String priceText = book.select("span.price, span.sale_price, span.prod_price").text();
            priceText = priceText.replaceAll("[^0-9]", "");
            int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText.split("\\s+")[0].replaceAll("[^0-9]", ""));

            // 빈 제목이면 건너뜀
            if (title.isEmpty()) {
                return null;
            }

            return CrawledBookDto.builder()
                    .title(title.length() > 100 ? title.substring(0, 100) : title)
                    .author(author.length() > 50 ? author.substring(0, 50) : author)
                    .publisher(publisher.length() > 50 ? publisher.substring(0, 50) : publisher)
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
}

