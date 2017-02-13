package org.teamfarce.mirch;

import org.junit.Test;
import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.map.Room;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
/**
 * Tests functions in the scenario builder class
 * @author jacobwunwin
 *
 */
public class ScenarioBuilderTest extends GameTest {

    @Test
    public void distributeCluesGiveRooms()
    {
        List<Clue> clues = new ArrayList<>();
        clues.add(new Clue("1", "1", "Axe.png"));
        clues.add(new Clue("2", "2", "Axe.png"));
        clues.add(new Clue("3", "3", "Axe.png"));
        clues.add(new Clue("4", "4", "Axe.png"));

        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(0, "testMap.tmx", "Test Room1"));
        rooms.add(new Room(1, "testMap.tmx", "Test Room2"));
        rooms.add(new Room(2, "testMap.tmx", "Test Room3"));
        rooms.add(new Room(3, "testMap.tmx", "Test Room4"));

        ScenarioBuilder.distributeClues(clues,rooms);

        for (Clue c: clues) {
            if (!rooms.contains(c.getRoom())) {
                fail("Clues not being assigned a room");
            }
        }


    }

    @Test
    public void distributeCluesDiffRooms() {

        List<Clue> clues = new ArrayList<>();
        clues.add(new Clue("1", "1", "Axe.png"));
        clues.add(new Clue("2", "2", "Axe.png"));
        clues.add(new Clue("3", "3", "Axe.png"));
        clues.add(new Clue("4", "4", "Axe.png"));

        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(0, "testMap.tmx", "Test Room1"));
        rooms.add(new Room(1, "testMap.tmx", "Test Room2"));
        rooms.add(new Room(2, "testMap.tmx", "Test Room3"));
        rooms.add(new Room(3, "testMap.tmx", "Test Room4"));

        ScenarioBuilder.distributeClues(clues,rooms);

        List<Room> tempRooms = new ArrayList<>();
        for (Clue c: clues) {
            if (tempRooms.contains(c.getRoom())) {
                fail("Clues being given the same room");
            } else {
                tempRooms.add(c.getRoom());
            }
        }

    }


    @Test
    public void testGenerateMotives() {
        ScenarioBuilderDatabase.DataMotive dataMotive = new ScenarioBuilderDatabase.DataMotive();
        dataMotive.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sagittis tincidunt augue, eu dignissim massa maximus at. Praesent egestas, arcu vitae dignissim pharetra, sem lectus luctus metus, ac suscipit nibh massa non lacus. In tortor sem, blandit non odio sed";

        List<Clue> clues = ScenarioBuilder.generateMotive(dataMotive);

        assertEquals(clues.get(0), new Clue("Motive Part 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sagittis tincidunt augue", "Axe.png"));
        assertEquals(clues.get(1), new Clue("Motive Part 2", ", eu dignissim massa maximus at. Praesent egestas, arcu vitae dignissim pharetra, sem le", "Axe.png"));
        assertEquals(clues.get(2), new Clue("Motive Part 3", "ctus luctus metus, ac suscipit nibh massa non lacus. In tortor sem, blandit non odio sed", "Axe.png"));


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
