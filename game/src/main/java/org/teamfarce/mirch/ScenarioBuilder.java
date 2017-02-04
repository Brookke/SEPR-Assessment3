package org.teamfarce.mirch;

import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.Entities.Suspect;
import org.teamfarce.mirch.ScenarioBuilderDatabase.*;
import org.teamfarce.mirch.dialogue.*;
import org.teamfarce.mirch.map.Map;
import org.teamfarce.mirch.map.Room;

import java.util.*;
import java.util.stream.Collectors;

public class ScenarioBuilder
{

    public static CharacterData chooseCharacters(
            ScenarioBuilderDatabase database,
            int minSuspectCount ,
            int maxSuspectCount,
            DataCharacterMotiveLink selectedCharacterMotiveLink,
            Random random
    ) throws ScenarioBuilderException
    {
        WeightedSelection selector = new WeightedSelection(random);
        CharacterData characterData = new CharacterData();

        // Extract the murderer, victim and motive.
        characterData.murderer = selectedCharacterMotiveLink.murderer;
        characterData.victim = selectedCharacterMotiveLink.victim;

        // Create a list of suspect which we can choose from to construct our suspect list. This
        // includes all of the suspects from our data minus the murderer and victim.
        List<DataCharacter> potentialSuspects = new ArrayList<>(database.characters.values());
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
                    potentialSuspects::remove
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

    public static Set<DataClue> getClues(
            DataMeans selectedMeans,
            DataMotive selectedMotive,
            DataCharacter selectedMurderer,
            DataCharacter selectedVictim
    )
    {
        Set<DataClue> selectedClues = new HashSet<>();

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


    public static CreateAdderResult createAdders(
            DataQuestioningIntention qiData,
            Set<DataClue> selectedClues,
            HashMap<DataCharacter, DialogueTree> dialogueTrees,
            Set<DataQuestioningStyle> chosenStyles
    )
    {
        return createAdders(qiData, selectedClues, dialogueTrees, chosenStyles, null);
    }

    public static CreateAdderResult createAdders(
            DataQuestioningIntention qiData,
            Set<DataClue> selectedClues,
            HashMap<DataCharacter, DialogueTree> dialogueTrees,
            Set<DataQuestioningStyle> chosenStyles,
            DataCharacter characterFilter
    )
    {
        ArrayList<IDialogueTreeAdder> returnList = new ArrayList<>();
        int motiveAcc = 0;
        int meansAcc = 0;

        // Construct out new question intent.
        QuestionIntent qi = new QuestionIntent(qiData.id, qiData.description);

        for (DataQuestionAndResponse qarData : qiData.questions) {
            DialogueTree treeToAddTo = dialogueTrees.get(qarData.saidBy);
            // If the tree does not exit, the character was not selected and we can discard this
            // attempt to build a QuestionAndResponse.
            if (treeToAddTo == null) {
                continue;
            }

            // Discard this QuestionAndResponse if the user has not chosen this style.
            if (!chosenStyles.contains(qarData.style)) {
                continue;
            }

            // Get the clues associated with this QuestionAndResponse and remove the clues that
            // have not been selected in this generation.
            ArrayList<DataClue> clueData = new ArrayList<>(qarData.impliesClues);
            clueData.retainAll(selectedClues);

            // Construct the QuestionAndResponse and add it to the QuestionIntent.
            QuestionAndResponse qar = new QuestionAndResponse(
                    qarData.questionText,
                    qarData.style.description,
                    qarData.responseText,
                    clueData
                            .stream()
                            .map(c -> new Clue(c.name, c.description, c.impliesMotiveRating, c.impliesMeansRating, c.resource))
                            .collect(Collectors.toList())
            );
            qi.addQuestion(qar);

            for (DataQuestioningIntention qiDataInner : qarData.followUpQuestion) {
                CreateAdderResult result = createAdders(
                        qiDataInner, selectedClues, dialogueTrees, chosenStyles
                );
                motiveAcc += result.sumProvesMotive;
                meansAcc += result.sumProvesMeans;
                for (IDialogueTreeAdder adder : result.adders) {
                    qar.addDialogueTreeAdder(adder);
                }
            }

            SingleDialogueTreeAdder adder = new SingleDialogueTreeAdder(treeToAddTo, qi);
            returnList.add(adder);
        }

        return new CreateAdderResult(returnList, motiveAcc, meansAcc);
    }

    public static GameSnapshot generateGame(
            ScenarioBuilderDatabase database,
            int minSuspectCount,
            int maxSuspectCount,
            Set<DataQuestioningStyle> chosenStyles,
            Random random
    ) throws ScenarioBuilderException
    {

        List<Room> rooms = Map.initialiseRooms();
        WeightedSelection selector = new WeightedSelection(random);


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
        List<DataCharacter> selectedSuspects = characterData.suspects;

        // Get our means.
        DataMeans selectedMeans = selector
                .selectWeightedObject(selectedMurderer.meansLink, x -> x.selectionWeight)
                .get()
                .means;

        // Get the clues
        Set<DataClue> selectedClues = getClues(
                selectedMeans, selectedMotive, selectedMurderer, selectedVictim
        );

        List<Clue> constructedClues = new ArrayList<>();

        for (DataClue c : selectedClues) {
            Clue tempClue = new Clue(c.name, c.description, c.impliesMotiveRating, c.impliesMeansRating, c.resource);

            Collections.shuffle(rooms);
            tempClue.setRoom(rooms.get(0));
            constructedClues.add(tempClue);
        }



        // TODO: Misleading Clues

        // Build up our map of dialogue tree roots. This will be the question intentions which are
        // marked as being starting questions.
        HashMap<DataCharacter, List<DataQuestioningIntention>> dialogueTreeRoots = new HashMap<>();
        for (DataCharacter character : selectedSuspects) {
            dialogueTreeRoots.put(character, new ArrayList<>());
        }

        // We'll want to consider all questioning intentions which are marked as being starting
        // questions.
        for (DataQuestioningIntention intention : database.questioningIntentions.values()) {
            if (!intention.startingQuestion) {
                continue;
            }
            // Next consider all of the questions associated response. We'll add the intention we
            // are currently considering to any character's dialogue tree root collection, if the
            // question and response we are considering is said by such character.
            for (DataQuestionAndResponse response : intention.questions) {
                // We only need to consider characters which we have selected to be in this
                // case of the game. The hashmap has previously be populated with the selected
                // characters mapped to empty arrays. Because of this, if the character is not
                // in the map's keys, then the character has not be selected and can be
                // discarded.
                if (!dialogueTreeRoots.containsKey(response.saidBy)) {
                    continue;
                }
                dialogueTreeRoots.get(response.saidBy).add(intention);
            }
        }


        HashMap<DataCharacter, DialogueTree> dialogueTrees = new HashMap<>();
        List<Suspect> constructedSuspects = new ArrayList<>();

        // Construct the characters.
        for (DataCharacter suspect : selectedSuspects) {
            // Select a random room and get its data which is useful for the following generation.
            int chosenRoom = random.nextInt(rooms.size());
            Vector2Int chosenPosition = rooms.get(chosenRoom).getRandomLocation();

            // Select a random position in the room to place the character.


            // Construct the character and associated dialogue tree object. The actual building of
            // the tree will happen later.
            DialogueTree dialogueTree = new DialogueTree();
            Suspect suspectObject = new Suspect(
                    suspect.name,
                    suspect.description,
                    suspect.resource.filename,
                    chosenPosition,
                    dialogueTree
            );

            // Assign the constructed data to the correct place.
            dialogueTrees.put(suspect, dialogueTree);
            constructedSuspects.add(suspectObject);
        }

        int sumProvesMotive = 0;
        int sumProvesMeans = 0;

        // Construct the dialogue trees.
        for (DataCharacter currentCharacter : selectedSuspects) {
            for (DataQuestioningIntention qiData : dialogueTreeRoots.get(currentCharacter)) {
                CreateAdderResult result = createAdders(
                        qiData, selectedClues, dialogueTrees, chosenStyles, currentCharacter
                );
                sumProvesMotive += result.sumProvesMotive;
                sumProvesMeans += result.sumProvesMeans;
                for (IDialogueTreeAdder adder : result.adders) {
                    adder.addToTrees();
                }
            }
        }


//        List<Clue> constructedClues = propsResult.props;
//        sumProvesMotive += propsResult.sumProvesMotive;
//        sumProvesMeans += propsResult.sumProvesMeans;

        distributeClues(constructedClues, rooms);
        return new GameSnapshot(
                constructedSuspects, constructedClues, rooms, sumProvesMotive, sumProvesMeans
        );
    }

    public static class ScenarioBuilderException extends Exception
    {
        public ScenarioBuilderException(String message)
        {
            super(message);
        }
    }

    public static class CreateAdderResult
    {
        public Collection<IDialogueTreeAdder> adders;
        public int sumProvesMotive;
        public int sumProvesMeans;

        public CreateAdderResult(
                Collection<IDialogueTreeAdder> adders, int sumProvesMotive, int sumProvesMeans
        )
        {
            this.adders = adders;
            this.sumProvesMotive = sumProvesMotive;
            this.sumProvesMeans = sumProvesMeans;
        }
    }


    private static class CharacterData
    {
        public DataCharacter victim = null;
        public DataCharacter murderer = null;
        public List<DataCharacter> suspects = new ArrayList<>();
    }


    /**
     * @param clues
     * @param rooms
     */
    public static void distributeClues(List<Clue> clues, List<Room> rooms) {

        Collections.shuffle(clues);
        int amountOfClues = clues.size() - 1;
        for (Room room : rooms) {
            if (amountOfClues == -1) return;

            Vector2Int randHidingSpot = room.getRandHidingSpot();

            if (randHidingSpot != null) {
                clues.get(amountOfClues).setTileCoordinates(randHidingSpot);
                clues.get(amountOfClues).setRoom(room);
                amountOfClues--;
            }

        }

    }
}
