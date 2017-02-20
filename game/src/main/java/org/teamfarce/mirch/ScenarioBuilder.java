package org.teamfarce.mirch;

import org.teamfarce.mirch.ScenarioBuilderDatabase.DataCharacter;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataClue;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataMotive;
import org.teamfarce.mirch.dialogue.Dialogue;
import org.teamfarce.mirch.entities.Clue;
import org.teamfarce.mirch.entities.Suspect;
import org.teamfarce.mirch.map.Map;
import org.teamfarce.mirch.map.Room;

import java.util.*;

public class ScenarioBuilder {

    /**
     * Takes a list of data characters,
     * randomly selects a killer from the list of possible killers.
     * Only generates clues for the possible killers.
     * Randomly selects a victim from the list of possible victims
     *
     * @param dataCharacters
     * @return
     */
    public static CharacterData generateCharacters(MIRCH game, HashMap<Integer, DataCharacter> dataCharacters) throws ScenarioBuilderException {
        CharacterData data = new CharacterData();

        List<Suspect> posKillers = new ArrayList<>();
        List<Suspect> posVictims = new ArrayList<>();
        dataCharacters.forEach((x, c) -> {
            Dialogue dialogue = null;
            try {
                dialogue = new Dialogue(c.dialogue.filename, false);
            } catch (Dialogue.InvalidDialogueException e) {
                e.printStackTrace();
                System.exit(0);
            }
            if (c.posKiller) {
                Suspect tempSuspect = new Suspect(game, c.name, c.description, c.spritesheet.filename, new Vector2Int(0, 0), dialogue);
                tempSuspect.relatedClues = (convertClues(c.relatedClues));
                posKillers.add(tempSuspect);

            } else {
                posVictims.add(new Suspect(game, c.name, c.description, c.spritesheet.filename, new Vector2Int(0, 0), dialogue));
            }
        });

        Collections.shuffle(posKillers);
        Collections.shuffle(posVictims);

        posKillers.get(0).setKiller();

        posVictims.get(0).setVictim();

        data.murderer = posKillers.get(0);
        data.victim = posVictims.get(0);

        if (data.murderer.relatedClues == null) {
            ScenarioBuilderException e = new ScenarioBuilderException("no clues related to the murderer");
            throw e;
        }

        data.allCharacters.addAll(posKillers);
        data.allCharacters.addAll(posVictims);

        return data;
    }

    /**
     * This takes a data motive and splits it up into 3 clues
     *
     * @param dataMotive
     * @return list of the 3 clues
     */
    public static List<Clue> generateMotive(DataMotive dataMotive) {

        final int third = dataMotive.description.length() / 3; //get the middle of the String
        String[] parts = {dataMotive.description.substring(0, third), dataMotive.description.substring(third, 2 * third), dataMotive.description.substring(2 * third)};
        System.out.println("Motive generated:");
        System.out.println(parts[0]);
        System.out.println(parts[1]);
        System.out.println(parts[2]);

        Clue part1 = new Clue("Motive Part 1", parts[0], "clueSheet.png", 1, 2, false);
        part1.setMotiveClue();
        Clue part2 = new Clue("Motive Part 2", parts[1], "clueSheet.png", 1, 2, false);
        part2.setMotiveClue();
        Clue part3 = new Clue("Motive Part 3", parts[2], "clueSheet.png", 1, 2, false);
        part3.setMotiveClue();

        List<Clue> out = Arrays.asList(part1, part2, part3);

        return out;
    }

    /**
     * Takes list of DataClues and generates a list of clues to be used in the game.
     *
     * @param clues
     * @return
     */
    private static List<Clue> convertClues(List<DataClue> clues) {
        if (clues == null) {
            return null;
        }
        List<Clue> output = new ArrayList<>();
        for (DataClue c : clues) {
            output.add(new Clue(c.name, c.description, c.sprite, c.assetX, c.assetY, c.isMeans));
        }

        return output;
    }

    /**
     * This method takes the database and build a GameSnapshot from the database
     *
     * @param game     MIRCH - Reference to main game class
     * @param database - The database of which to get info from
     * @param random   - Random, used to generate a random means clue, and a random motive
     * @return GameSnapshot - The generate gamesnapshot
     * @throws ScenarioBuilderException
     */
    public static GameSnapshot generateGame(MIRCH game, ScenarioBuilderDatabase database, Random random) throws ScenarioBuilderException {

        List<Clue> constructedClues = new ArrayList<>();

        Object[] motives = database.motives.values().toArray();
        DataMotive randomMotive = (DataMotive) motives[random.nextInt(motives.length)];
        constructedClues.addAll(generateMotive(randomMotive));

        Map map = new Map(game);

        List<Room> rooms = map.initialiseRooms();

        CharacterData characterData;
        characterData = generateCharacters(game, database.characters);

        Suspect victim = characterData.victim;
        Suspect murderer = characterData.murderer;

        List<Suspect> aliveSuspects = new ArrayList<Suspect>();
        for (Suspect suspect : characterData.allCharacters) {
            if (!suspect.isVictim()) {
                aliveSuspects.add(suspect);
            }
        }

        constructedClues.addAll(characterData.murderer.relatedClues);

        Object[] means = database.means.values().toArray();
        DataClue randomMean = (DataClue) means[random.nextInt(means.length)];
        Clue meansClue = new Clue(randomMean.name, randomMean.description, randomMean.sprite, randomMean.assetX, randomMean.assetY, randomMean.isMeans);
        constructedClues.add(meansClue);

        distributeClues(constructedClues, rooms);
        GameSnapshot snapshot = new GameSnapshot(game, map, rooms, aliveSuspects, constructedClues);
        snapshot.victim = victim;
        snapshot.murderer = murderer;
        snapshot.meansClue = meansClue;
        return snapshot;
    }

    /**
     * Takes a list of clues and rooms and gives each of the clues a random room and location
     *
     * @param clues
     * @param rooms
     */
    public static void distributeClues(List<Clue> clues, List<Room> rooms) {

        Collections.shuffle(clues);
        int amountOfClues = clues.size();

        List<Room> loopRooms = new ArrayList<Room>();
        loopRooms.addAll(rooms);
        Collections.shuffle(loopRooms);

        System.out.println("There are " + amountOfClues + " clues this game");

        for (int i = 0; i < amountOfClues; i++) {
            if (amountOfClues == 0) return;

            if (loopRooms.isEmpty()) {
                loopRooms.addAll(rooms);
                Collections.shuffle(loopRooms);
            }

            Vector2Int randHidingSpot = loopRooms.get(0).getRandHidingSpot();

            if (randHidingSpot != null) {
                clues.get(i).setTileCoordinates(randHidingSpot);
                loopRooms.get(0).addClue(clues.get(i));
                loopRooms.remove(0);
            }

        }

    }

    public static class ScenarioBuilderException extends Exception {
        public ScenarioBuilderException(String message) {
            super(message);
        }
    }


    private static class CharacterData {
        public Suspect victim = null;
        public Suspect murderer = null;
        public List<Suspect> allCharacters = new ArrayList<>();
    }
}
