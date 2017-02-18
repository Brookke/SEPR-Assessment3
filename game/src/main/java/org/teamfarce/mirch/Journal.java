package org.teamfarce.mirch;

import org.teamfarce.mirch.entities.Clue;

import java.util.ArrayList;
import java.util.List;

/**
 * The Journal stores relevant information to the players progress through the game.
 * <p>
 * This includes a list of all props discovered, and a log of all conversations.
 * </p>
 */
public class Journal
{

    public ArrayList<Clue> foundClues;

    /**
     * The conversation buffer.
     */
    public ArrayList<String> conversations;

    /**
     * Initialise the Journal in an empty state.
     */
    public Journal()
    {
        this.conversations = new ArrayList<>();
        this.foundClues = new ArrayList<>();
    }

    /**
     * Add a clue to the journal.
     *
     * @param clue The clue to add.
     */
    public void addClue(Clue clue)
    {
        this.foundClues.add(clue);
    }

    public List<Clue> getClues()
    {
        return this.foundClues;
    }


    /**
     * This gets the clues that can be used in the interview process
     * @return
     */
    public List<Clue> getQuestionableClues()
    {
        List<Clue> clues = new ArrayList<>();
        for (Clue c : this.foundClues) {
            if (!c.isMotiveClue() /*&& !c.isMeanClue()*/) {
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
    public void addConversation(String text, String characterName)
    {
        String convo = String.format("%1$s: %2$s", characterName, text);
        if (!this.conversations.contains(convo)) {
            this.conversations.add(convo);
        }

    }


    /**
     * Returns a list of conversations that are recorded in the journal.
     * @return
     */
    public List<String> getConversations()
    {
        return this.conversations;
    }

}
