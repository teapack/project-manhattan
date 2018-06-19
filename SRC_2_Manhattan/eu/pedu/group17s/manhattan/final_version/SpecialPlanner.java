/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.pedu.group17s.manhattan.final_version;

import eu.pedu.group17s.manhattan.final_version.DATA.DataPkg;
import eu.pedu.manhattan.fw17s.Destination;
import eu.pedu.manhattan.fw17s.Holder;
import eu.pedu.manhattan.fw17s.IPlanner;
import eu.pedu.manhattan.fw17s.IStreet;
import eu.pedu.manhattan.fw17s.ITownElement;
import eu.pedu.manhattan.fw17s.IVehicle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vratislav Jindra
 */
public class SpecialPlanner implements IPlanner {

    private boolean firstFileRead;
    private List<Destination> specialDestinations;
    private boolean specialDestinationsSet;
    List<IStreet> path;

    /**
     * Konstruktor třídy Planner.
     */
    public SpecialPlanner() {
        path = new ArrayList<>();
        System.out.println("Special planner");
        specialDestinations = new ArrayList<>();
        firstFileRead = true;
    }

    /**
     * Naplánuje pro zadané vozidlo další cíl. Dalším cílem je nejbližší
     * segment, na němž lze odbočit. Plánovač si zjistí, kde je, naplánuje, zda
     * se na něm odbočí, a pokud ano, tak na kterou stranu se má otočit a
     * současně kde se mají zapnout příslušné blinkry. Metoda vrátí přepravku s
     * těmito informacemi.
     *
     * @param vehicle Vozidlo, pro něž plánujeme další cíl
     * @return Přepravka s informacemi potřebnými pro korektní přesun k
     * naplánovanému cíli
     */
    @Override
    public Destination suggestNextDestination(IVehicle vehicle) {
        Destination d;
        Destination firstDestination;
        if (specialDestinationsSet == false) {
            specialDestinationsSet = true;
            String line;
            InputStream in = DataPkg.class.getResourceAsStream("special_path.txt");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
                while (((line = reader.readLine()) != null)) {
                    String[] words = line.split(" ");
                    int[] integers = new int[words.length];
                    for (int i = 0; i < words.length; i++) {
                        integers[i] = Integer.parseInt(words[i]);
                    }
                    ITownElement te1 = Holder.getTown().getElementAt(integers[0], integers[1]);
                    ITownElement te2 = Holder.getTown().getElementAt(integers[2], integers[3]);
                    Destination dest;
                    switch (integers[4]) {
                        case 0:
                            dest = new Destination((IStreet) te1, (IStreet) te2, Destination.LeftRight.LEFT);
                            break;
                        case 1:
                            dest = new Destination((IStreet) te1, (IStreet) te2, Destination.LeftRight.RIGHT);
                            break;
                        default:
                            dest = new Destination((IStreet) te1, (IStreet) te2, Destination.LeftRight.NO);
                            break;
                    }
                    specialDestinations.add(dest);
                }
            } catch (IOException ex) {
                throw new RuntimeException("Zadaný soubor se nepodařilo otevřít.");
            }
            d = specialDestinations.get(0);
        } else {
            // Došli jsme na konec souboru.
            if (specialDestinations.size()
                    == ((Vehicle) vehicle).getSpecialLine()) {
                ((Vehicle)vehicle).setSpecialLine(1);
                d = specialDestinations.get(((Vehicle) vehicle).getSpecialLine());
            } else {
                d = specialDestinations.get(((Vehicle) vehicle).getSpecialLine());
            }
        }
        return d;
    }

}
