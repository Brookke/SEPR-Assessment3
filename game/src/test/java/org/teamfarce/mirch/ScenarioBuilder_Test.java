package org.teamfarce.mirch;

import org.junit.Test;
import org.teamfarce.mirch.entities.Clue;
import org.teamfarce.mirch.map.Room;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests functions in the scenario builder class
 *
 * @author jacobwunwin
 */
public class ScenarioBuilder_Test extends GameTest {

    @Test
    public void distributeCluesGiveRooms() {
        List<Clue> clues = new ArrayList<>();

        clues.add(new Clue("1", "1", "clueSheet.png", 0, 0, false));
        clues.add(new Clue("2", "2", "clueSheet.png", 0, 0, false));
        clues.add(new Clue("3", "3", "clueSheet.png", 0, 0, false));
        clues.add(new Clue("4", "4", "clueSheet.png", 0, 0, false));

        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(0, "testMap.tmx", "Test Room1"));
        rooms.add(new Room(1, "testMap.tmx", "Test Room2"));
        rooms.add(new Room(2, "testMap.tmx", "Test Room3"));
        rooms.add(new Room(3, "testMap.tmx", "Test Room4"));

        ScenarioBuilder.distributeClues(clues, rooms);

        for (Room r : rooms) {
            if (!(r.getClues().size() > 0)) {
                fail("Rooms not being given clues");
            }
        }


    }

    @Test
    public void distributeCluesDiffRooms() {

        List<Clue> clues = new ArrayList<>();
        clues.add(new Clue("1", "1", "clueSheet.png", 0, 0, false));
        clues.add(new Clue("2", "2", "clueSheet.png", 0, 0, false));
        clues.add(new Clue("3", "3", "clueSheet.png", 0, 0, false));
        clues.add(new Clue("4", "4", "clueSheet.png", 0, 0, false));

        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(0, "testMap.tmx", "Test Room1"));
        rooms.add(new Room(1, "testMap.tmx", "Test Room2"));
        rooms.add(new Room(2, "testMap.tmx", "Test Room3"));
        rooms.add(new Room(3, "testMap.tmx", "Test Room4"));

        ScenarioBuilder.distributeClues(clues, rooms);

        for (Room r : rooms) {
            if (r.getClues().size() != 1) {
                fail("Clues being given the same room");
            }
        }

    }


    @Test
    public void generateMotives() {
        ScenarioBuilderDatabase.DataMotive dataMotive = new ScenarioBuilderDatabase.DataMotive();
        dataMotive.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sagittis tincidunt augue, eu dignissim massa maximus at. Praesent egestas, arcu vitae dignissim pharetra, sem lectus luctus metus, ac suscipit nibh massa non lacus. In tortor sem, blandit non odio sed";

        List<Clue> clues = ScenarioBuilder.generateMotive(dataMotive);

        assertEquals(clues.get(0), new Clue("Motive Part 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sagittis tincidunt augue", "clueSheet.png", 0, 0, false));
        assertEquals(clues.get(1), new Clue("Motive Part 2", ", eu dignissim massa maximus at. Praesent egestas, arcu vitae dignissim pharetra, sem le", "clueSheet.png", 0, 0, false));
        assertEquals(clues.get(2), new Clue("Motive Part 3", "ctus luctus metus, ac suscipit nibh massa non lacus. In tortor sem, blandit non odio sed", "clueSheet.png", 0, 0, false));


    }
}
