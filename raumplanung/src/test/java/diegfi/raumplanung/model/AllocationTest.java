package diegfi.raumplanung.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import diegfi.raumplanung.controller.Scheduler;

class AllocationTest {

	@BeforeEach
	void setUp() throws Exception {
		
		Scheduler.rooms = new ArrayList<Room>();
		Scheduler.allocs = new ArrayList<Allocation>();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFreeCount() {
		
		Room untersberg = new Room("Raum Untersberg");
		Room obersalzberg = new Room("Raum Obersalzberg");
		Allocation a = new Allocation(untersberg, LocalDate.of(2021,7,21));
		
		LocalDate dayOfVisit = LocalDate.of(2021, 8, 2);
		assertEquals(2, Scheduler.getFreeCount(dayOfVisit), "Should be two as rooms start being unscheduled");
		
		dayOfVisit = LocalDate.of(2021, 7, 21);
		assertEquals(1, Scheduler.getFreeCount(dayOfVisit), "Should be one");
		
		assertEquals(2, Scheduler.getFreeCount(LocalDate.of(1900, 1, 1)), "Should be 2 as no room booked in 1900");
		
	}
	
	@Test
	void testCheckFree() {
		
		Room untersberg = new Room("Raum Untersberg");
		LocalDate day = LocalDate.of(2001, 12, 31);
		assertTrue(Scheduler.isFree(untersberg, day), "Should be free as no booking was scheduled");
		Allocation a = new Allocation(untersberg, day);
		assertFalse(Scheduler.isFree(untersberg, day), "Should be taken as booking was made for the same day");
	
	}
	
	@Test 
	void testCheckFreeRange() {
		
		Room untersberg = new Room("Raum Scharitzkehlalm");
		Room watzmann = new Room("Watzmann");
		Room blaueis = new Room("Blaueishütte");
		new Allocation(untersberg, LocalDate.of(2022, 1, 1));
		new Allocation(untersberg, LocalDate.of(2022, 1, 3));
		new Allocation(untersberg, LocalDate.of(2022, 1, 4));
		new Allocation(untersberg, LocalDate.of(2022, 1, 10));
		
		new Allocation(watzmann, LocalDate.of(2022,1,4));
		new Allocation(watzmann, LocalDate.of(2022,1,5));
		
		new Allocation(blaueis, LocalDate.of(2022,1,5));
		
		assertTrue(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 2)), "Should be free as not booked on this day");
		assertTrue(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 5), LocalDate.of(2022, 1,9)));
		assertFalse(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 2), LocalDate.of(2022, 1,7)));
		assertFalse(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 4), LocalDate.of(2022, 1,10)));
		assertTrue(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 5), LocalDate.of(2022, 1,10)));
		assertFalse(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 10), LocalDate.of(2022, 1,10)));
		
	}

	
}
