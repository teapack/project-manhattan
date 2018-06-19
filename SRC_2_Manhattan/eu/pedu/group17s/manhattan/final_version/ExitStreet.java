package eu.pedu.group17s.manhattan.final_version;

import eu.pedu.group17s.manhattan.final_version.DATA.DataPkg;
import eu.pedu.lib17s.canvasmanager.Painter;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.lib17s.util.Direction8;
import eu.pedu.manhattan.fw17s.Destination;
import eu.pedu.manhattan.fw17s.IExitStreet;
import eu.pedu.manhattan.fw17s.IVehicle;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Instance třídy {@code ExitStreet} představují segment silnice umožňující
 * opuštění města. Opuštění je možno odbočením, které je možno na požádání
 * otevřít anebo zavřít.
 *
 * @author Vratislav Jindra
 * @version 201706021126
 */
public class ExitStreet implements IExitStreet {

    private boolean exitOpened;
    private boolean closeExitPending;
    private int module;
    private AffineTransform affineTransform;
    private Collection<Destination> destinations;
    private final Direction8 direction1;
    private final Direction8 direction2;
    private Image image;
    private IVehicle vehicle;
    private Position position;
    private URL imageUrl;

    /**
     * Konstruktor, pomocí kterého budu vytvářet instance třídy ExitStreet. Tyto
     * segmenty silnice reprezentují křižovatku, z níž je možno opustit město.
     * Možnost opustit město lze vypnout a zapnout.
     *
     * @param position políčková pozice vytvářeného segmentu silnice
     * @param direction1 směr, v němž je silnice průjezdná jen rovně
     * @param direction2 směr, z němž je možno na daném políčku odbočit vpravo
     * (bude-li otevřen) a opustit tak město
     */
    public ExitStreet(Position position, Direction8 direction1,
            Direction8 direction2) {
        this.position = position;
        this.direction1 = direction1;
        this.direction2 = direction2;
        setExitOpened(false);
        closeExitPending = false;
        module = 50;
        setImageUrl();
        setImage(imageUrl);
        setAffineTransform();
    }

    /**
     * Otevře odbočku umožňující opustit město.
     */
    @Override
    public void openExit() {
        setExitOpened(true);
        closeExitPending = false;
        setImageUrl();
        setImage(imageUrl);
    }

    /**
     * Zavře odbočku umožňující opustit město. Pokud si však před zavřením
     * nějaké auto objednalo odbočení, dojde k vlastnímu zavření až poté, co
     * dané auto projede příslušným segmentem.
     */
    @Override
    public void closeExit() {
        destinations = Town.getInstance().getDestinations();
        if (destinations.isEmpty()) {
            closeExitPending = false;
            setExitOpened(false);
            setImageUrl();
            setImage(imageUrl);
        } else {
            for (Destination d : destinations) {
                if (d.destStreet.equals(this) && d.blinkDirection
                        .equals(Destination.LeftRight.RIGHT)) {
                    closeExitPending = true;
                    return;
                } else {
                    closeExitPending = false;
                    setExitOpened(false);
                    setImageUrl();
                    setImage(imageUrl);
                }
            }
        }
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
        if (direction.equals(direction1)) {
            // Segment má být průjezdný jen rovně, nebo má být možné odbočit
            // vpravo. Proto do něj nejde přijet z jiného směru, než v jakém
            // směru tento segment směřuje (než jaký je směr "direction1").
            exits.add(direction);
            if (isExitOpened()) {
                exits.add(direction2);
            }
        } else {
            // Segment musí být průjezdný jen rovně, případně je v něm možné
            // odbočit doprava. Pokud se do něj tedy pokusí vozidlo vjet z
            // jiného směru, než jakým směrem je myšleno "rovně", je chyba v
            // návrhu města.
            throw new IllegalStateException("V návrhu města nebo v programu je "
                    + "chyba. Vozidlo vjelo do výjezdového segmentu z "
                    + "nepovoleného směru.");
        }
        return exits;
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
        return this.direction1;
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
            // Na této ulici je auto z parametru.
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
     * Metoda pro nastavení URL pro obrázek tohoto segmentu silnice.
     */
    private void setImageUrl() {
        if (isExitOpened()) {
            imageUrl = DataPkg.class.getResource("exit_street_opened.png");
        } else {
            imageUrl = DataPkg.class.getResource("exit_street_closed.png");
        }
    }

    /**
     * Metoda pro nastavení obrázku instance třídy ExitStreet.
     */
    private void setImage(URL imageUrl) {
        try {
            image = ImageIO.read(imageUrl);
        } catch (IOException ex) {
            Logger.getLogger(ExitStreet.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    /**
     * Metoda starající se o správné otočení obrázku.
     */
    private void setAffineTransform() {
        if (getDirection() != null) {
            affineTransform = new AffineTransform();
            switch (this.getDirection()) {
                case SOUTH:
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(2, 25.0, 25.0);
                    break;
                case WEST:
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(3, 25.0, 25.0);
                    break;
                case EAST:
                    affineTransform.translate(position.getX(), position.getY());
                    affineTransform.quadrantRotate(1, 25.0, 25.0);
                    break;
                default:
                    affineTransform = null;
            }
        } else {
            affineTransform = null;
            System.err.println("Silnice ExitStreet na pozici "
                    + getPosition().toString()
                    + " nemá nastavený výjezdový směr.");
        }
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
     * Setter na atribut exitOpened.
     *
     * @param exitOpened true, je-li exit (výjezd z města) otevřený; jinak false
     */
    private void setExitOpened(boolean exitOpened) {
        this.exitOpened = exitOpened;
    }

    /**
     * Getter na atribut exitOpened.
     *
     * @return true, je-li exit (výjezd z města) otevřený; jinak false
     */
    private boolean isExitOpened() {
        return this.exitOpened;
    }

    /**
     * Getter na atribut closeExitPending.
     *
     * @return true, čeká-li se na zavření exitu, jinak vrátí false
     */
    public boolean isCloseExitPending() {
        return closeExitPending;
    }
}
