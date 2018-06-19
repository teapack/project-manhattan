package eu.pedu.group17s.manhattan.final_version;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Třída Clock reprezentuje hodiny, které běží, jsou-li zapnuté. Při běhu hodin
 * se neustále volá metoda "run()" (objektu "TimerTask"). Ve zmíněné metodě se
 * pro každého "posluchače" hodin zavolá jeho metoda "tick()".
 *
 * @author Vratislav Jindra
 * @version 201705312108
 */
public class Clock {

    private boolean isAlive;
    private static final Clock SINGLETON = new Clock();
    private Collection<IListener> listeners;
    private Collection<IListener> newListeners;
    private Timer timer;
    private final TimerTask timerTask;

    /**
     * Konstruktor třídy Clock. V tomto konstruktoru se vytvoří objekt třídy
     * "TimerTask", a bude probíhat volání metody run(). V této metodě se pak
     * volá metoda tick() každého posluchače těchto hodin.
     */
    public Clock() {
        listeners = new ArrayList<>();
        newListeners = new ArrayList<>();
        isAlive = false;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                listeners.addAll(newListeners);
                newListeners.clear();
                Iterator<IListener> iter = listeners.iterator();
                while (iter.hasNext()) {
                    IListener listener = iter.next();
                    listener.tick();
                    if (((IListener) listener).isRemoveListener()) {
                        iter.remove();
                    }
                }
            }
        };
    }

    /**
     * Getter na jedinou instanci třídy Clock.
     *
     * @return instance třídy Clock
     */
    public static Clock getInstance() {
        return SINGLETON;
    }

    /**
     * Metoda pro prvku do seznamu listenerů.
     *
     * @param element přidávaný prvek
     */
    public void addListener(IListener element) {
        // Tady jsem původně plánoval přidávat prvky nejdřív do seznamu
        // "newListeners", a prvky z tohoto seznamu jsem pak najednou uložil
        // do seznamu "listeners". Tím jsem se zbavil
        // ConcurrentModificationException, která nastávala, když jsem přidával
        // prvky do seznamu "listeners" ve chvíli, kdy jsem tímto seznamem
        // iteroval pomocí iterátoru. Bohužel, při použití výše zmíněného
        // způsobu se hodiny neúměrně zrychlí, a nepodařilo se mi je zpomalit
        // tak, aby autíčka jezdila plynule a zároveň aby nebourala.
        newListeners.add(element);
    }

    /**
     * Metoda pro zapnutí nebo vypnutí hodin.
     *
     * @param alive true pro zapnutí hodin, false pro vypnutí hodin
     */
    public void setState(boolean alive) {
        isAlive = alive;
        if (isAlive) {
            (timer = new Timer()).schedule(timerTask, 50L, 50L);
        } else {
            timer.cancel();
            timer = null;
        }
    }
}
