package eu.pedu.group17s.manhattan.final_version;

import eu.pedu.group17s.manhattan.final_version.DATA.DataPkg;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.lib17s.util.Direction8;
import eu.pedu.manhattan.fw17s.Holder;
import eu.pedu.manhattan.fw17s.IBuilding;
import eu.pedu.manhattan.fw17s.IControlledStreet;
import eu.pedu.manhattan.fw17s.IExitStreet;
import eu.pedu.manhattan.fw17s.IFactories;
import eu.pedu.manhattan.fw17s.IStreet;
import eu.pedu.manhattan.fw17s.ITownBuilder;
import eu.pedu.manhattan.fw17s.ITownElement;
import eu.pedu.manhattan.fw17s.IVehicle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Instance interfejsu {@code ITownBuilder} představují budovatele měst, kteří
 * jsou schopni vytvořit město na základě informací v textovém souboru.
 * Předpokládají se dvě možné verze měst:
 * <ul>
 * <li>Zjednodušené zadání určené pro tříčlenné skupiny, v němž stačí vytvořit
 * město se zadaným počtem vozidel.
 * <br>&nbsp;</li>
 * <li>Rozšířené zadání určené pro čtyřčlenné skupiny, v němž je třeba dovybavit
 * město vstupním a výstupním segmentem, přičemž vstupní segment slouží pro
 * příjezd vozidel z oblasti mimo zobrazované město a výstupní segment slouží
 * pro jejich odjezd.
 * </li>
 * </ul>
 *
 * @author Vratislav Jindra
 * @version 201705311153
 */
public class TownBuilder implements ITownBuilder {

    private int specialCar = 0;
    private int charNumber;
    private int enterStreetNumber = -1;
    private int exitKey;
    private int inputKey;
    private int lineLength;
    private int lineNumber;
    private final int module = 50;
    private final List<IExitStreet> exitStreets = new ArrayList<>();
    private final List<ITownElement> townElements = new ArrayList<>();
    private final List<ITownElement> trafficLights = new ArrayList<>();
    private final List<IVehicle> vehicles = new ArrayList<>();
    private String line;
    private static final TownBuilder SINGLETON = new TownBuilder();
    private ITownElement[][] townElementsArray;

    /**
     * Getter na instanci třídy TownBuilder.
     *
     * @return instance třídy TownBuilder
     */
    public static TownBuilder getInstance() {
        return SINGLETON;
    }

