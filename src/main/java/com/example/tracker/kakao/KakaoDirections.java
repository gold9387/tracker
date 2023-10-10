package com.example.tracker.kakao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.tracker.dto.Info;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoDirections {

    @Value("${route-api.url}")
    private String routeApiUrl;

    @Value("${rest-api.key}")
    private String authorizationKey;

    private static final int HTTP_OK = 200;

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * 주어진 출발지와 도착지 좌표를 기반으로 카카오 길찾기 API를 호출하고 결과 경로를 반환합니다.
     *
     * @param startX 출발지 x 좌표
     * @param startY 출발지 y 좌표
     * @param endX   도착지 x 좌표
     * @param endY   도착지 y 좌표
     * @return 길찾기 결과의 좌표 리스트
     */
    public List<Info.Coordinate> findRoute(Double startX, Double startY, Double endX, Double endY) {
        String apiAddress = buildApiAddress(startX, startY, endX, endY);
        HttpGet httpGet = buildHttpGetRequest(apiAddress);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return handleHttpResponse(response);
        } catch (Exception e) {
            log.error("카카오 길찾기 API 호출 중 오류가 발생했습니다.", e);
            throw new RuntimeException("API 호출 오류", e);
        }
    }

    /**
     * API 주소를 구성합니다.
     */
    private String buildApiAddress(Double startX, Double startY, Double endX, Double endY) {
        return routeApiUrl
                + "?origin=" + startX + "," + startY
                + "&destination=" + endX + "," + endY
                + "&waypoints=&priority=RECOMMEND&car_fuel=GASOLINE&car_hipass=false&alternatives=false&road_details=false";
    }

    /**
     * HTTP GET 요청 객체를 생성합니다.
     */
    private HttpGet buildHttpGetRequest(String apiAddress) {
        HttpGet httpGet = new HttpGet(apiAddress);
        httpGet.setHeader("Authorization", "KakaoAK " + authorizationKey);
        return httpGet;
    }

    /**
     * HTTP 응답을 처리하여 좌표 목록을 반환합니다.
     */
    private List<Info.Coordinate> handleHttpResponse(CloseableHttpResponse response) throws Exception {
        if (response.getStatusLine().getStatusCode() != HTTP_OK) {
            throw new RuntimeException("API 응답 오류: " + response.getStatusLine().getStatusCode());
        }

        String responseBody = EntityUtils.toString(response.getEntity());
        Info routeInfo = objectMapper.readValue(responseBody, Info.class);
        return extractCoordinates(routeInfo.getRoutes().get(0));
    }

    /**
     * 경로 정보로부터 좌표 목록을 추출하고 평균 시간을 분배합니다.
     */
    private List<Info.Coordinate> extractCoordinates(Info.Route route) {
        List<Info.Coordinate> coordinates = new ArrayList<>();
        int totalDuration = (int) route.getSummary().getDuration();
        int totalCoordinates = calculateTotalCoordinates(route);
        int averageDurationPerCoordinate = totalDuration / totalCoordinates;

        addOriginCoordinate(route, coordinates, averageDurationPerCoordinate);
        addSectionCoordinates(route, coordinates, averageDurationPerCoordinate);
        addDestinationCoordinate(route, coordinates, averageDurationPerCoordinate);

        return coordinates;
    }

    /**
     * 전체 좌표의 수를 계산합니다.
     */
    private int calculateTotalCoordinates(Info.Route route) {
        int totalCoordinates = 0;
        for (Info.Section section : route.getSections()) {
            for (Info.Road road : section.getRoads()) {
                totalCoordinates += road.getVertexes().size() / 2;
            }
        }
        return totalCoordinates;
    }

    /**
     * 출발지 좌표를 추가합니다.
     */
    private void addOriginCoordinate(Info.Route route, List<Info.Coordinate> coordinates, int duration) {
        Info.Coordinate origin = route.getSummary().getOrigin();
        coordinates.add(new Info.Coordinate(origin.getX(), origin.getY(), origin.getName(), duration));
    }

    /**
     * 각 섹션의 도로 정보로부터 좌표를 추출하여 목록에 추가합니다.
     */
    private void addSectionCoordinates(Info.Route route, List<Info.Coordinate> coordinates, int duration) {
        for (Info.Section section : route.getSections()) {
            for (Info.Road road : section.getRoads()) {
                String roadName = road.getName();
                List<Double> vertexes = road.getVertexes();
                for (int i = 0; i < vertexes.size(); i += 2) {
                    double x = vertexes.get(i);
                    double y = vertexes.get(i + 1);
                    coordinates.add(new Info.Coordinate(x, y, roadName, duration));
                }
            }
        }
    }

    /**
     * 도착지 좌표를 추가합니다.
     */
    private void addDestinationCoordinate(Info.Route route, List<Info.Coordinate> coordinates, int duration) {
        Info.Coordinate destination = route.getSummary().getDestination();
        coordinates.add(new Info.Coordinate(destination.getX(), destination.getY(), destination.getName(), duration));
    }
}
