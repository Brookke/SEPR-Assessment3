package org.teamfarce.mirch;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;

public class ScenarioBuilderDatabase
{
    public HashMap<Integer, DataMeans> means;
    public HashMap<Integer, DataRoomType> roomTypes;
    public HashMap<Integer, DataResource> resources;
    public HashMap<Integer, DataClue> clues;
    public HashMap<Integer, DataQuestioningStyle> questioningStyles;
    public HashMap<Integer, DataMotive> motives;
    public HashMap<Integer, DataQuestioningIntention> questioningIntentions;
    public HashMap<Integer, DataCostume> costumes;
    public HashMap<Integer, DataProp> props;
    public HashMap<Integer, DataQuestionAndResponse> questionAndResponses;
    public HashMap<Integer, DataRoomTemplate> roomTemplates;
    public HashMap<Integer, DataCharacter> characters;
    public HashMap<Integer, DataCharacterMotiveLink> characterMotiveLinks;
    public HashMap<Integer, DataDialogueTextScreen> dialogueTextScreens;
    public HashMap<Integer, DataProtoprop> protoprops;
    public HashMap<Integer, DataCharacterMeansLink> characterMeansLinks;

    public ScenarioBuilderDatabase()
    {
        means = new HashMap<>();
        roomTypes = new HashMap<>();
        resources = new HashMap<>();
        clues = new HashMap<>();
        questioningStyles = new HashMap<>();
        motives = new HashMap<>();
        questioningIntentions = new HashMap<>();
        costumes = new HashMap<>();
        props = new HashMap<>();
        questionAndResponses = new HashMap<>();
        roomTemplates = new HashMap<>();
        characters = new HashMap<>();
        characterMotiveLinks = new HashMap<>();
        dialogueTextScreens = new HashMap<>();
        protoprops = new HashMap<>();
        characterMeansLinks = new HashMap<>();
    }

