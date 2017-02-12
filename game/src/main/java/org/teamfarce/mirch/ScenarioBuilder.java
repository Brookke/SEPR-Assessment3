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

    public static CharacterData generateCharacters(
            HashMap<Integer, DataCharacter> characters

    ) throws ScenarioBuilderException
    {

        CharacterData data = new CharacterData();

        List<Suspect> posKillers = new ArrayList<>();
        List<Suspect> posVictims = new ArrayList<>();
        characters.forEach((x, c) -> {
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

    private static List<Clue> convertClues(
            HashMap<Integer, DataClue> clues
    )
    {
        List<Clue> output = new ArrayList<>();
        clues.forEach((x,c) -> output.add(new Clue(c.name, c.description, c.sprite)));

        return output;
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
                            .map(c -> new Clue(c.name, c.description, c.impliesMotiveRating, c.impliesMeansRating, c.sprite))
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
            int suspectCount,
            Set<DataQuestioningStyle> chosenStyles,
            Random random
    ) throws ScenarioBuilderException
    {


        List<Clue> constructedClues = new ArrayList<>();
        List<Room> rooms = Map.initialiseRooms();

        CharacterData characterData = generateCharacters(database.characters);
        constructedClues.addAll(characterData.murderer.relatedClues);

        
        // Get our means.
        DataMeansClue selectedMeans = selector
                .selectWeightedObject(selectedMurderer.meansLink, x -> x.selectionWeight)
                .get()
                .means;

        // Get the clues
        Set<DataClue> selectedClues = getClues(
                selectedMeans, selectedMotive, selectedMurderer, selectedVictim
        );

        List<Clue> constructedClues = new ArrayList<>();

        for (DataClue c : selectedClues) {
            Clue tempClue = new Clue(c.name, c.description, c.sprite);

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
                    suspect.spritesheet.filename,
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
