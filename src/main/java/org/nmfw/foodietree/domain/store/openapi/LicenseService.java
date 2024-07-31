package org.nmfw.foodietree.domain.store.openapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Transactional

// 사업자등록번호 상태 조회 api
public class LicenseService {

    @Value("${license.enc}")
    private String licenseEncKey;
    @Value("${license.dec}")
    private String licenseDecKey;

    public LicenseResDto postLicense() {
        WebClient webClient = WebClient.builder().build();
        String url =
                "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey="
                + licenseDecKey;
        // StoreApproval PENDING 상태인 사업자등록번호 조회하도록 수정 필요
        String[] arr = {"1234567891", "1141916588", "2744700926", "8781302319"};

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
