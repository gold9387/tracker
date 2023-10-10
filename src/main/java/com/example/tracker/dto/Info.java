package com.example.tracker.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {
    private List<Route> routes;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Route {
        private int result_code;
        private String result_msg;
        private Summary summary;
        private List<Section> sections;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private Coordinate origin;
        private Coordinate destination;
        private List<Coordinate> waypoints;
        private String priority;
        private Bound bound;
        private Fare fare;
        private double distance;
        private double duration;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bound {
        private double min_x;
        private double min_y;
        private double max_x;
        private double max_y;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fare {
        private int taxi;
        private int toll;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Section {
        private int distance;
        private int duration;
        private Bound bound;
        private List<Road> roads;
        private List<Guide> guides;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Road {
        private String name;
        private int distance;
        private int duration;
        private int traffic_speed;
        private int traffic_state;
        private List<Double> vertexes;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Guide {
        private String name;
        private double x;
        private double y;
        private int distance;
        private int duration;
        private int type;
        private String guidance;
        private int road_index;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinate {
        private double x;
        private double y;
        private String name;
        private int duration;
    }
}
