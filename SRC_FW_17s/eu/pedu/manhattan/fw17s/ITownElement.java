/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;

import eu.pedu.lib17s.canvasmanager.ICMPaintable;
import eu.pedu.lib17s.geom.IModular;
import eu.pedu.lib17s.geom.Position;



/*******************************************************************************
 * Instance interfejsu {@code ITownElement} představují součásti města.
 * Ty umějí prozradit a nastavit svoji pozici a modul.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public  interface   ITownElement
        extends     IModular, ICMPaintable
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================
//== OTHER ABSTRACT METHODS ====================================================
//== DEFAULT GETTERS AND SETTERS ===============================================

    /***************************************************************************
     * Vrátí instanci třídy {@code Position} s políčkovou pozicí dané instance.
     * Tato pozice je se v průběhu života instance nemění.
     *
     * @return  Políčková pozice instance
     */
    default
    public Position getFieldPosition()
    {
        return CM.positionPoint2Field(getPosition());
    }



//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
