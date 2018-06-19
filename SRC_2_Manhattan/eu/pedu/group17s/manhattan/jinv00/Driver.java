package eu.pedu.group17s.manhattan.jinv00;

import eu.pedu.lib17s.geom.Multimover;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.manhattan.fw17s.Destination;
import eu.pedu.manhattan.fw17s.Holder;
import eu.pedu.manhattan.fw17s.IDriver;
import eu.pedu.manhattan.fw17s.IExitStreet;
import eu.pedu.manhattan.fw17s.IStreet;
import eu.pedu.manhattan.fw17s.ITownElement;
import eu.pedu.manhattan.fw17s.ITrafficLight;
import eu.pedu.manhattan.fw17s.IVehicle;

/**
 * Instance interfejsu {@code IDriver} představují řidiče, který dovede zadané
 * vozidlo k naplánovanému cíli.
 * <p>
 * Způsob zadávání cílů závisí na konkrétním implementujícím datovém typu.
 * Implicitní řešení předpokládá, že nemá-li řidič žádný vytčený cíl (případně
 * pokud již vytčeného cíle dosáhl), požádá o jeho zadání nějakého plánovače
 * cílů (instanci interfejsu {@link IPlanner}). Pro řidiče některých typů ale
 * mohou být cíle zadávány z klávesnice, anebo může být zvolen nějaký jiný
 * způsob.
 *
 * @author Vratislav Jindra
 * @version 201706011619
 */
public class Driver implements IDriver {

    private boolean removeDriver;
    private Destination destination;
    private IVehicle vehicle;

