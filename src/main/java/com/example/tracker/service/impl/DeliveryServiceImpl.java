package com.example.tracker.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.HttpStatus;
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

    private static final String AUTHORIZATION_PREFIX = "KakaoAK ";
    private static final String QUERY_PARAM = "?query=";

    @Value("${rest-api.key}")
    private String authorizationKey;

    @Value("${converter-api.url}")
    private String apiUrl;

    @Value("${batch.size}")
    private int batchSize;

    private final DeliveryRepository deliveryRepository;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final EntityManager em;

    /**
     * 주어진 의존성들로 {@code DeliveryServiceImpl} 객체를 생성합니다.
     *
     * @param deliveryRepository 배송 정보 관련 레포지토리
     * @param httpClient         HTTP 클라이언트
     * @param objectMapper       JSON 매퍼
     * @param em                 엔터티 매니저
     */
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
     * 주어진 주소를 좌표로 변환합니다.
     *
     * @param address 변환할 주소
     * @return 변환된 좌표. 변환 실패 시 null.
     */
    @Override
    public Double[] converter(String address) {
        try {
            String responseBody = executeHttpRequest(address);
            return extractCoordinatesFromResponse(responseBody);
        } catch (Exception e) {
            log.error("주소를 좌표로 변환하는 과정에서 오류가 발생했습니다.", e);
            return null;
        }
    }

    /**
     * HTTP 요청을 실행하고 응답 본문을 반환합니다.
     *
     * @param address 변환할 주소
     * @return HTTP 응답 본문
     * @throws Exception HTTP 요청 중 발생하는 예외
     */
    private String executeHttpRequest(String address) throws Exception {
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        String apiAddress = apiUrl + QUERY_PARAM + encodedAddress;

        HttpGet httpGet = new HttpGet(apiAddress);
        httpGet.setHeader("Authorization", AUTHORIZATION_PREFIX + authorizationKey);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
            return null;
        }
    }

    /**
     * 응답 본문에서 좌표를 추출합니다.
     *
     * @param responseBody HTTP 응답 본문
     * @return 추출된 좌표 배열
     * @throws Exception JSON 파싱 중 발생하는 예외
     */
    private Double[] extractCoordinatesFromResponse(String responseBody) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        if (jsonNode.has("documents")) {
            JsonNode documents = jsonNode.get("documents");
            if (documents.isArray() && documents.size() > 0) {
                JsonNode firstDocument = documents.get(0);
                if (firstDocument.has("x") && firstDocument.has("y")) {
                    return new Double[] { firstDocument.get("x").asDouble(), firstDocument.get("y").asDouble() };
                }
            }
        }
        return null;
    }

    /**
     * 배송 정보를 데이터베이스에 저장합니다.
     *
     * @param deliveryDto 저장할 배송 정보 DTO
     */
    @Override
    public void saveDelivery(DeliveryDto deliveryDto) {
        DeliveryEntity deliveryEntity = convertDtoToEntity(deliveryDto);
        deliveryRepository.save(deliveryEntity);
    }

    /**
     * DTO를 엔터티 객체로 변환합니다.
     *
     * @param deliveryDto 변환할 배송 정보 DTO
     * @return 변환된 배송 정보 엔터티 객체
     */
    private DeliveryEntity convertDtoToEntity(DeliveryDto deliveryDto) {
        return DeliveryEntity.builder()
                .startAddress(deliveryDto.getStartAddress())
                .endAddress(deliveryDto.getEndAddress())
                .name(deliveryDto.getName())
                .productName(deliveryDto.getProductName())
                .item(deliveryDto.getItem())
                .build();
    }

    /**
     * 주어진 좌표 리스트를 데이터베이스에 일괄 저장합니다.
     *
     * @param coordinates 저장할 좌표 리스트
     */
    @Transactional
    public void saveCoordinates(List<Info.Coordinate> coordinates) {
        for (int i = 0; i < coordinates.size(); i++) {
            persistCoordinate(coordinates.get(i));

            if (i % batchSize == 0 || i == coordinates.size() - 1) {
                em.flush();
                em.clear();
            }
        }
    }

    /**
     * 주어진 좌표를 데이터베이스에 저장합니다.
     *
     * @param coordinate 저장할 좌표 정보
     */
    private void persistCoordinate(Info.Coordinate coordinate) {
        CoordinateEntity entity = CoordinateEntity.builder()
                .name(coordinate.getName())
                .x(coordinate.getX())
                .y(coordinate.getY())
                .duration(coordinate.getDuration())
                .build();
        em.persist(entity);
    }
}
