package org.teamfarce.mirch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import org.teamfarce.mirch.ScenarioBuilderDatabase;
import org.teamfarce.mirch.WeightedSelection;
import org.teamfarce.mirch.ScenarioBuilderDatabase.QuestioningStyle;
import org.teamfarce.mirch.ScenarioBuilderDatabase.RoomTemplate;
import org.teamfarce.mirch.ScenarioBuilderDatabase.RoomType;
import org.teamfarce.mirch.ScenarioBuilderDatabase.CharacterMotiveLink;
import org.teamfarce.mirch.ScenarioBuilderDatabase.Means;
import org.teamfarce.mirch.ScenarioBuilderDatabase.Motive;
import org.teamfarce.mirch.ScenarioBuilderDatabase.Clue;
import org.teamfarce.mirch.ScenarioBuilderDatabase.QuestioningIntention;
import org.teamfarce.mirch.ScenarioBuilderDatabase.QuestionAndResponse;
import org.teamfarce.mirch.Room;

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
    private WeightedSelection selector;
    private ScenarioBuilderDatabase database;
    private HashSet<QuestioningStyle> chosenStyles;

    /**
     * Constructs a new scenario builder with some default values set.
     */
    public ScenarioBuilder() {
        minRoomCount = 8;
        maxRoomCount = 8;
        minSuspectCount = 5;
        maxSuspectCount = 5;
        random = new Random();
        selector = new WeightedSelection(random);
        database = null;
        chosenStyles = new HashSet<>();
    }

    /**
     * Sets the minimum room count.
     *
     * @param newValue The value to set the minimum room count to.
     * @return This to allow chaining.
     */
    public ScenarioBuilder setMinRoomCount(int newValue) {
        minRoomCount = newValue;
        if (minRoomCount < 1) {
            minRoomCount = 1;
        }
        if (maxRoomCount < minRoomCount) {
            maxRoomCount = minRoomCount;
        }
        return this;
    }

    /**
     * Sets the maximum room count.
     *
     * @param newValue The value to set the maximum room count to.
     * @return This to allow chaining.
     */
    public ScenarioBuilder setMaxRoomCount(int newValue) {
        maxRoomCount = newValue;
        if (maxRoomCount < 1) {
            maxRoomCount = 1;
        }
        if (minRoomCount > maxRoomCount) {
            minRoomCount = maxRoomCount;
        }
        return this;
    }

    /**
     * Sets the minimum suspect count.
     *
     * @param newValue The value to set the minimum suspect count to.
     * @return This to allow chaining.
     */
    public ScenarioBuilder setMinSuspectCount(int newValue) {
        minSuspectCount = newValue;
        if (minSuspectCount < 1) {
            minSuspectCount = 1;
        }
        if (maxSuspectCount < minSuspectCount) {
            maxSuspectCount = minSuspectCount;
        }
        return this;
    }

    /**
     * Sets the minimum suspect count.
     *
     * @param newValue The value to set the suspect maximum count to.
     * @return This to allow chaining.
     */
    public ScenarioBuilder setMaxSuspectCount(int newValue) {
        maxSuspectCount = newValue;
        if (maxSuspectCount < 1) {
            maxSuspectCount = 1;
        }
        if (minSuspectCount > maxSuspectCount) {
            minSuspectCount = maxSuspectCount;
        }
        return this;
    }

    /**
     * Sets the random object.
     *
     * @param newRandom The random object to set.
     * @return This to allow chaining.
     */
    public ScenarioBuilder setRandomObject(Random newRandom) {
        random = newRandom;
        selector = new WeightedSelection(random);
        return this;
    }

    /**
     * Sets the database.
     *
     * @param newDatabase The database to set.
     * @return This to allow chaining.
     */
    public ScenarioBuilder setData(ScenarioBuilderDatabase newDatabase) {
        database = newDatabase;
        return this;
    }

    /**
     * Adds a questioning style to the current set of styles to use.
     *
     * @param style The style to add.
     * @return This to allow chaining.
     */
    public ScenarioBuilder addStyle(QuestioningStyle style) {
        chosenStyles.add(style);
        return this;
    }

    /**
     * Adds the questioning styles in the collection to the current set of styles to use.
     *
     * @param styles The styles to add.
     * @return This to allow chaining.
     */
    public ScenarioBuilder addStyles(Collection<QuestioningStyle> styles) {
        chosenStyles.addAll(styles);
        return this;
    }

    /**
     * Replaces the current questioning styles with the ones in the collection.
     *
     * @param styles The styles to set.
     * @return This to allow chaining.
     */
    public ScenarioBuilder setStyles(Collection<QuestioningStyle> styles) {
        chosenStyles = new HashSet<>(styles);
        return this;
    }

    public GameSnapshot generateGame() throws ScenarioBuilderException {
        int targetRoomCount = minRoomCount + random.nextInt(maxRoomCount - minRoomCount + 1);

        // The room templates we have selected.
        ArrayList<RoomTemplate> selectedRoomTemplates = new ArrayList<>();

        // A list which represents the room types we can still use. This should include the room
        // types maxCount times. They should be removed from the list when a room template is
        // selected from them.
        ArrayList<RoomType> selectedRoomTypes = new ArrayList<>();

        for (RoomType roomType: database.roomTypes.values()) {
            // Include the room types which are required. These are the room types which have a
            // minimum count larger than one.
            for (int i = 0; i < roomType.minCount; ++i) {
                selectedRoomTemplates.add(selector.selectWeightedObject(
                    roomType.roomTemplates, x -> x.selectionWeight
                ).get());
            }

            // Since we have included room templates of the current type minCount times, we can
            // only include it maxCount - minCount more times.
            for (int i = 0; i < roomType.maxCount - roomType.minCount; ++i) {
                selectedRoomTypes.add(roomType);
            }
        }

        // First check that the minimum room requirements hasn't caused us to overrun our quota.
        if (selectedRoomTemplates.size() > maxRoomCount) {
            throw new ScenarioBuilderException("Could not fulfil maximum room count");
        }

        // Now check that we the maximum count constraint will allow us to fulfil our minimum room
        // count.
        if (selectedRoomTemplates.size() + selectedRoomTypes.size() < minRoomCount) {
            throw new ScenarioBuilderException("Could not fulfil minimum room count");
        }

        // Shuffle our list to get a unique combination of room types.
        Collections.shuffle(selectedRoomTypes, random);

        // Keep selecting room templates whilst we haven't reached our selected room count target.
        // Abandon this target if we run out of room types.
        while (selectedRoomTemplates.size() < targetRoomCount && selectedRoomTypes.size() > 0) {
            // Add the room template by selecting one from the room type "popped" off the selected
            // room types array.

            selectedRoomTemplates.add(selector.selectWeightedObject(
                selectedRoomTypes.remove(0).roomTemplates, x -> x.selectionWeight
            ).get());
        }

        // Select a character motive link to use.
        CharacterMotiveLink selectedCharacterMotiveLink = selector.selectWeightedObject(
            database.characterMotiveLinks.values(), x -> x.selectionWeight
        ).get();

        // Extract the murderer, victim and motive.
        ScenarioBuilderDatabase.Character selectedMurderer = selectedCharacterMotiveLink.murderer;
        ScenarioBuilderDatabase.Character selectedVictim = selectedCharacterMotiveLink.victim;
        Motive selectedMotive = selectedCharacterMotiveLink.motive;

        // Create a list of suspect which we can choose from to construct our suspect list. This
        // includes all of the suspects from our data minus the murderer and victim.
        ArrayList<ScenarioBuilderDatabase.Character> potentialSuspects =
            new ArrayList<>(database.characters.values());
        potentialSuspects.remove(selectedMurderer);
        potentialSuspects.remove(selectedVictim);

        int targetSuspectCount =
            minSuspectCount + random.nextInt(maxSuspectCount - minSuspectCount + 1);

        ArrayList<ScenarioBuilderDatabase.Character> selectedSuspects = new ArrayList<>();

        // Keep randomly adding suspects whilst we haven't reached our target and we still have
        // some characters to consider.
        while (selectedSuspects.size() < targetSuspectCount && potentialSuspects.size() > 0) {
            ScenarioBuilderDatabase.Character selectedSuspect = selector.selectWeightedObject(
                potentialSuspects,
                x -> x.selectionWeight,
                x -> potentialSuspects.remove(x)
            ).get();
        }

        // If we haven't got more suspects than the minimum count, the data in the database was
        // not sufficient to fulfil this requirement.
        if (selectedSuspects.size() < minSuspectCount) {
            throw new ScenarioBuilderException("Could not minimum suspect count");
        }

        // The murderer is a suspect as well.
        selectedSuspects.add(selectedMurderer);

        // Get our means.
        Means selectedMeans = selector
                .selectWeightedObject(selectedMurderer.meansLink, x -> x.selectionWeight)
                .get()
                .means;

        HashSet<Clue> selectedClues = new HashSet<>();

        // Get the clues. This is done by filtering out the clues of the selected motive/means
        // which required the victim/murderer to be different.
        List<Clue> meansClues = selectedMeans
            .clues
            .stream()
            .filter(c -> selectedMurderer.requiredAsMurderer.contains(c))
            .filter(c -> selectedVictim.requiredAsVictim.contains(c))
            .collect(Collectors.toList());
        selectedClues.addAll(meansClues);

        List<Clue> motiveClues = selectedMotive
            .clues
            .stream()
            .filter(c -> selectedMurderer.requiredAsMurderer.contains(c))
            .filter(c -> selectedVictim.requiredAsVictim.contains(c))
            .collect(Collectors.toList());
        selectedClues.addAll(motiveClues);

        // Build up our map of dialogue tree roots. This will be the question intentions which are
        // marked as being starting questions.
        HashMap<ScenarioBuilderDatabase.Character, ArrayList<QuestioningIntention>>
            dialgoueTreeRoots = new HashMap<>();
        for (ScenarioBuilderDatabase.Character character: selectedSuspects) {
            dialgoueTreeRoots.put(character, new ArrayList<>());
        }

        // We'll want to consider all questioning intentions which are marked as being starting
        // questions.
        for (QuestioningIntention intention: database.questioningIntentions.values()) {
            if (!intention.startingQuestion) { continue; }
            // Next consider all of the questions associated response. We'll add the intention we
            // are currently considering to any character's dialogue tree root collection, if the
            // question and response we are considering is said by such character.
            for (QuestionAndResponse response: intention.questions) {
                for (ScenarioBuilderDatabase.Character character: response.saidBy) {
                    // We only need to consider characters which we have selected to be in this
                    // case of the game. The hashmap has previously be populated with the selected
                    // characters mapped to empty arrays. Because of this, if the character is not
                    // in the map's keys, then the character has not be selected and can be
                    // discarded.
                    if (!dialgoueTreeRoots.containsKey(character)) { continue; }
                    dialgoueTreeRoots.get(character).add(intention);
                }
            }
        }

        ArrayList<Room> constructedRooms = new ArrayList<>();

        // Store the positions in which a room has been placed. We will use this to decide if we
        // can place a room in a particular position.
        ArrayList<Rectangle> claimedPositions = new ArrayList<>();

        // This is the vector we will add to the position if we cannot place the room.
        Vector2 conflictResolveDirection = new Vector2(1.0f, 0.0f);

        // Resolve all of the rooms.
        for (RoomTemplate template: selectedRoomTemplates) {
            Rectangle roomPosition = new Rectangle(0.0f, 0.0f, template.width, template.height);

            // Keep moving the room until it can be placed.
            while (claimedPositions.stream().anyMatch(x -> roomPosition.overlaps(x))) {
                roomPosition.setPosition(
                    roomPosition.getPosition(new Vector2()).add(conflictResolveDirection)
                );
            }

            // Indicate that we have placed the room.
            claimedPositions.add(roomPosition);

            // TODO: Actually construct a room
            Room room = null;
            constructedRooms.add(room);
        }

        return null;
    }
}
