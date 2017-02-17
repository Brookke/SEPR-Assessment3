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
    private List<Clue> clues;

    @Before
    public void setup() {
        clues = new ArrayList<>();
        clues.add(new Clue("Big Footprint", "1", "Axe.png"));
        clues.add(new Clue("Small Footprint", "2", "Axe.png"));
        clues.add(new Clue("Glasses", "3", "Axe.png"));
        clues.add(new Clue("Bag", "4", "Axe.png"));

        try {
            testDialogue = new Dialogue("template.JSON", clues);
        } catch (Dialogue.InvalidDialogueException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void constructor1() throws Exception
    {
        List<Clue> failClues = new ArrayList<>();
        failClues.add(new Clue("Hand", "1", "Axe.png"));
        failClues.add(new Clue("Leg", "2", "Axe.png"));
        failClues.add(new Clue("Glasses", "3", "Axe.png"));
        failClues.add(new Clue("Bag", "4", "Axe.png"));

        Dialogue testConstructor;

        try {
            testConstructor = new Dialogue("template.JSON", failClues);
            fail("JSON not being verified");
        } catch (Dialogue.InvalidDialogueException e) {

        }
    }

    @Test
    public void constructor2() {
        Dialogue testConstructor;
        try {
            testConstructor = new Dialogue("template.JSON", clues);
        } catch (Dialogue.InvalidDialogueException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void get1() throws Exception
    {
        assertEquals("test 2", testDialogue.get(clues.get(0)));
    }

    @Test
    public void get2() throws Exception
    {
        assertEquals("test 2", testDialogue.get(clues.get(0).getName()));
    }



    @Test
    public void get()
    {


    }


}