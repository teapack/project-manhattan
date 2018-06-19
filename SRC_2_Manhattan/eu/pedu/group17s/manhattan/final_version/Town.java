package eu.pedu.group17s.manhattan.final_version;

import eu.pedu.lib17s.canvasmanager.Painter;
import eu.pedu.lib17s.geom.Size;
import eu.pedu.manhattan.fw17s.Destination;
import eu.pedu.manhattan.fw17s.IDriver;
import eu.pedu.manhattan.fw17s.ITown;
import eu.pedu.manhattan.fw17s.ITownElement;
import eu.pedu.manhattan.fw17s.ITrafficLight;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Instance interfejsu {@code ITown} představuje město či dopravní hřiště, v
 * němž lze spustit simulaci provozu řízeného semafory. V rozšířené verzi musí
 * mít město definován alespoň jeden vstupní bod, odkud do města přijíždějí nová
 * vozidla, a alespoň jeden výstupní bod, kudy mohou vozidla zobrazovanou část
 * města opustit.
 *
 * @author Vratislav Jindra
 * @version 201705311216
 */
public class Town implements ITown, IListener {

    private static final Town SINGLETON = new Town();
    private Size size;
    private final Collection<Destination> destinations;
    private final Collection<IDriver> drivers;
    private final Collection<IDriver> newDrivers;
    private final Collection<ITrafficLight> trafficLights;
    private ITownElement[][] townElements;

    /**
     * Getter na jedinou instanci této třídy.
     *
     * @return instance této třídy
     */
    public static Town getInstance() {
        return SINGLETON;
    }

    /**
     * Konstruktor třídy Town.
     */
    public Town() {
        trafficLights = new ArrayList<>();
        destinations = new ArrayList<>();
        drivers = Collections.synchronizedList(new ArrayList<>());
        newDrivers = new ArrayList<>();
    }

    /**
     * Vrátí aktuální rozměr města (počet sloupců a řádků).
     *
     * @return Aktuální rozměr města = počet sloupců ({@code width}) a řádků
     * ({@code hight})
     */
    @Override
    public Size getSize() {
        return size;
    }

    /**
     * Vrátí prvek města na zadaných políčkových souřadnicích.
     *
     * @param column Sloupec požadovaného prvku
     * @param row Řádek požadovaného prvku
     * @return Prvek města na zadaných políčkových souřadnicích
     */
    @Override
    public ITownElement getElementAt(int column, int row) {
        return townElements[column][row];
    }

    /**
     * Vrátí (nemodifikovatelnou) kolekci semaforů řídících provoz ve městě.
     *
     * @return Kolekce (nemodifikovatelná) řidičů ovládajících vozidla ve městě
     */
    @Override
    public Collection<ITrafficLight> getTrafficLights() {
        return trafficLights;
    }

    /**
     * Vrátí (nemodifikovatelnou) kolekci řidičů ovládajících vozidla ve městě.
     *
     * @return Kolekce (nemodifikovatelná) řidičů ovládajících vozidla ve městě
     */
    @Override
    public Collection<IDriver> getDrivers() {
        return Collections.unmodifiableCollection(
                (Collection<? extends IDriver>) drivers);
    }

    /**
     * Spustí provoz ve městě, tj. začne ovládat semafory a prostřednictvím
     * opakovaného volání metody {@link IDriver#nextStep()} aktivovat řidiče k
     * provedení další akce.
     */
    @Override
    public void startTrafic() {
        Clock.getInstance().setState(true);
    }

    /**
     * Zastaví provoz ve městě, tj. zhasne všechny semafory a přestane vybízet
     * řidiče k provádění dalších kroků.
     */
    @Override
    public void stopTrafic() {
        Clock.getInstance().setState(false);
    }

    /**
     * Prostřednictvím dodaného kreslítka vykreslí obraz své instance.
     *
     * @param painter Kreslítko schopné kreslit na plátno ovládané správcem
     */
    @Override
    public void paint(Painter painter) {
    }

    /**
     * Metoda pro nastavení velikosti města.
     *
     * @param width šířka města
     * @param height výška města
     */
    public void setSize(int width, int height) {
        townElements = new ITownElement[width][height];
        this.size = new Size(width, height);
    }

    /**
     * Metoda, která nastaví zadaný prvek města na zadané souřadnice.
     *
     * @param townElement prvek města, který se má nastavit na zadané souřadnice
     * @param x x-ová souřadnice, na kterou se má nastavit zadaný prvek města
     * @param y y-ová souřadnice, na kterou se má nastavit zadaný prvek města
     */
    public void setTownElement(ITownElement townElement, int x, int y) {
        townElements[x][y] = townElement;
    }

    /**
     * Getter na dvourozměrné pole s prvky města.
     *
     * @return dvourozměrné pole s prvky města
     */
    public ITownElement[][] getTownElements() {
        return townElements;
    }

    /**
     * Metoda, která zapíše seznam s prvky města do dvourozměrného pole.
     *
     * @param townElements seznam s prvky města, který se má "přepsat" do pole
     */
    public void setTownElements(List<ITownElement> townElements) {
        townElements.stream().forEach(element -> {
            this.setTownElement(element, element.getX(), element.getY());
            GUI.getCanvasManager().add(element);
        });
    }

    /**
     * Metoda, která zadaný seznam se semafory "přepíše" do kolekce semaforů.
     *
     * @param trafficLights seznam se semafory, který se má uložit do kolekce
     */
    public void setTrafficLights(List<ITownElement> trafficLights) {
        trafficLights.stream().forEach(trafficLight -> {
            this.trafficLights.add((ITrafficLight) trafficLight);
            GUI.getCanvasManager().addToFront(trafficLight);
        });
    }

    /**
     * Metoda pro přidání řidiče do města.
     *
     * @param driver přidávaný řidič
     */
    public void addDriver(IDriver driver) {
        newDrivers.add(driver);
    }

    /**
     * Metoda pro odebrání řidiče z města.
     *
     * @param driver odebíraný řidič
     */
    public void removeDriver(IDriver driver) {
        drivers.remove(driver);
    }

    /**
     * Metoda pro přidání zadané destinace do seznamu destinací.
     *
     * @param destination destinace, která se má přidat do seznamu s destinacemi
     */
    public void addDestination(Destination destination) {
        destinations.add(destination);
    }

    /**
     * Metoda pro odebrání destinace ze seznamu s destinacemi.
     *
     * @param destination odebíraná destinace
     */
    public void removeDestination(Destination destination) {
        destinations.remove(destination);
    }

    /**
     * Metoda, která vrátí seznam destinací.
     *
     * @return seznam destinací
     */
    public Collection<Destination> getDestinations() {
        return destinations;
    }

    /**
     * Metoda, která proběhne při každém "ticku" hodin (viz třída Clock). V této
     * třídě tato metoda každého řidiče požádá, aby provedl "další krok" na
     * cestě ke své destinaci.
     */
    @Override
    public void tick() {
        drivers.addAll(newDrivers);
        Iterator<IDriver> iter = drivers.iterator();
        while (iter.hasNext()) {
            IDriver driver = iter.next();
            driver.nextStep();
            if (((Driver) driver).isRemoveDriver()) {
                iter.remove();
            }
        }
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
        return false;
    }
}
