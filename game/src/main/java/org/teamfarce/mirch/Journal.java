package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * The Journal stores relevant information to the players progress through the game.
 * <p>
 * This includes a list of all props discovered, and a log of all conversations.
 * </p>
 */
public class Journal {
    private ArrayList<Prop> foundProps;
    private ArrayList<Clue> foundClues;

    /**
     * The conversation buffer.
     */
    public String conversations;

    /**
     * Initialise the Journal in an empty state.
     */
    public Journal() {
        this.foundProps = new ArrayList<Prop>();
        this.conversations = "";
        this.foundClues = new ArrayList<Clue>();
    }

    /**
     * Add a prop to the journal.
     *
     * @param prop The prop to add.
     */
    public void addProp(Prop prop) {
        this.foundProps.add(prop);
    }

    /**
     * Add a clue to the journal.
     *
     * @param clue The clue to add.
     */
    public void addClue(Clue clue) {
        this.foundClues.add(clue);
    }

    /**
     * Add a conversation with a given character to the journal.
     *
     * @param text The text to add the journal.
     * @param characterName The character name to associate with the journal.
     */
    public void addConversation(String text, String characterName) {
        this.conversations = String.format(
            "%1$s: %2$s\n%3$s", characterName, text, this.conversations
        );
    }

    /**
     * Returns a list of all props clicked by the player.
     *
     * @return The props.
     */
    ArrayList<Prop> getProps() {
        return this.foundProps;
    }
}
