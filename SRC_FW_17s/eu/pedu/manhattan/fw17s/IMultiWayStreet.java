/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;

import eu.pedu.lib17s.util.Direction8;
import java.util.List;



/*******************************************************************************
 * Instance interfejsu {@code IMultiWayStreet} představují segmenty silnice,
 * z nichž lze pokračovat více směry.
 * To, zda musí vozidlo projet daný segment rovně,
 * anebo zde bude smět i odbočit, záleží na směru, v němž do segmentu přijíždí
 * (tj. záleží na směru přijíždějícího vozidla).
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public  interface   IMultiWayStreet
        extends     IStreet
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================

    /***************************************************************************
     * Vrátí seznam směrů, v nichž může být daný segment opuštěn vozidlem
     * přijíždějícím ze zadaného směru.
     *
     * @param direction Směr, v němž jede přijíždějící vozidlo
     * @return Seznam směrů, v nichž může být daný segment opuštěn
     *         po příjezdu ze zadaného směru
     */
//    @Override
    public List<Direction8> getExitsFor(Direction8 direction)
    ;



//== OTHER ABSTRACT METHODS ====================================================
//== DEFAULT GETTERS AND SETTERS ===============================================

    /***************************************************************************
     * Vrátí seznam směrů, v nichž může být daný segment opuštěn
     * zadaným vozidlem, přičemž se předpokládá,
     * že vozidlo se právě chystá vjet do segmentu.
     *
     * @param vehicle Vozidlo, které se ptá na možné pokračování
     * @return Seznam směrů, v nichž může být daný segment opuštěn
     *         zadaným vozidlem
     */
    default
    public List<Direction8> getExitsFor(IVehicle vehicle)
    {
        return getExitsFor(vehicle.getDirection());
    }



//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
