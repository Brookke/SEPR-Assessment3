package org.teamfarce.mirch.dialogue;

import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.GameTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by brookehatton on 16/02/2017.
 */
public class DialogueTest extends GameTest
{
    private Dialogue testDialogue;
    private Clue testClue;

    @Before
    public void setup() {
        testClue = new Clue("Big Footprint", "1", "Axe.png");
        try {
            testDialogue = new Dialogue("template.JSON");
        } catch (Dialogue.InvalidDialogueException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void constructor1() throws Exception
    {
        Dialogue testConstructor;

        try {
            testConstructor = new Dialogue("DoNotUse.JSON");
            fail("JSON not being verified");
        } catch (Dialogue.InvalidDialogueException e) {

        }
    }

    @Test
    public void constructor2() {
        Dialogue testConstructor;
        try {
            testConstructor = new Dialogue("template.JSON");
        } catch (Dialogue.InvalidDialogueException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void get1() throws Exception
    {

        assertEquals("test 2", testDialogue.get(testClue));
    }

    @Test
    public void get2() throws Exception
    {
        assertEquals("test 2", testDialogue.get(testClue.getName()));
    }

}