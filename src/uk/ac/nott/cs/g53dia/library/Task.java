package uk.ac.nott.cs.g53dia.library;

/**
 * A environment cell which contains an task
 *
 * @author Julian Zappala
 */

/*
 * Copyright (c) 2011 Julian  Zappala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class Task {
	/**
	 * The maximum size of a demand for water
	 */
	
	public static final int MAX_DEMAND = Tanker.MAX_WATER;
	
	Station station;
	int demand;
	int supplied;
	boolean accepted;
	boolean completed;
		
	Task(Station s) {
		this.station = s;
		accepted = false;
		completed = false;
		demand = (int)(Math.random() * MAX_DEMAND)+1;
		supplied = 0;
	}

	/**
	 * Get the position of the station to which water should be delivered
	 * 
	 */
	
	public Point getStationPosition() {
		return station.getPoint();
	}
	
	/**
	 * Get the amount of water to be delivered to the station
	 * 
	 */
	
	public int getWaterDemand() {
		return demand;
	}
	
	/**
	 * Is this task completed?
	 * 
	 */
	
	public boolean isComplete() {
		return supplied>=demand;
	}
	
	/**
	 * How much water is required to complete the task?
	 * 
	 */
	public int getRequired() {
		return demand-supplied;
	}
	
	
	protected void setWaterDemand(int d) {
		this.demand = d;
	}
	
	
	protected void accept() {
		this.accepted = true;
	}
	
	protected void supply(int s) {
		supplied += s;
		if (isComplete()) {
			this.station.removeTask();
		}
	}
	
	
}
