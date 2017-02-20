package org.teamfarce.mirch.screens.elements;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

/**
 * The InterviewResponseBox is used within the InterviewScreen
 * Used to present the player with options for responding to a Suspect
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
    private boolean isMultiRow = false;

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
     *
     * @param content    Text to display above buttons
     * @param buttonList List of InterviewResponseButtons for presenting the player with options
     * @param uiSkin     Skin used to style UI
     */
    public InterviewResponseBox(String content, ArrayList<InterviewResponseButton> buttonList, Skin uiSkin, boolean isMultiRow) {
        textContent = content;
        buttons = buttonList;
        this.uiSkin = uiSkin;
        this.isMultiRow = isMultiRow;
    }

    /**
     * This method returns a content table for the Interview screen. It does so based on what needs to be showed on it
     *
     * @return the constructed content table
     */
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
        if (buttonCount == 0 || isMultiRow) labelColSpan = 1;


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
                        button.eventHandler.trigger(button.result, button.clue);
                    }
                });

                //Add button to table, with appropriate spacing
                if (isMultiRow) {
                    table.add(buttonElement).width((TABLE_WIDTH - (2 * PADDING)) - (PADDING)).pad(PADDING, PADDING / 2, 0, PADDING / 2);
                    table.row().height(BUTTON_ROW_HEIGHT);
                } else {
                    table.add(buttonElement).width(buttonWidth).pad(PADDING, PADDING / 2, 0, PADDING / 2);
                }

            }
        }

        //Pack table
        table.pack();

        return table;
    }
}
