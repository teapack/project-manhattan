package eu.pedu.group17s.manhattan.final_version;

import eu.pedu.group17s.manhattan.final_version.DATA.DataPkg;
import eu.pedu.lib17s.canvasmanager.Painter;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.manhattan.fw17s.IControlledStreet;
import eu.pedu.manhattan.fw17s.ITrafficLight;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Instance třídy {@code TrafficLight} představují semafory řídící odjezd z
 * kontrolované části silnice.
 *
 * @author Vratislav Jindra
 * @version 201706021127
 */
public class TrafficLight implements ITrafficLight {

    private int module;
    private AffineTransform affineTransform;
    private final IControlledStreet controlledStreet;
    private Image image;
    private Position position;
    private TrafficLightState state;
    private URL imageUrl;

    /**
     * Konstruktor pro vytváření instancí třídy TrafficLight.
     *
     * @param controlledStreet silnice, kterou tento semafor kontroluje
     */
    public TrafficLight(IControlledStreet controlledStreet) {
        this.controlledStreet = controlledStreet;
        setPosition(new Position(
                controlledStreet.getPosition().getX()
                * GUI.getCanvasManager().getStep(),
                controlledStreet.getPosition().getY()
                * GUI.getCanvasManager().getStep()
        ));
        module = 50;
        state = TrafficLightState.GET_READY;
        setImageUrl();
        setImage(imageUrl);
        setAffineTransform();
        addToCanvasManager();
    }

    /**
     * Vrátí ovládaný segment silnice.
     *
     * @return Ovládaný segment silnice
     */
    @Override
    public IControlledStreet getControlledStreet() {
        return controlledStreet;
    }

    /**
     * Vrátí aktuální stav semaforu.
     *
     * @return Aktuální stav semaforu
     */
    @Override
    public TrafficLightState getState() {
        return state;
    }

    /**
     * Nastaví nový stav semaforu.
     *
     * @param newState Požadovaný nový stav semaforu
     */
    @Override
    public void setState(TrafficLightState newState) {
        this.state = newState;
        setImageUrl();
        setImage(imageUrl);
    }

    /**
     * Vrátí velikost modulu, tj. délku strany opsaného čtverce.
     *
     * @return Velikost modulu instance
     */
    @Override
    public int getModule() {
        return module;
    }

    /**
     * Změni velikost instance, aby měl její nový modul (= délku strany opsaného
     * čtverce) zadanou velikost.
     *
     * @param module Nově nastavovaný modul
     */
    @Override
    public void setModule(int module) {
        this.module = module;
    }

    /**
     * Vrátí x-ovou (vodorovnou) souřadnici pozice instance, tj. vodorovnou
     * souřadnici levého horního rohu opsaného obdélníku.
     *
     * @return Aktuální vodorovná (x-ová) souřadnice instance, x=0 má levý okraj
     * plátna, souřadnice roste doprava
     */
    @Override
    public int getX() {
        return GUI.getCanvasManager().positionPoint2Field(position).x;
    }

    /**
     * Vrátí y-ovou (svislou) souřadnici pozice instance, tj. svislou souřadnici
     * levého horního rohu opsaného obdélníku.
     *
     * @return Aktuální svislá (y-ová) souřadnice instance, y=0 má horní okraj
     * plátna, souřadnice roste dolů
     */
    @Override
    public int getY() {
        return GUI.getCanvasManager().positionPoint2Field(position).y;
    }

    /**
     * Přemístí instanci na zadanou pozici. Pozice instance je přitom definována
     * jako pozice levého horního rohu opsaného obdélníku.
     *
     * @param x Nově nastavovaná vodorovná (x-ová) souřadnice instance, x=0 má
     * levý okraj plátna, souřadnice roste doprava
     * @param y Nově nastavovaná svislá (y-ová) souřadnice instance, y=0 má
     * horní okraj plátna, souřadnice roste dolů
     */
    @Override
    public void setPosition(int x, int y) {
        position = new Position(x, y);
        ((ControlledStreet) getControlledStreet()).setTrafficLight(this);
    }

    /**
     * Prostřednictvím dodaného kreslítka vykreslí obraz své instance.
     *
     * @param painter Kreslítko schopné kreslit na plátno ovládané správcem
     */
    @Override
    public void paint(Painter painter) {
        painter.drawPicture(position.getX(), position.getY(), image,
                affineTransform);
    }

    /**
     * Přidá semafor do canvas manageru (aby byl vykreslený "nad" svojí
     * silnicí).
     */
    private void addToCanvasManager() {
        GUI.getCanvasManager().addToFront(this);
    }

    /**
     * Metoda starající se o správné otočení obrázku semaforu.
     */
    private void setAffineTransform() {
        affineTransform = new AffineTransform();
        affineTransform.translate(position.getX(), position.getY());
        switch (getControlledStreet().getDirection()) {
            case SOUTH:
                affineTransform.quadrantRotate(2, 25.0, 25.0);
                break;
            case WEST:
                affineTransform.quadrantRotate(3, 25.0, 25.0);
                break;
            case EAST:
                affineTransform.quadrantRotate(1, 25.0, 25.0);
                break;
            default:
                affineTransform = null;
        }
    }

    /**
     * Metoda pro nastavení URL pro obrázek semaforu.
     */
    private void setImageUrl() {
        switch (state) {
            case ATTENTION:
                imageUrl = DataPkg.class
                        .getResource("traffic_light_attention.png");
                break;
            case GET_READY:
                imageUrl = DataPkg.class
                        .getResource("traffic_light_get_ready.png");
                break;
            case GO:
                imageUrl = DataPkg.class.getResource("traffic_light_go.png");
                break;
            case LIGHTS_OFF:
                imageUrl = DataPkg.class
                        .getResource("traffic_light_lights_off.png");
                break;
            case LIGHTS_ON:
                imageUrl = DataPkg.class
                        .getResource("traffic_light_lights_on.png");
                break;
            case STOP:
                imageUrl = DataPkg.class.getResource("traffic_light_stop.png");
                break;
        }
    }

    /**
     * Metoda pro nastavení obrázku semaforu ze zadané URL adresy.
     *
     * @param imageUrl URL adresa nastavovaného obrázku
     */
    private void setImage(URL imageUrl) {
        try {
            image = ImageIO.read(imageUrl);
        } catch (IOException ex) {
            Logger.getLogger(TrafficLight.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
}
