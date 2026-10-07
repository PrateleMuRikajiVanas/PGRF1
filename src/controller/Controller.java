package controller;

import graphics.rasterizer.LineRasterizer;
import graphics.rasterizer.TrivialLineRasterizer;
import model.Line;
import model.Point;
import view.Canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import model.Polygon;
/**
 * Handles user input and controls the application flow related to the {@link Canvas}.
 * The controller coordinates input events, canvas operations, and rendering updates.
 *
 * @author PGRF FIM UHK
 * @version 2026
 */
public class Controller {

    private Point startPoint;
    private Point currentPoint;

    private final Canvas canvas;
    private final LineRasterizer rasterizer;

    private final List<Line> lines = new ArrayList<Line>();
    private static final int LINE_COLOR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();
    private static final int POLYGON_COLOR = Color.GREEN.getRGB();

    private final Polygon polygon = new Polygon();
    private boolean drawingPolygon = true;

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.rasterizer = new TrivialLineRasterizer(canvas.getRaster());
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Main functions -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void init() {
        canvas.clear();
        // Obsluha vstupu z myši
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = getPoint(e);
                currentPoint = startPoint;

                if (SwingUtilities.isLeftMouseButton(e)) {
                    drawingPolygon = true;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    drawingPolygon = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (drawingPolygon) {
                    if (polygon.getPoints().isEmpty()) {
                        polygon.addPoint(startPoint);
                    }
                    polygon.addPoint(getPoint(e));
                } else {
                    lines.add(new Line(startPoint, getPoint(e), LINE_COLOR));
                }
                currentPoint = null;
                startPoint = null;
                render();
            }
        });

        // Obsluha vstupu z klávesnice
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (startPoint == null) {
                    return;
                }
                currentPoint = getPoint(e);
                render();
            }
        });

        //Obsluha mazani (C)
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    lines.clear();
                    polygon.clear();
                    startPoint = null;
                    currentPoint = null;
                    render();
                }
            }
        });

        canvas.repaint();
    }

    private void render() {
        canvas.clear();
        for (Line line : lines) {
            rasterizer.rasterize(line);
        }

        List<Point> polyPoints = polygon.getPoints();
        for (int i = 0; i < polyPoints.size() - 1; i++) {
            rasterizer.rasterize(new Line(polyPoints.get(i), polyPoints.get(i + 1), POLYGON_COLOR));
        }
        //aby neproblikavala posledni usecka polynomu
        if (polyPoints.size() >= 2 && (!drawingPolygon || startPoint == null)) {
            rasterizer.rasterize(new Line(polyPoints.get(polyPoints.size() - 1), polyPoints.get(0), POLYGON_COLOR));
        }

        if (startPoint != null && currentPoint != null) {
            if (drawingPolygon) {
                if (polyPoints.size() > 0) {
                    Point first = polyPoints.get(0);
                    Point last = polyPoints.get(polyPoints.size() - 1);

                    // kresli náhled
                    rasterizer.rasterize(new Line(last, currentPoint, PREVIEW_COLOR));
                    rasterizer.rasterize(new Line(currentPoint, first, PREVIEW_COLOR));
                } else {
                    // první čára polygonu
                    rasterizer.rasterize(new Line(startPoint, currentPoint, PREVIEW_COLOR));
                }
            } else {
                // obyc usecka (pravy)
                rasterizer.rasterize(new Line(startPoint, currentPoint, PREVIEW_COLOR));
            }
        } else {
            // kdyz se netahne mysi, polynom se uzavre
            if (polyPoints.size() >= 2) {
                rasterizer.rasterize(new Line(polyPoints.get(polyPoints.size() - 1), polyPoints.get(0), POLYGON_COLOR));
            }
        }
        canvas.repaint();
    }

    private Point getPoint(MouseEvent e) {
        return new Point(e.getX(), e.getY());
    }

}