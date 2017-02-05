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
    private Table notepadPage;

    final static float JOURNAL_X_OFFSET = 10;
    final static float JOURNAL_Y_OFFSET = 20;
    final static float PAGE_X_OFFSET = 50;
    final static float PAGE_Y_OFFSET = 50;
    final static float JOURNAL_WIDTH = Gdx.graphics.getWidth() - (2 * JOURNAL_X_OFFSET);
    final static float JOURNAL_HEIGHT = Gdx.graphics.getHeight() - JOURNAL_Y_OFFSET;
    final static float NAV_BUTTON_WIDTH = 200;
    final static float NAV_BUTTON_HEIGHT = 40;
    final static float PAGE_WIDTH = (JOURNAL_WIDTH / 2) - PAGE_X_OFFSET;
    final static float PAGE_HEIGHT = JOURNAL_HEIGHT - (2 * PAGE_Y_OFFSET);
    final static float PAGE_MARGIN = 50;
    final static float PAGE_CONTENT_SPACE = 150;


    public JournalScreen(MIRCH game, Skin uiSkin)
    {
        super(game);
        this.game = game;
        this.gameSnapshot = game.gameSnapshot;
        this.uiSkin = uiSkin;

        statusBar = new StatusBar(game.gameSnapshot, uiSkin);

        //Initialise notepad page here to preserve page contents across screen transitions
        notepadPage = initJournalNotepadPage();
    }

    private void initStage() {
        //Initialise stage used to show journal contents
        journalStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));


        //Create table to represent journal "book"
        Table journalContainer = new Table();
        journalContainer.setBounds(JOURNAL_X_OFFSET, JOURNAL_Y_OFFSET, JOURNAL_WIDTH, JOURNAL_HEIGHT);
        //Set background image for journal
        Texture journalBackground = new Texture(Gdx.files.internal("Open_journal.png"));
        TextureRegion tr = new TextureRegion(journalBackground);
        TextureRegionDrawable trd = new TextureRegionDrawable(tr);
        journalContainer.setBackground(trd);


        //Load journal navigation controls to journal
        journalContainer.addActor(initJournalNavControls());


        //Load details page onto right journal page
        Table detailsPage;
        GameState currentState = game.gameSnapshot.getState();
        switch (currentState) {
            case journalClues:
                detailsPage = initJournalCluesPage();
                break;
            case journalQuestions:
                detailsPage = initJournalQuestionsPage();
                break;
            case journalNotepad:
                //Initialised in constructor to preserve page contents
                detailsPage = notepadPage;
                break;
            default:
                detailsPage = new Table();
                break;
        }
        journalContainer.addActor(detailsPage);


        journalStage.addActor(journalContainer);
    }

    private Table initJournalNavControls() {
        Table table = new Table();
        table.setBounds(PAGE_X_OFFSET, PAGE_Y_OFFSET, PAGE_WIDTH, PAGE_HEIGHT);

        //Initiate buttons
        table.addActor(getJournalNavButton("Clues", GameState.journalClues, 0));
        table.addActor(getJournalNavButton("Interview Log", GameState.journalQuestions, 1));
        table.addActor(getJournalNavButton("Notepad", GameState.journalNotepad, 2));


        //Initiate journal title label
        Label journalLabel = getJournalPageTitle("Journal");
        journalLabel.setWidth(100);
        journalLabel.setPosition(PAGE_WIDTH - PAGE_MARGIN - 100, PAGE_HEIGHT - PAGE_MARGIN);
        journalLabel.setFontScale(2f);
        table.addActor(journalLabel);

        return table;
    }

    private Table initJournalCluesPage() {
        Table page = new Table();
        page.setBounds(PAGE_WIDTH + PAGE_X_OFFSET, PAGE_Y_OFFSET, PAGE_WIDTH, PAGE_HEIGHT);

        page.addActor(getJournalPageTitle("Clues"));

        return page;
    }

    private Table initJournalQuestionsPage() {
        Table page = new Table();
        page.setBounds(PAGE_WIDTH + PAGE_X_OFFSET, PAGE_Y_OFFSET, PAGE_WIDTH, PAGE_HEIGHT);

        page.addActor(getJournalPageTitle("Interview Log"));

        return page;
    }

    private Table initJournalNotepadPage() {
        Table page = new Table();
        page.setBounds(PAGE_WIDTH + PAGE_X_OFFSET, PAGE_Y_OFFSET, PAGE_WIDTH, PAGE_HEIGHT);

        page.addActor(getJournalPageTitle("Notepad"));

        TextArea notepad = new TextArea("Type here to add some notes about this particularly intriguing crime...", uiSkin);
        notepad.setSize(PAGE_WIDTH - (2 * PAGE_MARGIN),PAGE_HEIGHT - PAGE_CONTENT_SPACE);
        notepad.setPosition(PAGE_MARGIN, PAGE_MARGIN);
        page.addActor(notepad);

        return page;
    }

    private Label getJournalPageTitle(String pageTitle) {
        Label title = new Label(pageTitle, uiSkin);
        title.setColor(Color.BLACK);
        title.setFontScale(1.5f);
        title.setPosition(PAGE_MARGIN, PAGE_HEIGHT - PAGE_MARGIN);
        return title;
    }

    private TextButton getJournalNavButton(String pageTitle, GameState linkState, int position) {
        TextButton button = new TextButton(pageTitle, this.uiSkin);
        button.setSize(NAV_BUTTON_WIDTH, NAV_BUTTON_HEIGHT);
        button.setPosition(PAGE_WIDTH - PAGE_MARGIN - NAV_BUTTON_WIDTH, PAGE_HEIGHT - PAGE_CONTENT_SPACE - (1.5f * position * NAV_BUTTON_HEIGHT));
        button.addListener(new ChangeListener()
        {
            public void changed(ChangeEvent event, Actor actor)
            {
                gameSnapshot.setState(linkState);
            }
        });
        return button;
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
        journalStage.getViewport().update(width, height, false);
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
