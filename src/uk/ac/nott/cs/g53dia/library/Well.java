package uk.ac.nott.cs.g53dia.library;

/**
 * A well is a source of water
 * 
 * @author Neil Madden
 */

/*
 * Copyright (c) 2011 Julian Zappala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class Well extends DefaultCell {
	/**
	 * yield of well
	 */
	public final static int WATER_YIELD = Tanker.MAX_WATER;
	
	
    public Well(Point point) {
    
    	super(point);
    	
        }

 }
