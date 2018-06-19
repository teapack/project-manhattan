/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;

import eu.pedu.lib17s.util.Direction8;



/*******************************************************************************
 * Instance interfejsu {@code IStreet} představují segmenty silnice ve městě.
 * Segment může být buď jednosměrně průjezdný,
 * anebo může umožňovat opuštění ve více směrech.
 * Jednosměrné segmenty jsou definovány
 * jako instance interfejsu {@link IOneWayStreet},
 * vícesměrné jako instance interfejsu {@link IMultiWayStreet}.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public  interface   IStreet
        extends     ITownElement
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================

    /***************************************************************************
     * Vrátí směr vozidla nově umístěného na daný segment silnice.
     * Ten je u jednosměrných ulic i směrem,
     * ve kterém se po daném segmentu silnice jezdí,
     * u rozcestí je to směr, kterým mohou odjet všechna přijíždějící vozidla.
     *
     * @return Požadovaný směr
     */
//    @Override
    public Direction8 getDirection()
    ;


    /***************************************************************************
     * Vrátí informaci o tom, je-li daný segment silnice prázdný
     * a může-li proto na něj zadané vozidlo vjet;
     * při povolení vjezdu se daný segment zablokuje,
     * takže nepovolí vjezd jiným vozidlům, dokud jej zadané vozidlo neopustí.
     *
     * @param vehicle Vozidlo, které chce na daný segment silnice vjet
     * @return Je-li blokovaný, vrátí {@code false}. Jinak vrátí {@code true}
     *         a současně se zablokuje pro vozidlo zadané v parametru
     */
    public boolean isEmptyFor(IVehicle vehicle)
    ;


    /***************************************************************************
     * Nastaví zadaný segment silnice opět jako prázdný, takže na něj může
     * kdokoliv vjet (samozřejmě až po předchozím zablokování).
     *
     * @param vehicle Vozidlo, které chce daný segment silnice uvolnit;
     *        Parametr slouží k tomu, aby si segment mohl zkontrolovat,
     *        že o uvolnění žádá ten, kdo si ji před tím zablokoval.
     */
//    @Override
    public void setEmpty(IVehicle vehicle)
    ;



//== OTHER ABSTRACT METHODS ====================================================
//== DEFAULT GETTERS AND SETTERS ===============================================
//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
