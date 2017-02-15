package org.teamfarce.mirch;

import org.junit.Test;
import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.map.Room;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;
/**
 * Tests functions in the scenario builder class
 * @author jacobwunwin
 *
 */
public class ScenarioBuilder_Test extends GameTest {

    @Test
    public void distributeCluesGiveRooms()
    {
        List<Clue> clues = new ArrayList<>();
        clues.add(new Clue("1", "1", 0,0, "Axe.png"));
        clues.add(new Clue("2", "2", 0,0, "Axe.png"));
        clues.add(new Clue("3", "3", 0,0, "Axe.png"));
        clues.add(new Clue("4", "4", 0,0, "Axe.png"));

        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(0, "testMap.tmx", "Test Room1"));
        rooms.add(new Room(1, "testMap.tmx", "Test Room2"));
        rooms.add(new Room(2, "testMap.tmx", "Test Room3"));
        rooms.add(new Room(3, "testMap.tmx", "Test Room4"));

        ScenarioBuilder.distributeClues(clues,rooms);

        for (Room r: rooms) {
            if (!(r.getClues().size() > 0)) {
                fail("Rooms not being given clues");
            }
        }


    }

    @Test
    public void distributeCluesDiffRooms() {

        List<Clue> clues = new ArrayList<>();
        clues.add(new Clue("1", "1", 0,0, "Axe.png"));
        clues.add(new Clue("2", "2", 0,0, "Axe.png"));
        clues.add(new Clue("3", "3", 0,0, "Axe.png"));
        clues.add(new Clue("4", "4", 0,0, "Axe.png"));

        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(0, "testMap.tmx", "Test Room1"));
        rooms.add(new Room(1, "testMap.tmx", "Test Room2"));
        rooms.add(new Room(2, "testMap.tmx", "Test Room3"));
        rooms.add(new Room(3, "testMap.tmx", "Test Room4"));

        ScenarioBuilder.distributeClues(clues,rooms);

        for (Room r: rooms) {
            if (r.getClues().size() != 1) {
                fail("Clues being given the same room");
            }
        }

    }

    /**
     * Test a typical use case of the ScenarioBuilder.
     */
	@Test
    public void testGenerateGame() {
        //ScenarioBuilder sb = new ScenarioBuilder();
        //sb.generateGame(10);
        //temporary output until test is fully built
        System.out.println("Generate Game Successful");
    }
    
    /*
    public void testChooseRoomTemplate(){
    	ScenarioBuilder sb = new ScenarioBuilder();
    	sb.chooseRoomTemplate(ScenarioBuilderDatabase, 8, 8, random);
    	System.out.println("Choose Room Template Successful");
    }
    */
}
