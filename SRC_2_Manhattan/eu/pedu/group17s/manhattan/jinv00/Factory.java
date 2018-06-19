package eu.pedu.group17s.manhattan.jinv00;

import eu.pedu.lib17s.geom.Position;
import eu.pedu.lib17s.util.Direction8;
import eu.pedu.manhattan.fw17s.Holder;
import eu.pedu.manhattan.fw17s.IBuilding;
import eu.pedu.manhattan.fw17s.IControlledStreet;
import eu.pedu.manhattan.fw17s.IDriver;
import eu.pedu.manhattan.fw17s.IExitStreet;
import eu.pedu.manhattan.fw17s.IFactories;
import eu.pedu.manhattan.fw17s.IMultiWayStreet;
import eu.pedu.manhattan.fw17s.IPlanner;
import eu.pedu.manhattan.fw17s.IStreet;
import eu.pedu.manhattan.fw17s.ITownElement;
import eu.pedu.manhattan.fw17s.ITrafficLight;
import eu.pedu.manhattan.fw17s.IVehicle;
import java.util.EnumSet;
import java.util.List;

/**
 * Instance interfejsu {@code IFactories} představují tovární objekty
 * poskytující sadu továrních metod pro výrobu jednotlivých součástí města. Toto
 * řešení umožňuje definovat tovární objekt tak, že se již definované metody
 * (povětšinou konstruktory) použijí přímo, a místo metod, na jejichž výslednou
 * definici se teprve čeká, se dočasně použijí nějaké záslepky.
 *
 * @author Vratislav Jindra
 * @version 201706011824
 */
public class Factory implements IFactories {

    private boolean townCreated;

    /**
     * Vytvoří na zadané políčkové pozici novou budovu, která bude v zadaných
     * směrech sousedit se silnicí. Budova bude zaujímat právě ono zadané pole.
     *
     * @param position Políčková pozice budovy
     * @param directions Směry, v nichž sousedí se segmenty silnice
     * @return Vytvořená budova
     */
    @Override
    public IBuilding newBuilding(Position position,
            EnumSet<Direction8> directions) {
        return new Building(position, directions);
    }

    /**
     * Vytvoří na zadané políčkové pozici nový segment jednosměrné silnice, po
     * němž se bude jezdit v zadaném směru. Vytvořený segment silnice bude
     * zaujímat právě ono zadané pole.
     *
     * @param position Políčková pozice vytvářeného segmentu silnice
     * @param direction Směr, v němž se bude po silnici jezdit
     * @return Vytvořený segment silnice
     */
    @Override
    public IStreet newOneWayStreet(Position position, Direction8 direction) {
        return new OneWayStreet(position, direction);
    }

    /**
     * Vytvoří na zadané políčkové pozici nový segment silnice, který je
     * součástí křižovatky. Vytvořený segment silnice bude zaujímat právě ono
     * zadané pole.
     *
     * @param position Políčková pozice vytvářeného segmentu silnice
     * @param turnLeftAllowed Je-li zde povoleno odbočení vlevo
     * @param directions Směry, v nichž lze segment opustit
     * @return Vytvořený segment silnice
     */
    @Override
    public IMultiWayStreet newMultiWayStreet(Position position,
            boolean turnLeftAllowed, Direction8... directions) {
        return new MultiWayStreet(position, turnLeftAllowed, directions);
    }

    /**
     * Vytvoří na zadané políčkové pozici nový segment jednosměrné silnice, na
     * němž bude odjezd řízen semaforem. Vytvořený segment silnice bude zaujímat
     * právě ono zadané pole.
     *
     * @param position Políčková pozice vytvářeného segmentu silnice
     * @param direction Směr, v němž se bude po silnici jezdit
     * @return Vytvořený segment řízené silnice
     */
    @Override
    public IControlledStreet newControlledStreet(Position position,
            Direction8 direction) {
        return new ControlledStreet(position, direction);
    }

