package org.teamfarce.mirch;

import org.lwjgl.Sys;
import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.Entities.Suspect;
import org.teamfarce.mirch.ScenarioBuilderDatabase.*;
import org.teamfarce.mirch.dialogue.*;
import org.teamfarce.mirch.map.Map;
import org.teamfarce.mirch.map.Room;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class ScenarioBuilder
{

    /**
     * Takes a list of data characters,
     * randomly selects a killer from the list of possible killers.
     * Only generates clues for the possible killers.
     * Randomly selects a victim from the list of possible victims
     * @param dataCharacters
     * @return
     */
    public static CharacterData generateCharacters(HashMap<Integer, DataCharacter> dataCharacters)
    {

        CharacterData data = new CharacterData();

        List<Suspect> posKillers = new ArrayList<>();
        List<Suspect> posVictims = new ArrayList<>();
        dataCharacters.forEach((x,c) -> {
            if (c.posKiller) {
                Suspect tempSuspect = new Suspect(c.name, c.description, c.spritesheet.filename, new Vector2Int(0, 0), null);
                tempSuspect.relatedClues = (convertClues(c.relatedClues));
                posKillers.add(tempSuspect);

            } else {
                posVictims.add(new Suspect(c.name, c.description, c.spritesheet.filename, new Vector2Int(0,0), null));
            }
        });

        Collections.shuffle(posKillers);
        Collections.shuffle(posVictims);

        posKillers.get(0).setKiller();
        posVictims.get(0).setVictim();

        data.murderer = posKillers.get(0);
        data.victim = posVictims.get(0);

        data.allCharacters.addAll(posKillers);
        data.allCharacters.addAll(posVictims);

        return data;

    }

    /**
     * This takes a data motive and splits it up into 3 clues
     * @param dataMotive
     * @return list of the 3 clues
     */
    public static List<Clue> generateMotive(DataMotive dataMotive) {

        final int third = dataMotive.description.length() / 3; //get the middle of the String
        String[] parts = {dataMotive.description.substring(0, third),dataMotive.description.substring(third, 2*third),dataMotive.description.substring(2*third)};

        System.out.println("Motive generated:");
        System.out.println(parts[0]);
        System.out.println(parts[1]);
        System.out.println(parts[2]);


        //TODO: update to the correct sprite if we are using them
        Clue part1 = new Clue("Motive Part 1", parts[0], "Axe.png");
        part1.setMotiveClue();
        Clue part2 = new Clue("Motive Part 2", parts[1], "Axe.png");
        part2.setMotiveClue();
        Clue part3 = new Clue("Motive Part 3", parts[2], "Axe.png");
        part3.setMotiveClue();

        List<Clue> out = Arrays.asList(part1, part2, part3);

        return out;
    }

    /**
     * Takes a hash map of DataClues and generates a list of clues to be used in the game.
     * @param clues
     * @return
     */
    private static List<Clue> convertClues(List<DataClue> clues)
    {
        List<Clue> output = new ArrayList<>();
        for (DataClue c: clues) {
            output.add(new Clue(c.name, c.description, c.sprite));
        }

        return output;
    }



    public static GameSnapshot generateGame(
            ScenarioBuilderDatabase database,
            int suspectCount,
            Set<DataQuestioningStyle> chosenStyles,
            Random random
    ) throws ScenarioBuilderException
    {

        List<Clue> constructedClues = new ArrayList<>();
        List<Room> rooms = Map.initialiseRooms();

        Object[] motives = database.motives.values().toArray();
        DataMotive randomMotive = (DataMotive) motives[random.nextInt(motives.length)];
        constructedClues.addAll(generateMotive(randomMotive));

        CharacterData characterData = generateCharacters(database.characters);
        constructedClues.addAll(characterData.murderer.relatedClues);

        Object[] means = database.means.values().toArray();
        DataClue randomMean = (DataClue) means[random.nextInt(means.length)];
        constructedClues.add(new Clue(randomMean.name, randomMean.description, randomMean.sprite));

        distributeClues(constructedClues, rooms);
        return new GameSnapshot(characterData.allCharacters, constructedClues, rooms, 0, 0);
    }

    public static class ScenarioBuilderException extends Exception
    {
        public ScenarioBuilderException(String message)
        {
            super(message);
        }
    }



    private static class CharacterData
    {
        public Suspect victim = null;
        public Suspect murderer = null;
        public List<Suspect> allCharacters = new ArrayList<>();
    }


    /**
     * Takes a list of clues and rooms and gives each of the clues a random room and location
     * @param clues
     * @param rooms
     */
    public static void distributeClues(List<Clue> clues, List<Room> rooms) {

        Collections.shuffle(clues);
        int amountOfClues = clues.size();
        for (Room room : rooms) {
            if (amountOfClues == 0) return;

            Vector2Int randHidingSpot = room.getRandHidingSpot();

            if (randHidingSpot != null) {
                //Subtract 1 as java counts from 0
                clues.get(amountOfClues - 1).setTileCoordinates(randHidingSpot);
                clues.get(amountOfClues - 1).setRoom(room);
                amountOfClues--;
            }

        }

    }
}
