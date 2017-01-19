package org.teamfarce.mirch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Collection;
import java.util.function.ToIntFunction;
import java.util.Random;
import java.sql.SQLException;
import org.teamfarce.mirch.ScenarioBuilderDatabase;

public class ScenarioBuilder {
    public class ScenarioBuilderException extends Exception {
        public ScenarioBuilderException(String message) {
            super(message);
        }
    }

    private int minRoomCount;
    private int maxRoomCount;
    private int minSuspectCount;
    private int maxSuspectCount;
    private Random random;

    ScenarioBuilder() {
        minRoomCount = 8;
        maxRoomCount = 8;
        minSuspectCount = 5;
        maxSuspectCount = 5;
        random = new Random();
    }

    public <T> T selectWeightedObject(Collection<T> objects, ToIntFunction<T> weightFunction)
        throws ScenarioBuilderException
    {
        int selectionWeightSum = objects
            .stream()
            .mapToInt(weightFunction)
            .sum();
        int selection = random.nextInt(selectionWeightSum);

        for (T object: objects) {
            selection -= weightFunction.applyAsInt(object);
            if (selection < 0) {
                return object;
            }
        }

        throw new ScenarioBuilderException("Could not select an object");
    }

    public GameSnapshot generateGame() throws SQLException, ScenarioBuilderException {
        ScenarioBuilderDatabase database = new ScenarioBuilderDatabase("database.db");

        int targetRoomCount = minRoomCount + random.nextInt(maxRoomCount - minRoomCount + 1);

        // The room templates we have selected.
        ArrayList<ScenarioBuilderDatabase.RoomTemplate> selectedRoomTemplates = new ArrayList<>();

        // A list which represents the room types we can still use. This should include the room
        // types maxCount times. They should be removed from the list when a room template is
        // selected from them.
        ArrayList<ScenarioBuilderDatabase.RoomType> selectedRoomTypes = new ArrayList<>();

        for (ScenarioBuilderDatabase.RoomType roomType: database.roomTypes.values()) {
            // Include the room types which are required. These are the room types which have a
            // minimum count larger than one.
            for (int i = 0; i < roomType.minCount; ++i) {
                selectedRoomTemplates.add(selectWeightedObject(
                    roomType.roomTemplates, x -> x.selectionWeight
                ));
            }

            // Since we have included room templates of the current type minCount times, we can
            // only include it maxCount - minCount more times.
            for (int i = 0; i < roomType.maxCount - roomType.minCount; ++i) {
                selectedRoomTypes.add(roomType);
            }
        }

        // First check that the minimum room requirements hasn't caused us to overrun our quota.
        if (selectedRoomTemplates.size() > maxRoomCount) {
            throw new ScenarioBuilderException("Could not fulfil maximum count value");
        }

        // Now check that we the maximum count constraint will allow us to fulfil our minimum room
        // count.
        if (selectedRoomTemplates.size() + selectedRoomTypes.size() < minRoomCount) {
            throw new ScenarioBuilderException("Could not fulfil minimum count value");
        }

        // Shuffle our list to get a unique combination of room types.
        Collections.shuffle(selectedRoomTypes, random);

        // Keep selecting room templates whilst we haven't reached our selected room count target.
        // Abandon this target if we run out of room types.
        while (selectedRoomTemplates.size() < targetRoomCount && selectedRoomTypes.size() > 0) {
            // Add the room template by selecting one from the room type "popped" off the selected
            // room types array.

            selectedRoomTemplates.add(selectWeightedObject(
                selectedRoomTypes.remove(0).roomTemplates, x -> x.selectionWeight
            ));
        }

        // Select a character motive link to use.
        ScenarioBuilderDatabase.CharacterMotiveLink selectedCharacterMotiveLink =
            selectWeightedObject(database.characterMotiveLinks.values(), x -> x.selectionWeight);

        // Extract the murderer, victim and motive.
        ScenarioBuilderDatabase.Character selectedMurderer = selectedCharacterMotiveLink.murderer;
        ScenarioBuilderDatabase.Character selectedVictim = selectedCharacterMotiveLink.victim;
        ScenarioBuilderDatabase.Motive selectedMotive = selectedCharacterMotiveLink.motive;

        // Create a list of suspect which we can choose from to construct our suspect list. This
        // includes all of the suspects from our data minus the murderer and victim.
        //
        // This next statement casts from `Object` to
        // `ArrayList<ScenarioBuilderDatabase.Character>` because the result of clone is object.
        // Because we know database.characters to be of this type we safely ignore this warning.
        @SuppressWarnings("unchecked")
        ArrayList<ScenarioBuilderDatabase.Character> potentialSuspects =
            (ArrayList<ScenarioBuilderDatabase.Character>)database.characters.clone();
        potentialSuspects.remove(selectedMurderer);
        potentialSuspects.remove(selectedVictim);

        int targetSuspectCount =
            minSuspectCount + random.nextInt(maxSuspectCount - minSuspectCount + 1);

        ArrayList<ScenarioBuilderDatabase.Character> selectedSuspects = new ArrayList<>();

        while (selectedSuspects.size() < targetSuspectCount && potentialSuspects.size() > 0) {
            ScenarioBuilderDatabase.Character selectedSuspect =
                selectWeightedObject(potentialSuspects, x -> x.selectionWeight);
            potentialSuspects.remove(potentialSuspects);
        }

        return null;
    }
}
