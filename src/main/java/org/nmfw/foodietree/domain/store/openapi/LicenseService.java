package org.nmfw.foodietree.domain.store.openapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Transactional

// 사업자등록번호 상태 조회 api
public class LicenseService {

    @Value("${license.enc}")
    private String licenseEncKey;

    /**
     * 유효한 사업자등록번호인지 공공데이터 API 조회
     * @param arr - 사업자등록번호만 담은 String 배열
     * @return LicenseResDto - HTTP, 총 조회결과, 사업자등록번호별 상태조회 (List<LicenseDto>)
     */
    public LicenseResDto verifyLicensesByOpenApi(String[] arr) {
        // String[] arr = {"1234567891", "1141916588", "2744700926", "8781302319"};
        // 사업자등록번호 예시 중 첫번째를 제외하고 계속사업자로 조회되어야 정상

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .build();
        String url =
                "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey="
                + licenseEncKey;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("b_no", arr);

        return webClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(LicenseResDto.class)
                .block();
    }
}
