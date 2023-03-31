package com.esummary.crawler.elearning;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CrawlingService {
    private final Map<String, Crawler> crawlerList;

    // 임시 서비스 구조
    public void crawlingService(String code) {
        Crawler crawler = crawlerList.get(code.concat("Crawler"));
    }
}
