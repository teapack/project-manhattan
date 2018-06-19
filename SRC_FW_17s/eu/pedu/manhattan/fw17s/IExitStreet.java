/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;



/*******************************************************************************
 * Instance interfejsu {@code IExitStreet} představují segment silnice
 * umožňující opuštění města. Opuštění je možno odbočením,
 * které je možno na požádání otevřít anebo zavřít.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public  interface   IExitStreet
        extends     IMultiWayStreet
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================
//== OTHER ABSTRACT METHODS ====================================================

    /***************************************************************************
     * Otevře odbočku umožňující opustit město.
     */
//    @Override
    public void openExit()
    ;


    /***************************************************************************
     * Zavře odbočku umožňující opustit město.
     * Pokud si však před zavřením nějaké auto objednalo odbočení,
     * dojde k vlastnímu zavření až poté,
     * co dané auto projede příslušným segmentem.
     */
//    @Override
    public void closeExit()
    ;



//== DEFAULT GETTERS AND SETTERS ===============================================
//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
