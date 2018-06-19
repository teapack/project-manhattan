package eu.pedu.group17s.manhattan.final_version;

import eu.pedu.group17s.manhattan.final_version.DATA.DataPkg;
import eu.pedu.lib17s.canvasmanager.Painter;
import eu.pedu.lib17s.geom.Multimover;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.lib17s.util.Direction8;
import eu.pedu.manhattan.fw17s.Destination;
import eu.pedu.manhattan.fw17s.IVehicle;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Instance interfejsu {@code IVehicle} představují vozidla schopná pohybu po
 * ulicích města, v němž je provoz řízen semafory. Vozidla nemají žádnou
 * speciální vlastní inteligenci, to, kdy a kam vozidlo pojede a kdy bude na
 * něco čekat specifikuje řidič, který má řízení daného vozidla na starosti.
 *
 * @author Vratislav Jindra
 * @version 201706021149
 */
public class Vehicle implements IVehicle, IListener {

    private int specialLine;
    private boolean specialCar;
    private boolean removeListener;
    private Position position;
    private final int BLINKING_SPEED = 10; // default: 10; The higher this
    // number is, the lower the blinking speed is!
    private final int SPEED = 50; // default: 50
    private int step;
    private int ticks;
    private Direction8 direction;
    private boolean blinking;
    private boolean blinkerOn;
    private int module;
    private Image image;
    private AffineTransform affineTransform;
    private Destination.LeftRight blinkDirection = null;
    private URL imageUrl;

    /**
     * Konstruktor, pomocí kterého vytvářím instance třídy Vehicle.
     *
     * @param position počáteční pozice vozidla
     * @param direction směr, kterým je vozidlo při vytvoření natočené
     */
    public Vehicle(Position position, Direction8 direction) {
        this.position = new Position(position.getX() * 50,
                position.getY() * 50);
        this.direction = direction;
        step = GUI.getCanvasManager().getStep();
        imageUrl = DataPkg.class.getResource("vehicle.png");
        setImage(imageUrl);
    }

    /**
     * Vrátí informaci o tom, jestli vozidlo právě bliká.
     *
     * @return Bliká-li, vrátí {@code true}, jinak vrátí {@code false}
     */
    @Override
    public boolean isBlinking() {
        return blinking;
    }

    /**
     * Posune vozidlo o zadaný počet bodů vpřed.
     *
     * @param distance Počet bodů, o něž se má vozidlo posunout
     */
    @Override
    public void forward(int distance) {
        int dx = getDirection().dx() * distance + getPosition().getX();
        int dy = getDirection().dy() * distance + getPosition().getY();
        Multimover m = Multimover.getInstance();
        if (!(m.isMoving(this))) {
            m.moveWithSpeed(SPEED, this, dx, dy);
        }
    }

    /**
     * Zapne levý blinkr.
     */
    @Override
    public void blinkLeft() {
        if (!isBlinking()) {
            // Pokud vozidlo nebliká, tak začne.
            blinking = true;
            // Vozidlo bliká doleva.
            blinkDirection = Destination.LeftRight.LEFT;
            // Obrázek použitý při zapnutém levém blinkru.
            imageUrl = DataPkg.class.getResource("vehicle_left1.png");
            setImage(imageUrl);
        }
    }

    /**
     * Zapne pravý blinkr.
     */
    @Override
    public void blinkRight() {
        if (!isBlinking()) {
            // Pokud vozidlo nebliká, tak začne.
            blinking = true;
            // Vozidlo bliká doprava.
            blinkDirection = Destination.LeftRight.RIGHT;
            // Obrázek použitý při zapnutém pravém blinkru.
            imageUrl = DataPkg.class.getResource("vehicle_right1.png");
            setImage(imageUrl);
        }
    }

    /**
     * Vypne blinkr.
     */
    @Override
    public void stopBlinking() {
        setBlinking(false);
        blinkDirection = null;
        imageUrl = DataPkg.class.getResource("vehicle.png");
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
        this.position = new Position(x, y);
    }

    /**
     * Vrátí směr, do nějž je daná instance natočena.
     *
     * @return Směr, do nějž je daná instance natočena
     */
    @Override
    public Direction8 getDirection() {
        return direction;
    }

