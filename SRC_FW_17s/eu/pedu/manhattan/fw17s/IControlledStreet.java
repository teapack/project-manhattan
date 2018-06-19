/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;



/*******************************************************************************
 * Instance interfejsu {@code IControlledStreet} představují segmenty silnice
 * ovládané semaforem. Daný segment silnice může jedoucí auto opustit pouze
 * v případě, pustí-li jej sdružený semafor.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public  interface   IControlledStreet
        extends     IOneWayStreet
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================

    /***************************************************************************
     * Vrátí informaci o tom, umožňuje-li sdružený řídící semafor vozidlu
     * opustit daný segment silnice.
     *
     * @return Lze-li odjet, vrátí {@code true}, jinak vrátí {@code false}
     */
//    @Override
    public boolean isExitOpen()
    ;


    /***************************************************************************
     * Vrátí sdružený semafor ovlivňující možnost výjezdu z daného segmentu.
     *
     * @return Semafor ovlivňující možnost výjezdu z daného pole
     */
//    @Override
    public ITrafficLight getTrafficLight()
    ;



//== OTHER ABSTRACT METHODS ====================================================
//== DEFAULT GETTERS AND SETTERS ===============================================
//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
