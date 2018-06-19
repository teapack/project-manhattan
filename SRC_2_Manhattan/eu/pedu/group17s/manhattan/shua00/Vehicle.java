/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.pedu.group17s.manhattan.shua00;

import eu.pedu.group17s.manhattan.jinv00.DATA.DataPkg;
import eu.pedu.group17s.manhattan.final_version.GUI;
import eu.pedu.group17s.manhattan.final_version.OneWayStreet;
import eu.pedu.lib17s.canvas.IPaintable;
import eu.pedu.lib17s.canvasmanager.Painter;
import eu.pedu.lib17s.geom.IMovable;
import eu.pedu.lib17s.geom.Multimover;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.lib17s.util.Direction8;
import eu.pedu.lib17s.util.IListener;
import eu.pedu.manhattan.fw17s.Destination;
import eu.pedu.manhattan.fw17s.IStreet;
import eu.pedu.manhattan.fw17s.IVehicle;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;

/*******************************************************************************
 * Instance třídy {@code Vehicle} představují vozidla schopná pohybu
 * po ulicích města, v němž je provoz řízen semafory.
 * Vozidla nemají žádnou speciální vlastní inteligenci,
 * to, kdy a kam vozidlo pojede a kdy bude na něco čekat
 * specifikuje řidič, který má řízení daného vozidla na starosti.
 *
 * @author  Anastasia Shuvalova
 * @version 0.00.0000 — 20yy-mm-dd
 */
public class Vehicle implements IVehicle, IPaintable, IListener
{
    
    private boolean isBlinking;
    private int module;
    private Position position;
    private Direction8 direction;
    private AffineTransform at;
    private short ticks;
    private Boolean blinkerOn = false;
    private Image image;
    private URL imageUrl; 
    private Destination.LeftRight blink = null;
    
    private Destination.LeftRight blinkDirection = null;

    
    public Vehicle(Position position, Direction8 direction)
    {
        this.position = position;
        
        module = 25;
        imageUrl = DataPkg.class.getResource("vehicle.png");
       
    }

    /***************************************************************************
     * Vrátí informaci o tom, jestli vozidlo právě bliká.
     *
     * @return Bliká-li, vrátí {@code true}, jinak vrátí {@code false}
     */
    @Override
    public boolean isBlinking() 
    {
        return this.isBlinking;
    }

    /***************************************************************************
     * Posune vozidlo o zadaný počet bodů vpřed.
     *
     * @param distance Počet bodů, o něž se má vozidlo posunout
     */
    @Override
    public void forward(int distance) 
    {
        int dx = this.getDirection().dx()* distance + this.getPosition().getX();
        int dy = this.getDirection().dy()* distance + this.getPosition().getY();
        Multimover m = Multimover.getInstance();
        if (!m.isMoving(this)) {
            m.moveWithSpeed(50, (IMovable)this, dx, dy);
        }

    }

    /***************************************************************************
     * Zapne levý blinkr.
     */
    @Override
    public void blinkLeft() 
    {
        if (!this.isBlinking()) 
        {
            this.isBlinking = true;
            this.blink = Destination.LeftRight.LEFT;
            this.imageUrl = DataPkg.class.getResource("vehicle_left.png");
            try 
            {
                this.image = ImageIO.read(this.imageUrl);
            }
            catch (IOException ex) 
            {
                Logger.getLogger(OneWayStreet.class.getName()).log(Level.SEVERE,
                                                                           null,
                                                                            ex);
            }          
        }
    }

    /***************************************************************************
     * Zapne pravý blinkr.
     */
    @Override
    public void blinkRight() 
    {
        if (!this.isBlinking()) 
        {
            this.isBlinking = true;
            this.blink = Destination.LeftRight.RIGHT;
            this.imageUrl = DataPkg.class.getResource("vehicle_right.png");
            try 
            {
                this.image = ImageIO.read(this.imageUrl);
            }
            catch (IOException ex) 
            {
                Logger.getLogger(OneWayStreet.class.getName()).log(Level.SEVERE,
                                                                           null,
                                                                            ex);
            }          
        }
    }

    /***************************************************************************
     * Vypne blikání.
     */
    @Override
    public void stopBlinking() 
    {
        if (this.isBlinking()) 
        {
            this.isBlinking = false;
            this.blink = Destination.LeftRight.NO;
            this.imageUrl = DataPkg.class.getResource("vehicle.png");
            try 
            {
                this.image = ImageIO.read(this.imageUrl);
            }
            catch (IOException ex) 
            {
                Logger.getLogger(OneWayStreet.class.getName()).log(Level.SEVERE,
                                                                           null,
                                                                            ex);
            }          
        } 
    }

    /***************************************************************************
     * Vrátí velikost modulu, tj. délku strany opsaného čtverce.
     *
     * @return   Velikost modulu instance
     */
    @Override
    public int getModule() 
    {
        return this.module;
    }

    /***************************************************************************
     * Změni velikost instance, aby měl její nový modul
     * (= délku strany opsaného čtverce) zadanou velikost.
     *
     * @param module    Nově nastavovaný modul
     */
    @Override
    public void setModule(int module) 
    {
        this.module = module;
    }