    /**
     * Otočí instanci o 90° vlevo.
     */
    @Override
    public void turnLeft() {
        setDirection(direction.leftTurn());
        // Po odbočení se vypne blinkr.
        stopBlinking();
    }

    /**
     * Otočí instanci o 90° vpravo.
     */
    @Override
    public void turnRight() {
        setDirection(direction.rightTurn());
        // Po odbočení se vypne blinkr.
        stopBlinking();
    }

    /**
     * Prostřednictvím dodaného kreslítka vykreslí obraz své instance.
     *
     * @param painter Kreslítko schopné kreslit na plátno ovládané správcem
     */
    @Override
    public void paint(Painter painter) {
        affineTransform = new AffineTransform();
        if (direction != null) {
            switch (direction) {
                case SOUTH:
                    affineTransform.translate(getPosition().getX(),
                            getPosition().getY());
                    affineTransform.quadrantRotate(2, 25.0D, 25.0D);
                    break;
                case EAST:
                    affineTransform.translate(getPosition().getX(),
                            getPosition().getY());
                    affineTransform.quadrantRotate(1, 25.0D, 25.0D);
                    break;
                case WEST:
                    affineTransform.translate(getPosition().getX(),
                            getPosition().getY());
                    affineTransform.quadrantRotate(3, 25.0D, 25.0D);
                    break;
                default:
                    affineTransform = null;
            }
        } else {
            affineTransform = null;
        }
        painter.drawPicture(getPosition().getX(), getPosition().getY(), image,
                affineTransform);
    }

    /**
     * Nastaví obrázek vozidla.
     *
     * @param imageUrl URL nastavovaného obrázku
     */
    private void setImage(URL imageUrl) {
        try {
            image = ImageIO.read(imageUrl);
        } catch (IOException ex) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    /**
     * Setter na atribut blinking.
     *
     * @param blinking true, má-li vozidlo blikat, nebo false, nemá-li vozidlo
     * blikat
     */
    private void setBlinking(boolean blinking) {
        this.blinking = blinking;
    }

    /**
     * Metoda z interfejsu IListener, která se zavolá při každém "ticku" hodin
     * (viz třída Clock). V tomto případě se tato metoda stará o změnu obrázků
     * vozidla při blikání.
     */
    @Override
    public void tick() {
        if (this.isBlinking()) {
            if (ticks >= BLINKING_SPEED) {
                ticks = 0;
                if (blinkerOn) {
                    blinkerOn = false;
                    imageUrl = DataPkg.class.getResource("vehicle.png");
                    try {
                        image = ImageIO.read(imageUrl);
                    } catch (IOException ex) {
                        Logger.getLogger(Vehicle.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                } else {
                    blinkerOn = true;
                    imageUrl = null;
                    if (blinkDirection == null) {
                        imageUrl = null;
                    } else {
                        switch (blinkDirection) {
                            case LEFT:
                                imageUrl = DataPkg.class.getResource(
                                        "vehicle_left1.png");
                                break;
                            case RIGHT:
                                imageUrl = DataPkg.class.getResource(
                                        "vehicle_right1.png");
                                break;
                            default:
                                imageUrl = null;
                                break;
                        }
                    }
                }
                setImage(imageUrl);
            }
        }
        ++ticks;
    }

    /**
     * Metoda pro získání pozice daného vozidla.
     *
     * @return pozice vozidla
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Metoda pro nastavení směru daného vozidla.
     *
     * @param direction nastavovaný směr
     */
    @Override
    public void setDirection(Direction8 direction) {
        this.direction = direction;
    }

    /**
     * Metoda, která vrátí true, má-li se listener odstranit ze seznamu
     * listenerů (ve třídě Clock).
     *
     * @return true, má-li se listener odstranit ze seznamu posluchačů; jinak
     * vrací false
     */
    @Override
    public boolean isRemoveListener() {
        return removeListener;
    }

    /**
     * Setter na atribut removeListener.
     *
     * @param removeListener nastavovaná hodnota
     */
    public void setRemoveListener(boolean removeListener) {
        this.removeListener = removeListener;
    }

    public void setSpecialCar(boolean specialCar) {
        this.specialCar = specialCar;
    }

    public boolean isSpecialCar() {
        return this.specialCar;
    }
    
    public void setSpecialLine(int specialLine) {
        this.specialLine = specialLine;
    }
    
    public int getSpecialLine() {
        return this.specialLine;
    }
}
