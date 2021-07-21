package diegfi.raumplanung.model;

import java.util.Date;

import diegfi.raumplanung.controller.Scheduler;

public class Room {
	
	String name;
		
	public Room(String name) {
		
		this.name = name;
		Scheduler.add(this);
	}
}
