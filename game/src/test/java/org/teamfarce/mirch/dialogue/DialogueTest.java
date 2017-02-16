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

        testDialogue = new Dialogue("template.json", clues);
    }
    @Test
    public void importDialogue() throws Exception
    {

    }

    @Test
    public void validateJsonAgainstClues() throws Exception
    {
        testDialogue.validateJsonAgainstClues(clues);
    }

}