package eu.pedu.group17s.manhattan.final_version;

import eu.pedu.group17s.manhattan.final_version.DATA.DataPkg;
import eu.pedu.lib17s.canvasmanager.Painter;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.lib17s.util.Direction8;
import eu.pedu.manhattan.fw17s.IMultiWayStreet;
import eu.pedu.manhattan.fw17s.IVehicle;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Instance třídy {@code MultiWayStreet} představují segmenty silnice, z nichž
 * lze pokračovat více směry. To, zda musí vozidlo projet daný segment rovně,
 * anebo zde bude smět i odbočit, záleží na směru, v němž do segmentu přijíždí
 * (tj. záleží na směru přijíždějícího vozidla).
 *
 * @author Vratislav Jindra
 * @version 201706021126
 */
public class MultiWayStreet implements IMultiWayStreet {

    private int module;
    private final boolean turnLeftAllowed;
    private AffineTransform affineTransform;
    private final Direction8[] directions;
    private Image image;
    private IVehicle vehicle;
    private Position position;
    private URL imageUrl;

    /**
     * Konstruktor, pomocí něhož lze vytvářet instance třídy MultiWayStreet.
     *
     * @param position pozice, na které se daný segment silnice nachází
     * @param turnLeftAllowed true, je-li možné na daném segmentu silnice
     * zahnout doleva; jinak false
     * @param direction směry, kterými je možné daný segment opustit
     */
    public MultiWayStreet(Position position, boolean turnLeftAllowed,
            Direction8... direction) {
        this.position = position;
        this.turnLeftAllowed = turnLeftAllowed;
        this.directions = direction;
        module = 50;
        setImageUrl();
        setImage(imageUrl);
        setAffineTransform();
    }

    /**
     * Vrátí seznam směrů, v nichž může být daný segment opuštěn vozidlem
     * přijíždějícím ze zadaného směru.
     *
     * @param direction Směr, v němž jede přijíždějící vozidlo
     * @return Seznam směrů, v nichž může být daný segment opuštěn po příjezdu
     * ze zadaného směru
     */
    @Override
    public List<Direction8> getExitsFor(Direction8 direction) {
        List<Direction8> exits = new ArrayList<>();
        if (turnLeftAllowed) {
            // Pokud se může odbočovat doleva, je možné segment opustit ve
            // kterémkoliv směru z možných exitů.
            exits.addAll(Arrays.asList(directions));
        } else {
            // Není možné odbočit vlevo, musím tedy projít seznam možných exitů
            // a ošetřit to, aby nešlo odbočit doleva.
            switch (direction) {
                case NORTH:
                    if (Arrays.asList(directions).contains(Direction8.NORTH)
                            && Arrays.asList(directions)
                                    .contains(Direction8.EAST)) {
                        exits.addAll(Arrays.asList(directions));
                    } else {
                        exits.add(Direction8.NORTH);
                    }
                    break;
                case SOUTH:
                    if (Arrays.asList(directions).contains(Direction8.SOUTH)
                            && Arrays.asList(directions)
                                    .contains(Direction8.WEST)) {
                        exits.addAll(Arrays.asList(directions));
                    } else {
                        exits.add(Direction8.SOUTH);
                    }
                    break;
                case WEST:
                    if (Arrays.asList(directions).contains(Direction8.NORTH)
                            && Arrays.asList(directions)
                                    .contains(Direction8.WEST)) {
                        exits.addAll(Arrays.asList(directions));
                    } else {
                        exits.add(Direction8.WEST);
                    }
                    break;
                case EAST:
                    if (Arrays.asList(directions).contains(Direction8.SOUTH)
                            && Arrays.asList(directions)
                                    .contains(Direction8.EAST)) {
                        exits.addAll(Arrays.asList(directions));
                    } else {
                        exits.add(Direction8.EAST);
                    }
                    break;
            }
        }
        return exits;
    }

    /**
     * Vrátí seznam směrů, v nichž může být daný segment opuštěn zadaným
     * vozidlem, přičemž se předpokládá, že vozidlo se právě chystá vjet do
     * segmentu.
     *
     * @param vehicle Vozidlo, které se ptá na možné pokračování
     * @return Seznam směrů, v nichž může být daný segment opuštěn zadaným
     * vozidlem
     */
    @Override
    public List<Direction8> getExitsFor(IVehicle vehicle) {
        return getExitsFor(vehicle.getDirection());
    }

