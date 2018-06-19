/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;

import eu.pedu.lib17s.util.Direction8;
import java.util.EnumSet;



/*******************************************************************************
 * Instance interfejsu {@code IBuilding} představují budovy ve městě.
 * Budovy jsou orientované, což znamená, že vědí, kudy kolem nich vede silnice.
 * Tuto informaci je možno využít např. pro výběr obrázku pro nakreslení budovy.
 * Na kraji budovy sousedícím se silnicí bude zobrazen chodník,
 * aby se dala vizuálně snadno poznat orientace budovy.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public  interface   IBuilding
        extends     ITownElement
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================

    /***************************************************************************
     * Vrátí množinu směrů, v nichž se nachází sousední segmenty silnice.
     * U budovy zcela uvnitř zástavby bude tato množina prázdná,
     * u osamocené budovy obklopené silnicemi budou součástí množiny
     * všechny čtyři hlavní světové strany,
     * ostatní typy budov budou mít nastaveno něco mezi tím.
     * Tato informace naznačuje, kde lze vedle budovy nalézt chodník,
     * po němž by případně v budoucnu mohli chodit chodci.
     *
     * @return Množina směrů, v nichž se nachází sousední segmenty silnice
     */
//    @Override
    public EnumSet<Direction8> getDirectionsToStreet()
    ;



//== OTHER ABSTRACT METHODS ====================================================
//== DEFAULT GETTERS AND SETTERS ===============================================
//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
