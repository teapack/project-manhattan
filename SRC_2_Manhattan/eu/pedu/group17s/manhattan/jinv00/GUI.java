package eu.pedu.group17s.manhattan.jinv00;

import eu.pedu.lib17s.canvasmanager.CanvasManager;
import eu.pedu.lib17s.geom.Size;
import eu.pedu.manhattan.fw17s.IExitStreet;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Tato třída se stará o komunikaci aplikace s Canvas Managerem a o vykreslování
 * grafického uživatelského rozhraní.
 *
 * @author Vratislav Jindra
 * @version 201705311112
 */
public class GUI {

    private boolean exitOpen;
    private static CanvasManager canvasManager;
    private static final GUI SINGLETON = new GUI();

    /**
     * Prázdný konstruktor.
     */
    public GUI() {
    }

    /**
     * Getter na jedinou instanci (jedináčka) této třídy.
     *
     * @return jediná instance této třídy
     */
    public static GUI getInstance() {
        return SINGLETON;
    }

    /**
     * Setter na Canvas Manager.
     *
     * @param visible true, chci-li zobrazit co Canvas Manager vykresluje
     */
    public void setCanvasManager(boolean visible) {
        canvasManager = CanvasManager.getInstance(visible);
    }

    /**
     * Předá potřebné parametry Canvas Manageru.
     *
     * @param mapSize velikost mapy
     */
    public void start(Size mapSize) {
        canvasManager.setStepAndSize(50, mapSize.width, mapSize.height);
        canvasManager.setName("Project Manhattan");
        canvasManager.setPosition(0, 0);
        canvasManager.addKeyboardListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == TownBuilder.getInstance().getInputKey()) {
                    TownBuilder.getInstance().addVehicle();
                } else if (e.getKeyCode()
                        == TownBuilder.getInstance().getExitKey()) {
                    if (exitOpen == false) {
                        // Výjezdové silnice z města jsou zavřené. Nyní je
                        // chceme otevřít.
                        exitOpen = true;
                        for (IExitStreet exitStreet
                                : TownBuilder.getInstance().getExitStreets()) {
                            exitStreet.openExit();
                        }
                        canvasManager.repaint();
                    } else {
                        // Výjezdové silnice z města jsou otevřené, nyní je
                        // chceme zavřít
                        exitOpen = false;
                        for (IExitStreet exitStreet
                                : TownBuilder.getInstance().getExitStreets()) {
                            exitStreet.closeExit();
                        }
                        canvasManager.repaint();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * Getter na Canvas Manager.
     *
     * @return instance třídy Canvas Manager
     */
    public static CanvasManager getCanvasManager() {
        return canvasManager;
    }

    /**
     * Nastaví Canvas Manager, aby byl viditelný.
     */
    public void setVisible() {
        canvasManager.setVisible(true);
    }
}
