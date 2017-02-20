package org.teamfarce.mirch;

import org.teamfarce.mirch.entities.Clue;

import java.util.ArrayList;
import java.util.List;

/**
 * The Journal stores relevant information to the players progress through the game.
 *
 * This includes a list of all props discovered, and a log of all conversations.
 * </p>
 */
public class Journal {
    public ArrayList<Clue> foundClues;
    /**
     * The conversation buffer.
     */
    public ArrayList<String> conversations;
    MIRCH game;
    /**
     * These variables store information about what clues have been found
     */
    private int motivesFound = 0;
    private boolean foundWeapon = false;

    /**
     * Initialise the Journal in an empty state.
     */
    public Journal(MIRCH game) {
        this.game = game;
        this.conversations = new ArrayList<>();
        this.foundClues = new ArrayList<>();
    }

    /**
     * Add a clue to the journal.
     *
     * @param clue The clue to add.
     */
    public void addClue(Clue clue) {
        this.foundClues.add(clue);

        game.gameSnapshot.setAllUnlocked();

        game.gameSnapshot.modifyScore(5);

        if (clue.isMotiveClue()) motivesFound++;

        if (motivesFound == 3) {
            displayMotive();
        }

        if (clue.isMeansClue()) {
            foundWeapon = true;
        }
    }

    /**
     * This method displays the complete motive to the player
     */
    private void displayMotive() {
        game.guiController.narratorScreen.setSpeech("Congratulations! You have solved the killers motive! Let's take a look at those clues...\n \n"
                + getMotive() + "\n \nI can't believe someone would kill someone for that!\n \n"
                + "Well, you go out there and find out who committed this murder!\n \nGood Luck!")
                .setButton("Continue Game", new Runnable() {
                    @Override
                    public void run() {
                        game.gameSnapshot.setState(GameState.map);
                    }
                });

        game.gameSnapshot.setState(GameState.narrator);
    }

    /**
     * This method returns the full motive from the 3 seperated clues.
     *
     * It also adds a full motive to the journal clues and removes the seperate parts
     *
     * @return String motive
     */
    private String getMotive() {
        String[] motives = new String[]{"", "", ""};

        Clue[] remove = new Clue[3];

        for (Clue c : foundClues) {
            if (c.getName().contains("Motive")) {
                if (c.getName().contains("1")) {
                    motives[0] = c.getDescription();
                    remove[0] = c;
                } else if (c.getName().contains("2")) {
                    motives[1] = c.getDescription();
                    remove[1] = c;
                } else if (c.getName().contains("3")) {
                    motives[2] = c.getDescription();
                    remove[2] = c;
                }
            }
        }

        foundClues.remove(remove[0]);
        foundClues.remove(remove[1]);
        foundClues.remove(remove[2]);

        addClue(new Clue("Motive Clue", "After piecing it together, this is the motive:\n" + motives[0] + motives[1] + motives[2], "clueSheet.png", 1, 2, false));

        return motives[0] + motives[1] + motives[2];
    }

    public List<Clue> getClues() {
        return this.foundClues;
    }


    /**
     * This gets the clues that can be used in the interview process
     *
     * @return
     */
    public List<Clue> getQuestionableClues() {
        List<Clue> clues = new ArrayList<>();
        for (Clue c : this.foundClues) {
            if (!c.isMotiveClue() && !c.isMeansClue()) {
                clues.add(c);
            }
        }
        return clues;
    }

    /**
     * Add a conversation with a given character to the journal.
     *
     * @param text          The text to add the journal.
     * @param characterName The character name to associate with the journal.
     */
    public void addConversation(String text, String characterName) {
        String convo = String.format("%1$s: %2$s", characterName, text);
        if (!this.conversations.contains(convo)) {
            this.conversations.add(convo);
        }

    }

    /**
     * This method returns whether the murder weapon has been found or not
     */
    public boolean hasFoundMurderWeapon() {
        return foundWeapon;
    }

    /**
     * This method returns whether the motive clue has been found or not
     */
    public boolean hasFoundMotiveClue() {
        return motivesFound >= 3;
    }

    /**
     * Returns a list of conversations that are recorded in the journal.
     *
     * @return
     */
    public List<String> getConversations() {
        return this.conversations;
    }

}
