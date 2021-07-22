package diegfi.raumplanung.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import diegfi.raumplanung.model.Room;

class SchedulerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	
	@Disabled("Until Room::Romm() constructor is fixed to not add the room to the scheduler")
	@Test
	void testGetRooms() {
		
		ArrayList<Room> rooms = Scheduler.getRooms();
		assertTrue(rooms.size() == 0, "Should be zero as no rooms defined");
			
		new Room("Raum Obersalzberg");
		assertTrue(rooms.size() == 1, "Should be one as oneo rooms was defined");
		 
	}

}
