package diegfi.raumplanung.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
		Allocation a = new Allocation(untersberg, LocalDate.of(2021, 7, 21));

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

		new Allocation(watzmann, LocalDate.of(2022, 1, 4));
		new Allocation(watzmann, LocalDate.of(2022, 1, 5));

		new Allocation(blaueis, LocalDate.of(2022, 1, 5));

		assertTrue(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 2)), "Should be free as not booked on this day");
		assertTrue(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 5), LocalDate.of(2022, 1, 9)));
		assertFalse(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 2), LocalDate.of(2022, 1, 7)));
		assertFalse(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 4), LocalDate.of(2022, 1, 10)));
		assertTrue(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 5), LocalDate.of(2022, 1, 10)));
		assertFalse(Scheduler.isFree(untersberg, LocalDate.of(2022, 1, 10), LocalDate.of(2022, 1, 10)));

	}

	@Test
	void testAllocateRange() {

		Room kehlstein = new Room("Raum Kehlstein");
		Room watzmann = new Room("Raum Watzmann");
		new Allocation(kehlstein, LocalDate.of(2022, 1, 10), LocalDate.of(2022, 1, 15));
		assertFalse(Scheduler.isFree(kehlstein, LocalDate.of(2022, 1, 12)));
		assertFalse(Scheduler.isFree(kehlstein, LocalDate.of(2022, 1, 13), LocalDate.of(2022, 1, 16)));
		assertTrue(Scheduler.isFree(kehlstein, LocalDate.of(2022, 1, 1)));
		assertTrue(Scheduler.isFree(watzmann, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 31)));

		new Allocation(watzmann, LocalDate.of(2022, 1, 10));
		assertFalse(Scheduler.isFree(watzmann, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 31)));
		assertTrue(Scheduler.isFree(watzmann, LocalDate.of(2022, 1, 31), LocalDate.of(2022, 2, 5)));

	}

	@Test
	void testFindFreeRoom() {

		// ...........123456789A
		// Untersberg.x.xx.x
		// Watzmann......xx
		// Blaueishütte...x

		Room untersberg = bookUntersberg();
		Room watzmann = bookWatzmann();
		Room blaueis = bookBlaueis();

		ArrayList<Room> free = Scheduler.findFreeRooms(LocalDate.of(2022, 1, 2));
		assertEquals(3, free.size(), "Should be three free rooms");

		ArrayList<Room> freeInRange = Scheduler.findFreeRooms(LocalDate.of(2022, 1, 7), LocalDate.of(2022, 1, 10));
		assertEquals(2, freeInRange.size(), "Should be two free rooms");
		assertTrue(freeInRange.contains(watzmann) && freeInRange.contains(blaueis) && !freeInRange.contains(untersberg),
				"Should contain only Watzmann and Blaueishütte");

		freeInRange = Scheduler.findFreeRooms(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10));
		assertEquals(0, freeInRange.size(), "Should not have any free rooms");

		freeInRange = Scheduler.findFreeRooms(LocalDate.of(2022, 1, 5), LocalDate.of(2022, 1, 6));
		assertEquals(1, freeInRange.size(), "Should have only Untersberg free");
		assertTrue(freeInRange.contains(untersberg), "Should contain only Untersberg");
	}
	
	@Test
	void testFindFreeRoomsWithMinAudience() {
		
		//                  123456789A
		// Untersberg(10)   x xx    x      
		// Watzmann  (3)       xx
		// Blaueishütte(20)     x
		Room untersberg = bookUntersberg(); // 10
		Room watzmann = bookWatzmann();     // 3
		Room blaueis = bookBlaueis();       // 20
		
		ArrayList<Room> free = Scheduler.findFreeRoomsForAudienceOf(5, LocalDate.of(2022, 1, 3), LocalDate.of(2022, 1, 5));
		assertEquals(1, free.size(), "Should be ony one room, Blaueis");
		assertTrue(free.contains(blaueis), "Should contain only Blaueis");

		ArrayList<Room> freeHuge = Scheduler.findFreeRoomsForAudienceOf(100, LocalDate.of(2022, 1, 3), LocalDate.of(2022, 1, 5));
		assertEquals(0, freeHuge.size(), "Should be empty");
		
		freeHuge = Scheduler.findFreeRoomsForAudienceOf(100, LocalDate.of(2022, 1, 5), LocalDate.of(2022, 1, 8));
		assertEquals(0, freeHuge.size(), "Should be empty");
	}

	private Room bookUntersberg() {
		Room untersberg = new Room("Raum Untersberg", 10);
		new Allocation(untersberg, LocalDate.of(2022, 1, 1));
		new Allocation(untersberg, LocalDate.of(2022, 1, 3));
		new Allocation(untersberg, LocalDate.of(2022, 1, 4));
		new Allocation(untersberg, LocalDate.of(2022, 1, 9));
		return untersberg;
	}

	private Room bookWatzmann() {
		Room watzmann = new Room("Watzmann", 3);
		new Allocation(watzmann, LocalDate.of(2022, 1, 4));
		new Allocation(watzmann, LocalDate.of(2022, 1, 5));
		return watzmann;
	}

	private Room bookBlaueis() {
		Room blaueis = new Room("Blaueishütte", 20);
		new Allocation(blaueis, LocalDate.of(2022, 1, 5));
		return blaueis;
	}

	@Test
	void testDoubleBooking() {
		Room untersberg = new Room("Blaueishütte");
		new Allocation(untersberg, LocalDate.of(2022, 1, 1));
		Assertions.assertThrows(RuntimeException.class, () -> new Allocation(untersberg, LocalDate.of(2022, 1, 1)), "Should collide with 2022/Jan/1st");
		new Allocation(untersberg, LocalDate.of(2022, 1, 5));
		Assertions.assertThrows(RuntimeException.class,
				() -> new Allocation(untersberg, LocalDate.of(2022, 1, 3), LocalDate.of(2022, 1, 8)), "Should collide with 2022/Jan/5th");
	}

	@Test
	void testReadAllocations() {

		//                  123456789A
		// Untersberg       x xx    x
		// Watzmann            xx
		// Blaueishütte         x

		Room untersberg = bookUntersberg();
		Room watzmann = bookWatzmann();
		Room blaueis = bookBlaueis();

		ArrayList<Integer> allocatedU = Scheduler.getAllocation(untersberg, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10));
		Integer[] u = {1,0,1,1,0,0,0,0,1};
		ArrayList<Integer> bookedUntersberg = new ArrayList<Integer>(Arrays.asList(u));
		
		ArrayList<Integer> allocatedW = Scheduler.getAllocation(watzmann, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10));
		Integer[] w = {0,0,0,1,1,0,0,0,0};
		ArrayList<Integer> bookedWatzmann = new ArrayList<Integer>(Arrays.asList(w));
		
		ArrayList<Integer> allocatedB = Scheduler.getAllocation(blaueis, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10));
		Integer[] b = {0,0,0,0,1,0,0,0,0};
		ArrayList<Integer> bookedBlaueis = new ArrayList<Integer>(Arrays.asList(b));
		
		assertTrue(bookedUntersberg.equals(allocatedU), "Booking patterns should match");
		assertTrue(bookedWatzmann.equals(allocatedW), "Booking patterns should match");
		assertTrue(bookedBlaueis.equals(allocatedB), "Booking patterns should match");
		
		
	}

}
