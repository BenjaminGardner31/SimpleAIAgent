package uk.ac.nott.cs.g53dia.library;
/**
 * Action which loads water into the tanker.
 *
 * @author Neil Madden
 */

/*
 * Copyright (c) 2011 Julian Zapppala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class LoadWaterAction implements Action {

    public LoadWaterAction() {}

    public void execute(Environment env, Tanker tank)
        throws ActionFailedException
    {
    	
    	Cell c = env.getCell(tank.getPosition());
    	
    	if (!(c instanceof Well)) {
    		throw new ActionFailedException("LoadWater: Not at Well");
    	}
    	
        if (tank.waterLevel >= Tanker.MAX_WATER) {
            throw new ActionFailedException("LoadWater: Water tank is full");
        }
        
        tank.waterLevel += Well.WATER_YIELD;
        
        if (tank.waterLevel > Tanker.MAX_WATER) {
        	tank.waterLevel =Tanker.MAX_WATER;
        }
        
    }

    public String toString() {
        return "LoadWater";
    }
}
