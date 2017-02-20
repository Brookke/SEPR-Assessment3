package org.teamfarce.mirch;

import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.dialogue.Dialogue;
import org.teamfarce.mirch.entities.Clue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by brookehatton on 16/02/2017.
 */
public class Dialogue_Test extends GameTest {
    private Dialogue testDialogue;
    private Clue testClue;

    @Before
    public void setup() {
        testClue = new Clue("Big Footprint", "1", "clueBox.png", 1, 2, false);
        try {
            testDialogue = new Dialogue("template.JSON", false);
        } catch (Dialogue.InvalidDialogueException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void constructorValidationFail() throws Exception {
        Dialogue testConstructor;

        try {
            testConstructor = new Dialogue("ForDialogueTestingOnly.JSON", false);
            fail("JSON not being verified");
        } catch (Dialogue.InvalidDialogueException e) {

        }
    }

    @Test
    public void constructor2ValidationPass() {
        Dialogue testConstructor;
        try {
            testConstructor = new Dialogue("template.JSON", false);
        } catch (Dialogue.InvalidDialogueException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getUsingClue() throws Exception {

        assertEquals("test 2", testDialogue.get(testClue));
    }

    @Test
    public void getUsingString() throws Exception {
        assertEquals("test 2", testDialogue.get(testClue.getName()));
    }

}