/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;

//import eu.pedu.manhattan.fw16w.putaway.ITownMap;



/*******************************************************************************
 * Objekt třídy {@code Holder} funguje jako schránka na hlavní dva objekty,
 * a to za prvé na tovární objekt nabízející tovární metody pro vytvoření
 * města a jeho jednotlivých objektů v průběhu jeho budování,
 * a za druhé na vytvořené město, které je dotazováno během provozu.
 * <p>
 * Slouží především k tomu, aby bylo možno vytvářet části projektu
 * nezávisle na stavu realizace některých jiných částí. Tovární objekty
 * ani město nelze definovat (a následně používat) jako jedináčky,
 * protože obecně nemusíme znát jejich skutečné mateřské třídy.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public class Holder
{
//== CONSTANT CLASS ATTRIBUTES =================================================
//== VARIABLE CLASS ATTRIBUTES =================================================

    /** Jediná udržovaná instance města. */
    private static volatile IFactories factories;
//
//    /** Charakteristika průjezdnosti města. */
//    private static volatile ITownMap townMap;

    /** Jediná udržovaná instance města. */
    private static volatile ITown town;



//== STATIC INITIALIZER (CLASS CONSTRUCTOR) ====================================
//== CLASS GETTERS AND SETTERS =================================================

    /***************************************************************************
     * Vrátí nastavenou instanci továrního objektu.
     * Pokud ještě nikdo žádnou nenastavil, vrátí {@code null}.
     *
     * @return Instance továrního objektu nebo {@code null}
     */
    public static IFactories getFactories()
    {
        return factories;
    }


    /***************************************************************************
     * Nastaví zadanou instanci továrního objektu.
     * Instanci je možno nastavit pouze jednou.
     * Při druhém pokusu o nastavení instance vyhodí výjimku.
     *
     * @param factories Nastavovaná instance továrního objektu
     * @throws IllegalStateException Instance továrního objektu je již nastavena
     */
    public static void setFactories(IFactories factories)
    {
        if (Holder.factories == null) {
            synchronized(Holder.class) {
                if (Holder.factories == null) {
                    Holder.factories = factories;
                    return;
                }
            }
        }
        throw new IllegalStateException(
                "\nDruhý pokus o zadání odkazu na obekt s továrnami – " +
                "odkaz je možno zadat jen jednou");
    }

//
//    /***************************************************************************
//     * Vrátí nastavenou instanci města.
//     * Pokud ještě nikdo žádnou nenastavil, vrátí {@code null}.
//     *
//     * @return Instance města nebo {@code null}
//     */
//    public static ITownMap getTownMap()
//    {
//        return townMap;
//    }
//
//
//    /***************************************************************************
//     * Nastaví zadanou instanci mapy města.
//     * Instanci je možno nastavit pouze jednou.
//     * Při druhém pokusu o nastavení instance vyhodí výjimku.
//     *
//     * @param townMap Nastavovaná instance
//     * @throws IllegalStateException Instance mapy města je již nastavena
//     */
//    public static void setTownMap(ITownMap townMap)
//    {
//        if (Holder.townMap == null) {
//            synchronized (Holder.class) {
//                if (Holder.townMap == null) {
//                    Holder.townMap = townMap;
//                    return;
//                }
//            }
//        }
//        throw new IllegalStateException(
//                "\nDruhý pokus o zadání odkazu na mapu města – " +
//                        "odkaz je možno zadat jen jednou");
//    }
//

    /***************************************************************************
     * Vrátí nastavenou instanci města.
     * Pokud ještě nikdo žádnou nenastavil, vrátí {@code null}.
     *
     * @return Instance města nebo {@code null}
     */
    public static ITown getTown()
    {
        return town;
    }


    /***************************************************************************
     * Nastaví zadanou instanci města.
     * Instanci je možno nastavit pouze jednou.
     * Při druhém pokusu o nastavení instance vyhodí výjimku.
     *
     * @param town Nastavovaná instance
     * @throws IllegalStateException Instance města je již nastavena
     */
    public static void setTown(ITown town)
    {
        if (Holder.town == null) {
            synchronized(Holder.class) {
                if (Holder.town == null) {
                    Holder.town = town;
                    return;
                }
            }
        }
        throw new IllegalStateException(
                "\nDruhý pokus o zadání odkazu na město – " +
                "odkaz je možno zadat jen jednou");
    }



//== OTHER NON-PRIVATE CLASS METHODS ===========================================
//== PRIVATE AND AUXILIARY CLASS METHODS =======================================



//##############################################################################
//== CONSTANT INSTANCE ATTRIBUTES ==============================================
//== VARIABLE INSTANCE ATTRIBUTES ==============================================



//== CONSTRUCTORS AND FACTORY METHODS ==========================================

    /***************************************************************************
     * Soukromý konstruktor zabraňující vytvoření instance.
     */
    private Holder() {}



//== ABSTRACT METHODS ==========================================================
//== INSTANCE GETTERS AND SETTERS ==============================================
//== OTHER NON-PRIVATE INSTANCE METHODS ========================================
//== PRIVATE AND AUXILIARY INSTANCE METHODS ====================================



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
