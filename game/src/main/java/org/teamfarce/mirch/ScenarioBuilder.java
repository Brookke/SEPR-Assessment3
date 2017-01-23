package org.teamfarce.mirch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import org.teamfarce.mirch.ScenarioBuilderDatabase;
import org.teamfarce.mirch.WeightedSelection;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataQuestioningStyle;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataRoomTemplate;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataRoomType;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataCharacterMotiveLink;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataMeans;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataMotive;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataQuestioningIntention;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataProtoprop;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataCharacter;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataClue;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataProp;
import org.teamfarce.mirch.ScenarioBuilderDatabase.DataQuestionAndResponse;
import org.teamfarce.mirch.Room;
import org.teamfarce.mirch.Suspect;
import org.teamfarce.mirch.dialogue.DialogueTree;
import org.teamfarce.mirch.dialogue.QuestionIntent;
import org.teamfarce.mirch.dialogue.QuestionAndResponse;
import org.teamfarce.mirch.dialogue.SingleDialogueTreeAdder;

public class ScenarioBuilder {
    public static class ScenarioBuilderException extends Exception {
        public ScenarioBuilderException(String message) {
            super(message);
        }
    }

    private static class CharacterData {
        public DataCharacter victim = null;
        public DataCharacter murderer = null;
        public ArrayList<DataCharacter> suspects = new ArrayList<>();
    }

