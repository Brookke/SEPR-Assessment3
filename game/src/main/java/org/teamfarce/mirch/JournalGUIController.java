package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.teamfarce.mirch.Entities.Clue;

/**
 * The journal GUI controller draws the Journal GUI to the screen and handles any inputs
 * whilst the journal is displayed
 *
 * @author jacobwunwin
 */
public class JournalGUIController
{
    Stage journalStage;
    Stage journalCluesStage;
    Stage journalQuestionsStage;
    Stage journalNotepadStage;
    private Gdx globalGdx;
    private Sprite journalSprite;
    private Sprite dialogueSprite;
    private Skin uiSkin;
    private GameSnapshot gameSnapshot;
    private Table cluesTable;
    private Table questionsTable;
    private SpriteBatch batch;


    /**
     * Initialise the journal controller, creating GUI stages for each view
     *
     * @param skin
     * @param gSnapshot
     * @param batch
     */
    JournalGUIController(Skin skin, GameSnapshot gSnapshot, SpriteBatch batch)
    {
        this.uiSkin = skin;
        this.gameSnapshot = gSnapshot;
        this.batch = batch;

        this.journalCluesStage = new Stage();
        this.journalStage = new Stage();
        this.journalQuestionsStage = new Stage();
        this.journalNotepadStage = new Stage();

        //create a sprite for the journal background
        Texture journalBackground = new Texture(Gdx.files.internal("Open_journal.png"));
        this.journalSprite = new Sprite(journalBackground);
        this.journalSprite.setPosition(220, 90);

        Texture dialogueBackground = new Texture(Gdx.files.internal("dialogue_b.png"));
        this.dialogueSprite = new Sprite(dialogueBackground);
        this.dialogueSprite.setPosition(220, 90);

        //++++CREATE JOURNAL HOME STAGE++++

        //Create buttons and labels
        final TextButton cluesButton = new TextButton("Clues", this.uiSkin);
        final TextButton questionsButton = new TextButton("Interview Log", this.uiSkin);
        final TextButton notepadButton = new TextButton("Notepad", this.uiSkin);
        Label journalLabel = new Label("Journal", this.uiSkin);

        //textButton.setPosition(200, 200);
        journalLabel.setPosition(360, 600);
        journalLabel.setColor(Color.BLACK);
        journalLabel.setFontScale(1.5f);

        cluesButton.setPosition(380, 400);
        questionsButton.setPosition(350, 350);
        notepadButton.setPosition(370, 300);
        //journalStage.addActor(textButton);
        this.journalStage.addActor(cluesButton);
        this.journalStage.addActor(journalLabel);
        this.journalStage.addActor(notepadButton);
        this.journalStage.addActor(questionsButton);

        // Add a listener to the clues button
        cluesButton.addListener(new ChangeListener()
        {
            public void changed(ChangeEvent event, Actor actor)
            {
                System.out.println("Clues button was pressed");
                gameSnapshot.setState(GameState.journalClues);
            }
        });

        //add a listener for the show interview log button
        questionsButton.addListener(new ChangeListener()
        {
            public void changed(ChangeEvent event, Actor actor)
            {
                System.out.println("Questions button was pressed");
                gameSnapshot.setState(GameState.journalQuestions);
            }
        });

        //add a listener for the show interview log button
        notepadButton.addListener(new ChangeListener()
        {
            public void changed(ChangeEvent event, Actor actor)
            {
                System.out.println("Notepad button was pressed");
                gameSnapshot.setState(GameState.journalNotepad);
            }
        });

        //++++CREATE JOURNAL CLUES STAGE++++
        Label clueLabel = new Label("Clues", uiSkin);

        clueLabel.setColor(Color.BLACK);
        clueLabel.setFontScale(1.5f);

        clueLabel.setPosition(750, 600);

        //create a new table to store clues
        cluesTable = new Table(uiSkin);

        //place the clues table in a scroll pane
        Table container = new Table(uiSkin);
        ScrollPane scroll = new ScrollPane(cluesTable, uiSkin);


        scroll.layout();
        //add the scroll pane to an external container
        container.add(scroll).width(300f).height(400f);
        container.row();
        container.setPosition(800, 360); //set the position of the extenal container

        //add actors to the clues stage
        journalCluesStage.addActor(clueLabel);
        journalCluesStage.addActor(container);


        //++++CREATE JOURNAL INTERVIEW STAGE++++

        //Create labels
        Label questionsLabel = new Label("Interview Log", uiSkin);

        questionsLabel.setColor(Color.BLACK);
        questionsLabel.setFontScale(1.5f);

        questionsLabel.setPosition(720, 600);

        questionsTable = new Table(uiSkin);

        Table qcontainer = new Table(uiSkin);
        ScrollPane qscroll = new ScrollPane(questionsTable, uiSkin);
        //scroll.setStyle("-fx-background: transparent;"); //the numpteys depreciated this very useful command to make the background transparent


        qscroll.layout();
        qcontainer.add(qscroll).width(300f).height(400f);
        qcontainer.row();
        qcontainer.setPosition(800, 360);

        this.journalQuestionsStage.addActor(questionsLabel);
        this.journalQuestionsStage.addActor(qcontainer);

        //++++CREATE JOURNAL NOTEPAD STAGE++++

        //Create labels
        Label notepadLabel = new Label("Notepad", uiSkin);

        notepadLabel.setColor(Color.BLACK);
        notepadLabel.setFontScale(1.5f);

        notepadLabel.setPosition(720, 600);
        TextArea notepad = new TextArea("Here are my notes about a particularly develish crime...", uiSkin);
        notepad.setX(650);
        notepad.setY(150);
        notepad.setWidth(290);
        notepad.setHeight(400);

        this.journalNotepadStage.addActor(notepadLabel);
        this.journalNotepadStage.addActor(notepad);

    }

