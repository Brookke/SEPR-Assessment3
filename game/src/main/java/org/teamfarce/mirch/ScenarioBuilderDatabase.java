package org.teamfarce.mirch;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ScenarioBuilderDatabase {
    HashMap<Integer, DataClue> means;
    HashMap<Integer, DataResource> resources;
    HashMap<Integer, DataClue> clues;
    HashMap<Integer, List<DataClue>> characterClues;
    HashMap<Integer, DataQuestioningStyle> questioningStyles;
    HashMap<Integer, DataMotive> motives;
    HashMap<Integer, DataCharacter> characters;

    public ScenarioBuilderDatabase() {
        means = new HashMap<>();
        resources = new HashMap<>();
        clues = new HashMap<>();
        characterClues = new HashMap<>();
        questioningStyles = new HashMap<>();
        motives = new HashMap<>();
        characters = new HashMap<>();
    }

    /**
     * This method directly accesses the database and gets all important information into classes
     *
     * @param databaseName - The name of the database to retrieve info from
     * @throws SQLException
     */
    public ScenarioBuilderDatabase(String databaseName) throws SQLException {
        this();

        Connection sqlConn = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
        Statement sqlStmt = sqlConn.createStatement();

        ResultSet rsResource = sqlStmt.executeQuery("SELECT * FROM resources");
        while (rsResource.next()) {
            DataResource resource = new DataResource();
            resource.id = rsResource.getInt("id");
            resource.filename = rsResource.getString("filename");
            resource.characters = new HashSet<>();
            resources.put(resource.id, resource);
        }

        ResultSet rsMeans = sqlStmt.executeQuery("SELECT * FROM clues WHERE is_means == 1");
        while (rsMeans.next()) {
            DataClue singleMeans = new DataClue();
            singleMeans.id = rsMeans.getInt("id");
            singleMeans.name = rsMeans.getString("name");
            singleMeans.description = rsMeans.getString("description");
            singleMeans.sprite = resources.get(rsMeans.getInt("resource")).filename;
            singleMeans.assetX = rsMeans.getInt("resourceX");
            singleMeans.assetY = rsMeans.getInt("resourceY");
            singleMeans.isMeans = true;
            means.put(singleMeans.id, singleMeans);
        }


        ResultSet rsClue = sqlStmt.executeQuery("SELECT * FROM clues WHERE is_means != 1");
        while (rsClue.next()) {
            DataClue clue = new DataClue();
            clue.id = rsClue.getInt("id");
            clue.name = rsClue.getString("name");
            clue.description = rsClue.getString("description");
            clue.sprite = resources.get(rsMeans.getInt("resource")).filename;
            clue.assetX = rsClue.getInt("resourceX");
            clue.assetY = rsClue.getInt("resourceY");
            clue.isMeans = false;
            clues.put(clue.id, clue);
        }

        ResultSet rsQuestioningStyle = sqlStmt.executeQuery("SELECT * FROM questioning_styles");
        while (rsQuestioningStyle.next()) {
            DataQuestioningStyle questioningStyle = new DataQuestioningStyle();
            questioningStyle.id = rsQuestioningStyle.getInt("id");
            questioningStyle.description = rsQuestioningStyle.getString("description");
            questioningStyles.put(questioningStyle.id, questioningStyle);
        }

        ResultSet rsMotive = sqlStmt.executeQuery("SELECT * FROM motives");
        while (rsMotive.next()) {
            DataMotive motive = new DataMotive();
            motive.id = rsMotive.getInt("id");
            motive.description = rsMotive.getString("description");
            motives.put(motive.id, motive);
        }

        ResultSet rsCharacterClues = sqlStmt.executeQuery("SELECT * FROM character_clues");
        while (rsCharacterClues.next()) {
            int characterId = rsCharacterClues.getInt("characters");
            DataClue clue = clues.get(rsCharacterClues.getInt("clues"));
            if (characterClues.containsKey(characterId)) {

                characterClues.get(characterId).add(clue);
            } else {
                characterClues.put(characterId, new ArrayList<DataClue>());
                characterClues.get(characterId).add(clue);
            }
        }

        ResultSet rsCharacter = sqlStmt.executeQuery("SELECT * FROM characters");
        while (rsCharacter.next()) {
            DataCharacter character = new DataCharacter();
            character.id = rsCharacter.getInt("id");
            character.name = rsCharacter.getString("name");
            character.description = rsCharacter.getString("description");
            character.selectionWeight = rsCharacter.getInt("selection_weight");
            character.spritesheet = resources.get(rsCharacter.getInt("resource_spritesheet"));
            character.posKiller = rsCharacter.getInt("posKiller") == 1;
            character.dialogue = resources.get(rsCharacter.getInt("resource_dialogue"));
            character.relatedClues = characterClues.get(character.id);
            characters.put(character.id, character);
        }


    }

    public static class DataMotive {
        public int id;
        public String description;
    }

    public class DataResource {
        public int id;
        public String filename;
        public HashSet<DataCharacter> characters;
    }

    public class DataClue {
        public int id;
        public String name;
        public String description;
        public String sprite;
        public int assetX;
        public int assetY;
        public boolean isMeans;
    }

    public class DataQuestioningStyle {
        public int id;
        public String description;
    }

    public class DataCharacter {
        public int id;
        public String name;
        public String description;
        public int selectionWeight;
        public boolean posKiller;
        public DataResource spritesheet;
        public DataResource dialogue;
        public List<DataClue> relatedClues;
    }

}
