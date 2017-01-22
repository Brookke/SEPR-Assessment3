package org.teamfarce.mirch;

import java.util.HashSet;
import java.util.HashMap;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.sqlite.JDBC;

public class ScenarioBuilderDatabase {
    public class Means {
        public int id;
        public String description;
        public HashSet<CharacterMeansLink> characterLink;
        public HashSet<Clue> clues;
    }

    public class RoomType {
        public int id;
        public String name;
        public int minCount;
        public int maxCount;
        public HashSet<RoomTemplate> roomTemplates;
    }

    public class Resource {
        public int id;
        public String filename;
        public HashSet<Character> characters;
        public HashSet<Costume> costumes;
        public HashSet<RoomTemplate> roomTemplates;
        public HashSet<Prop> props;
    }

    public class Clue {
        public int id;
        public String description;
        public int impliesMeansRating;
        public int impliesMotiveRating;
        public HashSet<Prop> props;
        public HashSet<Motive> motives;
        public HashSet<Means> means;
        public HashSet<Character> requiredAsMurderers;
        public HashSet<Character> requiredAsVictims;
        public HashSet<QuestionAndResponse> questionAndResponses;
    }

    public class QuestioningStyle {
        public int id;
        public String description;
        public HashSet<QuestionAndResponse> questions;
    }

    public class Motive {
        public int id;
        public String description;
        public HashSet<CharacterMotiveLink> characterMotiveLink;
        public HashSet<Clue> clues;
    }

    public class QuestioningIntention {
        public int id;
        public String description;
        public boolean startingQuestion;
        public HashSet<QuestionAndResponse> previousResponses;
        public HashSet<QuestionAndResponse> questions;
    }

    public class Costume {
        public int id;
        public String description;
        public Resource resource;
        public HashSet<Character> characters;
    }

    public class Prop {
        public int id;
        public String name;
        public String description;
        public boolean mustBeClue;
        public Resource resource;
        public HashSet<Clue> clues;
        public HashSet<Protoprop> protoprops;
    }

    public class QuestionAndResponse {
        public int id;
        public String question_text;
        public String response_text;
        public boolean mustBeClue;
        public QuestioningStyle style;
        public QuestioningIntention intention;
        public HashSet<QuestioningIntention> followUpQuestion;
        public HashSet<Clue> impliesClues;
        public HashSet<DialogueTextScreen> dialogueScreens;
        public HashSet<Character> saidBy;
    }

    public class RoomTemplate {
        public int id;
        public int width;
        public int height;
        public int selectionWeight;
        public RoomType roomType;
        public HashSet<Protoprop> protoprops;
        public Resource background;
    }

    public class Character {
        public int id;
        public String name;
        public String description;
        public int selectionWeight;
        public Resource resource;
        public HashSet<Costume> costumes;
        public HashSet<CharacterMotiveLink> murdererMotiveLink;
        public HashSet<CharacterMotiveLink> victimMotiveLink;
        public HashSet<CharacterMeansLink> meansLink;
        public HashSet<Clue> requiredAsMurderer;
        public HashSet<Clue> requiredAsVictim;
        public HashSet<QuestionAndResponse> responses;
    }

    public class CharacterMotiveLink {
        public int id;
        public int selectionWeight;
        public Character murderer;
        public Character victim;
        public Motive motive;
    }

    public class DialogueTextScreen {
        public int id;
        public String text;
        public int order;
        public QuestionAndResponse dialogue;
    }

    public class Protoprop {
        public int id;
        public float x;
        public float y;
        public RoomTemplate roomTemplate;
        public HashSet<Prop> props;
    }

    public class CharacterMeansLink {
        public int id;
        public int selectionWeight;
        public Character character;
        public Means means;
    }

    public HashMap<Integer, Means> means;
    public HashMap<Integer, RoomType> roomTypes;
    public HashMap<Integer, Resource> resources;
    public HashMap<Integer, Clue> clues;
    public HashMap<Integer, QuestioningStyle> questioningStyles;
    public HashMap<Integer, Motive> motives;
    public HashMap<Integer, QuestioningIntention> questioningIntentions;
    public HashMap<Integer, Costume> costumes;
    public HashMap<Integer, Prop> props;
    public HashMap<Integer, QuestionAndResponse> questionAndResponses;
    public HashMap<Integer, RoomTemplate> roomTemplates;
    public HashMap<Integer, Character> characters;
    public HashMap<Integer, CharacterMotiveLink> characterMotiveLinks;
    public HashMap<Integer, DialogueTextScreen> dialogueTextScreens;
    public HashMap<Integer, Protoprop> protoprops;
    public HashMap<Integer, CharacterMeansLink> characterMeansLinks;

