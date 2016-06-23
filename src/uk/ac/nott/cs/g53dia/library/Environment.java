package uk.ac.nott.cs.g53dia.library;
import java.util.*;
/**
 * An infinite grid of cells representing the current state of 
 * the environment at a given timestep. 
 * <p>
 * Each cell in the Environment implements the {@link Cell} interface.
 *
 * @author Neil Madden.
 */
/*
 * Copyright (c) 2003 Stuart Reeves.
 * Copyright (c) 2003-2005 Neil Madden.
 * Copyright (c) 2011 Julian Zappala.
 *
 * See the file "license.terms" for information on usage and redistribution 
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public class Environment {
    /**
     * Mapping from an (x,y) point to a Cell.
     */
    private Map<Point,Cell> map;
    /**
     * CellFactory which is called to generate new environment.
     */
    private CellFactory cfactory;
    /**
     * Current timestep of the simulation.
     */
    private long timestep;
    /**
     * List of stations
     */
    public ArrayList<Station> stations;
   
    
   
    
    
    
    /**
     * Initialises the Tanker environment.
     *
     * Creates an initial environment of wells.
     * @param size size of the initial environment
     * @param factory factory used to create new cells
     */
    public Environment(int size, CellFactory factory) {
        this.map        = new HashMap<Point,Cell>();
        this.cfactory   = factory;
        this.timestep   = 0l;
        this.stations 	= new ArrayList<Station>();
        

        // Generate initial environment
        for (int x = -size; x <= size; x++) {
            for (int y = -size; y <= size; y++) {
                cfactory.generateCell(this, new Point(x, y));
            }
        }
        
   
        
    }

    public Environment(int size) { this(size, new DefaultCellFactory()); }

    /**
     * Return the cells a Tanker can see.
     * @param pos position of the Tanker
     * @param size distance the Tanker can see
     * @return an array containing the cells visible from pos.
     */
    public synchronized Cell[][] getView(Point pos, int size) {
        Cell[][] res = new Cell[size*2+1][size*2+1];
        for (int x = pos.x-size; x <= pos.x+size; x++) {
            for (int y = pos.y-size; y <= pos.y+size; y++) {
            	int i = x - (pos.x - size);
            	int j = (2*size) - (y - (pos.y - size));
            	res[i][j] = getViewerSafeCell(new Point(x, y));
            }
        }
        return res;
    }
    
    private Cell getViewerSafeCell(Point pos) {
    	Cell c = getCell(pos);
    	if (c instanceof Station) {
    		c = ((Station)c).clone();
    	}
    	return c;
    	
    }
    

    /**
     * Get the cell at a specified location
     * 
     * @param pos coordinates of the cell
     */
    public Cell getCell(Point pos) {
    	if (!map.containsKey(pos)) {
    		// No cell at this position, so make a new one
    		cfactory.generateCell(this, pos);
    		
    	}
       
    	return (Cell)map.get(pos);
    }

    /**
     * Get the current timestep.
     * 
     * @return the current timestep
     */
    public long getTimestep() {
        return timestep;
    }
    /**
     * Increase timestep.
     */
    public void tick() {
        timestep++;
        generateTasks();
    }

    /**
     * Add a cell to the environment.
     * 
     * @param c cell to be added
     */
    public synchronized void putCell(Cell c) {
        map.put(c.getPoint(), c);
    }   
    
    private void generateTasks() {
    	for (Station s: stations) {
    		s.generateTask();
    	}
    }
    
    
}