    public ScenarioBuilderDatabase(String databaseName) throws SQLException
    {
        this();

        Connection sqlConn = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
        Statement sqlStmt = sqlConn.createStatement();

        ResultSet rsMeans = sqlStmt.executeQuery("SELECT * FROM means");
        while (rsMeans.next()) {
            DataMeans singleMeans = new DataMeans();
            singleMeans.id = rsMeans.getInt("id");
            singleMeans.description = rsMeans.getString("description");
            singleMeans.characterLink = new HashSet<>();
            singleMeans.clues = new HashSet<>();
            means.put(singleMeans.id, singleMeans);
        }

        ResultSet rsRoomType = sqlStmt.executeQuery("SELECT * FROM room_types");
        while (rsRoomType.next()) {
            DataRoomType roomType = new DataRoomType();
            roomType.id = rsRoomType.getInt("id");
            roomType.name = rsRoomType.getString("name");
            roomType.minCount = rsRoomType.getInt("minimum_count");
            roomType.maxCount = rsRoomType.getInt("maximum_count");
            roomType.roomTemplates = new HashSet<>();
            roomTypes.put(roomType.id, roomType);
        }

        ResultSet rsResource = sqlStmt.executeQuery("SELECT * FROM resources");
        while (rsResource.next()) {
            DataResource resource = new DataResource();
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
            DataClue clue = new DataClue();
            clue.id = rsClue.getInt("id");
            //TODO: Update to Name column when done
            //clue.name = rsClue.getString("name");
            clue.name = "Default";
            clue.description = rsClue.getString("description");
            clue.impliesMeansRating = rsClue.getInt("implies_means_rating");
            clue.impliesMotiveRating = rsClue.getInt("implies_motive_rating");
            //TODO: set to resource when column has been added
            clue.resource = "clock.png";
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
            DataQuestioningStyle questioningStyle = new DataQuestioningStyle();
            questioningStyle.id = rsQuestioningStyle.getInt("id");
            questioningStyle.description = rsQuestioningStyle.getString("description");
            questioningStyle.questions = new HashSet<>();
            questioningStyles.put(questioningStyle.id, questioningStyle);
        }

        ResultSet rsMotive = sqlStmt.executeQuery("SELECT * FROM motives");
        while (rsMotive.next()) {
            DataMotive motive = new DataMotive();
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
            DataQuestioningIntention questioningIntention = new DataQuestioningIntention();
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
            DataCostume costume = new DataCostume();
            costume.id = rsCostume.getInt("id");
            costume.description = rsCostume.getString("description");
            costume.resource = resources.get(rsCostume.getInt("resource"));
            costume.resource.costumes.add(costume);
            costume.characters = new HashSet<>();
            costumes.put(costume.id, costume);
        }

        ResultSet rsProp = sqlStmt.executeQuery("SELECT * FROM props");
        while (rsProp.next()) {
            DataProp prop = new DataProp();
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


        ResultSet rsRoomTemplate = sqlStmt.executeQuery("SELECT * FROM room_templates");
        while (rsRoomTemplate.next()) {
            DataRoomTemplate roomTemplate = new DataRoomTemplate();
            roomTemplate.id = rsRoomTemplate.getInt("id");
            roomTemplate.width = rsRoomTemplate.getInt("width");
            roomTemplate.height = rsRoomTemplate.getInt("height");
            roomTemplate.selectionWeight = rsRoomTemplate.getInt("selection_weight");
            roomTemplate.roomType = roomTypes.get(rsRoomTemplate.getInt("room_type"));
            roomTemplate.roomType.roomTemplates.add(roomTemplate);
            roomTemplate.protoprops = new HashSet<>();
            roomTemplate.background = resources.get(rsRoomTemplate.getInt("background_resource"));
            roomTemplate.background.roomTemplates.add(roomTemplate);
            roomTemplates.put(roomTemplate.id, roomTemplate);
        }

        ResultSet rsCharacter = sqlStmt.executeQuery("SELECT * FROM characters");
        while (rsCharacter.next()) {
            DataCharacter character = new DataCharacter();
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

        ResultSet rsQuestionAndResponse = sqlStmt.executeQuery(
                "SELECT * FROM question_and_responses"
        );
        while (rsQuestionAndResponse.next()) {
            DataQuestionAndResponse questionAndResponse = new DataQuestionAndResponse();
            questionAndResponse.id = rsQuestionAndResponse.getInt("id");
            questionAndResponse.questionText = rsQuestionAndResponse.getString("question_text");
            questionAndResponse.responseText = rsQuestionAndResponse.getString("response_text");
            questionAndResponse.mustBeClue = rsQuestionAndResponse.getBoolean("must_be_clue");
            questionAndResponse.saidBy = characters.get(rsQuestionAndResponse.getInt("said_by"));
            questionAndResponse.style = questioningStyles.get(
                    rsQuestionAndResponse.getInt("question_style")
            );
            questionAndResponse.style.questions.add(questionAndResponse);
            questionAndResponse.intention = questioningIntentions.get(rsQuestionAndResponse.getInt(
                    "question_intention"
            ));
            questionAndResponse.intention.questions.add(questionAndResponse);
            questionAndResponse.followUpQuestion = new HashSet<>();
            questionAndResponse.impliesClues = new HashSet<>();
            questionAndResponse.dialogueScreens = new HashSet<>();
            questionAndResponses.put(questionAndResponse.id, questionAndResponse);
        }

        ResultSet rsCharacterMotiveLink = sqlStmt.executeQuery(
                "SELECT * FROM character_motive_links"
        );
        while (rsCharacterMotiveLink.next()) {
            DataCharacterMotiveLink characterMotiveLink = new DataCharacterMotiveLink();
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
            DataDialogueTextScreen dialogueTextScreen = new DataDialogueTextScreen();
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
            DataProtoprop protoprop = new DataProtoprop();
            protoprop.id = rsProtoprop.getInt("id");
            protoprop.x = rsProtoprop.getFloat("x_pos");
            protoprop.y = rsProtoprop.getFloat("y_pos");
            protoprop.roomTemplate = roomTemplates.get(rsProtoprop.getInt("room_template_id"));
            protoprop.roomTemplate.protoprops.add(protoprop);
            protoprop.props = new HashSet<>();
            protoprops.put(protoprop.id, protoprop);
        }

        ResultSet rsCharacterMeansLink = sqlStmt.executeQuery(
                "SELECT * FROM character_means_links"
        );
        while (rsCharacterMeansLink.next()) {
            DataCharacterMeansLink characterMeansLink = new DataCharacterMeansLink();
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
            DataClue clue = clues.get(rsClueMotiveRequirement.getInt("clue"));
            DataMotive motive = motives.get(rsClueMotiveRequirement.getInt("motive"));
            clue.motives.add(motive);
            motive.clues.add(clue);
        }

        ResultSet rsClueMeansRequirement = sqlStmt.executeQuery(
                "SELECT * FROM clue_means_requirements"
        );
        while (rsClueMeansRequirement.next()) {
            DataClue clue = clues.get(rsClueMeansRequirement.getInt("clue"));
            DataMeans singleMeans = means.get(rsClueMeansRequirement.getInt("means"));
            clue.means.add(singleMeans);
            singleMeans.clues.add(clue);
        }

        ResultSet rsCharacterCostumeLink = sqlStmt.executeQuery(
                "SELECT * FROM character_costume_links"
        );
        while (rsCharacterCostumeLink.next()) {
            DataCharacter character = characters.get(rsCharacterCostumeLink.getInt("character"));
            DataCostume costume = costumes.get(rsCharacterCostumeLink.getInt("costume"));
            character.costumes.add(costume);
            costume.characters.add(character);
        }

        ResultSet rsClueVictimRequirement = sqlStmt.executeQuery(
                "SELECT * FROM clue_victim_requirements"
        );
        while (rsClueVictimRequirement.next()) {
            DataClue clue = clues.get(rsClueVictimRequirement.getInt("clue"));
            DataCharacter victim = characters.get(rsClueVictimRequirement.getInt("victim"));
            clue.requiredAsVictims.add(victim);
            victim.requiredAsVictim.add(clue);
        }

        ResultSet rsFollowUpQuestion = sqlStmt.executeQuery(
                "SELECT * FROM follow_up_questions"
        );
        while (rsFollowUpQuestion.next()) {
            DataQuestioningIntention followingQuestion = questioningIntentions.get(
                    rsFollowUpQuestion.getInt("following_question")
            );
            DataQuestionAndResponse precedingResponse = questionAndResponses.get(
                    rsFollowUpQuestion.getInt("preceding_response")
            );
            followingQuestion.previousResponses.add(precedingResponse);
            precedingResponse.followUpQuestion.add(followingQuestion);
        }

        ResultSet rsClueMurdererRequirement = sqlStmt.executeQuery(
                "SELECT * FROM clue_murderer_requirements"
        );
        while (rsClueMurdererRequirement.next()) {
            DataClue clue = clues.get(rsClueMurdererRequirement.getInt("clue"));
            DataCharacter murderer = characters.get(rsClueMurdererRequirement.getInt("murderer"));
            clue.requiredAsMurderers.add(murderer);
            murderer.requiredAsMurderer.add(clue);
        }

        ResultSet rsPropClueImplication = sqlStmt.executeQuery(
                "SELECT * FROM prop_clue_implication"
        );
        while (rsPropClueImplication.next()) {
            DataProp prop = props.get(rsPropClueImplication.getInt("prop"));
            DataClue clue = clues.get(rsPropClueImplication.getInt("clue"));
            prop.clues.add(clue);
            clue.props.add(prop);
        }

        ResultSet rsResponseClueImplication = sqlStmt.executeQuery(
                "SELECT * FROM response_clue_implication"
        );
        while (rsResponseClueImplication.next()) {
            DataQuestionAndResponse questionAndResponse = questionAndResponses.get(
                    rsResponseClueImplication.getInt("response")
            );
            DataClue clue = clues.get(rsResponseClueImplication.getInt("clue"));
            questionAndResponse.impliesClues.add(clue);
            clue.questionAndResponses.add(questionAndResponse);
        }

        ResultSet rsPotentialPropInstances = sqlStmt.executeQuery(
                "SELECT * FROM potential_prop_instances"
        );
        while (rsPotentialPropInstances.next()) {
            DataProp prop = props.get(rsPotentialPropInstances.getInt("prop"));
            DataProtoprop protoprop = protoprops.get(rsPotentialPropInstances.getInt("protoprop"));
            prop.protoprops.add(protoprop);
            protoprop.props.add(prop);
        }
    }

    public class DataMeans
    {
        public int id;
        public String description;
        public HashSet<DataCharacterMeansLink> characterLink;
        public HashSet<DataClue> clues;
    }

    public class DataRoomType
    {
        public int id;
        public String name;
        public int minCount;
        public int maxCount;
        public HashSet<DataRoomTemplate> roomTemplates;
    }

    public class DataResource
    {
        public int id;
        public String filename;
        public HashSet<DataCharacter> characters;
        public HashSet<DataCostume> costumes;
        public HashSet<DataRoomTemplate> roomTemplates;
        public HashSet<DataProp> props;
    }

    public class DataClue
    {
        public int id;
        public String name;
        public String description;
        public String resource;
        public int impliesMeansRating;
        public int impliesMotiveRating;
        public HashSet<DataProp> props;
        public HashSet<DataMotive> motives;
        public HashSet<DataMeans> means;
        public HashSet<DataCharacter> requiredAsMurderers;
        public HashSet<DataCharacter> requiredAsVictims;
        public HashSet<DataQuestionAndResponse> questionAndResponses;
    }

    public class DataQuestioningStyle
    {
        public int id;
        public String description;
        public HashSet<DataQuestionAndResponse> questions;
    }

    public class DataMotive
    {
        public int id;
        public String description;
        public HashSet<DataCharacterMotiveLink> characterMotiveLink;
        public HashSet<DataClue> clues;
    }

    public class DataQuestioningIntention
    {
        public int id;
        public String description;
        public boolean startingQuestion;
        public HashSet<DataQuestionAndResponse> previousResponses;
        public HashSet<DataQuestionAndResponse> questions;
    }

    public class DataCostume
    {
        public int id;
        public String description;
        public DataResource resource;
        public HashSet<DataCharacter> characters;
    }

    public class DataProp
    {
        public int id;
        public String name;
        public String description;
        public boolean mustBeClue;
        public DataResource resource;
        public HashSet<DataClue> clues;
        public HashSet<DataProtoprop> protoprops;
    }

    public class DataQuestionAndResponse
    {
        public int id;
        public String questionText;
        public String responseText;
        public boolean mustBeClue;
        public DataQuestioningStyle style;
        public DataQuestioningIntention intention;
        public HashSet<DataQuestioningIntention> followUpQuestion;
        public HashSet<DataClue> impliesClues;
        public HashSet<DataDialogueTextScreen> dialogueScreens;
        public DataCharacter saidBy;
    }

    public class DataRoomTemplate
    {
        public int id;
        public int width;
        public int height;
        public int selectionWeight;
        public DataRoomType roomType;
        public HashSet<DataProtoprop> protoprops;
        public DataResource background;
    }

    public class DataCharacter
    {
        public int id;
        public String name;
        public String description;
        public int selectionWeight;
        public DataResource resource;
        public HashSet<DataCostume> costumes;
        public HashSet<DataCharacterMotiveLink> murdererMotiveLink;
        public HashSet<DataCharacterMotiveLink> victimMotiveLink;
        public HashSet<DataCharacterMeansLink> meansLink;
        public HashSet<DataClue> requiredAsMurderer;
        public HashSet<DataClue> requiredAsVictim;
        public HashSet<DataQuestionAndResponse> responses;
    }

    public class DataCharacterMotiveLink
    {
        public int id;
        public int selectionWeight;
        public DataCharacter murderer;
        public DataCharacter victim;
        public DataMotive motive;
    }

    public class DataDialogueTextScreen
    {
        public int id;
        public String text;
        public int order;
        public DataQuestionAndResponse dialogue;
    }

    public class DataProtoprop
    {
        public int id;
        public float x;
        public float y;
        public DataRoomTemplate roomTemplate;
        public HashSet<DataProp> props;
    }

    public class DataCharacterMeansLink
    {
        public int id;
        public int selectionWeight;
        public DataCharacter character;
        public DataMeans means;
    }
}
