package uk.ac.nott.cs.g53dia.library;
/**
 * Action which delivers water from the tanker to a station.
 *
 * @author Julian Zappala
 */

/*
 * Copyright (c) 2011 Julian Zappala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class DeliverWaterAction implements Action {

	Task task;
	
    public DeliverWaterAction(Task t) {
    	task = t;
    	
    }

    public void execute(Environment env, Tanker tank)
        throws ActionFailedException
    {
    	if (task.isComplete()) {
		throw new ActionFailedException("DeliverWater: Task already complete");
	}
    	
    	if (!(tank.getPosition().equals(task.getStationPosition()))) {
    		throw new ActionFailedException("DeliverWater: Not at Station");
    	}
    	
        if (tank.waterLevel <= 0) {
            throw new ActionFailedException("DeliverWater: Not Enough Water");
        }
        
        if (tank.waterLevel >= task.getRequired()) {
        	tank.waterLevel -= task.getRequired();
        	task.supply(task.getRequired());
        }
        else {
        	task.supply(tank.waterLevel);
        	tank.waterLevel = 0;
        }
        
   
        
        if (task.isComplete()) {
        	tank.incCompleted();
        	tank.waterDelivered+=task.demand;
        }
    }

    public String toString() {
        return "DeliverWater";
    }
}
