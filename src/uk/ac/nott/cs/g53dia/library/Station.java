package uk.ac.nott.cs.g53dia.library;

/**
 * A environment cell which contains a water station.
 * 
 * @author Julian  Zappala
 */

/*
 * Copyright (c) 2010 Julian  Zappala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class Station extends DefaultCell {
	
	final static double NEW_TASK_PROBABILITY = 0.001;
	private Task task;
	
	Station(Point pos) {
		super(pos);
	}
	
	protected void generateTask() {
		if (this.task==null) {
			if (Math.random()<NEW_TASK_PROBABILITY) {
				this.task=new Task(this);
			}
		}
	}
	
	public Task getTask() {
		return this.task;
	}
	
	protected void removeTask() {
		this.task = null;
	}
	
	protected Station clone() {
		Station s = new Station(this.getPoint());
		s.task = this.task;
		return s;
	}
	
	public boolean equals(Object o) {
		Station s = (Station)o;
		if (this.getPoint().equals(s.getPoint())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
}
