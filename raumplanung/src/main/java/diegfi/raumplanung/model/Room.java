package diegfi.raumplanung.model;

import java.util.Date;

import diegfi.raumplanung.controller.Scheduler;

public class Room {
	
	String name;
	boolean booked;
	
	public Room(String name) {
		
		this.name = name;
		this.booked = false;
		
		Scheduler.add(this);
	}
}
