package graphics.rasterizer;

import graphics.Raster;
import model.Line;
/**
 * Algoritmus rasterizace úsečky: Triviální algoritmus.
 */
public class TrivialLineRasterizer implements LineRasterizer {

    private final Raster raster;

    public TrivialLineRasterizer(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        rasterize(
                line.getPoint1().getX(),
                line.getPoint1().getY(),
                line.getPoint2().getX(),
                line.getPoint2().getY(),
                line.getColor()
        );

    }

    private void rasterize(int x1, int y1, int x2, int y2, int color) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        //vyber dominantni osy
        if (Math.abs(dx) > Math.abs(dy)) {
            //X
            if (x1 > x2) {
                int tempX = x1; x1 = x2; x2 = tempX;
                int tempY = y1; y1 = y2; y2 = tempY;
            }

        float k = (y2 - y1) / (float) (x2 - x1);
        float q = y1 - k * x1;

            for (int x = x1; x <= x2; x++) {
                int y = Math.round(k * x + q);
                raster.setPixel(x, y, color);
            }

        } else {
            //Y
            if (y1 > y2) {
                int tempX = x1;
                x1 = x2;
                x2 = tempX;
                int tempY = y1;
                y1 = y2;
                y2 = tempY;
            }

            if (y1 == y2) {
                raster.setPixel(x1, y1, color);
                return;
            }
        }
            float k = (float) (x2 - x1) / (y2 - y1);
            float q = x1 - k * y1;

        for (int y = y1; y <= y2; y++) {
            int x = Math.round(k * y + q);
            raster.setPixel(x, Math.round(y), color);
        }
    }
}
