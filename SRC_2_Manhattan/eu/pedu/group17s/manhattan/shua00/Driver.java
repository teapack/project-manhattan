/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.pedu.group17s.manhattan.shua00;

import eu.pedu.group17s.manhattan.final_version.ControlledStreet;
import eu.pedu.group17s.manhattan.final_version.GUI;
import eu.pedu.lib17s.geom.Multimover;
import eu.pedu.lib17s.geom.Position;
import eu.pedu.manhattan.fw17s.Destination;
import eu.pedu.manhattan.fw17s.Holder;
import eu.pedu.manhattan.fw17s.IDriver;
import eu.pedu.manhattan.fw17s.IPlanner;
import eu.pedu.manhattan.fw17s.IStreet;
import eu.pedu.manhattan.fw17s.ITown;
import eu.pedu.manhattan.fw17s.ITownElement;
import eu.pedu.manhattan.fw17s.ITrafficLight;
import eu.pedu.manhattan.fw17s.IVehicle;

/*******************************************************************************
 * Instance třídy {@code Driver} představují řidiče,
 * který dovede zadané vozidlo k naplánovanému cíli.
 * <p>
 * Způsob zadávání cílů závisí na konkrétním implementujícím datovém typu.
 * Implicitní řešení předpokládá, že nemá-li řidič žádný vytčený cíl
 * (případně pokud již vytčeného cíle dosáhl), požádá o jeho zadání
 * nějakého plánovače cílů (instanci interfejsu {@link IPlanner}).
 * Pro řidiče některých typů ale mohou být cíle zadávány z klávesnice,
 * anebo může být zvolen nějaký jiný způsob.
 *
 * @author  Anastasia Shuvalova
 * @version 0.00.0000 — 20yy-mm-dd
 */
public class Driver implements IDriver 
{
    
    private IVehicle vehicle;
    private IPlanner planner;
    private Destination destination;
    


    /***************************************************************************
     * Vrátí ovládané vozidlo.
     *
     * @return Ovládané vozidlo
     */
    @Override
    public IVehicle getVehicle()
    {
        return this.vehicle;
    }

    /***************************************************************************
     * Korektně popojede se svěřeným autem o další krok k vytčenému cíli,
     * přičemž cestou respektuje semafory a překážející vozidla a v případě,
     * má-li na konci odbočit, včas zapne blinkry.
     * <p>
     * Nemá-li žádný vytčený cíl (případně pokud již vytčeného cíle dosáhl,
     * požádá nějakého plánovače cílů (instanci interfejsu {@link IPlanner}),
     * aby mu cíl našel, a k tomuto cíli pak vozidlo vede.
     * <p>
     * Tato metoda slouží k tomu, aby město řídící svůj provoz
     * mohlo synchronizovat dění tak, že zadaných časových intervalech (krocích)
     * žádá řidiče, aby provedli další krok.
     * Frekvence volání této metody závisí na řídícím městě.
     */
    @Override
    public void nextStep() 
    {
        Position vehiclePosition = this.vehicle.getPosition();
        Position vehicleCoordinates = GUI.getCanvasManager().positionPoint2Field(vehiclePosition);
        IStreet currentStreet = (IStreet)Holder.getTown().getElementAt(vehicleCoordinates);
        if (this.destination == null) {
            this.destination = Holder.getFactories().newPlanner().suggestNextDestination(this.vehicle);
        }
        Position one = this.destination.blinkStreet.getPosition();
        Position two = vehicleCoordinates;
        if (this.destination.blinkStreet != null && this.destination.blinkStreet.getPosition().x == vehicleCoordinates.x && this.destination.blinkStreet.getPosition().y == vehicleCoordinates.y) {
            if (this.destination.blinkDirection == Destination.LeftRight.LEFT) {
                this.vehicle.blinkLeft();
            } else if (this.destination.blinkDirection == Destination.LeftRight.RIGHT) {
                this.vehicle.blinkRight();
            }
        }
        if (Multimover.getInstance().isMoving(this.vehicle)) {
            return;
        }
        if (this.equalDestination(currentStreet, this.destination)) {
            if (this.destination.blinkDirection == Destination.LeftRight.LEFT) {
                this.vehicle.turnLeft();
            } else if (this.destination.blinkDirection == Destination.LeftRight.RIGHT) {
                this.vehicle.turnRight();
            }
            this.destination = Holder.getFactories().newPlanner().suggestNextDestination(this.vehicle);
        }
        Boolean clearPass = true;
        if (currentStreet instanceof ControlledStreet && ((ControlledStreet)currentStreet).getTrafficLight().getState() != ITrafficLight.TrafficLightState.GO && ((ControlledStreet)currentStreet).getTrafficLight().getState() != ITrafficLight.TrafficLightState.ATTENTION && ((ControlledStreet)currentStreet).getTrafficLight().getState() != ITrafficLight.TrafficLightState.LIGHTS_OFF) {
            clearPass = false;
        }
        if (clearPass.booleanValue()) {
            int newPositionX = this.vehicle.getDirection().dx() + GUI.getCanvasManager().positionPoint2Field((Position)this.vehicle.getPosition()).x;
            int newPositionY = this.vehicle.getDirection().dy() + GUI.getCanvasManager().positionPoint2Field((Position)this.vehicle.getPosition()).y;
            Position newPosition = new Position(newPositionX, newPositionY);
            ITownElement nextElement = Holder.getTown().getElementAt(newPosition);
            if (!(nextElement instanceof IStreet)) {
                System.err.println("na pozici: " + this.vehicle.getPosition().toString());
                throw new IllegalStateException("Auto by narazilo do budovy!");
            }
            IStreet nextStreet = (IStreet)nextElement;
            if (nextStreet.isEmptyFor(this.vehicle)) {
                this.vehicle.forward(GUI.getCanvasManager().getStep());
                currentStreet.setEmpty(this.vehicle);
            }
        }
        
    }
    
     private boolean equalDestination(IStreet street, Destination futureDestination) {
        return street.getPosition().x == futureDestination.destStreet.getPosition().x && street.getPosition().y == futureDestination.destStreet.getPosition().y;
    }
    
    private void setVehicle(Vehicle vehicle)
    {
        this.vehicle = vehicle;
    }
}
