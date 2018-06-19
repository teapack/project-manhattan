/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;



/*******************************************************************************
 * Instance interfejsu {@code IPlanner} představují plánovače,
 * které lze požádat, aby zadanému vozidlu naplánovaly příští cíl cesty.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public interface IPlanner
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================
//== OTHER ABSTRACT METHODS ====================================================

    /***************************************************************************
     * Naplánuje pro zadané vozidlo další cíl.
     * Další, cílem je nejbližší segment, na němž lze odbočit.
     * Plánovač si zjistí, kde je, naplánuje, zda se na něm odbočí,
     * a pokud ano, tak na kterou stranu se má otočit a současně
     * kde se mají zapnout příslušné blinkry.
     * Metoda vrátí přepravku s těmito informacemi.
     *
     * @param vehicle Vozidlo, pro něž plánujeme další cíl
     * @return Přepravka s informacemi potřebnými pro korektní přesun
     *         k naplánovanému cíli
     */
//    @Override
    public Destination suggestNextDestination(IVehicle vehicle)
    ;



//== DEFAULT GETTERS AND SETTERS ===============================================
//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
