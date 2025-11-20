package org.example.stamppaw_backend.random.generator;

public class RandomPointGenerator {

    private static final double EARTH_RADIUS = 6371000; // meter

    public static LatLng generateRandomPoint(double baseLat, double baseLng, double radiusMeters) {
        double lat = Math.toRadians(baseLat);
        double lng = Math.toRadians(baseLng);

        double distance = Math.sqrt(Math.random()) * radiusMeters;
        double angle = Math.random() * 2 * Math.PI;

        double deltaLat = (distance * Math.cos(angle)) / EARTH_RADIUS;
        double deltaLng = (distance * Math.sin(angle)) / (EARTH_RADIUS * Math.cos(lat));

        double newLat = lat + deltaLat;
        double newLng = lng + deltaLng;

        return new LatLng(Math.toDegrees(newLat), Math.toDegrees(newLng));
    }

    public record LatLng(double lat, double lng) {}
}
