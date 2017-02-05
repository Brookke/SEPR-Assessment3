package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Screens.Elements.StatusBar;

/**
 * The journal screen draws the Journal GUI to the screen and handles any inputs
 * whilst the journal is displayed
 */
public class JournalScreen extends AbstractScreen
{
    private MIRCH game;
    private GameSnapshot gameSnapshot;

    public Stage journalStage;
    private Skin uiSkin;
    private StatusBar statusBar;

    public JournalScreen(MIRCH game, Skin uiSkin)
    {
        super(game);
        this.game = game;
        this.gameSnapshot = game.gameSnapshot;
        this.uiSkin = uiSkin;

        statusBar = new StatusBar(game.gameSnapshot, uiSkin);
    }

    private void initStage() {
        //Initialise stage used to show journal contents
        journalStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        //Create table to present contents within
        Table table = new Table();
        table.setBounds(80, 20, Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 20);

        //Set background image for journal
        Texture journalBackground = new Texture(Gdx.files.internal("Open_journal.png"));
        TextureRegion tr = new TextureRegion(journalBackground);
        TextureRegionDrawable trd = new TextureRegionDrawable(tr);
        table.setBackground(trd);

        //Load journal navigation controls
        initJournalNavControls(table);

        GameState currentState = game.gameSnapshot.getState();
        switch (currentState) {
            case journalClues:
                initJournalCluesPage(table);
                break;
            case journalQuestions:
                initJournalQuestionsPage(table);
                break;
            case journalNotepad:
                initJournalNotepadPage(table);
                break;
            default:
                break;
        }


        journalStage.addActor(table);
    }

    private void initJournalNavControls(Table table) {
        //Initiate clues button
        final TextButton cluesButton = new TextButton("Clues", this.uiSkin);
        cluesButton.setPosition(380, 400);
        cluesButton.addListener(new ChangeListener()
        {
            public void changed(ChangeEvent event, Actor actor)
            {
                gameSnapshot.setState(GameState.journalClues);
            }
        });


        //Initiate questions button
        final TextButton questionsButton = new TextButton("Interview Log", this.uiSkin);
        questionsButton.setPosition(350, 350);
        questionsButton.addListener(new ChangeListener()
        {
            public void changed(ChangeEvent event, Actor actor)
            {
                gameSnapshot.setState(GameState.journalQuestions);
            }
        });


        //Initiate notepad button
        final TextButton notepadButton = new TextButton("Notepad", this.uiSkin);
        notepadButton.setPosition(370, 300);
        notepadButton.addListener(new ChangeListener()
        {
            public void changed(ChangeEvent event, Actor actor)
            {
                gameSnapshot.setState(GameState.journalNotepad);
            }
        });


        //Initiate journal title label
        Label journalLabel = new Label("Journal", this.uiSkin);
        journalLabel.setPosition(360, 600);
        journalLabel.setColor(Color.BLACK);
        journalLabel.setFontScale(2f);


        //Add controls to table for display
        table.addActor(cluesButton);
        table.addActor(journalLabel);
        table.addActor(notepadButton);
        table.addActor(questionsButton);
    }

    private void initJournalCluesPage(Table table) {

        //Initialise clue page title
        Label clueLabel = new Label("Clues", uiSkin);
        clueLabel.setColor(Color.BLACK);
        clueLabel.setFontScale(1.5f);
        clueLabel.setPosition(750, 600);

        //create a new table to store clues
        Table cluesTable = new Table(uiSkin);

        //place the clues table in a scroll pane
        Table container = new Table(uiSkin);
        ScrollPane scroll = new ScrollPane(cluesTable, uiSkin);
        scroll.layout();

        //add the scroll pane to an external container
        container.add(scroll).width(300f).height(400f);
        container.row();
        container.setPosition(800, 360); //set the position of the extenal container

        //add actors to the clues stage
        table.addActor(clueLabel);
        table.addActor(container);

    }

    private void initJournalQuestionsPage(Table table) {
        Label questionsLabel = new Label("Interview Log", uiSkin);

        questionsLabel.setColor(Color.BLACK);
        questionsLabel.setFontScale(1.5f);

        questionsLabel.setPosition(720, 600);

        Table questionsTable = new Table(uiSkin);

        ScrollPane qscroll = new ScrollPane(questionsTable, uiSkin);
        qscroll.layout();

        Table qcontainer = new Table(uiSkin);
        qcontainer.add(qscroll).width(300f).height(400f);
        qcontainer.row();
        qcontainer.setPosition(800, 360);

        table.addActor(questionsLabel);
        table.addActor(qcontainer);

    }

    private void initJournalNotepadPage(Table table) {
        //Create labels
        Label notepadLabel = new Label("Notepad", uiSkin);

        notepadLabel.setColor(Color.BLACK);
        notepadLabel.setFontScale(1.5f);
        notepadLabel.setPosition(720, 600);

        TextArea notepad = new TextArea("Here are my notes about a particularly develish crime...", uiSkin);
        notepad.setBounds(650,150,290,400);

        table.addActor(notepadLabel);
        table.addActor(notepad);

    }

    @Override
    public void show()
    {
        initStage();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(journalStage);
        multiplexer.addProcessor(statusBar.stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta)
    {
        journalStage.act();
        journalStage.draw();
        statusBar.render();
    }

    @Override
    public void resize(int width, int height)
    {
        journalStage.getViewport().update(width, height, true);
        statusBar.resize(width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose()
    {
        journalStage.dispose();
        statusBar.dispose();
    }
}
