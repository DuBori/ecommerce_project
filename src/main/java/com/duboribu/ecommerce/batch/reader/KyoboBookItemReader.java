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

                // 상품 리스트: div.flex > div.flex.items-top (각 책 아이템)
                Elements bookElements = doc.select("div.flex.items-top.justify-start");
                
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
            String title = book.select("a.prod_link.line-clamp-2.font-medium").text();
            if (title.isEmpty()) {
                title = book.select("a.prod_link").text();
            }
            
            // 저자/출판사 정보: div.line-clamp-2.flex 안의 텍스트 (최서영 · 북로망스 · 2025.10.01)
            String authorPublisherInfo = book.select("div.line-clamp-2.flex.overflow-hidden").text();
            String author = "";
            String publisher = "";
            
            if (!authorPublisherInfo.isEmpty()) {
                // " · " 또는 "·"로 분리
                String[] parts = authorPublisherInfo.split("\\s*·\\s*");
                if (parts.length >= 1) {
                    author = parts[0].trim();
                }
                if (parts.length >= 2) {
                    publisher = parts[1].trim();
                }
            }
            
            // 가격: span.font-bold 다음의 숫자 (17,550)
            // 할인가 우선, 없으면 정가
            String priceText = "";
            Element priceElement = book.selectFirst("span.inline-block.align-top.fz-16 span.font-bold");
            if (priceElement != null) {
                priceText = priceElement.text();
            }
            if (priceText.isEmpty()) {
                // 정가 가져오기
                Element originalPrice = book.selectFirst("s.text-gray-700");
                if (originalPrice != null) {
                    priceText = originalPrice.text();
                }
            }
            
            // 가격 숫자만 추출
            int price = 0;
            if (!priceText.isEmpty()) {
                String numericPrice = priceText.replaceAll("[^0-9]", "");
                if (!numericPrice.isEmpty()) {
                    price = Integer.parseInt(numericPrice);
                }
            }
            
            // 이미지 URL: img 태그의 src
            /*String imageUrl = "";
            Element imgElement = book.selectFirst("a.prod_link img");
            if (imgElement != null) {
                imageUrl = imgElement.attr("src");
                if (imageUrl.isEmpty()) {
                    imageUrl = imgElement.attr("data-src");
                }
            }*/
            
            // 책 소개: p.prod_introduction
            String introduction = book.select("p.prod_introduction").text();

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
