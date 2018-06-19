/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;



/*******************************************************************************
 * Instance třídy {@code Destination} představují přepravky s informacemi
 * o naplánované cílové pozici a pozici, kdy se mají zapnout ukazatele směru.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public class Destination
{
//== CONSTANT CLASS ATTRIBUTES =================================================
//== VARIABLE CLASS ATTRIBUTES =================================================

//== STATIC INITIALIZER (CLASS CONSTRUCTOR) ====================================
//== CLASS GETTERS AND SETTERS =================================================
//== OTHER NON-PRIVATE CLASS METHODS ===========================================
//== PRIVATE AND AUXILIARY CLASS METHODS =======================================



//##############################################################################
//== CONSTANT INSTANCE ATTRIBUTES ==============================================

    /** Cílový segment silnice. */
    public final IStreet destStreet;

    /** Segment silnice, po jehož dosažení se má zapnout ukazatel směru. */
    public final IStreet blinkStreet;

    /** Směr, do nějž bude auto zatáčet, a kam bude proto blikat. */
    public final LeftRight blinkDirection;



//== VARIABLE INSTANCE ATTRIBUTES ==============================================



//== CONSTRUCTORS AND FACTORY METHODS ==========================================

    /***************************************************************************
     * Vytvoří novou přepravku se zadanými informacemi pro přesun.
     *
     * @param destStreet     Cílový segment silnice
     * @param blinkStreet    Segment silnice, po jehož dosažení se má zapnout
     *                       ukazatel směru
     * @param blinkDirection Směr, do nějž bude auto zatáčet,
     *                       a kam bude proto blikat
     */
    public Destination(IStreet destStreet, IStreet   blinkStreet,
                                           LeftRight blinkDirection)
    {
        this.destStreet     = destStreet;
        this.blinkStreet    = blinkStreet;
        this.blinkDirection = blinkDirection;
    }



//== ABSTRACT METHODS ==========================================================
//== INSTANCE GETTERS AND SETTERS ==============================================
//== OTHER NON-PRIVATE INSTANCE METHODS ========================================

    /***************************************************************************
     * Textový podpis instance určený především pro účely ladění.
     *
     * @return Textový podpis instance
     */
    @Override
    public String toString()
    {
        return "Destination{\n  destStreet="     + destStreet     +
                           "\n  blinkStreet="    + blinkStreet    +
                           "\n  blinkDirection=" + blinkDirection +
               "\n}";
    }



//== ABSTRACT METHODS ==========================================================
//== INSTANCE GETTERS AND SETTERS ==============================================
//== OTHER NON-PRIVATE INSTANCE METHODS ========================================



//== PRIVATE AND AUXILIARY INSTANCE METHODS ====================================



//##############################################################################
//== NESTED DATA TYPES =========================================================

    /***************************************************************************
     * Výčtový typ se směry, kam bude vozidlo zatáčet a blinkr blikat.
     */
    public static enum LeftRight
    {
        /** Bude se zatáčet a blikat doleva.       */   LEFT,
        /** Bude se zatáčet a blikat doprava.      */   RIGHT,
        /** Nebude se zatáčet, a proto ani blikat. */   NO,
        ;
    }
}
