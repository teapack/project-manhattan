package eu.pedu.group17s.manhattan.jinv00;

/**
 * Interface IListener implementují všechny třídy, které potřebují "poslouchat
 * tikání" hodin - tedy takové třídy, jejichž instance každý "tick" hodin musejí
 * vykonat nějakou činnost. Zmíněnou vykonávanou činnost bude provádět metoda
 * "tick()", která bude volána každý "tick" hodin.
 *
 * @author Vratislav Jindra
 * @version 201706041530
 */
public interface IListener {

    /**
     * Metoda, která se bude volat při každém "ticku" hodin (viz třída Clock).
     */
    public void tick();

    /**
     * Metoda, která vrátí true, má-li se listener odstranit ze seznamu
     * listenerů (ve třídě Clock).
     *
     * @return true, má-li se listener odstranit ze seznamu posluchačů; jinak
     * vrací false
     */
    public boolean isRemoveListener();
}