    /**
     * Adds the scrollable conversation text collected from conversations with characters
     * to the questionTable UI item
     *
     * @param qTable
     * @param journal
     */
    private void genJournalQuestionsStage(Table qTable, Journal journal)
    {
        qTable.reset(); //reset the table

        Label tlabel = new Label(journal.conversations, uiSkin);
        qTable.add(tlabel).width(300f); //set a maximum width on the row of 300 pixels
        qTable.row(); //end the row
    }

    /**
     * Adds the scrollable list of clues to the cluesTable UI item
     *
     * @param cluesTable
     * @param journal
     */
    private void genJournalCluesStage(Table cluesTable, Journal journal)
    {
        cluesTable.reset(); //reset the table

        for (Clue clue : journal.getClues()) {
            Label label = new Label(clue.getName() + " : " + clue.getDescription(), uiSkin);
            cluesTable.add(label).width(280f); //set a maximum width on the row of 300 pixels
            cluesTable.row(); //end the row
        }
    }

    /**
     * Draw the journal home screen
     */
    void drawHome()
    {
        this.batch.begin();
        this.journalSprite.draw(this.batch); //draw the journal background
        this.batch.end();
        this.journalStage.draw();
    }

    /**
     * Draw the journal clues screen
     */
    void drawClues()
    {
        this.batch.begin();
        this.journalSprite.draw(this.batch); //draw the journal background
        this.batch.end();
        this.journalStage.draw();
        this.genJournalCluesStage(this.cluesTable, this.gameSnapshot.journal); //generate the clues table
        this.journalCluesStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); //draw the clues table to the screen
        this.journalCluesStage.draw();
    }

    /**
     * Draw the journal interview log screen
     */
    void drawInterviewLog()
    {
        this.batch.begin();
        this.journalSprite.draw(this.batch);
        this.batch.end();
        this.genJournalQuestionsStage(this.questionsTable, this.gameSnapshot.journal); //generate the interview questions table
        this.journalStage.draw();
        this.journalQuestionsStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); //draw the questions table to the screen
        this.journalQuestionsStage.draw();
    }

    /**
     * draw the notepad screen
     */
    void drawNotepad()
    {
        this.batch.begin();
        this.journalSprite.draw(this.batch); //draw the journal background
        this.batch.end();
        this.journalStage.draw();
        this.journalNotepadStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); //draw the clues table to the screen
        this.journalNotepadStage.draw();
    }
}
