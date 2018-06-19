package eu.pedu.group17s.manhattan.jinv00;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Iterator<IListener> iter = listeners.iterator();
                while (iter.hasNext()) {
                    IListener listener = iter.next();
                    listener.tick();
                    if (listener.isRemoveListener()) {
                        iter.remove();
                    }
                }
                listeners.addAll(newListeners);
                //listeners.forEach(l -> l.tick());
            }
        };
        // Používám synchronizedList, abych se vyhnul
        // ConcurrentModificationException při přidávání nebo při odebírání
        // prvků do/z seznamu listeners).
        listeners = Collections.synchronizedList(new ArrayList<>());
        newListeners = Collections.synchronizedList(new ArrayList<>());
        isAlive = false;
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
