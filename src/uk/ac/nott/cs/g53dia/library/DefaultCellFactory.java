package uk.ac.nott.cs.g53dia.library;
/**
 * A default CellFactory which populates the environment with stations and wells.
 * 
 * @author Neil Madden
 */

/*
 * Copyright (c) 2005 Neil Madden.
 * Copyright (c) 2010 University of Nottingham.
 * Copyright (c) 2011 Julian Zappala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class DefaultCellFactory implements CellFactory {
    /**
     * Default well density.
     */
    final static double DEFAULT_STATION_DENSITY = 2.0/(2.0 * (Tanker.VIEW_RANGE + 1) * 2.0 * (Tanker.VIEW_RANGE + 1));
    final static double DEFAULT_WELL_DENSITY = 1.0/(2.0 * (Tanker.VIEW_RANGE + 1) * 2.0 * (Tanker.VIEW_RANGE + 1 ));
    
    
    /**
     * Create a DefaultCellFactory.
     * 
     */
    
    public DefaultCellFactory() {}

    /**
     * Create new cells and wells in the environment.
     * @param env environment to which the cell is to be added
     * @param pos position of the new cell
     */

    public void generateCell(Environment env, Point pos) {
    	if (pos.x==0 & pos.y==0) {
    		env.putCell(new FuelPump(pos));
    	}
    	else if (Math.random() < DEFAULT_WELL_DENSITY) {
    		env.putCell(new Well(pos));
    	}
    	
    	 
    	else if (Math.random() < DEFAULT_STATION_DENSITY) {
    		env.putCell(new Station(pos));
    		env.stations.add((Station)env.getCell(pos));
    	}
    	else {
        	env.putCell(new EmptyCell(pos));
        }
    }
}
