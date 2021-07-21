package diegfi.raumplanung.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import diegfi.raumplanung.model.Allocation;
import diegfi.raumplanung.model.Room;

public class Scheduler {

	public static ArrayList<Room> rooms = new ArrayList<Room>();
	
	public static ArrayList<Allocation> allocs = new ArrayList<Allocation>();
		
	public static int getFreeCount(LocalDate day) {
		
		int freeCount = rooms.size();
		for(Room r : rooms) {
			
			if (!Scheduler.isFree(r, day)) {
				freeCount--;
			}
		}
		return freeCount;
	}

	public static boolean isFree(Room room, LocalDate day) {
	
		boolean free = true;
		for(Allocation a : allocs) {
			if (a.getDay().equals(day) && a.getRoom() == room) {
				free = false;
				break;
			}
		}
		return free;
	}

	public static void add(Room room) {
		rooms.add(room);
		
	}
}
