package com.example.tracker.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.tracker.dto.DeliveryDto;
import com.example.tracker.entity.DeliveryEntity;
import com.example.tracker.repository.DeliveryRepository;
import com.example.tracker.service.DeliveryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Value("${rest-api.key}")
    String authorization_key;

    private final DeliveryRepository deliveryRepository;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DeliveryServiceImpl(
            DeliveryRepository deliveryRepository,
            CloseableHttpClient httpClient,
            ObjectMapper objectMapper) {
        this.deliveryRepository = deliveryRepository;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * 주소를 좌표로 변환하는 메서드.
     *
     * @param address 변환할 주소
     * @return 좌표를 담은 Double 배열. 실패 시 null 반환.
     */
    @Override
    public Double[] converter(String address) {
        Double[] coordinate = null;
        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";

        try {
            // 주소를 UTF-8로 인코딩합니다.
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String apiAddress = apiUrl + "?query=" + encodedAddress;

            // HttpGet 객체를 생성하고 헤더를 설정합니다.
            HttpGet httpGet = new HttpGet(apiAddress);
            httpGet.setHeader("Authorization", "KakaoAK " + authorization_key);

            // HTTP 요청을 실행합니다.
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    coordinate = new Double[2];

                    // JSON 응답에서 좌표를 추출합니다.
                    if (jsonNode.has("documents")) {
                        JsonNode documents = jsonNode.get("documents");
                        if (documents.isArray() && documents.size() > 0) {
                            JsonNode firstDocument = documents.get(0);
                            if (firstDocument.has("x") && firstDocument.has("y")) {
                                coordinate[0] = firstDocument.get("x").asDouble();
                                coordinate[1] = firstDocument.get("y").asDouble();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("주소를 좌표로 변환하는 과정에서 오류가 발생했습니다.", e);
            return null;
        }
        return coordinate;
    }

    @Override
    public void saveDelivery(DeliveryDto deliveryDto) {
        DeliveryEntity deliveryEntity = DeliveryEntity.builder()
                .startAddress(deliveryDto.getStartAddress())
                .endAddress(deliveryDto.getEndAddress())
                .name(deliveryDto.getName())
                .productName(deliveryDto.getProductName())
                .item(deliveryDto.getItem())
                .build();
        deliveryRepository.save(deliveryEntity);
    }
}
