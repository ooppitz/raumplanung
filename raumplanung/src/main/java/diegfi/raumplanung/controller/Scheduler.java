package diegfi.raumplanung.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

import diegfi.raumplanung.model.Allocation;
import diegfi.raumplanung.model.Room;

public class Scheduler {

	public static ArrayList<Room> rooms = new ArrayList<Room>();

	public static ArrayList<Allocation> allocs = new ArrayList<Allocation>();

	public static void add(Room room) {
		rooms.add(room);

	}

	public static int getFreeCount(LocalDate day) {

		int freeCount = rooms.size();
		for (Room r : rooms) {

			if (!Scheduler.isFree(r, day)) {
				freeCount--;
			}
		}
		return freeCount;
	}

	public static boolean isFree(Room room, LocalDate day) {

		boolean free = true;
		for (Allocation a : allocs) {
			if (a.getDay().equals(day) && a.getRoom() == room) {
				free = false;
				break;
			}
		}
		return free;
	}

	public static boolean isFree(Room room, LocalDate begin, LocalDate endExclusive) {

		LocalDate start = begin;

		if (begin.equals(endExclusive)) {
			return isFree(room, begin);
		}
		Stream<LocalDate> range = begin.datesUntil(endExclusive);
		Iterator<LocalDate> iter = range.iterator();

		while (iter.hasNext()) {
			LocalDate date = iter.next();
			if (!Scheduler.isFree(room, date)) {
				return false;
			}
		}
		return true;
	}

	public static ArrayList<Room> findFreeRooms(LocalDate d) {
	
		ArrayList<Room> free = new ArrayList<Room>();
		for(Room r : rooms) {
			if (Scheduler.isFree(r, d)) {
				free.add(r);
			}
		}
		return free;
	}

	public static ArrayList<Room> findFreeRooms(LocalDate begin, LocalDate endExclusive) {

		ArrayList<Room> free = (ArrayList<Room>) rooms.clone();

		Iterator iter = rooms.iterator();
		for(Room r: rooms) {
			if (!Scheduler.isFree(r, begin, endExclusive) ) {
				free.remove(r);
			}
		}
		return free;
	}
}
