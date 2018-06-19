/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;



/*******************************************************************************
 * Instance interfejsu {@code IDriver} představují řidiče,
 * který dovede zadané vozidlo k naplánovanému cíli.
 * <p>
 * Způsob zadávání cílů závisí na konkrétním implementujícím datovém typu.
 * Implicitní řešení předpokládá, že nemá-li řidič žádný vytčený cíl
 * (případně pokud již vytčeného cíle dosáhl), požádá o jeho zadání
 * nějakého plánovače cílů (instanci interfejsu {@link IPlanner}).
 * Pro řidiče některých typů ale mohou být cíle zadávány z klávesnice,
 * anebo může být zvolen nějaký jiný způsob.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public interface IDriver
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================

    /***************************************************************************
     * Vrátí ovládané vozidlo.
     *
     * @return Ovládané vozidlo
     */
//    @Override
    public IVehicle getVehicle()
    ;



//== OTHER ABSTRACT METHODS ====================================================

    /***************************************************************************
     * Korektně popojede se svěřeným autem o další krok k vytčenému cíli,
     * přičemž cestou respektuje semafory a překážející vozidla a v případě,
     * má-li na konci odbočit, včas zapne blinkry.
     * <p>
     * Nemá-li žádný vytčený cíl (případně pokud již vytčeného cíle dosáhl,
     * požádá nějakého plánovače cílů (instanci interfejsu {@link IPlanner}),
     * aby mu cíl našel, a k tomuto cíli pak vozidlo vede.
     * <p>
     * Tato metoda slouží k tomu, aby město řídící svůj provoz
     * mohlo synchronizovat dění tak, že zadaných časových intervalech (krocích)
     * žádá řidiče, aby provedli další krok.
     * Frekvence volání této metody závisí na řídícím městě.
     */
//    @Override
    public void nextStep()
    ;



//== DEFAULT GETTERS AND SETTERS ===============================================
//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
