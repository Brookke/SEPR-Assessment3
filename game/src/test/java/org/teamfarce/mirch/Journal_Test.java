/**
 *
 */
package org.teamfarce.mirch;

import org.junit.Test;
import org.teamfarce.mirch.Entities.Clue;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Tests the journal class
 *
 * @author jacobwunwin
 */
public class Journal_Test extends GameTest
{
    @Test
    public void addClue()
    {
        Journal journal = new Journal();
		    Clue clue = new Clue("Clue name", "Description","clueSheet.png", 0, 0);
        journal.addClue(clue);

        assertEquals(clue, journal.foundClues.get(0));

    }

    @Test
    public void getClues()
    {
        Journal journal = new Journal();
        ArrayList<Clue> cluesList = new ArrayList<>();

		    Clue clue = new Clue("Clue name", "Description","clueSheet.png", 0, 0);
		    Clue clue2 = new Clue("Clue name 2", "Description","clueSheet.png", 0, 0);

        journal.addClue(clue);
        journal.addClue(clue2);

        cluesList.add(clue);
        cluesList.add(clue2);

        assertEquals(cluesList, journal.foundClues);
    }

    @Test
    public void addConversation()
    {
        Journal journal = new Journal();

        String dialogue = "Convo text";
        String character = "Character name";
        journal.addConversation(dialogue, character);

        String result = character + ": " + dialogue;

        assertEquals(result, journal.getConversations().get(0));
    }

    @Test
    public void getConversations()
    {
        Journal journal = new Journal();

        journal.addConversation("Dialogue", "Character 1");
        journal.addConversation("Dialogue", "Character 2");
        journal.addConversation("Dialogue", "Character 3");

        assertEquals(3, journal.getConversations().size());
    }
}
