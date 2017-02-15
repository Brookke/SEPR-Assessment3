package org.teamfarce.mirch.Screens.Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Jason on 15/02/2017.
 */
public class InterviewResponseBox {
    /**
     * Layout constants
     */
    private static final int PADDING = 8;
    private static final int TABLE_WIDTH = 900;
    private static final int TABLE_HEIGHT = 250;
    private static final int TEXT_ROW_HEIGHT = 30;
    private static final int BUTTON_ROW_HEIGHT = 40;

    private Skin uiSkin;

    /**
     * This is the text component to display.
     */
    private String textContent;

    /**
     * List of buttons to be displayed on the SpeechBox
     */
    private ArrayList<InterviewResponseButton> buttons;

    /**
     * The constructor for the InterviewResponseBox
     */
    public InterviewResponseBox(String content, ArrayList<InterviewResponseButton> buttonList, Skin uiSkin) {
        textContent = content;
        buttons = buttonList;
        this.uiSkin = uiSkin;
    }

    public Table getContent() {
        Table table = new Table();
        table.setSize(TABLE_WIDTH, TABLE_HEIGHT);

        //Calculate constants for use later
        int buttonCount;
        try {
            buttonCount = buttons.size();
        } catch (Exception noButtons) {
            buttonCount = 0;
        }


        //Calculate number of columns for label row to span
        int labelColSpan = buttonCount;
        if (buttonCount == 0) labelColSpan = 1;


        //Initialize text row
        table.row().height(TEXT_ROW_HEIGHT);
        Label contentLabel = new Label(textContent, uiSkin);
        table.add(contentLabel).colspan(labelColSpan).pad(-PADDING, PADDING, 0, PADDING).top().left();


        //Initialize button row
        if (buttonCount > 0) {
            table.row().height(BUTTON_ROW_HEIGHT);

            int buttonWidth = ((TABLE_WIDTH - (2 * PADDING)) / buttonCount) - (PADDING);
            for (int i = 0; i < buttonCount; i++) {

                final InterviewResponseButton button = buttons.get(i); //find button in array

                //Create button, and add listener for click event
                TextButton buttonElement = new TextButton(button.text, uiSkin);
                buttonElement.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        //Trigger click event handler for current button (see button definition)
                        button.eventHandler.trigger(button.result);
                    }
                });

                //Add button to table, with appropriate spacing
                table.add(buttonElement).width(buttonWidth).pad(PADDING, PADDING / 2, 0, PADDING / 2);

            }
        }

        //Pack table
        table.pack();

        return table;
    }
}
