package eu.pedu.group17s.manhattan;

import eu.pedu.group17s.manhattan.final_version.Factory;
import eu.pedu.group17s.manhattan.final_version.TownBuilder;
import eu.pedu.manhattan.fw17s.Holder;

/**
 * Hlavní třída projektu, odkud se spouští celý projekt.
 *
 * @author Vratislav Jindra
 * @version 201705311218
 */
public class Manhattan {

    public static void main(String[] args) throws Exception {
        TownBuilder.getInstance().buildAppropriateTown(new Factory(), 0);
        Holder.getTown().startTrafic();
    }
}
