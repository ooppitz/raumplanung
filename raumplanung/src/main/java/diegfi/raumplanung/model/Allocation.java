package diegfi.raumplanung.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Stream;

import diegfi.raumplanung.controller.Scheduler;

public class Allocation {

	Room room;
	LocalDate day;

	public Allocation(Room r, LocalDate d) {

		if (!Scheduler.isFree(r, d)) {
			throw new RuntimeException("This date is already booked" + d);
		}
		
		this.room = r;
		this.day = d;

		Scheduler.allocs.add(this);

	}

	/**
	 * Allocates a room for a consecutive range of days
	 * @param r
	 * @param begin
	 * @param endExclusive
	 * @throws RuntimeException when trying to book an unavailable date
	 */
	public Allocation(Room r, LocalDate begin, LocalDate endExclusive) {

		Stream<LocalDate> range = begin.datesUntil(endExclusive);
		Iterator<LocalDate> iter = range.iterator();

		while (iter.hasNext()) {
			LocalDate date = iter.next();
			if (!Scheduler.isFree(r, date)) {
				throw new RuntimeException("This date is already booked" + date);
			}
			new Allocation(r, date);
		}
	}

	public LocalDate getDay() {

		return day;
	}

	public Room getRoom() {

		return room;
	}

}
