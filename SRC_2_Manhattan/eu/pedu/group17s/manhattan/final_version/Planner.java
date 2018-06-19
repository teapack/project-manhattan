package eu.pedu.group17s.manhattan.final_version;

import eu.pedu.group17s.manhattan.final_version.DATA.DataPkg;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.lib17s.util.Direction8;
import eu.pedu.manhattan.fw17s.Destination;
import eu.pedu.manhattan.fw17s.Holder;
import eu.pedu.manhattan.fw17s.IPlanner;
import eu.pedu.manhattan.fw17s.IStreet;
import eu.pedu.manhattan.fw17s.ITownElement;
import eu.pedu.manhattan.fw17s.IVehicle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Instance interfejsu {@code IPlanner} představují plánovače, které lze
 * požádat, aby zadanému vozidlu naplánovaly příští cíl cesty.
 *
 * @author Vratislav Jindra
 * @version 201706041058
 */
public class Planner implements IPlanner {

    List<IStreet> path;

    /**
     * Konstruktor třídy Planner.
     */
    public Planner() {
        path = new ArrayList<>();
    }

    /**
     * Naplánuje pro zadané vozidlo další cíl. Dalším cílem je nejbližší
     * segment, na němž lze odbočit. Plánovač si zjistí, kde je, naplánuje, zda
     * se na něm odbočí, a pokud ano, tak na kterou stranu se má otočit a
     * současně kde se mají zapnout příslušné blinkry. Metoda vrátí přepravku s
     * těmito informacemi.
     *
     * @param vehicle Vozidlo, pro něž plánujeme další cíl
     * @return Přepravka s informacemi potřebnými pro korektní přesun k
     * naplánovanému cíli
     */
    @Override
    public Destination suggestNextDestination(IVehicle vehicle) {
        // Z pozice vozidla zjistím, na jakém segmentu města se nachází.
        Position position = vehicle.getPosition();
        Direction8 direction = vehicle.getDirection();
        Position coordinates = GUI.getCanvasManager()
                .positionPoint2Field(position);
        ITownElement currentElement = Holder.getTown()
                .getElementAt(coordinates);
        if (!(currentElement instanceof IStreet)) {
            // Nenachází-li se vozidlo na silnici, je někde v návrhu města nebo
            // v programu chyba.
            throw new IllegalStateException("Vozidlo na pozici "
                    + position.toString() + " není na silnici.");
        }
        // Pokud je vozidlo na silnici, přidám jeho aktuální polohu do seznamu,
        // který reprezentuje cestu, kterou vozidlo pojede k jeho příští
        // destinaci.
        path.add((IStreet) currentElement);
        int nextX = direction.dx() + coordinates.x;
        int nextY = direction.dy() + coordinates.y;
        Position nextPosition = new Position(nextX, nextY);
        nextPosition = GUI.getCanvasManager().positionField2Point(nextPosition);
        Destination d;
        d = scanDestination(nextPosition, direction);
        return d;
    }

    /**
     * Pro zadanou pozici a směr vrátí vhodnou destinaci.
     *
     * @param position současná pozice, pro kterou hledáme sousedící pozici
     * @param direction směr, ve kterém hledáme (od současné pozice) její
     * sousední pozici
     * @return hledaná destinace
     */
    private Destination scanDestination(Position position,
            Direction8 direction) {
        Destination d;
        Position coordinates = GUI.getCanvasManager()
                .positionPoint2Field(position);
        ITownElement nextElement = Holder.getTown().getElementAt(coordinates);
        if (!(nextElement instanceof IStreet)) {
            // Není-li tento element druhem silnice, jedná se pravděpodobně o
            // budovu.
            throw new IllegalStateException("Vozidlo na pozici "
                    + position.toString() + " by narazilo do budovy.");
        }
        // Přidám aktuální zkoumanou pozici do seznamu path.
        path.add((IStreet) nextElement);
        // Vezmu si z aktuálně zkoumané pozice směr, kterým z ní mohou odjet
        // všechna přijíždějící vozidla.
        Direction8 nextDirection = ((IStreet) nextElement).getDirection();
        if (nextElement instanceof MultiWayStreet
                || nextElement instanceof ExitStreet) {
            List<Direction8> availableExits;
            // Aktuálně zkoumaná pozice je křižovatka, našel jsem tedy svoji
            // destinaci. Uložím si možné výjezdy z této křižovatky.
            if (nextElement instanceof MultiWayStreet) {
                availableExits = ((MultiWayStreet) nextElement)
                        .getExitsFor(direction);
            } else {
                availableExits = ((ExitStreet) nextElement)
                        .getExitsFor(direction);
            }
            // Vyberu náhodný výjezd z křižovatky.
            int randomDirection = new Random().nextInt(availableExits.size());
            nextDirection = availableExits.get(randomDirection);
        }
        if (!nextDirection.equals(direction)) {
            // Příští směr se nerovná směru, jakým je vozidlo aktuálně otočené.
            // Zjistím, jakým směrem se vozidlo otočí (jestli doleva nebo
            // doprava - abych nemusel složitě pracovat se světovými stranami).
            Destination.LeftRight turnDirection = getTurnDirection(direction,
                    nextDirection);
            if (path.isEmpty()) {
                // Pokud je seznam "path" prázdný, někde se stala chyba, a
                // vozidlo nemělo odkud vyjet.
                throw new IllegalStateException("Vozidlo vyjelo odnikud");
            }
            // Ulice, na které se začne blikat (bude-li vozidlo odbočovat).
            IStreet blinkStreet;
            if (path.size() >= 2) {
                blinkStreet = path.get(path.size() - 2);
            } else {
                blinkStreet = path.get(0);
            }
            // Vytvoří se nová destinace, která se na konci této metody vrátí.
            d = new Destination((IStreet) nextElement, blinkStreet,
                    turnDirection);
        } else {
            // Vozidlo nebude odbočovat.
            Position coords = GUI.getCanvasManager()
                    .positionPoint2Field(position);
            int nextX = direction.dx() + coords.x;
            int nextY = direction.dy() + coords.y;
            Position nextPosition = new Position(nextX, nextY);
            nextPosition = GUI.getCanvasManager()
                    .positionField2Point(nextPosition);
            // Jelikož vozidlo neodbočilo, bude pokračovat rovně, a proto
            // hledáme další destinaci - znovu se zavolá tato metoda.
            d = scanDestination(nextPosition, direction);
        }
        return d;
    }

    /**
     * Vrátí směr, kam bude vozidlo při odbočování blikat.
     *
     * @param previousDirection směr, ze kterého auto do křižovatky přijíždí
     * @param newDirection směr, kam bude auto z křižovatky pokračovat
     * @return směr, kam bude auto při odbočování blikat
     */
    private Destination.LeftRight getTurnDirection(Direction8 currentDirection,
            Direction8 nextDirection) {
        // Zjistím si "vzdálenost" mezi zadanými směry (rozdíl pořadí těchto
        // směrů).
        int ordinalDistance = currentDirection.ordinalDistanceTo(nextDirection);
        Destination.LeftRight turnDirection = null;
        switch (ordinalDistance) {
            case -6:
            case 2: {
                turnDirection = Destination.LeftRight.LEFT;
                break;
            }
            case -2:
            case 6: {
                turnDirection = Destination.LeftRight.RIGHT;
                break;
            }
            default: {
                turnDirection = Destination.LeftRight.NO;
                break;
            }
        }
        return turnDirection;
    }
}
