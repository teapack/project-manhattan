/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package eu.pedu.manhattan.fw17s;

import eu.pedu.lib17s.util.InputStreamUTF8Reader;

import java.awt.event.KeyEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JFileChooser;



/*******************************************************************************
 * Instance interfejsu {@code ITownBuilder} představují budovatele měst,
 * kteří jsou schopni vytvořit město na základě informací v textovém souboru.
 * Předpokládají se dvě možné verze měst:
 * <ul>
 *    <li>Zjednodušené zadání určené pro tříčlenné skupiny,
 *        v němž stačí vytvořit město se zadaným počtem vozidel.
 *        <br>&nbsp;</li>
 *    <li>Rozšířené zadání určené pro čtyřčlenné skupiny,
 *        v němž je třeba dovybavit město vstupním a výstupním segmentem,
 *        přičemž vstupní segment slouží pro příjezd vozidel z oblasti mimo
 *        zobrazované město a výstupní segment slouží pro jejich odjezd.
 *        </li>
 * </ul>
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public interface ITownBuilder
{
//== STATIC CONSTANTS ==========================================================
//== STATIC METHODS ============================================================



//##############################################################################
//== ABSTRACT GETTERS AND SETTERS ==============================================
//== OTHER ABSTRACT METHODS ====================================================

    /***************************************************************************
     * Na základě informací ze zadaného souboru vytvoří město
     * a uloží je do schránky ve třídě {@link Holder}.
     *
     * @param reader    Vstupní proud s podklady pro tvorbu města
     * @param factories Tovární objekt pro výrobu součástí města
     * @param inputKey  Kód klávesy, po jejímž stisku se na vstupu objeví
     *                  nové vozidlo
     * @param exitKey   Kód klávesy, po jejímž stisku se otevře výstup
     *                  pro jedno vozidlo
     */
//    @Override
    public void buildTown(BufferedReader reader, IFactories factories,
                          int inputKey, int exitKey)
    ;


    /***************************************************************************
     * Na základě informací ze zadaného souboru vytvoří město
     * se zadaným počtem vozidel, které realizuje zjednodušené zadání,
     * takže nemusí mít vstupní a výstupní segment;
     * vytvořené město uloží do schránky ve třídě {@link Holder}.
     *
     * @param reader            Vstupní proud s podklady pro tvorbu města
     * @param factories         Tovární objekt pro výrobu součástí města
     * @param numberOfVehicles  Počet vozidel, která je třeba
     *                          náhodně rozmístit po městě
     */
//    @Override
    public void buildSimplerTown(BufferedReader reader, IFactories factories,
                                 int numberOfVehicles)
    ;