    /**
     * Na základě informací ze zadaného souboru vytvoří město a uloží je do
     * schránky ve třídě {@link Holder}.
     *
     * @param reader Vstupní proud s podklady pro tvorbu města
     * @param factories Tovární objekt pro výrobu součástí města
     * @param inputKey Kód klávesy, po jejímž stisku se na vstupu objeví nové
     * vozidlo
     * @param exitKey Kód klávesy, po jejímž stisku se otevře výstup pro jedno
     * vozidlo
     */
    @Override
    public void buildTown(BufferedReader reader, IFactories factories,
            int inputKey, int exitKey) {
        Holder.setFactories(factories);
        GUI.getInstance().setCanvasManager(true);
        GUI.getCanvasManager().stopPainting();
        // Přečte zadaný soubor a "postaví" z něj město.
        try {
            handleCharacters(reader);
        } catch (IOException ex) {
            Logger.getLogger(TownBuilder.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        // Nastavíme velikost města podle souboru s plánkem města.
        Town.getInstance().setSize(lineLength, lineNumber);
        // Zinicializuji si dvourozměrné pole s prvky města.
        townElementsArray = new ITownElement[lineLength][lineNumber];
        // Upravím budovy tak, aby věděly o silnicích vedle sebe.
        editBuildings();
        // Předám městu prvky, ze kterých se město skládá (již včetně upravených
        // budov.
        Town.getInstance().setTownElements(townElements);
        Town.getInstance().setTrafficLights(trafficLights);

        Holder.getFactories().createTown(Town.getInstance().getTownElements(),
                null);
        GUI.getInstance().start(Town.getInstance().getSize());

        Clock.getInstance().addListener(Town.getInstance());
        TrafficLightHandler.getInstance();

        GUI.getCanvasManager().returnPainting();

        this.inputKey = inputKey;
        this.exitKey = exitKey;
    }

    /**
     * Na základě informací ze zadaného souboru vytvoří město se zadaným počtem
     * vozidel, které realizuje zjednodušené zadání, takže nemusí mít vstupní a
     * výstupní segment; vytvořené město uloží do schránky ve třídě
     * {@link Holder}.
     *
     * @param reader Vstupní proud s podklady pro tvorbu města
     * @param factories Tovární objekt pro výrobu součástí města
     * @param numberOfVehicles Počet vozidel, která je třeba náhodně rozmístit
     * po městě
     */
    @Override
    public void buildSimplerTown(BufferedReader reader, IFactories factories,
            int numberOfVehicles) {
        Holder.setFactories(factories);
        GUI.getInstance().setCanvasManager(true);
        GUI.getCanvasManager().stopPainting();
        // Přečte zadaný soubor a "postaví" z něj město.
        try {
            handleCharacters(reader);
        } catch (IOException ex) {
            Logger.getLogger(TownBuilder.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        // Nastavíme velikost města podle souboru s plánkem města.
        Town.getInstance().setSize(lineLength, lineNumber);
        // Zinicializuji si dvourozměrné pole s prvky města.
        townElementsArray = new ITownElement[lineLength][lineNumber];
        // Upravím budovy tak, aby věděly o silnicích vedle sebe.
        editBuildings();
        // Předám městu prvky, ze kterých se město skládá (již včetně upravených
        // budov.
        Town.getInstance().setTownElements(townElements);
        Town.getInstance().setTrafficLights(trafficLights);
        for (int i = 0; i < numberOfVehicles; i++) {
            IStreet street = getFreeStreet();
            IVehicle vehicle = Holder.getFactories().newVehicle(
                    street.getPosition(), street.getDirection());
            if (street.isEmptyFor(vehicle)) {
                vehicles.add(vehicle);
            }
        }
        Holder.getFactories().createTown(Town.getInstance().getTownElements(),
                vehicles);
        GUI.getInstance().start(Town.getInstance().getSize());

        Clock.getInstance().addListener(Town.getInstance());
        TrafficLightHandler.getInstance();

        GUI.getCanvasManager().returnPainting();
    }

    /**
     * Metoda pro zpracování znaků ze zadaného souboru.
     *
     * @param reader reader, který čte text ze zadaného souboru
     * @throws IOException výjimka vyhozená při chybě při chybě při čtení
     * souboru
     */
    private void handleCharacters(BufferedReader reader) throws IOException {
        lineLength = 0;
        lineNumber = 0;
        // V této while smyčce se bude číst řádek dokud se nedojde na konec
        // souboru nebo dokud se nenarazí na řádek začínající znaky "#=".
        while (((line = reader.readLine()) != null) && !line.startsWith("#=")) {
            // Kontrola, jestli řádek nezačíná znakem "#". Pokud začíná, jde o
            // komentář v souboru s mapou, a tento řádek se přeskočí a jde se
            // na další řádek.
            if (!line.startsWith("#")) {
                // Je-li řádek prázdný, přeskočí se a jde se dál.
                if (line.isEmpty()) {
                    continue;
                }
                charNumber = 0;
                // V této for smyčce se projdou všechny znaky na právě zkoumaném
                // řádku.
                for (char ch : line.toCharArray()) {
                    if (charNumber >= lineLength) {
                        // Nastavíme délku řádku podle nejvyššího počtu znaků na
                        // řádku.
                        lineLength = charNumber;
                    }
                    // Vytvoříme novou pozici o souřadnicích
                    // "číslo_znaku * velikost_modulu",
                    // "číslo_řádku * velikost_modulu".
                    Position position = new Position(charNumber * module,
                            lineNumber * module);
                    // Nyní daný znak porovnám se všemi možnými znaky, které se
                    // mohou vyskytovat v textovém souboru s mapou města.
                    // Najde-li se shoda s některým ze znaků, vytvoří se
                    // příslušný prvek města.
                    switch (ch) {
                        case '◘':
                            EnumSet<Direction8> directions = EnumSet
                                    .noneOf(Direction8.class);
                            IBuilding building = Holder.getFactories()
                                    .newBuilding(position, directions);
                            townElements.add(building);
                            break;
                        case '▸':
                            IStreet oneWayStreetEast = Holder.getFactories()
                                    .newOneWayStreet(position, Direction8.EAST);
                            townElements.add(oneWayStreetEast);
                            break;
                        case '▴':
                            IStreet oneWayStreetNorth = Holder.getFactories()
                                    .newOneWayStreet(position,
                                            Direction8.NORTH);
                            townElements.add(oneWayStreetNorth);
                            break;
                        case '◂':
                            IStreet oneWayStreetWest = Holder.getFactories()
                                    .newOneWayStreet(position, Direction8.WEST);
                            townElements.add(oneWayStreetWest);
                            break;
                        case '▾':
                            IStreet oneWayStreetSouth = Holder.getFactories()
                                    .newOneWayStreet(position,
                                            Direction8.SOUTH);
                            townElements.add(oneWayStreetSouth);
                            break;
                        case '╢':
                            IControlledStreet controlledStreetEast = Holder
                                    .getFactories().newControlledStreet(
                                            position, Direction8.EAST);
                            townElements.add(controlledStreetEast);
                            trafficLights.add(Holder.getFactories()
                                    .newTrafficLight(controlledStreetEast));
                            break;
                        case '╤':
                            IControlledStreet controlledStreetNorth = Holder
                                    .getFactories().newControlledStreet(
                                            position, Direction8.NORTH);
                            townElements.add(controlledStreetNorth);
                            trafficLights.add(Holder.getFactories()
                                    .newTrafficLight(controlledStreetNorth));
                            break;
                        case '╟':
                            IControlledStreet controlledStreetWest = Holder
                                    .getFactories().newControlledStreet(
                                            position, Direction8.WEST);
                            townElements.add(controlledStreetWest);
                            trafficLights.add(Holder.getFactories()
                                    .newTrafficLight(controlledStreetWest));
                            break;
                        case '╧':
                            IControlledStreet controlledStreetSouth = Holder
                                    .getFactories().newControlledStreet(
                                            position, Direction8.SOUTH);
                            townElements.add(controlledStreetSouth);
                            trafficLights.add(Holder.getFactories()
                                    .newTrafficLight(controlledStreetSouth));
                            break;
                        case '└':
                            IStreet multiWayStreetNorthEast = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            true, Direction8.NORTH,
                                            Direction8.EAST);
                            townElements.add(multiWayStreetNorthEast);
                            break;
                        case '┘':
                            IStreet multiWayStreetNorthWest = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            true, Direction8.NORTH,
                                            Direction8.WEST);
                            townElements.add(multiWayStreetNorthWest);
                            break;
                        case '┐':
                            IStreet multiWayStreetSouthWest = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            true, Direction8.SOUTH,
                                            Direction8.WEST);
                            townElements.add(multiWayStreetSouthWest);
                            break;
                        case '┌':
                            IStreet multiWayStreetSouthEast = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            true, Direction8.SOUTH,
                                            Direction8.EAST);
                            townElements.add(multiWayStreetSouthEast);
                            break;
                        case '┴':
                            IStreet multiWayStreetNorthWestEast = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            true, Direction8.NORTH,
                                            Direction8.WEST, Direction8.EAST);
                            townElements.add(multiWayStreetNorthWestEast);
                            break;
                        case '┤':
                            IStreet multiWayStreetNorthSouthWest = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            true, Direction8.NORTH,
                                            Direction8.SOUTH, Direction8.WEST);
                            townElements.add(multiWayStreetNorthSouthWest);
                            break;
                        case '┬':
                            IStreet multiWayStreetSouthWestEast = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            true, Direction8.SOUTH,
                                            Direction8.WEST, Direction8.EAST);
                            townElements.add(multiWayStreetSouthWestEast);
                            break;
                        case '├':
                            IStreet multiWayStreetNorthSouthEast = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            true, Direction8.NORTH,
                                            Direction8.SOUTH, Direction8.EAST);
                            townElements.add(multiWayStreetNorthSouthEast);
                            break;
                        case '─':
                            IStreet multiWayStreetWestEast = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            true, Direction8.WEST,
                                            Direction8.EAST);
                            townElements.add(multiWayStreetWestEast);
                            break;
                        case '│':
                            IStreet multiWayStreetNorthSouth = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            true, Direction8.NORTH,
                                            Direction8.SOUTH);
                            townElements.add(multiWayStreetNorthSouth);
                            break;
                        case '╘':
                            IStreet multiWayStreetNoLeftNorthEast = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            false, Direction8.NORTH,
                                            Direction8.EAST);
                            townElements.add(multiWayStreetNoLeftNorthEast);
                            break;
                        case '╜':
                            IStreet multiWayStreetNoLeftNorthWest = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            false, Direction8.NORTH,
                                            Direction8.WEST);
                            townElements.add(multiWayStreetNoLeftNorthWest);
                            break;
                        case '╕':
                            IStreet multiWayStreetNoLeftSouthWest = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            false, Direction8.SOUTH,
                                            Direction8.WEST);
                            townElements.add(multiWayStreetNoLeftSouthWest);
                            break;
                        case '╓':
                            IStreet multiWayStreetNoLeftSouthEast = Holder
                                    .getFactories().newMultiWayStreet(position,
                                            false, Direction8.SOUTH,
                                            Direction8.EAST);
                            townElements.add(multiWayStreetNoLeftSouthEast);
                            break;
                        case '▲':
                            IStreet enterStreetNorth = Holder.getFactories()
                                    .newOneWayStreet(position,
                                            Direction8.NORTH);
                            townElements.add(enterStreetNorth);
                            enterStreetNumber = townElements.size() - 1;
                            break;
                        case '▼':
                            IStreet enterStreetSouth = Holder.getFactories()
                                    .newOneWayStreet(position,
                                            Direction8.SOUTH);
                            townElements.add(enterStreetSouth);
                            enterStreetNumber = townElements.size() - 1;
                            break;
                        case '◄':
                            IStreet enterStreetWest = Holder.getFactories()
                                    .newOneWayStreet(position, Direction8.WEST);
                            townElements.add(enterStreetWest);
                            enterStreetNumber = townElements.size() - 1;
                            break;
                        case '►':
                            IStreet enterStreetEast = Holder.getFactories()
                                    .newOneWayStreet(position, Direction8.EAST);
                            townElements.add(enterStreetEast);
                            enterStreetNumber = townElements.size() - 1;
                            break;
                        case '↑':
                            IStreet exitStreetNorth = Holder.getFactories()
                                    .newExitStreet(position, Direction8.NORTH,
                                            Direction8.EAST);
                            townElements.add(exitStreetNorth);
                            exitStreets.add((IExitStreet) exitStreetNorth);
                            break;
                        case '↓':
                            IStreet exitStreetSouth = Holder.getFactories()
                                    .newExitStreet(position, Direction8.SOUTH,
                                            Direction8.WEST);
                            townElements.add(exitStreetSouth);
                            exitStreets.add((IExitStreet) exitStreetSouth);
                            break;
                        case '←':
                            IStreet exitStreetWest = Holder.getFactories()
                                    .newExitStreet(position, Direction8.WEST,
                                            Direction8.NORTH);
                            townElements.add(exitStreetWest);
                            exitStreets.add((IExitStreet) exitStreetWest);
                            break;
                        case '→':
                            IStreet exitStreetEast = Holder.getFactories()
                                    .newExitStreet(position, Direction8.EAST,
                                            Direction8.SOUTH);
                            townElements.add(exitStreetEast);
                            exitStreets.add((IExitStreet) exitStreetEast);
                            break;
                        default:
                            throw new RuntimeException("Soubor s mapou obsahuje"
                                    + " nepodporované symboly.");
                    }
                    charNumber++;
                }
                lineNumber++;
            }
        }
        lineLength++;
    }

    /**
     * Požádá uživatele o označení vstupního souboru a na základě informací z
     * tohoto souboru vytvoří město odpovídajícího typu; vytvořené město uloží
     * do schránky ve třídě {@link Holder}.
     *
     * @param factories Tovární objekt pro výrobu součástí města
     * @param numberOfVehicles Počet vozidel, která je třeba náhodně rozmístit
     * po městě. Je-li zadán nulový počet vozidel, vytváří se město s implicitně
     * ovládaným vstupním a výstupním segmentem, tj. město, do nějž mohou
     * vozidla přijíždět a zase z něj odjíždět.
     */
    @Override
    public void buildAppropriateTown(IFactories factories,
            int numberOfVehicles) {
        InputStream in = DataPkg.class.getResourceAsStream("Default_town.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                in, "UTF-8"))) {
            if (numberOfVehicles == 0) {
                this.buildTown(reader, factories);
            } else {
                buildSimplerTown(reader, factories, numberOfVehicles);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Zadaný soubor se nepodařilo otevřít.");
        }
    }

    /**
     * Metoda pro převedení seznamu s elementy města na dvourozměrné pole s těmi
     * stejnými elementy.
     *
     * @param townElement aktuálně vkládaný prvek města do pole
     * @param row řádek, kam se prvek vloží
     * @param column sloupec, kam se prvek vloží
     */
    private void townElementToArray(ITownElement townElement, int column,
            int row) {
        townElementsArray[column][row] = townElement;
    }

    /**
     * Metoda, která proiteruje seznam townElements a změní budovy, vedle
     * kterých je v nějakém směru silnice na takové budovy, které už o své
     * sousední silnici ví.
     */
    private void editBuildings() {
        // Dočasně si v této třídě vytvořím dvourozměrné pole kam dám prvky ze
        // seznamu townElements.
        townElements.stream().forEach(element -> {
            townElementToArray(element, element.getX(), element.getY());
        });
        EnumSet<Direction8> directions = EnumSet.noneOf(Direction8.class);
        IBuilding building;
        int i = 0;
        for (int y = 0; y < lineNumber; y++) {
            for (int x = 0; x < lineLength; x++) {
                directions.clear();
                if (townElementsArray[x][y] instanceof IBuilding) {
                    // Je-li na řádku "x" a sloupci "y" budova, provedou
                    // se následující čtyři try-catch bloky kódu, které
                    // zkontrolují, jestli je někde vedle budovy silnice. Pokud
                    // ano, přidá se směr, ve kterém je od budovy silnice, do
                    // seznamu "directions", a s pomocí tohoto seznamu se pak
                    // vytvoří nová budova, která už bude vědět o silnicích
                    // vedle sebe (a bude tedy moct použít příslušný obrázek
                    // s chodníkem).
                    try {
                        if (townElementsArray[x - 1][y] instanceof IStreet) {
                            directions.add(Direction8.WEST);
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                    }
                    try {
                        if (townElementsArray[x + 1][y] instanceof IStreet) {
                            directions.add(Direction8.EAST);
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                    }
                    try {
                        if (townElementsArray[x][y - 1] instanceof IStreet) {
                            directions.add(Direction8.NORTH);
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                    }
                    try {
                        if (townElementsArray[x][y + 1] instanceof IStreet) {
                            directions.add(Direction8.SOUTH);
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                    }
                    building = Holder.getFactories().newBuilding(
                            new Position(x * module, y * module),
                            directions);
                    townElements.set(i, building);
                }
                i++;
            }
        }
    }

    /**
     * Metoda, která projde seznam s prvky města, a vrátí náhodný volný segment
     * silnice. Volným segmentem silnice se rozumí takový segment, na kterém
     * není žádné vozidlo.
     *
     * @return volný segment silnice
     */
    private IStreet getFreeStreet() {
        IStreet street = null;
        ITownElement townElement;
        while (true) {
            townElement = townElements.get((new Random())
                    .nextInt(townElements.size()));
            if (townElement instanceof IStreet) {
                street = (IStreet) townElement;
                break;
            }
        }
        return street;
    }

    /**
     * Metoda, která vrátí číselnou hodnotu klávesy, která má být stisknuta, aby
     * se na vjezdu do města objevilo nové vozidlo.
     *
     * @return číselný kód klávesy, která má být stisknuta, aby se na vjezdu do
     * města objevilo nové vozidlo
     */
    public int getInputKey() {
        return this.inputKey;
    }

    /**
     * Metoda, která vrátí číselnou hodnotu klávesy, která má být stisknuta, aby
     * se otevřel výjezd z města pro jedno vozidlo.
     *
     * @return číselný kód klávesy, která má být stisknuta, aby se otevřel
     * výjezd z města pro jedno vozidlo
     */
    public int getExitKey() {
        return this.exitKey;
    }

    /**
     * Metoda, která do města přidá vozidlo. Vozidlo bude přidáno na příjezdovou
     * cestu do města. Pokud příjezdová cesta do města neexistuje, bude vozidlo
     * přidáno na náhodný volný segment silnice.
     */
    public void addVehicle() {
        IStreet street;
        if (enterStreetNumber == -1) {
            // Neexistuje příjezdová cesta do města. Vozidlo do města bude
            // přidáno na náhodný volný segment silnice.
            street = getFreeStreet();

        } else {
            // Existuje příjezdová cesta do města.
            street = (IStreet) townElements.get(enterStreetNumber);
        }
        specialCar++;
        IVehicle vehicle;
        if (specialCar == 1) {
            vehicle = ((Factory)Holder.getFactories()).newSpecialVehicle(street.getPosition(), street.getDirection());
            ((Vehicle)vehicle).setSpecialCar(true);
        } else {
            vehicle = Holder.getFactories().newVehicle(
                street.getPosition(), street.getDirection());
        }
        if (street.isEmptyFor(vehicle)) {
            vehicles.add(vehicle);
        }
    }

    /**
     * Metoda pro odebrání vozidla, které vjelo na "odjezdový" segment z města,
     * a odbočilo na něm "ven" z města.
     *
     * @param vehicle odebírané vozidlo
     */
    public void removeVehicle(IVehicle vehicle) {
        GUI.getCanvasManager().remove(vehicle);
        ((Vehicle) vehicle).setRemoveListener(true);
        vehicles.remove(vehicle);
    }

    /**
     * Getter na seznam segmentů silnic, ze kterých se dá opustit město.
     *
     * @return seznam segmentů silnic, ze kterých lze opustit město
     */
    public List<IExitStreet> getExitStreets() {
        return this.exitStreets;
    }
}