    /**
     * Vytvoří nový semafor řídící odjezd ze segmentu zadaného v parametru.
     * Semafor bude umístěn v levé horní částí pole vpravo vedle řízeného
     * segmentu silnice (počítáno vůči směru, v němž řídí dopravu) tak, aby v
     * případě potřeby bylo možno umístit do jednoho pole čtyři semafory řídící
     * odjezd ze čtyř sousedních polí daného pole, aniž by se tyto semafory
     * nějak překrývaly.
     *
     * @param checkedStreet Segment silnice řízený vytvořeným semaforem
     * @return Vytvořený semafor
     */
    @Override
    public ITrafficLight newTrafficLight(IControlledStreet checkedStreet) {
        return new TrafficLight(checkedStreet);
    }

    /**
     * Vytvoří na zadané políčkové pozici nové vozidlo, natočené do zadaného
     * směru. Vozidlo bude zaujímat právě ono zadané pole.
     *
     * @param position Výchozí políčková pozice vytvářeného vozidla
     * @param direction Směr, do nějž bude vozidlo natočeno
     * @return Vytvořené vozidlo
     */
    @Override
    public IVehicle newVehicle(Position position, Direction8 direction) {
        IVehicle vehicle = new Vehicle(position, direction);
        newDriver(vehicle);
        if (townCreated) {
            // Město již bylo vytvořeno. Pokud bylo vytvořeno bez vozidel, můžu
            // zavolat tuto metodu (newVehicle), abych do města přidal nová
            // vozidla. Každé nové vozidlo přidané až po vytvoření města tedy
            // bude na následujících dvou řádcích kódu přidáno do Canvas
            // Manageru, a bude přidáno na seznam "posluchačů" hodin (třídy
            // Clock).
            Clock.getInstance().addListener((IListener) vehicle);
            GUI.getCanvasManager().add(vehicle);
        }
        return vehicle;
    }

    /**
     * Vrátí řidiče, který dokáže dovést přidělené vozidlo k naplánovanému cíli.
     *
     * @param vehicle Vozidlo ovládané daným řidičem
     * @return Vytvořený řidič
     */
    @Override
    public IDriver newDriver(IVehicle vehicle) {
        IDriver driver = new Driver(vehicle);
        Town.getInstance().addDriver(driver);
        return driver;
    }

    /**
     * Vrátí plánovače, kterého lze požádat, aby zadanému vozidlu naplánoval
     * příští cíl cesty.
     *
     * @return Vytvořený plánovač
     */
    @Override
    public IPlanner newPlanner() {
        return new Planner();
    }

    /**
     * Vytvoří na zadané políčkové pozici nový segment silnice, který je
     * součástí křižovatky, z níž je někdy možno opustit město. Možnost opustit
     * město lze zapnout a vypnout. Vytvořený segment silnice bude zaujímat
     * právě ono zadané pole.
     *
     * @param position Políčková pozice vytvářeného segmentu silnice
     * @param direction1 Směr, v němž je silnice průjezdná jen rovně
     * @param direction2 Směr, z nějž je možno na daném políčku odbočit vpravo
     * (bude-li otevřen) a opustit tak město
     * @return Vytvořený segment křižovatky; V jednodušší verzi může metoda
     * vracet prázdný odkaz
     */
    @Override
    public IExitStreet newExitStreet(Position position, Direction8 direction1,
            Direction8 direction2) {
        return new ExitStreet(position, direction1, direction2);
    }

    /**
     * Vytvoří město se zadanými statickými prvky a v nich umístěnými vozidly a
     * uloží je do objektu třídy {@link Holder}.
     *
     * @param townElements Pole všech statických prvků města
     * @param vehicles Seznam spravovaných vozidel
     */
    @Override
    public void createTown(ITownElement[][] townElements,
            List<IVehicle> vehicles) {
        Holder.setTown(Town.getInstance());
        this.townCreated = true;
        if (vehicles != null) {
            vehicles.stream().forEach(vehicle -> {
                Clock.getInstance().addListener((IListener) vehicle);
                GUI.getCanvasManager().add(vehicle);
            });
        }
    }
}