    public static ArrayList<DataRoomTemplate> chooseRoomTemplates(
        ScenarioBuilderDatabase database,
        int minRoomCount,
        int maxRoomCount,
        Random random
    ) throws ScenarioBuilderException {
        WeightedSelection selector = new WeightedSelection(random);

        // This is an arbitrary target that lies in between the minimum and maximum room count. The
        // following process will use this as an indication of when it has enough rooms. Due to
        // other constraints, the final room count may be different to this value but will lie in
        // the provided maximum and minimum.
        int targetRoomCount = minRoomCount + random.nextInt(maxRoomCount - minRoomCount + 1);

        // The room templates we have selected.
        ArrayList<DataRoomTemplate> selectedRoomTemplates = new ArrayList<>();

        // A list which represents the room types we can still use. This should include the room
        // types maxCount times. They should be removed from the list when a room template is
        // selected from them.
        ArrayList<DataRoomType> selectedRoomTypes = new ArrayList<>();

        for (DataRoomType roomType: database.roomTypes.values()) {
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
        // count. Since every room type has been included in selectedRoomTypes maxCount - minCount
        // times, the size of selectedRoomTypes is how many more rooms we can add. If the currently
        // selected rooms plus to total room types we can still add are under the minimum room
        // count, we cannot fulfil it.
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

        return selectedRoomTemplates;
    }

    public static CharacterData chooseCharacters(
        ScenarioBuilderDatabase database,
        int minSuspectCount,
        int maxSuspectCount,
        DataCharacterMotiveLink selectedCharacterMotiveLink,
        Random random
    ) throws ScenarioBuilderException {
        WeightedSelection selector = new WeightedSelection(random);
        CharacterData characterData = new CharacterData();

        // Extract the murderer, victim and motive.
        characterData.murderer = selectedCharacterMotiveLink.murderer;
        characterData.victim = selectedCharacterMotiveLink.victim;

        // Create a list of suspect which we can choose from to construct our suspect list. This
        // includes all of the suspects from our data minus the murderer and victim.
        ArrayList<DataCharacter> potentialSuspects =
            new ArrayList<>(database.characters.values());
        potentialSuspects.remove(characterData.murderer);
        potentialSuspects.remove(characterData.victim);

        int targetSuspectCount =
            minSuspectCount + random.nextInt(maxSuspectCount - minSuspectCount + 1);

        // Keep randomly adding suspects whilst we haven't reached our target and we still have
        // some characters to consider.
        while (
            characterData.suspects.size() < targetSuspectCount && potentialSuspects.size() > 0
        ) {
            characterData.suspects.add(selector.selectWeightedObject(
                potentialSuspects,
                x -> x.selectionWeight,
                x -> potentialSuspects.remove(x)
            ).get());
        }

        // If we haven't got more suspects than the minimum count, the data in the database was
        // not sufficient to fulfil this requirement.
        if (characterData.suspects.size() < minSuspectCount) {
            throw new ScenarioBuilderException("Could not minimum suspect count");
        }

        // The murderer is a suspect as well.
        characterData.suspects.add(characterData.murderer);

        return characterData;
    }

    public static HashSet<DataClue> getClues(
        DataMeans selectedMeans,
        DataMotive selectedMotive,
        DataCharacter selectedMurderer,
        DataCharacter selectedVictim
    ) {
        HashSet<DataClue> selectedClues = new HashSet<>();

        // Get the clues. This is done by filtering out the clues of the selected motive/means
        // which required the victim/murderer to be different.
        List<DataClue> meansClues = selectedMeans
            .clues
            .stream()
            .filter(c -> selectedMurderer.requiredAsMurderer.contains(c))
            .filter(c -> selectedVictim.requiredAsVictim.contains(c))
            .collect(Collectors.toList());
        selectedClues.addAll(meansClues);

        List<DataClue> motiveClues = selectedMotive
            .clues
            .stream()
            .filter(c -> selectedMurderer.requiredAsMurderer.contains(c))
            .filter(c -> selectedVictim.requiredAsVictim.contains(c))
            .collect(Collectors.toList());
        selectedClues.addAll(motiveClues);

        return selectedClues;
    }

    public static ArrayList<Room> constructRooms(
        ArrayList<DataRoomTemplate> selectedRoomTemplates
    ) {
        ArrayList<Room> constructedRooms = new ArrayList<>();

        // Store the positions in which a room has been placed. We will use this to decide if we
        // can place a room in a particular position.
        ArrayList<Rectangle> claimedPositions = new ArrayList<>();

        // This is the vector we will add to the position if we cannot place the room.
        Vector2 conflictResolveDirection = new Vector2(1.0f, 0.0f);

        // Resolve all of the rooms.
        for (DataRoomTemplate template: selectedRoomTemplates) {
            Rectangle roomPosition = new Rectangle(0.0f, 0.0f, template.width, template.height);

            // Keep moving the room until it can be placed.
            while (claimedPositions.stream().anyMatch(x -> roomPosition.overlaps(x))) {
                roomPosition.setPosition(
                    roomPosition.getPosition(new Vector2()).add(conflictResolveDirection)
                );
            }

            // Indicate that we have placed the room.
            claimedPositions.add(roomPosition);
            conflictResolveDirection.rotate90(-1);

            // Construct and add a room.
            Room room = Room.constructWithUnitSizes(
                template.background.filename,
                roomPosition.getPosition(new Vector2())
            );
            constructedRooms.add(room);
        }

        return constructedRooms;
    }

    public static ArrayList<Prop> constructProps(
        Set<DataClue> selectedClues,
        List<DataRoomTemplate> selectedRoomTemplates,
        List<Room> constructedRooms,
        Random random
    ) {
        ArrayList<Prop> constructedProps = new ArrayList<>();
        Set<DataProp> propsWithClues =
            selectedClues.stream().flatMap(c -> c.props.stream()).collect(Collectors.toSet());

        // Iterate through all of the rooms to add their props to them.
        for (int i = 0; i < selectedRoomTemplates.size(); ++i) {
            DataRoomTemplate roomTemplate = selectedRoomTemplates.get(i);
            Room room = constructedRooms.get(i);

            for (DataProtoprop protoprop: roomTemplate.protoprops) {
                // Copy the set so that we don't mutate the database.
                List<DataProp> reducedPropSelection = new ArrayList<>(protoprop.props);

                // Remove the props which do not have a clues associated with them
                reducedPropSelection.retainAll(propsWithClues);

                // If the size of the result is 0, then we should attempt to select from the full
                // range of props, which include props without clues.
                if (reducedPropSelection.size() == 0) {
                    // reducedPropSelection = new ArrayList<>(protoprop.props);
                    reducedPropSelection = protoprop
                        .props
                        .stream()
                        // Since we have no clues for this protoprop instance, we can remove all of
                        // the props which are required to be clues.
                        .filter(x -> !x.mustBeClue)
                        .collect(Collectors.toList());
                }

                // Select a random prop from our reduced list of props.
                int propDataSelection = random.nextInt(reducedPropSelection.size());
                DataProp propData = reducedPropSelection.get(propDataSelection);

                // Create a list of all of the clues this new prop will have. It will be the
                // potential clues this prop was associated with in the database, intersected with
                // the clues we selected earlier.
                ArrayList<DataClue> propClueData = new ArrayList<>(propData.clues);
                propClueData.retainAll(selectedClues);

                List<Clue> propClues = propData
                    .clues
                    .stream()
                    .filter(p -> selectedClues.contains(p))
                    .map(p -> new Clue(p.impliesMotiveRating, p.impliesMeansRating, p.description))
                    .collect(Collectors.toList());

                // Create and add the instance.
                Prop propInstance = new Prop(
                    propData.name,
                    propData.description,
                    propData.resource.filename,
                    new Vector2((float)protoprop.x, (float)protoprop.y),
                    room,
                    propClues
                );
                constructedProps.add(propInstance);
            }
        }

        return constructedProps;
    }

    public static QuestionIntent generateDialogueTree(
        DataQuestioningIntention qiData,
        HashMap<DataCharacter, DialogueTree> dialogueTrees
    ) {
        QuestionIntent qi = new QuestionIntent(qiData.description);
        for (DataQuestionAndResponse qarData: qiData.questions) {
            DialogueTree currentDialogueTree = dialogueTrees.get(qarData.saidBy);
            QuestionAndResponse qar = new QuestionAndResponse(
                qarData.questionText,
                qarData.style.description,
                qarData.responseText,
                new ArrayList<>()
            );
            qi.addQuestion(qar);
            for (DataQuestioningIntention qiDataInner: qarData.followUpQuestion) {
                QuestionIntent qiInner = generateDialogueTree(qiDataInner, dialogueTrees);
                qar.addDialogueTreeAdder(new SingleDialogueTreeAdder(
                    currentDialogueTree, qiInner
                ));
            }
        }
        return qi;
    }

    public static GameSnapshot generateGame(
        ScenarioBuilderDatabase database,
        int minRoomCount,
        int maxRoomCount,
        int minSuspectCount,
        int maxSuspectCount,
        Set<DataQuestioningStyle> chosenStyles,
        Random random
    ) throws ScenarioBuilderException {
        WeightedSelection selector = new WeightedSelection(random);

        ArrayList<DataRoomTemplate> selectedRoomTemplates = chooseRoomTemplates(
            database,
            minRoomCount,
            maxRoomCount,
            random
        );

        // Select a character motive link to use.
        DataCharacterMotiveLink selectedCharacterMotiveLink = selector.selectWeightedObject(
            database.characterMotiveLinks.values(), x -> x.selectionWeight
        ).get();
        DataMotive selectedMotive = selectedCharacterMotiveLink.motive;

        CharacterData characterData = chooseCharacters(
            database,
            minSuspectCount,
            maxSuspectCount,
            selectedCharacterMotiveLink,
            random
        );
        DataCharacter selectedMurderer = characterData.murderer;
        DataCharacter selectedVictim = characterData.victim;
        ArrayList<DataCharacter> selectedSuspects = characterData.suspects;

        // Get our means.
        DataMeans selectedMeans = selector
                .selectWeightedObject(selectedMurderer.meansLink, x -> x.selectionWeight)
                .get()
                .means;

        // Get the clues
        HashSet<DataClue> selectedClues = getClues(
            selectedMeans, selectedMotive, selectedMurderer, selectedVictim
        );

        // TODO: Misleading Clues

        // Build up our map of dialogue tree roots. This will be the question intentions which are
        // marked as being starting questions.
        HashMap<DataCharacter, ArrayList<DataQuestioningIntention>>
            dialgoueTreeRoots = new HashMap<>();
        for (DataCharacter character: selectedSuspects) {
            dialgoueTreeRoots.put(character, new ArrayList<>());
        }

        // We'll want to consider all questioning intentions which are marked as being starting
        // questions.
        for (DataQuestioningIntention intention: database.questioningIntentions.values()) {
            if (!intention.startingQuestion) { continue; }
            // Next consider all of the questions associated response. We'll add the intention we
            // are currently considering to any character's dialogue tree root collection, if the
            // question and response we are considering is said by such character.
            for (DataQuestionAndResponse response: intention.questions) {
                // We only need to consider characters which we have selected to be in this
                // case of the game. The hashmap has previously be populated with the selected
                // characters mapped to empty arrays. Because of this, if the character is not
                // in the map's keys, then the character has not be selected and can be
                // discarded.
                if (!dialgoueTreeRoots.containsKey(response.saidBy)) { continue; }
                dialgoueTreeRoots.get(response.saidBy).add(intention);
            }
        }

        ArrayList<Room> constructedRooms = constructRooms(selectedRoomTemplates);

        HashMap<DataCharacter, DialogueTree> dialogueTrees = new HashMap<>();
        ArrayList<Suspect> constructedSuspects = new ArrayList<>();

        // Construct the characters.
        for (DataCharacter suspect: selectedSuspects) {
            Room chosenRoom = constructedRooms.get(random.nextInt(constructedRooms.size()));
            Vector2 roomOrigin = new Vector2(chosenRoom.position);
            roomOrigin.x += roomOrigin.x + 20;
            roomOrigin.y += roomOrigin.y + 20;

            DialogueTree dialogueTree = new DialogueTree();
            Suspect suspectObject = new Suspect(
                suspect.name,
                suspect.description,
                suspect.resource.filename,
                roomOrigin, // TODO: Place them in a proper place.
                dialogueTree
            );

            dialogueTrees.put(suspect, dialogueTree);
            constructedSuspects.add(suspectObject);
        }

        // Construct the dialogue trees.
        for (DataCharacter currentCharacter: selectedSuspects) {
            DialogueTree dialogueTree = dialogueTrees.get(currentCharacter);
            ArrayList<DataQuestioningIntention> intentions =
                dialgoueTreeRoots.get(currentCharacter);
            for (DataQuestioningIntention intention: intentions) {
                QuestionIntent qi = generateDialogueTree(intention, dialogueTrees);
                dialogueTree.addQuestionIntent(qi);
            }
        }

        ArrayList<Prop> constructedProps = constructProps(
            selectedClues, selectedRoomTemplates, constructedRooms, random
        );

        return new GameSnapshot(constructedSuspects, constructedProps, constructedRooms);
    }
}
