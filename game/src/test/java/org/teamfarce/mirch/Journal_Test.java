/**
 *
 */
package org.teamfarce.mirch;

import org.junit.Test;
import org.teamfarce.mirch.entities.Clue;
import org.teamfarce.mirch.entities.Suspect;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Tests the journal class
 *
 * @author jacobwunwin
 */
public class Journal_Test extends GameTest {
    @Test
    public void addClue() {
        MIRCH game = new MIRCH();
        game.gameSnapshot = new GameSnapshot(game, null, null, new ArrayList<Suspect>(), null);
        game.gameSnapshot.score = 0;

        Journal journal = new Journal(game);
        Clue clue = new Clue("Clue name", "Description", "clueSheet.png", 0, 0, false);
        journal.addClue(clue);

        assertEquals(clue, journal.foundClues.get(0));

    }

    @Test
    public void getClues() {
        MIRCH game = new MIRCH();
        game.gameSnapshot = new GameSnapshot(game, null, null, new ArrayList<Suspect>(), null);
        game.gameSnapshot.score = 0;

        Journal journal = new Journal(game);
        ArrayList<Clue> cluesList = new ArrayList<>();

        Clue clue = new Clue("Clue name", "Description", "clueSheet.png", 0, 0, false);
        Clue clue2 = new Clue("Clue name 2", "Description", "clueSheet.png", 0, 0, false);

        journal.addClue(clue);
        journal.addClue(clue2);

        cluesList.add(clue);
        cluesList.add(clue2);

        assertEquals(cluesList, journal.foundClues);
    }

    @Test
    public void addConversation() {
        Journal journal = new Journal(null);

        String dialogue = "Convo text";
        String character = "Character name";
        journal.addConversation(dialogue, character);

        String result = character + ": " + dialogue;

        assertEquals(result, journal.getConversations().get(0));
    }

    @Test
    public void getConversations() {
        Journal journal = new Journal(null);

        journal.addConversation("Dialogue", "Character 1");
        journal.addConversation("Dialogue", "Character 2");
        journal.addConversation("Dialogue", "Character 3");

        assertEquals(3, journal.getConversations().size());
    }
}