    /**
     * Konstruktor pro vytváření instancí třídy Driver.
     *
     * @param vehicle vozidlo, které daný řidič ovládá
     */
    public Driver(IVehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Vrátí ovládané vozidlo.
     *
     * @return Ovládané vozidlo
     */
    @Override
    public IVehicle getVehicle() {
        return vehicle;
    }

    /**
     * Korektně popojede se svěřeným autem o další krok k vytčenému cíli,
     * přičemž cestou respektuje semafory a překážející vozidla a v případě,
     * má-li na konci odbočit, včas zapne blinkry.
     * <p>
     * Nemá-li žádný vytčený cíl (případně pokud již vytčeného cíle dosáhl,
     * požádá nějakého plánovače cílů (instanci interfejsu {@link IPlanner}),
     * aby mu cíl našel, a k tomuto cíli pak vozidlo vede.
     * <p>
     * Tato metoda slouží k tomu, aby město řídící svůj provoz mohlo
     * synchronizovat dění tak, že zadaných časových intervalech (krocích) žádá
     * řidiče, aby provedli další krok. Frekvence volání této metody závisí na
     * řídícím městě.
     */
    @Override
    public void nextStep() {
        // Z vozidla řidiče si zjistím jeho pozici.
        Position position = vehicle.getPosition();
        Position coordinates = GUI.getCanvasManager()
                .positionPoint2Field(position);
        // Zjistím, na jaké jsem aktuálně silnici a tuto uložím do
        // currentStreet.
        IStreet currentStreet = (IStreet) Town.getInstance()
                .getElementAt(coordinates);
        // Pokud destinace neexistuje (je null), pak se v následujícím "ifu"
        // navrhne nová destinace.
        if (destination == null) {
            destination = Holder.getFactories().newPlanner()
                    .suggestNextDestination(vehicle);
            // Uložím destinaci do seznamu destinací ve třídě Town.
            Town.getInstance().addDestination(destination);
        }
        // Po navržení destinace se tady kontroluje, jestli (a kde) má vozidlo
        // použít blinkr (pokud ano, tak jaký).
        if (destination.blinkStreet != null
                && destination.blinkStreet.getPosition().x == coordinates.x
                && destination.blinkStreet.getPosition().y == coordinates.y) {
            // Vozidlo je na místě, kde má začít blikat.
            if (destination.blinkDirection == Destination.LeftRight.LEFT) {
                vehicle.blinkLeft();
            } else if (destination.blinkDirection
                    == Destination.LeftRight.RIGHT) {
                vehicle.blinkRight();
            }
        }
        // Pokud je daný objekt mezi právě přesouvanými, tato metoda dál
        // nepokračuje a skončí (dokud nebude zavolána znovu).
        if (Multimover.getInstance().isMoving(vehicle)) {
            return;
        }
        // Pokud vozidlo dorazilo do destinace, provede se následující blok
        // kódu.
        if (equalDestination(currentStreet, destination)) {
            // Má-li vozidlo v destinaci odbočit na nějakou stranu, stane se
            // tomu tak.
            if (destination.blinkDirection == Destination.LeftRight.LEFT) {
                vehicle.turnLeft();
            } else if (destination.blinkDirection
                    == Destination.LeftRight.RIGHT) {
                vehicle.turnRight();
            }
            // Stará destinace se odstraní ze seznamu s destinacemi.
            Town.getInstance().removeDestination(destination);
            // Má-li vozidlo opustit město, stane se tomu tak.
            if (destination.destStreet instanceof IExitStreet
                    && destination.blinkDirection
                            .equals(Destination.LeftRight.RIGHT)) {
                // Destinací byla "exit street", a mělo se na ní odbočit
                // doprava, což znamená, že se mělo odjet z města.
                // Odstraním vozidlo ze seznamu v TownBuilderu.
                TownBuilder.getInstance().removeVehicle(this.getVehicle());
                // Změním boolean removeDriver na true, abych ho při dalším
                // zavolání této metody mohl bezpečně (pomocí iterátoru)
                // odstranit.
                removeDriver = true;
                // Nastavím aktuální ulici na volnou.
                currentStreet.setEmpty(vehicle);
                // Po odstranění vozidla a řidiče z města zjistím, jestli se má
                // exit po opuštění vozidla zavřit. Pokud ano, zavřu ho.
                if (((ExitStreet) destination.destStreet)
                        .isCloseExitPending()) {
                    ((ExitStreet) destination.destStreet).closeExit();
                }
                return;
            }
            // Navrhne se nová destinace.
            destination = Holder.getFactories().newPlanner()
                    .suggestNextDestination(vehicle);
            // Do seznamu s destinacemi se přidá nová destinace.
            Town.getInstance().addDestination(destination);
        }
        // Boolean canGo je true, může-li vozidlo pokračovat v jízdě. Pokud mu
        // to semafor na kontrolované silnici neumožní, bude tento boolean
        // false, a vozidlo bude moct pokračovat v jízdě až v případě, že se
        // tento boolean znovu nabyde hodnoty true.
        boolean canGo = true;
        if (currentStreet instanceof ControlledStreet
                && ((ControlledStreet) currentStreet).getTrafficLight()
                        .getState() != ITrafficLight.TrafficLightState.GO
                && ((ControlledStreet) currentStreet).getTrafficLight()
                        .getState() != ITrafficLight.TrafficLightState.ATTENTION
                && ((ControlledStreet) currentStreet).getTrafficLight()
                        .getState() != ITrafficLight.TrafficLightState.LIGHTS_OFF) {
            // Je-li vozidlo na silnici kontrolované semaforem, a není-li stav
            // tohoto semaforu ani "GO" (zelená), ani "ATTENTION" (oranžová),
            // ani "LIGHTS_OFF" (vypnutá světla), pak bude boolean canGo false,
            // neboď vozidlo v tuto chvíli nemůže kvůli stavu semaforu jet dál.
            canGo = false;
        }
        if (canGo) {
            // Vozidlo může jet dál.
            int newPositionX = vehicle.getDirection().dx()
                    + GUI.getCanvasManager().positionPoint2Field(vehicle
                            .getPosition()).x;
            int newPositionY = vehicle.getDirection().dy()
                    + GUI.getCanvasManager().positionPoint2Field(vehicle
                            .getPosition()).y;
            Position newPosition = new Position(newPositionX, newPositionY);
            ITownElement nextElement = Town.getInstance()
                    .getElementAt(newPosition);
            if (!(nextElement instanceof IStreet)) {
                // Pokud další segment není silnice, musí to být budova (protože
                // jiné části města ve městě nejsou). V návrhu města nebo v kódu
                // programu je pravděpodobně chyba.
//                throw new IllegalStateException("Vozidlo na pozici "
//                        + vehicle.getPosition().toString()
//                        + " narazilo do budovy.");
                // Při vyšších rychlostech se stává, že nextElement je budova,
                // protože při hledání nextElementu ještě není vozidlo otočené
                // do směru, v jakém by mělo být, a pak je tím nextElementem
                // budova.
                return;
            }
            IStreet nextStreet = (IStreet) nextElement;
            if (nextStreet.isEmptyFor(vehicle)) {
                vehicle.forward(GUI.getCanvasManager().getStep());
                if (currentStreet.isEmptyFor(vehicle)) {
                }
                currentStreet.setEmpty(vehicle);
            }
        }
    }

    /**
     * Porovná, zda je zadaný segment silnice shodný s cílovým segmentem zadané
     * destinace.
     *
     * @param street zadaná ulice, která se porovnává s cílovým segmentem zadané
     * destinace
     * @param destination zadaná cílová destinace
     * @return true, jsou-li oba segmenty shodné; false, není-li zadaný segment
     * silnice shodný s cílovým segmentem destinace
     */
    private boolean equalDestination(IStreet street, Destination destination) {
        return street.getPosition().x == destination.destStreet.getPosition().x
                && street.getPosition().y == destination.destStreet
                .getPosition().y;
    }

    /**
     * Setter na atribut vehicle (vozidlo).
     *
     * @param vehicle nastavované vozidlo
     */
    public void setVehicle(IVehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Getter na atribut removeDriver.
     *
     * @return true, má-li se řidič odstranit; jinak vrací false
     */
    public boolean isRemoveDriver() {
        return this.removeDriver;
    }
}
