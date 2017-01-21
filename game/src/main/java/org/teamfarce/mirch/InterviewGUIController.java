/**
 * 
 */
package org.teamfarce.mirch;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;

/**
 * @author jacobwunwin
 *
 */
public class InterviewGUIController {
	
	Skin uiSkin;
	GameSnapshot gameSnapshot;
	SpriteBatch batch;
	Stage interviewStage;
	Sprite dialogueSprite;
	
	InterviewGUIController (Skin skin, GameSnapshot gSnapshot, SpriteBatch batch){
		this.uiSkin = skin;
		this.gameSnapshot = gSnapshot;
		this.batch = batch;
		this.interviewStage = new Stage();
		Texture dialogueBackground = new Texture(Gdx.files.internal("assets/dialogue_b.png"));
		this.dialogueSprite = new Sprite(dialogueBackground);
		dialogueSprite.setPosition(220, 90);
	}
	
	/**
	 * Generates the base stage for the questions screen. This includes character images and names.
	 * @param this.interviewStage
	 * @param suspect
	 * @param player
	 */
	private void genQuestionBase(Suspect suspect, Sprite player){
		//Create buttons and labels
		Label characterName = new Label (suspect.name, uiSkin);
		Image theSuspect = new Image(new Texture(Gdx.files.internal("assets/characters/" + suspect.filename)));
		Image thePlayer = new Image (player.getTexture());
		Label playerName = new Label ("You", uiSkin);


		//textButton.setPosition(200, 200);
		characterName.setPosition(830, 630);
		characterName.setColor(Color.WHITE);
		characterName.setFontScale(1.5f);
		
	
		theSuspect.setPosition(770, 360);
		theSuspect.scaleBy(2f);
				
		thePlayer.setPosition(210, 160);
		thePlayer.scaleBy(2f);
		
		playerName.setPosition(280,  120);
		playerName.setFontScale(1.5f);
		
		
		this.interviewStage.addActor(characterName);
		this.interviewStage.addActor(thePlayer);
		this.interviewStage.addActor(theSuspect);
		this.interviewStage.addActor(playerName);
	}
	
	
	/**
	 * Generates the question response screen
	 * @param this.interviewStage
	 * @param suspect
	 * @param player
	 * @param intent
	 * @param style
	 */
	private void genQResponseScreen(Suspect suspect, Sprite player, int intent, int style){
		this.interviewStage.clear();
		
		genQuestionBase(suspect, player);
		
		String response = suspect.dialogueTree.selectStyledQuestion(intent, style, gameSnapshot.journal, suspect);
		gameSnapshot.journal.addConversation(response, suspect.name + " to You"); //add the response to the diagram
		
		Label comment = new Label (response, uiSkin);
		comment.setPosition(300, 480);
		this.interviewStage.addActor(comment);
		
		
		TextButton exitButton = new TextButton("Exit Conversation", uiSkin);
		exitButton.setPosition(500, 250);

		exitButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Exit was pressed");
				gameSnapshot.setState(GameState.map);
			}
		});
		
		TextButton ctButton = new TextButton("Continue Conversation", uiSkin);
		ctButton.setPosition(800, 250);

		ctButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Continue was pressed");
				genIntentionScreen(suspect, player);
				
			}
		});
		

		this.interviewStage.addActor(ctButton);
		this.interviewStage.addActor(exitButton);
		
		
	}
	
	/**
	 * Generates the question style selection screen UI stage
	 * @param this.interviewStage
	 * @param suspect
	 * @param player
	 */
	private void genStyleScreen(Suspect suspect, Sprite player, int intent){
		this.interviewStage.clear();
		
		Label comment = new Label ("Be careful of your phrasing...", uiSkin);
		comment.setPosition(300, 490);
		this.interviewStage.addActor(comment);
		
		genQuestionBase(suspect, player);
		
		float buttonSpace = 50;
		ArrayList<String> styles = suspect.dialogueTree.getAvailableStyles(intent);
		
		Table theTable = new Table(uiSkin);
		theTable.align(Align.left);

		Table qcontainer = new Table(uiSkin);
		ScrollPane qscroll = new ScrollPane(theTable, uiSkin);
		//scroll.setStyle("-fx-background: transparent;"); //the numpteys depreciated this very useful command to make the background transparent


		qscroll.layout();
		qcontainer.add(qscroll).width(550f).height(130f);
		qcontainer.row();
		qcontainer.setPosition(720, 270);
		
		for (int i = 0; i < styles.size(); i++){
			TextButton button = new TextButton(styles.get(i), uiSkin);

			final int k = i;
			button.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					System.out.println("Style was pressed");
					genQResponseScreen(suspect, player, intent, k);
				}
			});

			theTable.add(button);
			theTable.row();
		}
		
		this.interviewStage.addActor(qcontainer);
	}
	
	/**
	 * Generates the question intention selection screen
	 * @param this.interviewStage
	 * @param suspect
	 * @param player
	 */
	private void genIntentionScreen(Suspect suspect, Sprite player){
		this.interviewStage.clear(); //clear the stage
		
		genQuestionBase(suspect, player); //generate the base stage that we can the build off of	
		
		if (suspect.dialogueTree.getAvailableIntents().size() > 0){

			Label comment = new Label ("Go on then, ask your question.", uiSkin);
			comment.setPosition(300, 500);
			this.interviewStage.addActor(comment);
			
			Table theTable = new Table(uiSkin);
			theTable.align(Align.left);

			Table qcontainer = new Table(uiSkin);
			ScrollPane qscroll = new ScrollPane(theTable, uiSkin);
			//scroll.setStyle("-fx-background: transparent;"); //the numpteys depreciated this very useful command to make the background transparent


			qscroll.layout();
			qcontainer.add(qscroll).width(550f).height(130f);
			qcontainer.row();
			qcontainer.setPosition(720, 270);
			
			TextButton accuse = new TextButton("Accuse the Suspect", uiSkin);
			theTable.add(accuse);
			theTable.row();
			
			accuse.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					System.out.println("Button was pressed");
					gameSnapshot.setState(GameState.accuse);
				}
			});
			
			

			for (int i = 0; i < suspect.dialogueTree.getAvailableIntents().size(); i++){
				TextButton button = new TextButton(suspect.dialogueTree.getAvailableIntentsAsString().get(i), uiSkin);

				final int k = i;
				button.addListener(new ChangeListener() {
					public void changed (ChangeEvent event, Actor actor) {
						System.out.println("Button was pressed");
						gameSnapshot.journal.addConversation(suspect.dialogueTree.getAvailableIntentsAsString().get(k), "You to " + suspect.name); //add the question to the journal
						genStyleScreen(suspect, player, k);
					}
				});
				
				theTable.add(button);
				theTable.row();
				
				this.interviewStage.addActor(qcontainer);
			}
		} else {
			Label comment = new Label ("Go away, I'm not talking to you any more.", uiSkin);
			comment.setPosition(300, 500);
			this.interviewStage.addActor(comment);
			
		
			TextButton button = new TextButton("Leave the Interview", uiSkin);
			button.setPosition(500, 280);
			this.interviewStage.addActor(button);
			
			TextButton accuse = new TextButton("Accuse the Suspect", uiSkin);
			accuse.setPosition(700, 280);
			this.interviewStage.addActor(accuse);

			button.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					System.out.println("Button was pressed");
					gameSnapshot.setState(GameState.map);
				}
			});
			
			accuse.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					System.out.println("Button was pressed");
					gameSnapshot.setState(GameState.accuse);
				}
			});
			
			
		}
		
		
	}
	
	void initInterviewStage(Suspect suspect, Sprite player){
		this.genIntentionScreen(suspect, player);
	}
	
	void displayInterviewStage(){
	      this.batch.begin();
	      this.dialogueSprite.draw(batch);
	      this.batch.end();
	      this.interviewStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	      this.interviewStage.draw();
	}
}
