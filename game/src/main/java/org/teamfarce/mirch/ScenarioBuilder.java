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
        minSuspectCount = 6;
        maxSuspectCount = 6;
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
        ArrayList<ScenarioBuilderDatabase.RoomTemplate> selectedRoomTemplates =
            new ArrayList<ScenarioBuilderDatabase.RoomTemplate>();

        // A list which represents the room types we can still use. This should include the room
        // types maxCount times. They should be removed from the list when a room template is
        // selected from them.
        ArrayList<ScenarioBuilderDatabase.RoomType> selectedRoomTypes =
            new ArrayList<ScenarioBuilderDatabase.RoomType>();

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

        return null;
    }
}