    /**
     * Vrátí směr vozidla nově umístěného na daný segment silnice. Ten je u
     * jednosměrných ulic i směrem, ve kterém se po daném segmentu silnice
     * jezdí, u rozcestí je to směr, kterým mohou odjet všechna přijíždějící
     * vozidla.
     *
     * @return Požadovaný směr
     */
    @Override
    public Direction8 getDirection() {
        if (turnLeftAllowed) {
            // Je možné odbočit vlevo, na tomto druhu silnice tedy neexistuje
            // jediný směr, kterým mohou odjet všechna přijíždějící vozidla (je
            // to množina směrů). Vyberu proto z těchto směrů jeden náhodný.
            int randomDirection = new Random().nextInt(directions.length);
            return directions[randomDirection];
        } else {
            // Není možné odbočit vlevo, proto existuje jediný směr, který mohou
            // odjet všechna přijíždějící vozidla.
            if (Arrays.asList(directions).contains(Direction8.NORTH)
                    && Arrays.asList(directions).contains(Direction8.EAST)) {
                return Direction8.EAST;
            } else if (Arrays.asList(directions).contains(Direction8.NORTH)
                    && Arrays.asList(directions).contains(Direction8.WEST)) {
                return Direction8.NORTH;
            } else if (Arrays.asList(directions).contains(Direction8.SOUTH)
                    && Arrays.asList(directions).contains(Direction8.WEST)) {
                return Direction8.WEST;
            } else {
                return Direction8.SOUTH;
            }
        }
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
     * Metoda pro nastavení obrázku instancí třídy MultiWayStreet.
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
     * Metoda pro nastavení URL obrázku, který bude na zobrazené mapě města
     * reprezentovat danou instanci.
     */
    private void setImageUrl() {
        if (!turnLeftAllowed) {
            // Není možné odbočit vlevo.
            imageUrl = DataPkg.class.getResource(
                    "multi_way_street_false.png");
        } else {
            if (directions.length == 3) {
                // Výjezdy jsou tři.
                imageUrl = DataPkg.class.getResource(
                        "multi_way_street_3.png");
            } else if ((Arrays.asList(directions).contains(Direction8.NORTH)
                    && Arrays.asList(directions).contains(Direction8.SOUTH))
                    || (Arrays.asList(directions).contains(Direction8.WEST)
                    && Arrays.asList(directions).contains(Direction8.EAST))) {
                // Výjezdy jsou dva a jsou "proti sobě".
                imageUrl = DataPkg.class.getResource(
                        "multi_way_street_2_counter.png");
            } else {
                // Výjezdy jsou dva a jsou na sebe kolmé.
                imageUrl = DataPkg.class.getResource(
                        "multi_way_street_2_perpendicular.png");
            }
        }
    }

    /**
     * Metoda starající se o správné otočení obrázku tohoto segmentu silnice.
     */
    private void setAffineTransform() {
        if (directions != null) {
            affineTransform = new AffineTransform();
            if (!turnLeftAllowed) {
                // Není možné odbočit vlevo, proto je už díky metodě setImageUrl
                // vybrán správný obrázek (mutli_way_street_false.png), a tady
                // ho jen správně otočím.
                switch (getDirection()) {
                    case NORTH:
                        affineTransform.translate(position.getX(),
                                position.getY());
                        affineTransform.quadrantRotate(3, 25.0D, 25.0D);
                        break;
                    case SOUTH:
                        affineTransform.translate(position.getX(),
                                position.getY());
                        affineTransform.quadrantRotate(1, 25.0D, 25.0D);
                        break;
                    case WEST:
                        affineTransform.translate(position.getX(),
                                position.getY());
                        affineTransform.quadrantRotate(2, 25.0D, 25.0D);
                        break;
                    default:
                        affineTransform = null;
                        break;
                }
            } else {
                // Je možné odbočit vlevo, proto je metodou setImageUrl vybrán
                // jeden z množiny tří možných obrázků, a tady ho opět jen
                // správně otočím.
                if (directions.length == 3) {
                    // Je vybrán obrázek se třemi možnými výjezdy z daného
                    // segmentu.
                    if (!Arrays.asList(directions).contains(Direction8.NORTH)) {
                        affineTransform.translate(position.getX(),
                                position.getY());
                        affineTransform.quadrantRotate(2, 25.0D, 25.0D);
                    } else if (!Arrays.asList(directions)
                            .contains(Direction8.WEST)) {
                        affineTransform.translate(position.getX(),
                                position.getY());
                        affineTransform.quadrantRotate(1, 25.0D, 25.0D);
                    } else if (!Arrays.asList(directions)
                            .contains(Direction8.EAST)) {
                        affineTransform.translate(position.getX(),
                                position.getY());
                        affineTransform.quadrantRotate(3, 25.0D, 25.0D);
                    } else {
                        affineTransform = null;
                    }
                } else if ((Arrays.asList(directions).contains(Direction8.WEST)
                        && Arrays.asList(directions).contains(Direction8.EAST))
                        || (Arrays.asList(directions).contains(Direction8.SOUTH)
                        && Arrays.asList(directions)
                                .contains(Direction8.EAST))) {
                    // Buď je vybrán obrázek s výjezdy "proti sobě" na západ a
                    // na východ, nebo obrázek s kolmými výjezdy.
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(1, 25.0D, 25.0D);
                } else if (Arrays.asList(directions).contains(Direction8.SOUTH)
                        && Arrays.asList(directions)
                                .contains(Direction8.WEST)) {
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(2, 25.0D, 25.0D);
                } else if (Arrays.asList(directions).contains(Direction8.NORTH)
                        && Arrays.asList(directions)
                                .contains(Direction8.WEST)) {
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(3, 25.0D, 25.0D);
                } else {
                    // V této možnosti je opět buď vybrán obrázek s výjezdy
                    // "proti sobě" (tentokrát na sever a na jih), nebo obrázek
                    // s kolmými výjezdy.
                    affineTransform = null;
                }
            }
        }
    }
}