//== DEFAULT GETTERS AND SETTERS ===============================================
//== OTHER DEFAULT METHODS =====================================================

    /***************************************************************************
     * Na základě informací z implicitního souboru {@code Default_town.txt}
     * vytvoří město se implicitním počtem vozidel, které realizuje
     * zjednodušené zadání, takže nemusí mít vstupní a výstupní segment;
     * vytvořené město uloží do schránky ve třídě {@link Holder}.
     * Tento soubor by měl být součástí balíčku, v němž se nachází
     * třída daného budovatele města.
     */
    default
    public void buildDefaultSimplerTown()
    {
        buildDefaultSimplerTown(0);
    }


    /***************************************************************************
     * Na základě informací z implicitního souboru {@code Default_town.txt}
     * vytvoří město se zadaným počtem vozidel, které realizuje
     * zjednodušené zadání, takže nemusí mít vstupní a výstupní segment;
     * vytvořené město uloží do schránky ve třídě {@link Holder}.
     * Tento soubor by měl být součástí balíčku, v němž se nachází
     * třída daného budovatele města.
     *
     * @param numberOfVehicles  Počet vozidel, která je třeba
     *                          náhodně rozmístit po městě
     */
    default
    public void buildDefaultSimplerTown(int numberOfVehicles)
    {
        buildDefaultSimplerTown(Holder.getFactories(), numberOfVehicles);
    }


    /***************************************************************************
     * Na základě informací z implicitního souboru {@code Default_town.txt}
     * vytvoří město se zadaným počtem vozidel, které realizuje
     * zjednodušené zadání, takže nemusí mít vstupní a výstupní segment;
     * vytvořené město uloží do schránky ve třídě {@link Holder}.
     * Tento soubor by měl být součástí balíčku, v němž se nachází
     * třída daného budovatele města.
     *
     * @param factories         Tovární objekt pro výrobu součástí města
     * @param numberOfVehicles  Počet vozidel, která je třeba
     *                          náhodně rozmístit po městě
     */
    default
    public void buildDefaultSimplerTown(IFactories factories,
                                        int numberOfVehicles)
    {
        Class<?>       cls    = ITownBuilder.class; //this.getClass();
//        URL url1 = cls.getResource("ITownBuilder.class");
//        URL url2 = cls.getResource("Default_town.txt");
        InputStream    stream = cls.getResourceAsStream("Default_town.txt");
        BufferedReader reader = new BufferedReader(
                                    new InputStreamUTF8Reader(stream));
        buildSimplerTown(reader, factories, numberOfVehicles);
    }


    /***************************************************************************
     * Požádá uživatele o označení vstupního souboru a na základě informací
     * z tohoto souboru vytvoří město;
     * vytvořené město uloží do schránky ve třídě {@link Holder}.
     *
     * @param reader    Vstupní proud s podklady pro tvorbu města
     * @param factories Tovární objekt pro výrobu součástí města
     */
    default
    public void buildTown(BufferedReader reader, IFactories factories)
    {
        buildTown(reader, factories, KeyEvent.VK_SPACE, KeyEvent.VK_ESCAPE);
    }


    /***************************************************************************
     * Požádá uživatele o označení vstupního souboru
     * a na základě informací z tohoto souboru vytvoří město;
     * vytvořené město uloží do schránky ve třídě {@link Holder}.
     *
     * @param factories Tovární objekt pro výrobu součástí města
     */
    default
    public void buildTown(IFactories factories)
    {
        buildAppropriateTown(factories, 0);
    }


    /***************************************************************************
     * Požádá uživatele o označení vstupního souboru
     * a na základě informací z tohoto souboru vytvoří město
     * se zadaným počtem vozidel, které realizuje zjednodušené zadání,
     * takže nemusí mít vstupní a výstupní segment;
     * vytvořené město uloží do schránky ve třídě {@link Holder}.
     *
     * @param factories         Tovární objekt pro výrobu součástí města
     * @param numberOfVehicles  Počet vozidel, která je třeba
     *                          náhodně rozmístit po městě
     */
    default
    public void buildSimplerTown(IFactories factories,
                                 int numberOfVehicles)
    {
        buildAppropriateTown(factories, numberOfVehicles);
    }


    /***************************************************************************
     * Požádá uživatele o označení vstupního souboru a na základě informací
     * z tohoto souboru vytvoří město odpovídajícího typu;
     * vytvořené město uloží do schránky ve třídě {@link Holder}.
     *
     * @param factories         Tovární objekt pro výrobu součástí města
     * @param numberOfVehicles  Počet vozidel, která je třeba
     *                          náhodně rozmístit po městě.
     *        Je-li zadán nulový počet vozidel, vytváří se město
     *        s implicitně ovládaným vstupním a výstupním segmentem, tj. město,
     *        do nějž mohou vozidla přijíždět a zase z něj odjíždět.
     */
    default void buildAppropriateTown(IFactories factories,
                                      int numberOfVehicles)
    {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(null);
        if(returnVal != JFileChooser.APPROVE_OPTION) {
            System.exit(1);
        }
        File  file = fileChooser.getSelectedFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            if (numberOfVehicles == 0) {
                buildTown(reader, factories);
            }
            else {
                buildSimplerTown(reader, factories, numberOfVehicles);
            }
        }
        catch(IOException ex) {
            throw new RuntimeException(
                    "\nZadaný soubor se nepodařilo otevřít: " + file);
        }
    }



//##############################################################################
//== NESTED DATA TYPES =========================================================
}
