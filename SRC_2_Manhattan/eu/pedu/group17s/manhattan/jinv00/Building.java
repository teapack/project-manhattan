package eu.pedu.group17s.manhattan.jinv00;

import eu.pedu.group17s.manhattan.jinv00.DATA.DataPkg;
import eu.pedu.lib17s.canvasmanager.Painter;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.lib17s.util.Direction8;
import eu.pedu.manhattan.fw17s.IBuilding;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Instance třídy {@code Building} představují budovy ve městě. Budovy jsou
 * orientované, což znamená, že vědí, kudy kolem nich vede silnice. Tuto
 * informaci je možno využít např. pro výběr obrázku pro nakreslení budovy. Na
 * kraji budovy sousedícím se silnicí bude zobrazen chodník, aby se dala
 * vizuálně snadno poznat orientace budovy.
 *
 * @author Vratislav Jindra
 * @version 201706021059
 */
public class Building implements IBuilding {

    private int module;
    private AffineTransform affineTransform;
    private final EnumSet<Direction8> directionsToStreet;
    private Image image;
    private Position position;
    private URL imageUrl;

    /**
     * Konstruktor pro vytváření instancí třídy Building.
     *
     * @param position pozice, kde se budova bude na plánku města nacházet
     * @param directionsToStreet množina směrů (světových stran), na nichž
     * budova sousedí se silnicí
     */
    public Building(Position position, EnumSet<Direction8> directionsToStreet) {
        this.directionsToStreet = directionsToStreet;
        this.position = position;
        module = 50;
        setImageUrl();
        setImage();
        setAffineTransform();
    }

    /**
     * Vrátí množinu směrů, v nichž se nachází sousední segmenty silnice. U
     * budovy zcela uvnitř zástavby bude tato množina prázdná, u osamocené
     * budovy obklopené silnicemi budou součástí množiny všechny čtyři hlavní
     * světové strany, ostatní typy budov budou mít nastaveno něco mezi tím.
     * Tato informace naznačuje, kde lze vedle budovy nalézt chodník, po němž by
     * případně v budoucnu mohli chodit chodci.
     *
     * @return Množina směrů, v nichž se nachází sousední segmenty silnice
     */
    @Override
    public EnumSet<Direction8> getDirectionsToStreet() {
        return directionsToStreet;
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
     * Metoda pro nastavení obrázku instancí třídy Building.
     */
    private void setImage() {
        try {
            image = ImageIO.read(imageUrl);
        } catch (IOException ex) {
            Logger.getLogger(Building.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    /**
     * Metoda pro nastavení URL pro obrázek budovy. Různé URL odkazují na různé
     * obrázky, které se liší umístěním chodníku.
     */
    private void setImageUrl() {
        if (directionsToStreet.isEmpty()) {
            imageUrl = DataPkg.class.getResource("building.png");
        } else if (directionsToStreet.size() == 1) {
            imageUrl = DataPkg.class.getResource("building_1.png");
        } else if (directionsToStreet.size() == 2) {
            if ((directionsToStreet.contains(Direction8.NORTH)
                    && directionsToStreet.contains(Direction8.SOUTH))
                    || (directionsToStreet.contains(Direction8.WEST)
                    && directionsToStreet.contains(Direction8.EAST))) {
                imageUrl = DataPkg.class.getResource("building_2_counter.png");
            } else {
                imageUrl = DataPkg.class.getResource(
                        "building_2_perpendicular.png");
            }
        } else if (directionsToStreet.size() == 3) {
            imageUrl = DataPkg.class.getResource("building_3.png");
        } else {
            imageUrl = DataPkg.class.getResource("building_4.png");
        }
    }

    /**
     * Metoda starající se o správné otočení obrázku budovy.
     */
    private void setAffineTransform() {
        affineTransform = new AffineTransform();
        if (directionsToStreet.isEmpty() || directionsToStreet.size() == 4
                || directionsToStreet == null) {
            // Když budova nesousedí s žádným segmentem silnice, nebo když je
            // silnice ze všech čtyř stran od budovy.
            affineTransform = null;
        } else if (directionsToStreet.size() == 1) {
            // Když budova sousedí se silnicí v jednom směru.
            if (directionsToStreet.contains(Direction8.SOUTH)) {
                affineTransform.translate(position.getX(), position.getY());
                affineTransform.quadrantRotate(2, 25.0D, 25.0D);
            } else if (directionsToStreet.contains(Direction8.WEST)) {
                affineTransform.translate(position.getX(), position.getY());
                affineTransform.quadrantRotate(3, 25.0D, 25.0D);
            } else if (directionsToStreet.contains(Direction8.EAST)) {
                affineTransform.translate(position.getX(), position.getY());
                affineTransform.quadrantRotate(1, 25.0D, 25.0D);
            } else {
                affineTransform = null;
            }
        } else if (directionsToStreet.size() == 3) {
            // Budova sousedí se silnicí ve třech směrech.
            if (!directionsToStreet.contains(Direction8.NORTH)) {
                affineTransform.translate(position.getX(), position.getY());
                affineTransform.quadrantRotate(2, 25.0D, 25.0D);
            } else if (!directionsToStreet.contains(Direction8.WEST)) {
                affineTransform.translate(position.getX(), position.getY());
                affineTransform.quadrantRotate(1, 25.0D, 25.0D);
            } else if (!directionsToStreet.contains(Direction8.EAST)) {
                affineTransform.translate(position.getX(), position.getY());
                affineTransform.quadrantRotate(3, 25.0D, 25.0D);
            } else {
                affineTransform = null;
            }
        } else if (directionsToStreet.size() == 2) {
            // Budova sousedí se silnicí ve dvou směrech.
            if ((directionsToStreet.contains(Direction8.NORTH)
                    && directionsToStreet.contains(Direction8.SOUTH))
                    || (directionsToStreet.contains(Direction8.WEST)
                    && directionsToStreet.contains(Direction8.EAST))) {
                // Budova sousedí se silnicí ve dvou protilehlých směrech.
                if (directionsToStreet.contains(Direction8.WEST)) {
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(1, 25.0D, 25.0D);
                } else {
                    affineTransform = null;
                }
            } else {
                // Budova sousedí se silnicí ve dvou směrech, které jsou
                // navzájem kolmé.
                if (directionsToStreet.contains(Direction8.SOUTH)
                        && directionsToStreet.contains(Direction8.EAST)) {
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(1, 25.0D, 25.0D);
                } else if (directionsToStreet.contains(Direction8.SOUTH)
                        && directionsToStreet.contains(Direction8.WEST)) {
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(2, 25.0D, 25.0D);
                } else if (directionsToStreet.contains(Direction8.NORTH)
                        && directionsToStreet.contains(Direction8.WEST)) {
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(3, 25.0D, 25.0D);
                } else {
                    affineTransform = null;
                }
            }
        }
    }
}
