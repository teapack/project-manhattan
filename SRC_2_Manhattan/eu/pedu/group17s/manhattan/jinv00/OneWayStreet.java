package eu.pedu.group17s.manhattan.jinv00;

import eu.pedu.group17s.manhattan.jinv00.DATA.DataPkg;
import eu.pedu.lib17s.canvasmanager.Painter;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.lib17s.util.Direction8;
import eu.pedu.manhattan.fw17s.IOneWayStreet;
import eu.pedu.manhattan.fw17s.IVehicle;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Instance třídy {@code Street} představují jednosměrné segmenty silnice ve
 * městě.
 *
 * @author Vratislav Jindra
 * @version 201706021127
 */
public class OneWayStreet implements IOneWayStreet {

    private int module;
    private AffineTransform affineTransform;
    private final Direction8 direction;
    private Image image;
    private IVehicle vehicle;
    private Position position;
    private final URL imageUrl;

    /**
     * Konstruktor pro vytváření instancí třídy OneWayStreet.
     *
     * @param position pozice daného segmentu silnice
     * @param direction směr, kterým se v tomto segmentu jezdí
     */
    public OneWayStreet(Position position, Direction8 direction) {
        this.direction = direction;
        this.position = position;
        module = 50;
        imageUrl = DataPkg.class.getResource("one_way_street.png");
        setImage(imageUrl);
        setAffineTransform();
    }

    /**
     * Vrátí směr, ve kterém se po daném segmentu silnice jezdí.
     *
     * @return Požadovaný směr
     */
    @Override
    public Direction8 getDirection() {
        return direction;
    }

    /**
     * Vrátí informaci o tom, je-li daný segment silnice prázdný a může-li proto
     * na něj zadané vozidlo vjet; při povolení vjezdu se daný segment
     * zablokuje, takže nepovolí vjezd jiným vozidlům, dokud jej zadané vozidlo
     * neopustí.
     *
     * @param vehicle Vozidlo, které chce na daný segment silnice vjet
     * @return Je-li blokovaný, vrátí {@code false}. Jinak vrátí {@code true} a
     * současně se zablokuje pro vozidlo zadané v parametru
     */
    @Override
    public boolean isEmptyFor(IVehicle vehicle) {
        if (this.vehicle == null) {
            // Na teto ulici zadne auto neni, auto z parametru tam muze vjet a
            // zablokovat si ji pro sebe.
            blockFor(vehicle);
            return true;
        } else if (this.vehicle.equals(vehicle)) {
            // Na teto ulici je auto z parametru.
            blockFor(vehicle);
            return true;
        } else {
            // Na teto ulici je nejake jine auto, neni prazdna.
            return false;
        }
    }

    /**
     * Nastaví zadaný segment silnice opět jako prázdný, takže na něj může
     * kdokoliv vjet (samozřejmě až po předchozím zablokování).
     *
     * @param vehicle Vozidlo, které chce daný segment silnice uvolnit; Parametr
     * slouží k tomu, aby si segment mohl zkontrolovat, že o uvolnění žádá ten,
     * kdo si ji před tím zablokoval.
     */
    @Override
    public void setEmpty(IVehicle vehicle) {
//        if (this.vehicle.equals(vehicle)) {
            this.vehicle = null;
//        }
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
     * Nastaví daný segment silnice jako zablokovaný, takže na něj nemůže nikdo
     * kromě vozidla z parametru vjet.
     *
     * @param vehicle Vozidlo, které si ulici zablokuje
     */
    private void blockFor(IVehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Metoda pro nastavení obrázku instancí třídy OneWayStreet.
     */
    private void setImage(URL imageUrl) {
        try {
            image = ImageIO.read(imageUrl);
        } catch (IOException ex) {
            Logger.getLogger(OneWayStreet.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    /**
     * Metoda starající se o správné otočení obrázku jednosměrky.
     */
    private void setAffineTransform() {
        if (direction != null) {
            affineTransform = new AffineTransform();
            switch (direction) {
                case SOUTH:
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(2, 25.0D, 25.0D);
                    break;
                case WEST:
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(3, 25.0D, 25.0D);
                    break;
                case EAST:
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(1, 25.0D, 25.0D);
                    break;
                default:
                    affineTransform = null;
            }
        } else {
            affineTransform = null;
        }
    }
}
