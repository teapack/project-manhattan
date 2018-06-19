/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;



/*******************************************************************************
 * Instance interfejsu {@code ITrafficLight} představují semafory
 * řídící odjezd z kontrolované části silnice.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public  interface   ITrafficLight
        extends     ITownElement
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================

    /***************************************************************************
     * Vrátí ovládaný segment silnice.
     *
     * @return Ovládaný segment silnice
     */
//    @Override
    public IControlledStreet getControlledStreet()
    ;


    /***************************************************************************
     * Vrátí aktuální stav semaforu.
     *
     * @return Aktuální stav semaforu
     */
//    @Override
    public TrafficLightState getState()
    ;


    /***************************************************************************
     * Nastaví nový stav semaforu.
     *
     * @param newState Požadovaný nový stav semaforu
     */
//    @Override
    public void setState(TrafficLightState newState)
    ;



//== OTHER ABSTRACT METHODS ====================================================
//== DEFAULT GETTERS AND SETTERS ===============================================
//== OTHER DEFAULT METHODS =====================================================



//##############################################################################
//== NESTED DATA TYPES =========================================================

    /***************************************************************************
     * Instance výčtového typu {@code TrafficLightState}
     * představují stavy semaforu.
     *
     * @author Rudolf PECINOVSKÝ
     * @version 6.03.2017 — 2010-11-08
     */
    public static enum TrafficLightState
    {
        /** Svítí pouze červená.              */  STOP,
        /** Svítí žlutá spolu s červenou.     */  GET_READY,
        /** Svítí pouze zelená.               */  GO,
        /** Svítí pouze žlutá (oranžová).     */  ATTENTION,
        /** Všechna světla jsou zhasnuta.     */  LIGHTS_OFF,
        /** Svítí všechna světla - pro testy. */  LIGHTS_ON;

        /** Počet řádných stavů bez testovacího. */
        private static final int STATES = LIGHTS_ON.ordinal();

        /** Pole se všemi stavy pro usnadnění přepínání mezi stavy. */
        private static final TrafficLightState[] VALUES =
                                                 TrafficLightState.values();


        /***********************************************************************
         * Vrátí následující stav semaforu, přičemž po {@code LIGHTS_OFF}
         * vrací opět stav {@code STOP}.
         *
         * @return Následující stav
         */
        public TrafficLightState getNext()
        {
            TrafficLightState state = VALUES[(this.ordinal() + 1)  %  STATES];
            return state;
        }

    }
}
