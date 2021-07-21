package diegfi.raumplanung.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import diegfi.raumplanung.controller.Scheduler;

class AllocationTest {

	@BeforeEach
	void setUp() throws Exception {
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
	}

}
