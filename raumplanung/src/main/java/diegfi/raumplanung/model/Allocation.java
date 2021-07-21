package diegfi.raumplanung.model;

import java.time.LocalDate;
import java.util.Date;

import diegfi.raumplanung.controller.Scheduler;

public class Allocation {

	Room room;
	LocalDate day;
	
	public Allocation(Room room, LocalDate day) {
		
		this.room = room;
		this.day = day;
		
		Scheduler.allocs.add(this);
		
	}

	public LocalDate getDay() {
		
		return day;
	}

	public Room getRoom() {
		
		return room;
	}

}
