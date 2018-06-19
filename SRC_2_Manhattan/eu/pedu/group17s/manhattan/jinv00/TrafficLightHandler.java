package eu.pedu.group17s.manhattan.jinv00;

import eu.pedu.lib17s.util.Direction8;
import eu.pedu.manhattan.fw17s.Holder;
import eu.pedu.manhattan.fw17s.ITrafficLight;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Tato třída se stará o správný chod semaforů ve městě. Implementuje interface
 * IListener, kvůli čemuž musí být v této třídě definovaná metoda "tick()",
 * která bude zavolána při každém "ticku" hodin (viz třída Clock).
 *
 * @author Vratislav Jindra
 * @version 201706041202
 */
public class TrafficLightHandler implements IListener {

    private int ticks;
    private final int TRAFFIC_LIGHT_SPEED = 500; // default: 50
    private final int ORANGE_SPEED = 5; // default: 5
    private Collection<ITrafficLight> trafficLightsNorthSouth;
    private Collection<ITrafficLight> trafficLightsWestEast;
    private static final TrafficLightHandler SINGLETON
            = new TrafficLightHandler();

    /**
     * Konstruktor, pomocí kterého si vytvořím instanci této třídy.
     */
    private TrafficLightHandler() {
        // Seznam semaforů rozdělím na dvě části - semafory, které ovládají
        // silnice vedoucí na sever/jih, a na semafory, které ovládají silnice
        // vedoucí na západ/východ. Toto rozdělení je vhodné z toho důvodu, že
        // budeme chtít, aby se každá ze skupin semaforů vždy nacházela v jiném
        // stavu (aby nebyla např. zelená na obou skupinách semaforů).
        trafficLightsNorthSouth = new ArrayList<>();
        trafficLightsWestEast = new ArrayList<>();
        ticks = 0;
        Collection<ITrafficLight> trafficLights = Holder.getTown()
                .getTrafficLights();
        trafficLights.forEach(light -> {
            if (light.getControlledStreet().getDirection() == Direction8.NORTH
                    || light.getControlledStreet().getDirection()
                    == Direction8.SOUTH) {
                trafficLightsNorthSouth.add(light);
            } else {
                trafficLightsWestEast.add(light);
            }
        });
        // Původní stav všech semaforů ihned po jejich vytvoření je "GET_READY".
        // V následujícím bloku kódu tento stav pro jednu ze skupin semaforů
        // změním (posunu) o dva stavy "dál".
        trafficLightsWestEast.forEach(trafficLight -> {
            // Nastavím semafory na stav "GO".
            trafficLight.setState(trafficLight.getState().getNext());
            // Nastavím semafory na stav "ATTENTION".
            trafficLight.setState(trafficLight.getState().getNext());
        });
        Clock.getInstance().addListener(this);
    }

    /**
     * Metoda pro získání jediné instance této třídy.
     *
     * @return jediná isntance této třídy
     */
    public static TrafficLightHandler getInstance() {
        return SINGLETON;
    }

    /**
     * Metoda, která proběhne při každém "ticku" hodin. Tato metoda se stará o
     * střídání různých stavů na semaforech.
     */
    @Override
    public void tick() {
        if (trafficLightsNorthSouth.isEmpty()
                && trafficLightsWestEast.isEmpty()) {
            // Ve městě nejsou žádné semafory.
            return;
        }
        if (ticks > TRAFFIC_LIGHT_SPEED) {
            // Když "počítadlo" doběhlo do konce, je načase změnit stav na
            // semaforech na nový.
            // Vynuluji počítadlo.
            ticks = 0;
            // N
            trafficLightsWestEast.forEach(trafficLight -> {
                if (trafficLight.getState().equals(
                        ITrafficLight.TrafficLightState.ATTENTION)) {
                    // Svítí-li na semaforu oranžová, musím přeskočit na stav
                    // "STOP" (nemůžu jednodueš zavolat getNext(), neboť další
                    // stav je takový, ve kterém jsou vypnuta všechna světla).
                    trafficLight.setState(ITrafficLight.TrafficLightState.STOP);
                } else {
                    trafficLight.setState(trafficLight.getState().getNext());
                }
            });
            trafficLightsNorthSouth.forEach(trafficLight -> {
                if (trafficLight.getState().equals(
                        ITrafficLight.TrafficLightState.ATTENTION)) {
                    // Svítí-li na semaforu oranžová, musím přeskočit na stav
                    // "STOP" (nemůžu jednodueš zavolat getNext(), neboť další
                    // stav je takový, ve kterém jsou vypnuta všechna světla).
                    trafficLight.setState(ITrafficLight.TrafficLightState.STOP);
                } else {
                    trafficLight.setState(trafficLight.getState().getNext());
                }
            });
        }
        ITrafficLight.TrafficLightState state;
        // Do proměnné "state" si uložím stav jednoho ze semaforů.
        if (!trafficLightsWestEast.isEmpty()) {
            state = trafficLightsWestEast.iterator().next().getState();
        } else {
            state = trafficLightsNorthSouth.iterator().next().getState();
        }
        if (state.equals(ITrafficLight.TrafficLightState.ATTENTION)
                || state.equals(ITrafficLight.TrafficLightState.GET_READY)) {
            // Když svítí oranžová, tady se postarám o to, aby nesvítila stejně
            // dlouho jako zelená nebo červená. Doba, jak dlouho bude svítit
            // oranžová se dá změnit upravením konstanty ORANGE_SPEED.
            ticks += ORANGE_SPEED;
        } else {
            ticks++;
        }
    }

    /**
     * Metoda, která vrátí true, má-li se listener odstranit ze seznamu
     * listenerů (ve třídě Clock).
     *
     * @return true, má-li se listener odstranit ze seznamu posluchačů; jinak
     * vrací false
     */
    @Override
    public boolean isRemoveListener() {
        return false;
    }
}
