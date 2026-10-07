package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private final List<Point> points = new ArrayList<>();

    public Polygon() {
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void clear() {
        points.clear();
    }
}
