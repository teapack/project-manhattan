/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.pedu.group17s.manhattan.shua00;

import eu.pedu.group17s.manhattan.final_version.GUI;
import eu.pedu.group17s.manhattan.final_version.MultiWayStreet;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.lib17s.util.Direction8;
import eu.pedu.manhattan.fw17s.Destination;
import eu.pedu.manhattan.fw17s.Holder;
import eu.pedu.manhattan.fw17s.IPlanner;
import eu.pedu.manhattan.fw17s.IStreet;
import eu.pedu.manhattan.fw17s.ITownElement;
import eu.pedu.manhattan.fw17s.IVehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/*******************************************************************************
 * Instance třídy {@code Planner} představují plánovače,
 * které lze požádat, aby zadanému vozidlu naplánovaly příští cíl cesty.
 *
 * @author  Anastasia Shuvalova
 * @version 0.00.0000 — 20yy-mm-dd
 */
public class Planner implements IPlanner 
{
    
    List<IStreet> path = new ArrayList<IStreet>();
    
    /***************************************************************************
     * Naplánuje pro zadané vozidlo další cíl.
     * Další, cílem je nejbližší segment, na němž lze odbočit.
     * Plánovač si zjistí, kde je, naplánuje, zda se na něm odbočí,
     * a pokud ano, tak na kterou stranu se má otočit a současně
     * kde se mají zapnout příslušné blinkry.
     * Metoda vrátí přepravku s těmito informacemi.
     *
     * @param vehicle Vozidlo, pro něž plánujeme další cíl
     * @return Přepravka s informacemi potřebnými pro korektní přesun
     *         k naplánovanému cíli
     */
    @Override
    public Destination suggestNextDestination(IVehicle vehicle) 
    {
        Position position = vehicle.getPosition();
        Direction8 direction = vehicle.getDirection();
        ITownElement currentElement = Holder.getTown()
                .getElementAt(new Position(vehicle.getX(), vehicle.getY()));
        if (!(currentElement instanceof IStreet))
        {
            System.err.println("Auto se nachazi " + vehicle.getPosition()
                    .toString());
            throw new IllegalStateException("Auto je mimo silnici");
            
        }
        this.path.add((IStreet)currentElement);
        Position coordinates = GUI.getCanvasManager()
                .positionPoint2Field(position);
        int nextX = direction.dx() + coordinates.x;
        int nextY = direction.dy() + coordinates.y;
        Position nextPos = new Position(nextX, nextY);
        nextPos = GUI.getCanvasManager().positionField2Point(nextPos);
        Destination des = this.findDestination(nextPos, direction);
        return des;
        
        
        
    }
    
    private Destination findDestination(Position position, Direction8 direction)
    {
        Destination des;
        Position pos = position;
        Position coords = GUI.getCanvasManager().positionPoint2Field(position);
        ITownElement nextElement = Holder.getTown().getElementAt(coords);
        if(!(nextElement instanceof IStreet))
        {
            throw new IllegalStateException("Auto narazi do budovy");
        }
        this.path.add((IStreet)nextElement);
        Direction8 nextDirection = ((IStreet)nextElement).getDirection();
        if (nextElement instanceof MultiWayStreet) 
        {
           List<Direction8>exits = ((MultiWayStreet)nextElement)
                                                        .getExitsFor(direction);
           int random = ThreadLocalRandom.current().nextInt(0, exits.size());
           nextDirection = exits.get(random);
        }
        if (!nextDirection.equals((Object)direction))
        {
            Destination.LeftRight turn = this.getTurn(direction, 
                                                               nextDirection);
            IStreet blinkStreet = this.path.size() >= 2 ? 
                    this.path.get(this.path.size() - 2) : this.path.get(0);
            des = new Destination((IStreet)nextElement, blinkStreet, turn);
            
        }
        else
        {
            Position coordinates = GUI.getCanvasManager()
                                                 .positionField2Point(position);
            int nextX = direction.dx() + coords.x;
            int nextY = direction.dy() + coords.y;
            Position nextPos = new Position(nextX, nextY);
            des = this.findDestination(nextPos, direction);
        }
      return des;
  
    }
    
    private Destination.LeftRight getTurn(Direction8 from, Direction8 to) {
        Destination.LeftRight turnDirection;
        int ordinalDistance = from.ordinalDistanceTo(to);
        switch (ordinalDistance) {
            case -6: 
            case 2: {
                turnDirection = Destination.LeftRight.LEFT;
                break;
            }
            case -2: 
            case 6: {
                turnDirection = Destination.LeftRight.RIGHT;
                break;
            }
            default: {
                turnDirection = Destination.LeftRight.NO;
            }
        }
        return turnDirection;
        }
 }
    

