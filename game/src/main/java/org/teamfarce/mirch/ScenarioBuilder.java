package org.teamfarce.mirch;

import java.util.ArrayList;

public class ScenarioBuilder {
    /**
     * Generates an ArrayList of suspects and returns the ArrayList.
     *
     * @return ArrayList of Suspects
     */
    public ArrayList<Suspect> generateSuspects() {
        return null;
    }

    /**
     * Chooses a suspect from the suspect list and returns the suspect object.
     *
     * @param suspectList The list of potential victims
     * @return Suspect
     */
    public Suspect chooseVictim(ArrayList<Suspect> suspectList) {
        return null;
    }

    /**
     * Randomly selects a murderer from the suspect list, marks that suspect and then returns the
     * edited list.
     *
     * @param suspectList The list of potential murderers
     * @return updates suspectList with murderer marked
     */
    public ArrayList<Suspect> chooseMurderer(ArrayList<Suspect> suspectList) {
        return suspectList;
    }

    /**
     * Generates an array list of rooms of size 'size' and returns the list.
     *
     * @param size The number of rooms to generate
     * @return ArrayList of rooms
     */
    public ArrayList<Room> generateRooms(int size) {
        return null;
    }

    /**
     * Takes an ArrayList of props and a room ID, adds additional props to the list and returns the
     * extended list.
     *
     * @param props The props which can be added into the room
     * @param roomId The room to add props to
     * @return ArrayList of Props
     */
    public ArrayList<Prop> generateProps(ArrayList<Prop> props, int roomId) {
        return null;
    }

    /**
     * Generates a clue object and returns it.
     *
     * @return Clue
     */
    public Clue generateClue() {
        return null;
    }

    /**
     * Generate the initial game state.
     *
     * @param roomNumber The number of rooms to generate
     * @return The generated initial game state
     */
    public GameSnapshot generateGame(int roomNumber/*Pass character traits here*/) {
        //Generate suspects
        ArrayList<Suspect> suspects = this.generateSuspects();

        //Choose Victim
        Suspect victim = this.chooseVictim(suspects);

        //Remove victim from suspect list
        boolean success = suspects.remove(victim);
        if (success) {
            System.out.println("Suspect removal successful");
        } else {
            System.out.println("Suspect removal failed");
        }

        //Choose Murderer
        suspects = this.chooseMurderer(suspects);

        //Choose Murder Weapon??!

        //Generate Rooms
        ArrayList<Room> rooms = this.generateRooms(roomNumber);

        //For Each Room, generate Props
        ArrayList<Prop> props = null; //create an empty list of props

        //Iterate through each room in the rooms ArrayList
        for (Room room: rooms) {
            props = this.generateProps(props, room.id);
        }

        //For Each Prop, generate Clues
        //Iterate through each prop in the props ArrayList
        for (Prop prop : props) {
            prop.clue.add(this.generateClue()); //add a clue to the prop
        }

        //Generate Detective - using character traits

        //Generate Dialogue Tree
        //Iterate through each suspect in the suspect list
        for (Suspect suspect : suspects) {
            //generate a dialogue tree for the suspect
        }

        //Produce Game Snapshot

        //Return Game Snapshot
        return null;
    }
}
