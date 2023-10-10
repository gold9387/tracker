package com.example.tracker.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.tracker.dto.DeliveryDto;
import com.example.tracker.dto.Info;
import com.example.tracker.entity.CoordinateEntity;
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
    private String authorizationKey;
    private static final int HTTP_OK = 200;

    private final DeliveryRepository deliveryRepository;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final EntityManager em;

    public DeliveryServiceImpl(
            DeliveryRepository deliveryRepository,
            CloseableHttpClient httpClient,
            ObjectMapper objectMapper,
            EntityManager em) {
        this.deliveryRepository = deliveryRepository;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.em = em;
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
            httpGet.setHeader("Authorization", "KakaoAK " + authorizationKey);

            // HTTP 요청을 실행합니다.
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() == HTTP_OK) {
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

    /**
     * 배송 정보를 저장하는 메서드.
     *
     * @param deliveryDto 저장할 배송 정보의 DTO 객체
     */
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

    /**
     * 대량의 경로 좌표 데이터를 데이터베이스에 저장합니다.
     *
     * @param coordinates 저장할 경로 좌표 리스트
     */
    @Transactional
    public void saveCoordinates(List<Info.Coordinate> coordinates) {
        int batchSize = 50;

        for (int i = 0; i < coordinates.size(); i++) {
            Info.Coordinate coordinate = coordinates.get(i);

            CoordinateEntity entity = CoordinateEntity.builder()
                    .name(coordinate.getName())
                    .x(coordinate.getX())
                    .y(coordinate.getY())
                    .duration(coordinate.getDuration())
                    .build();
            em.persist(entity);

            if (i % batchSize == 0 || i == coordinates.size() - 1) {
                em.flush();
                em.clear();
            }
        }
    }
}
