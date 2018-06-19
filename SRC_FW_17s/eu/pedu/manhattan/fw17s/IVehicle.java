/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;



/*******************************************************************************
 * Instance interfejsu {@code IVehicle} představují vozidla schopná pohybu
 * po ulicích města, v němž je provoz řízen semafory.
 * Vozidla nemají žádnou speciální vlastní inteligenci,
 * to, kdy a kam vozidlo pojede a kdy bude na něco čekat
 * specifikuje řidič, který má řízení daného vozidla na starosti.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public  interface   IVehicle
        extends     IMovingObject
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================

    /***************************************************************************
     * Vrátí informaci o tom, jestli vozidlo právě bliká.
     *
     * @return Bliká-li, vrátí {@code true}, jinak vrátí {@code false}
     */
//    @Override
    public boolean isBlinking();



//== OTHER ABSTRACT METHODS ====================================================

    /***************************************************************************
     * Posune vozidlo o zadaný počet bodů vpřed.
     *
     * @param distance Počet bodů, o něž se má vozidlo posunout
     */
//    @Override
    public void forward(int distance)
    ;


    /***************************************************************************
     * Zapne levý blinkr.
     */
//    @Override
    public void blinkLeft()
    ;


    /***************************************************************************
     * Zapne pravý blinkr.
     */
//    @Override
    public void blinkRight()
    ;


    /***************************************************************************
     * Vypne blikání.
     */
//    @Override
    public void stopBlinking()
    ;



//== DEFAULT GETTERS AND SETTERS ===============================================
//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