    public ScenarioBuilderDatabase() {
        means = new HashMap<Integer, Means>();
        roomTypes = new HashMap<Integer, RoomType>();
        resources = new HashMap<Integer, Resource>();
        clues = new HashMap<Integer, Clue>();
        questioningStyles = new HashMap<Integer, QuestioningStyle>();
        motives = new HashMap<Integer, Motive>();
        questioningIntentions = new HashMap<Integer, QuestioningIntention>();
        costumes = new HashMap<Integer, Costume>();
        props = new HashMap<Integer, Prop>();
        questionAndResponses = new HashMap<Integer, QuestionAndResponse>();
        roomTemplates = new HashMap<Integer, RoomTemplate>();
        characters = new HashMap<Integer, Character>();
        characterMotiveLinks = new HashMap<Integer, CharacterMotiveLink>();
        dialogueTextScreens = new HashMap<Integer, DialogueTextScreen>();
        protoprops = new HashMap<Integer, Protoprop>();
        characterMeansLinks = new HashMap<Integer, CharacterMeansLink>();
    }

    public ScenarioBuilderDatabase(String databaseName) throws SQLException {
        this();

        Connection sqlConn = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
        Statement sqlStmt = sqlConn.createStatement();

        ResultSet rsMeans = sqlStmt.executeQuery("SELECT * FROM means");
        while (rsMeans.next()) {
            Means singleMeans = new Means();
            singleMeans.id = rsMeans.getInt("id");
            singleMeans.description = rsMeans.getString("description");
            singleMeans.characterLink = new HashSet<>();
            singleMeans.clues = new HashSet<>();
            means.put(singleMeans.id, singleMeans);
        }

        ResultSet rsRoomType = sqlStmt.executeQuery("SELECT * FROM room_types");
        while (rsRoomType.next()) {
            RoomType roomType = new RoomType();
            roomType.id = rsRoomType.getInt("id");
            roomType.name = rsRoomType.getString("name");
            roomType.minCount = rsRoomType.getInt("minimum_count");
            roomType.maxCount = rsRoomType.getInt("maximum_count");
            roomType.roomTemplates = new HashSet<>();
            roomTypes.put(roomType.id, roomType);
        }

        ResultSet rsResource = sqlStmt.executeQuery("SELECT * FROM resources");
        while (rsResource.next()) {
            Resource resource = new Resource();
            resource.id = rsResource.getInt("id");
            resource.filename = rsResource.getString("filename");
            resource.characters = new HashSet<>();
            resource.costumes = new HashSet<>();
            resource.roomTemplates = new HashSet<>();
            resource.props = new HashSet<>();
            resources.put(resource.id, resource);
        }

        ResultSet rsClue = sqlStmt.executeQuery("SELECT * FROM clues");
        while (rsClue.next()) {
            Clue clue = new Clue();
            clue.id = rsClue.getInt("id");
            clue.description = rsClue.getString("description");
            clue.impliesMeansRating = rsClue.getInt("implies_means_rating");
            clue.impliesMotiveRating = rsClue.getInt("implies_motive_rating");
            clue.props = new HashSet<>();
            clue.motives = new HashSet<>();
            clue.means = new HashSet<>();
            clue.requiredAsMurderers = new HashSet<>();
            clue.requiredAsVictims = new HashSet<>();
            clue.questionAndResponses = new HashSet<>();
            clues.put(clue.id, clue);
        }

        ResultSet rsQuestioningStyle = sqlStmt.executeQuery("SELECT * FROM questioning_styles");
        while (rsQuestioningStyle.next()) {
            QuestioningStyle questioningStyle = new QuestioningStyle();
            questioningStyle.id = rsQuestioningStyle.getInt("id");
            questioningStyle.description = rsQuestioningStyle.getString("description");
            questioningStyles.put(questioningStyle.id, questioningStyle);
        }

        ResultSet rsMotive = sqlStmt.executeQuery("SELECT * FROM motives");
        while (rsMotive.next()) {
            Motive motive = new Motive();
            motive.id = rsMotive.getInt("id");
            motive.description = rsMotive.getString("description");
            motive.characterMotiveLink = new HashSet<>();
            motive.clues = new HashSet<>();
            motives.put(motive.id, motive);
        }

        ResultSet rsQuestioningIntention = sqlStmt.executeQuery(
            "SELECT * FROM question_intentions"
        );
        while (rsQuestioningIntention.next()) {
            QuestioningIntention questioningIntention = new QuestioningIntention();
            questioningIntention.id = rsQuestioningIntention.getInt("id");
            questioningIntention.description = rsQuestioningIntention.getString("description");
            questioningIntention.startingQuestion = rsQuestioningIntention.getBoolean(
                "starting_question"
            );
            questioningIntention.previousResponses = new HashSet<>();
            questioningIntention.questions = new HashSet<>();
            questioningIntentions.put(questioningIntention.id, questioningIntention);
        }

        ResultSet rsCostume = sqlStmt.executeQuery("SELECT * FROM costumes");
        while (rsCostume.next()) {
            Costume costume = new Costume();
            costume.id = rsCostume.getInt("id");
            costume.description = rsCostume.getString("description");
            costume.resource = resources.get(rsCostume.getInt("resource"));
            costume.resource.costumes.add(costume);
            costume.characters = new HashSet<>();
            costumes.put(costume.id, costume);
        }

        ResultSet rsProp = sqlStmt.executeQuery("SELECT * FROM props");
        while (rsProp.next()) {
            Prop prop = new Prop();
            prop.id = rsProp.getInt("id");
            prop.name = rsProp.getString("name");
            prop.description = rsProp.getString("description");
            prop.mustBeClue = rsProp.getBoolean("must_be_clue");
            prop.resource = resources.get(rsProp.getInt("resource"));
            prop.resource.props.add(prop);
            prop.clues = new HashSet<>();
            prop.protoprops = new HashSet<>();
            props.put(prop.id, prop);
        }

        ResultSet rsQuestionAndResponse = sqlStmt.executeQuery(
            "SELECT * FROM question_and_responses"
        );
        while (rsQuestionAndResponse.next()) {
            QuestionAndResponse questionAndResponse = new QuestionAndResponse();
            questionAndResponse.id = rsProp.getInt("id");
            questionAndResponse.question_text = rsProp.getString("question_text");
            questionAndResponse.response_text = rsProp.getString("response_text");
            questionAndResponse.mustBeClue = rsProp.getBoolean("must_be_clue");
            questionAndResponse.style = questioningStyles.get(rsProp.getInt("question_style"));
            questionAndResponse.style.questions.add(questionAndResponse);
            questionAndResponse.intention = questioningIntentions.get(rsProp.getInt(
                "question_intention"
            ));
            questionAndResponse.intention.questions.add(questionAndResponse);
            questionAndResponse.followUpQuestion = new HashSet<>();
            questionAndResponse.impliesClues = new HashSet<>();
            questionAndResponse.dialogueScreens = new HashSet<>();
            questionAndResponse.saidBy = new HashSet<>();
            questionAndResponses.put(questionAndResponse.id, questionAndResponse);
        }

        ResultSet rsRoomTemplate = sqlStmt.executeQuery("SELECT * FROM room_templates");
        while (rsRoomTemplate.next()) {
            RoomTemplate roomTemplate = new RoomTemplate();
            roomTemplate.id = rsRoomTemplate.getInt("id");
            roomTemplate.width = rsRoomTemplate.getInt("width");
            roomTemplate.height = rsRoomTemplate.getInt("height");
            roomTemplate.selectionWeight = rsRoomTemplate.getInt("selection_weight");
            roomTemplate.roomType = roomTypes.get(rsRoomTemplate.getInt("room_type"));
            roomTemplate.roomType.roomTemplates.add(roomTemplate);
            roomTemplate.protoprops = new HashSet<>();
            roomTemplate.background = resources.get(rsRoomTemplate.getInt("background"));
            roomTemplate.background.roomTemplates.add(roomTemplate);
            roomTemplates.put(roomTemplate.id, roomTemplate);
        }

        ResultSet rsCharacter = sqlStmt.executeQuery("SELECT * FROM characters");
        while (rsCharacter.next()) {
            Character character = new Character();
            character.id = rsCharacter.getInt("id");
            character.name = rsCharacter.getString("name");
            character.description = rsCharacter.getString("description");
            character.selectionWeight = rsCharacter.getInt("selection_weight");
            character.resource = resources.get(rsCharacter.getInt("resource"));
            character.resource.characters.add(character);
            character.costumes = new HashSet<>();
            character.murdererMotiveLink = new HashSet<>();
            character.victimMotiveLink = new HashSet<>();
            character.meansLink = new HashSet<>();
            character.requiredAsMurderer = new HashSet<>();
            character.requiredAsVictim = new HashSet<>();
            character.responses = new HashSet<>();
            characters.put(character.id, character);
        }

        ResultSet rsCharacterMotiveLink = sqlStmt.executeQuery(
            "SELECT * FROM character_motive_links"
        );
        while (rsCharacterMotiveLink.next()) {
            CharacterMotiveLink characterMotiveLink = new CharacterMotiveLink();
            characterMotiveLink.id = rsCharacterMotiveLink.getInt("id");
            characterMotiveLink.selectionWeight = rsCharacterMotiveLink.getInt("selection_weight");
            characterMotiveLink.murderer = characters.get(rsCharacterMotiveLink.getInt(
                "murderer"
            ));
            characterMotiveLink.murderer.murdererMotiveLink.add(characterMotiveLink);
            characterMotiveLink.victim = characters.get(rsCharacterMotiveLink.getInt("victim"));
            characterMotiveLink.victim.victimMotiveLink.add(characterMotiveLink);
            characterMotiveLink.motive = motives.get(rsCharacterMotiveLink.getInt("motive"));
            characterMotiveLink.motive.characterMotiveLink.add(characterMotiveLink);
            characterMotiveLinks.put(characterMotiveLink.id, characterMotiveLink);
        }

        ResultSet rsDialogueTextScreen = sqlStmt.executeQuery(
            "SELECT * FROM dialogue_text_screens"
        );
        while (rsDialogueTextScreen.next()) {
            DialogueTextScreen dialogueTextScreen = new DialogueTextScreen();
            dialogueTextScreen.id = rsDialogueTextScreen.getInt("id");
            dialogueTextScreen.text = rsDialogueTextScreen.getString("text");
            dialogueTextScreen.order = rsDialogueTextScreen.getInt("order");
            dialogueTextScreen.dialogue = questionAndResponses.get(rsDialogueTextScreen.getInt(
                "dialogue"
            ));
            dialogueTextScreen.dialogue.dialogueScreens.add(dialogueTextScreen);
            dialogueTextScreens.put(dialogueTextScreen.id, dialogueTextScreen);
        }

        ResultSet rsProtoprop = sqlStmt.executeQuery("SELECT * FROM protoprops");
        while (rsProtoprop.next()) {
            Protoprop protoprop = new Protoprop();
            protoprop.id = rsProtoprop.getInt("id");
            protoprop.x = rsProtoprop.getFloat("x_pos");
            protoprop.y = rsProtoprop.getFloat("y_pos");
            protoprop.roomTemplate = roomTemplates.get(rsProtoprop.getInt("room_template"));
            protoprop.roomTemplate.protoprops.add(protoprop);
            protoprop.props = new HashSet<>();
            protoprops.put(protoprop.id, protoprop);
        }

        ResultSet rsCharacterMeansLink = sqlStmt.executeQuery(
            "SELECT * FROM character_means_links"
        );
        while (rsCharacterMeansLink.next()) {
            CharacterMeansLink characterMeansLink = new CharacterMeansLink();
            characterMeansLink.id = rsCharacterMeansLink.getInt("id");
            characterMeansLink.selectionWeight = rsCharacterMeansLink.getInt("selection_weight");
            characterMeansLink.character = characters.get(rsCharacterMeansLink.getInt(
                "character"
            ));
            characterMeansLink.character.meansLink.add(characterMeansLink);
            characterMeansLink.means = means.get(rsCharacterMeansLink.getInt("means"));
            characterMeansLink.means.characterLink.add(characterMeansLink);
            characterMeansLinks.put(characterMeansLink.id, characterMeansLink);
        }

        ResultSet rsClueMotiveRequirement = sqlStmt.executeQuery(
            "SELECT * FROM clue_motive_requirement"
        );
        while (rsClueMotiveRequirement.next()) {
            Clue clue = clues.get(rsClueMotiveRequirement.getInt("clue"));
            Motive motive = motives.get(rsClueMotiveRequirement.getInt("motive"));
            clue.motives.add(motive);
            motive.clues.add(clue);
        }

        ResultSet rsClueMeansRequirement = sqlStmt.executeQuery(
            "SELECT * FROM clue_means_requirements"
        );
        while (rsClueMeansRequirement.next()) {
            Clue clue = clues.get(rsClueMeansRequirement.getInt("clue"));
            Means singleMeans = means.get(rsClueMeansRequirement.getInt("means"));
            clue.means.add(singleMeans);
            singleMeans.clues.add(clue);
        }

        ResultSet rsCharacterCostumeLink = sqlStmt.executeQuery(
            "SELECT * FROM character_costume_links"
        );
        while (rsCharacterCostumeLink.next()) {
            Character character = characters.get(rsCharacterCostumeLink.getInt("character"));
            Costume costume = costumes.get(rsCharacterCostumeLink.getInt("costume"));
            character.costumes.add(costume);
            costume.characters.add(character);
        }

        ResultSet rsClueVictimRequirement = sqlStmt.executeQuery(
            "SELECT * FROM clue_victim_requirements"
        );
        while (rsClueVictimRequirement.next()) {
            Clue clue = clues.get(rsClueVictimRequirement.getInt("clue"));
            Character victim = characters.get(rsClueVictimRequirement.getInt("victim"));
            clue.requiredAsVictims.add(victim);
            victim.requiredAsVictim.add(clue);
        }

        ResultSet rsFollowUpQuestion = sqlStmt.executeQuery(
            "SELECT * FROM follow_up_questions"
        );
        while (rsFollowUpQuestion.next()) {
            QuestioningIntention followingQuestion = questioningIntentions.get(
                rsFollowUpQuestion.getInt("following_question")
            );
            QuestionAndResponse precedingResponse = questionAndResponses.get(
                rsFollowUpQuestion.getInt("preceding_response")
            );
            followingQuestion.previousResponses.add(precedingResponse);
            precedingResponse.followUpQuestion.add(followingQuestion);
        }

        ResultSet rsClueMurdererRequirement = sqlStmt.executeQuery(
            "SELECT * FROM clue_murderer_requirements"
        );
        while (rsClueMurdererRequirement.next()) {
            Clue clue = clues.get(rsClueMurdererRequirement.getInt("clue"));
            Character murderer = characters.get(rsClueMurdererRequirement.getInt("murderer"));
            clue.requiredAsMurderers.add(murderer);
            murderer.requiredAsMurderer.add(clue);
        }

        ResultSet rsPropClueImplication = sqlStmt.executeQuery(
            "SELECT * FROM prop_clue_implication"
        );
        while (rsPropClueImplication.next()) {
            Prop prop = props.get(rsPropClueImplication.getInt("prop"));
            Clue clue = clues.get(rsPropClueImplication.getInt("clue"));
            prop.clues.add(clue);
            clue.props.add(prop);
        }

        ResultSet rsResponseClueImplication = sqlStmt.executeQuery(
            "SELECT * FROM response_clue_implication"
        );
        while (rsResponseClueImplication.next()) {
            QuestionAndResponse questionAndResponse = questionAndResponses.get(
                rsResponseClueImplication.getInt("response")
            );
            Clue clue = clues.get(rsResponseClueImplication.getInt("clue"));
            questionAndResponse.impliesClues.add(clue);
            clue.questionAndResponses.add(questionAndResponse);
        }

        ResultSet rsPotentialPropInstances = sqlStmt.executeQuery(
            "SELECT * FROM potential_prop_instances"
        );
        while (rsPotentialPropInstances.next()) {
            Prop prop = props.get(rsPotentialPropInstances.getInt("prop"));
            Protoprop protoprop = protoprops.get(rsPotentialPropInstances.getInt("protoprop"));
            prop.protoprops.add(protoprop);
            protoprop.props.add(prop);
        }
    }
}
