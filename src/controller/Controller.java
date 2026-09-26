package controller;

import view.Canvas;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Handles user input and controls the application flow related to the {@link Canvas}.
 * The controller coordinates input events, canvas operations, and rendering updates.
 *
 * @author PGRF FIM UHK
 * @version 2026
 */
public class Controller {

    // Aktuální pozice kurzoru ovládaného klávesami
    private int x, y;
    private final Canvas canvas;

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        // Inicializace pozice do středu plátna
        this.x = canvas.getWidth() / 2;
        this.y = canvas.getHeight() / 2;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Main functions -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void init() {
        // Inicializace obsahu plátna
        canvas.draw();
        // Obsluha vstupu z myši
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Nastavení barvy pixelu na pozici kurzoru myši
                canvas.getRaster().setRGB(e.getX(), e.getY(), 0xff0000);
                // Aktualizace zobrazení
                canvas.repaint();
            }
        });

        // Obsluha vstupu z klávesnice
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // A. Vykreslení horizontální úsečky ze středu plátna k pravému okraji
                if (e.getKeyCode() == KeyEvent.VK_X) {
                    int centerX = canvas.getWidth() / 2;
                    int centerY = canvas.getHeight() / 2;
                    int rightBorder = canvas.getWidth();

                    // Rasterizace horizontální úsečky
                    for (int x = centerX; x < rightBorder; x++) {
                        // Kontrola platnosti souřadnic
                        if(0 <= x && x < canvas.getWidth() && 0 <= y && y < canvas.getHeight()) {
                            canvas.getRaster().setRGB(x, centerY, 0xff0000);
                        }
                    }
                }

                // B. SNAKE
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    x++; // Posun doprava
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    x--; // Posun doleva
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    y++; // Posun dolů
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    y--; // Posun nahoru
                }

                // Kontrola, zda se aktuální pozice nachází v mezích plátna
                if (0 <= x && x < canvas.getWidth() && 0 <= y && y < canvas.getHeight()) {
                    canvas.getRaster().setRGB(x, y, 0xffff00);
                }
                // Aktualizace zobrazení
                canvas.repaint();
            }
        });
    }

}