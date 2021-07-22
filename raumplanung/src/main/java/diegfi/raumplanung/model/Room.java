package diegfi.raumplanung.model;

import java.util.Date;

import diegfi.raumplanung.controller.Scheduler;

public class Room {
	
	String name;
	
	int seatsForAudience;
		
	public int getSeatsForAudience() {
		return seatsForAudience;
	}

	public Room(String name) {
		
		this.name = name;
		Scheduler.add(this);
	}
	
	public Room(String name, int audienceCount) {
		
		this(name);
		this.seatsForAudience = audienceCount;
		
	}
}
