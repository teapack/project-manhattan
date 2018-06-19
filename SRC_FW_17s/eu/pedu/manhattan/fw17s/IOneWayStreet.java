/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;

import eu.pedu.lib17s.util.Direction8;



/*******************************************************************************
 * Instance interfejsu {@code IStreet} představují jednosměrné
 * segmenty silnice ve městě.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public  interface   IOneWayStreet
        extends     IStreet
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================

    /***************************************************************************
     * Vrátí směr, ve kterém se po daném segmentu silnice jezdí.
     *
     * @return Požadovaný směr
     */
    @Override
    public Direction8 getDirection()
    ;



//== OTHER ABSTRACT METHODS ====================================================
//== DEFAULT GETTERS AND SETTERS ===============================================
//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