    /***************************************************************************
     * Vrátí x-ovou (vodorovnou) souřadnici pozice instance,
     * tj. vodorovnou souřadnici levého horního rohu opsaného obdélníku.
     *
     * @return  Aktuální vodorovná (x-ová) souřadnice instance,
     *          x=0 má levý okraj plátna, souřadnice roste doprava
     */
    @Override
    public int getX() 
    {
        return GUI.getCanvasManager().positionPoint2Field((Position)this
                .position).x;
    }

    /***************************************************************************
     * Vrátí y-ovou (svislou) souřadnici pozice instance,
     * tj. svislou souřadnici levého horního rohu opsaného obdélníku.
     *
     * @return  Aktuální svislá (y-ová) souřadnice instance,
     *          y=0 má horní okraj plátna, souřadnice roste dolů
     */
    @Override
    public int getY() 
    {
        return GUI.getCanvasManager().positionPoint2Field((Position)this
                .position).y;
    }

    /***************************************************************************
     * Přemístí instanci na zadanou pozici.
     * Pozice instance je přitom definována jako pozice
     * levého horního rohu opsaného obdélníku.
     *
     * @param x  Nově nastavovaná vodorovná (x-ová) souřadnice instance,
     *           x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y  Nově nastavovaná svislá (y-ová) souřadnice instance,
     *           y=0 má horní okraj plátna, souřadnice roste dolů
     */
    @Override
    public void setPosition(int x, int y) 
    {
        this.position = new Position(x, y);
      
    }

    /***************************************************************************
     * Vrátí směr, do nějž je daná instance natočena.
     *
     * @return Směr, do nějž je daná instance natočena
     */
    @Override
    public Direction8 getDirection() 
    {
        return this.direction;
    }

    /***************************************************************************
     * Otočí instanci o 90° vlevo.
     */
    @Override
    public void turnLeft() 
    {
        this.setDirection(this.direction.leftTurn());
        stopBlinking();
    }

    /***************************************************************************
     * Otočí instanci o 90° vpravo.
     */
    @Override
    public void turnRight() 
    {
        this.setDirection(this.direction.rightTurn());
        stopBlinking();
    }

    /***************************************************************************
     * Prostřednictvím dodaného kreslítka vykreslí obraz své instance.
     *
     * @param painter Kreslítko schopné kreslit na plátno ovládané správcem
     */
    @Override
    public void paint(Painter painter) 
    {
        AffineTransform at = new AffineTransform();
        if (null == this.direction) 
        {
            at = null;
        } 
        else 
        {
            switch (this.direction) 
            {
                case EAST: 
                {
                    at.translate(this.getPosition().getX(), this.getPosition().getY());
                    at.quadrantRotate(1, 25.0, 25.0);
                    break;
                }
                case SOUTH: 
                {
                    at.translate(this.getPosition().getX(), this.getPosition().getY());
                    at.quadrantRotate(-2, 25.0, 25.0);
                    break;
                }
                case WEST: 
                {
                    at.translate(this.getPosition().getX(), this.getPosition().getY());
                    at.quadrantRotate(-1, 25.0, 25.0);
                    break;
                }
                default:
                {
                    at = null;
                }
            }
        }
        painter.drawPicture(position.getX(), position.getY(), image, at);

    }
    

    /***************************************************************************
     * Zobrazí svoji instanci, tj.vykreslí její obraz na plátno.
     */
    @Override
    public void paint()
    {
        GUI.getCanvasManager().add(this);
    }

    /***************************************************************************
     * Smaže obraz své instance z plátna,
     * t.j. nakreslí ji barvou pozadí plátna.
     */
    @Override
    public void rubOut() 
    {
         GUI.getCanvasManager().remove(this);
    }

    /***************************************************************************
     * Hlášeni o výskytu očekávané události.
     *
     * @param informant Objekt, který je schopen poskytnout informace
     *                  o události, kterou zavolání dané metody ohlašuje
     */
    @Override
    public void notify(Object informant) 
    {
        informant.notify();
    }
    
    public void tick() 
    {
        if (this.isBlinking()) 
        {
            if (this.ticks >= 5) 
            {
                this.ticks = 0;
                if (this.blinkerOn.booleanValue())
                {
                    this.blinkerOn = false;
                    URL blinkerUrl = DataPkg.class.getResource("vehicle.png");
//                    try 
//                    {
//                        this.paint(ImageIO.read(blinkerUrl));
//                    }
//                    catch (IOException ex)
//                    {
//                        Logger.getLogger(OneWayStreet.class.getName())
//                                                   .log(Level.SEVERE, null, ex);
//                    }
                } 
                else 
                {
                    URL blinkerUrl;
                    this.blinkerOn = true;
                    if (null == this.blinkDirection)
                    {
                        blinkerUrl = null;
                    } 
                    else 
                    {
                        switch (this.blinkDirection) 
                        {
                            case LEFT: 
                            {
                                blinkerUrl = DataPkg.class
                                               .getResource("vehicle_left.png");
                                break;
                            }
                            case RIGHT: 
                            {
                                blinkerUrl = DataPkg.class 
                                              .getResource("vehicle_right.png");
                                break;
                            }
                            default: 
                            {
                                blinkerUrl = null;
                            }
                        }
                    }
//                    try 
//                    {
//                        this.setImage(ImageIO.read(blinkerUrl));
//                    }
//                    catch (IOException ex) 
//                    {
//                        Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }
            }
            this.ticks = (short)(this.ticks + 1);
        }
    }  
    


}
