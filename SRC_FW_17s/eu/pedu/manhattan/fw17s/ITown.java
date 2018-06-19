/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;

import eu.pedu.lib17s.geom.Position;
import eu.pedu.lib17s.geom.Size;

import java.util.Collection;
import eu.pedu.lib17s.canvasmanager.ICMPaintable;



/*******************************************************************************
 * Instance interfejsu {@code ITown} představuje město či dopravní hřiště,
 * v němž lze spustit simulaci provozu řízeného semafory.
 * V rozšířené verzi musí mít město definován alespoň jeden vstupní bod,
 * odkud do města přijíždějí nová vozidla,
 * a alespoň jeden výstupní bod,
 * kudy mohou vozidla zobrazovanou část města opustit.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public  interface   ITown
        extends     ICMPaintable
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================

    /***************************************************************************
     * Vrátí aktuální rozměr města (počet sloupců a řádků).
     *
     * @return Aktuální rozměr města = počet sloupců ({@code width})
     *         a řádků ({@code hight})
     */
//    @Override
    public Size getSize()
    ;


    /***************************************************************************
     * Vrátí prvek města na zadaných políčkových souřadnicích.
     *
     * @param column Sloupec požadovaného prvku
     * @param row    Řádek požadovaného prvku
     * @return Prvek města na zadaných políčkových souřadnicích
     */
//    @Override
    public ITownElement getElementAt(int column, int row)
    ;


    /***************************************************************************
     * Vrátí (nemodifikovatelnou) kolekci semaforů
     * řídících provoz ve městě.
     *
     * @return Kolekce (nemodifikovatelná) řidičů ovládajících vozidla ve městě
     */
//    @Override
    public Collection<ITrafficLight> getTrafficLights()
    ;


    /***************************************************************************
     * Vrátí (nemodifikovatelnou) kolekci řidičů
     * ovládajících vozidla ve městě.
     *
     * @return Kolekce (nemodifikovatelná) řidičů ovládajících vozidla ve městě
     */
//    @Override
    public Collection<IDriver> getDrivers()
    ;



//== OTHER ABSTRACT METHODS ====================================================

    /***************************************************************************
     * Spustí provoz ve městě, tj. začne ovládat semafory a prostřednictvím
     * opakovaného volání metody {@link IDriver#nextStep()} aktivovat řidiče
     * k provedení další akce.
     */
//    @Override
    public void startTrafic()
    ;


    /***************************************************************************
     * Zastaví provoz ve městě, tj. zhasne všechny semafory a přestane
     * vybízet řidiče k provádění dalších kroků.
     */
//    @Override
    public void stopTrafic()
    ;



//== DEFAULT GETTERS AND SETTERS ===============================================

    /***************************************************************************
     * Vrátí prvek města na zadané políčkové pozici.
     *
     * @param position Políčková pozice požadovaného prvku
     * @return Prvek města na zadané políčkové pozici
     */
    default
    public ITownElement getElementAt(Position position)
    {
        return getElementAt(position.x, position.y);
    }



//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
