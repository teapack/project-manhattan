/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;

import eu.pedu.lib17s.util.Direction8;
import eu.pedu.lib17s.geom.Position;

import java.util.EnumSet;
import java.util.List;



/*******************************************************************************
 * Instance interfejsu {@code IFactories} představují tovární objekty
 * poskytující sadu továrních metod pro výrobu jednotlivých součástí města.
 * Toto řešení umožňuje definovat tovární objekt tak,
 * že se již definované metody (povětšinou konstruktory) použijí přímo,
 * a místo metod, na jejichž výslednou definici se teprve čeká,
 * se dočasně použijí nějaké záslepky.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public interface IFactories
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================
//== OTHER ABSTRACT METHODS ====================================================

    /***************************************************************************
     * Vytvoří na zadané políčkové pozici novou budovu,
     * která bude v zadaných směrech sousedit se silnicí.
     * Budova bude zaujímat právě ono zadané pole.
     *
     * @param position   Políčková pozice budovy
     * @param directions Směry, v nichž sousedí se segmenty silnice
     * @return Vytvořená budova
     */
//    @Override
    public IBuilding newBuilding(Position position,
                                 EnumSet<Direction8> directions)
    ;


    /***************************************************************************
     * Vytvoří na zadané políčkové pozici nový segment jednosměrné silnice,
     * po němž se bude jezdit v zadaném směru.
     * Vytvořený segment silnice bude zaujímat právě ono zadané pole.
     *
     * @param position  Políčková pozice vytvářeného segmentu silnice
     * @param direction Směr, v němž se bude po silnici jezdit
     * @return Vytvořený segment silnice
     */
//    @Override
    public IStreet newOneWayStreet(Position position, Direction8 direction)
    ;


    /***************************************************************************
     * Vytvoří na zadané políčkové pozici nový segment silnice,
     * který je součástí křižovatky.
     * Vytvořený segment silnice bude zaujímat právě ono zadané pole.
     *
     * @param position        Políčková pozice vytvářeného segmentu silnice
     * @param turnLeftAllowed Je-li zde povoleno odbočení vlevo
     * @param directions      Směry, v nichž lze segment opustit
     * @return Vytvořený segment silnice
     */
//    @Override
    public IMultiWayStreet newMultiWayStreet(Position position,
                           boolean turnLeftAllowed, Direction8... directions)
    ;


    /***************************************************************************
     * Vytvoří na zadané políčkové pozici nový segment jednosměrné silnice,
     * na němž bude odjezd řízen semaforem.
     * Vytvořený segment silnice bude zaujímat právě ono zadané pole.
     *
     * @param position  Políčková pozice vytvářeného segmentu silnice
     * @param direction Směr, v němž se bude po silnici jezdit
     * @return Vytvořený segment řízené silnice
     */
//    @Override
    public IControlledStreet newControlledStreet(Position position,
                                                 Direction8 direction)
    ;


    /***************************************************************************
     * Vytvoří nový semafor řídící odjezd ze segmentu zadaného v parametru.
     * Semafor bude umístěn v levé horní částí pole vpravo vedle řízeného
     * segmentu silnice (počítáno vůči směru, v němž řídí dopravu) tak,
     * aby v případě potřeby bylo možno umístit do jednoho pole
     * čtyři semafory řídící odjezd ze čtyř sousedních polí daného pole,
     * aniž by se tyto semafory nějak překrývaly.
     *
     * @param checkedStreet Segment silnice řízený vytvořeným semaforem
     * @return Vytvořený semafor
     */
//    @Override
    public ITrafficLight newTrafficLight(IControlledStreet checkedStreet)
    ;


    /***************************************************************************
     * Vytvoří na zadané políčkové pozici nové vozidlo,
     * natočené do zadaného směru.
     * Vozidlo bude zaujímat právě ono zadané pole.
     *
     * @param position  Výchozí políčková pozice vytvářeného vozidla
     * @param direction Směr, do nějž bude vozidlo natočeno
     * @return Vytvořené vozidlo
     */
//    @Override
    public IVehicle newVehicle(Position position, Direction8 direction)
    ;


    /***************************************************************************
     * Vrátí řidiče, který dokáže dovést přidělené vozidlo k naplánovanému cíli.
     *
     * @param vehicle Vozidlo ovládané daným řidičem
     * @return Vytvořený řidič
     */
//    @Override
    public IDriver newDriver(IVehicle vehicle)
    ;


    /***************************************************************************
     * Vrátí plánovače, kterého lze požádat, aby zadanému vozidlu
     * naplánoval příští cíl cesty.
     *
     * @return Vytvořený plánovač
     */
//    @Override
    public IPlanner newPlanner()
    ;


    /***************************************************************************
     * Vytvoří na zadané políčkové pozici nový segment silnice,
     * který je součástí křižovatky, z níž je někdy možno opustit město.
     * Možnost opustit město lze zapnout a vypnout.
     * Vytvořený segment silnice bude zaujímat právě ono zadané pole.
     *
     * @param position   Políčková pozice vytvářeného segmentu silnice
     * @param direction1 Směr, v němž je silnice průjezdná jen rovně
     * @param direction2 Směr, z nějž je možno na daném políčku odbočit vpravo
     *                   (bude-li otevřen) a opustit tak město
     * @return Vytvořený segment křižovatky;
     *         V jednodušší verzi může metoda vracet prázdný odkaz
     */
//    @Override
    public IExitStreet newExitStreet(Position position,
                       Direction8 direction1, Direction8 direction2)
    ;


    /***************************************************************************
     * Vytvoří město se zadanými statickými prvky a v nich umístěnými vozidly
     * a uloží je do objektu třídy {@link Holder}.
     *
     * @param townElements  Pole všech statických prvků města
     * @param vehicles      Seznam spravovaných vozidel
     */
//    @Override
    public void createTown(ITownElement[][] townElements,
                           List<IVehicle> vehicles)
    ;



//== DEFAULT GETTERS AND SETTERS ===============================================
//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
