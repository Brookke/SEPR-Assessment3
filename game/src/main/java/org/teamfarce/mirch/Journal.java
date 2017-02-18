package org.teamfarce.mirch;

import org.lwjgl.opencl.CL;
import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.Screens.NarratorScreen;

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
    MIRCH game;

    public ArrayList<Clue> foundClues;

    public int motivesFound = 0;
    public boolean foundWeapon = false;

    /**
     * The conversation buffer.
     */
    public ArrayList<String> conversations;

    /**
     * Initialise the Journal in an empty state.
     */
    public Journal(MIRCH game)
    {
        this.game = game;
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

        game.gameSnapshot.modifyScore(5);

        if (clue.isMotiveClue()) motivesFound ++;

        if (motivesFound == 3)
        {
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

            game.gameSnapshot.modifyScore(10);
        }

        if (clue.isMeansClue())
        {
            foundWeapon = true;
        }
    }

    private String getMotive()
    {
        String[] motives = new String[]{"","",""};

        Clue[] remove = new Clue[3];

        for (Clue c : foundClues)
        {
            if (c.getName().contains("Motive"))
            {
                if (c.getName().contains("1"))
                {
                    motives[0] = c.getDescription();
                    remove[0] = c;
                }
                else if (c.getName().contains("2"))
                {
                    motives[1] = c.getDescription();
                    remove[1] = c;
                }
                else if (c.getName().contains("3"))
                {
                    motives[2] = c.getDescription();
                    remove[2] = c;
                }
            }
        }

        foundClues.remove(remove[0]);
        foundClues.remove(remove[1]);
        foundClues.remove(remove[2]);

        addClue(new Clue("Motive Clue", "After peicing it together, this is the motive:\n" + motives[0] + motives[1] + motives[2], "clueSheet.png", 1, 2, false));

        return motives[0] + motives[1] + motives[2];
    }

    public List<Clue> getClues()
    {
        return this.foundClues;
    }

    /**
     * Add a conversation with a given character to the journal.
     *
     * @param text          The text to add the journal.
     * @param characterName The character name to associate with the journal.
     */
    public void addConversation(String text, String characterName)
    {
        this.conversations.add(String.format("%1$s: %2$s", characterName, text));
    }

    public List<String> getConversations()
    {
        return this.conversations;
    }

}
