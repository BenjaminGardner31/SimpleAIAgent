package uk.ac.nott.cs.g53dia.library;

/**
 * An abstract base class for Tankers in the standard {@link Environment}.
 *
 * @author Julian Zappala
 */
/*
 * Copyright (c) 2003 Stuart Reeves
 * Copyright (c) 2003-2005 Neil Madden (nem@cs.nott.ac.uk).
 * Copyright (c) 2011 Julian Zappala (jxz@cs.nott.ac.uk).
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public abstract class Tanker {
    /**
     * Sub-classes must implement this method to provide the "brains" for the
     * Tanker.
     * @param view the cells the Tanker can currently see
     * @param timestep The current timestep
     * @return an action to perform
     */
    public abstract Action senseAndAct(Cell[][] view, long timestep);
    // Fields used by the environment
    /**
     * The initial level of fuel in the tanker
     */
    int fuelLevel = MAX_FUEL;
    
    /**
     * The initial level of water in the tanker
     */
    int waterLevel = 0;
    
  
    /**
     * The initial number of tasks completed
     */    
    int completedCount = 0;
    
   
    
    /**
     * The total amount of water delivered (completed tasks only)
     */
    
    int waterDelivered = 0;
    
    /**
     * The maximum amount of fuel a Tanker can have.
     */
    public final static int MAX_FUEL = 100;
    
    /**
     * The maximum amount of water a Tanker can have.
     */
    public final static int MAX_WATER = 10000;
    

    /**
     * The distance a Tanker can "see".
     */
    public final static int VIEW_RANGE = MAX_FUEL/8;

    /**
     * Location of fuel pump
     */
    public final static Point FUEL_PUMP_LOCATION = new Point(0,0);
    
    
    
  
    /**
     * Use fuel - used by move actions/
     */
    public void useFuel(int amount) throws ActionFailedException {
	   if (fuelLevel <= 0) {
           throw new OutOfFuelException("no fuel");
       }
    	fuelLevel -= amount;
     
    }
    /**
     * How much fuel does this tanker have?
     */
    public int getFuelLevel() {
        return fuelLevel;
    }
    /**
     * How much water does this tanker have?
     * 
     */
    
    public int getWaterLevel() {
    	return waterLevel;
    }

    /**
     * The Tanker's current position in the environment.
     */
    Point position = new Point(0,0); // Default to origin
    /**
     * Get the Tanker's current position in the environment.
     */
    public Point getPosition() {
        return (Point)position.clone();
    }
    
    
    /**
     * Get the cell currently occupied by the Tanker.
     * @param view the cells the Tanker can currently see
     * @return a reference to the cell currently occupied by this Tanker
     */
    
    public Cell getCurrentCell(Cell[][] view) {
    	return view[VIEW_RANGE][VIEW_RANGE];
    
    }
    
    
    /**
     * Get the number of tasks completed
     */
    
    public int getCompletedCount() {
    	return completedCount;
    }
     
     /**
      * Increment Completed count
      */
     
     void incCompleted() {
    	 completedCount++;
     }
     
     protected long getScore() {
    	 return (long)completedCount * (long)waterDelivered;
    	 
     }
     
}
