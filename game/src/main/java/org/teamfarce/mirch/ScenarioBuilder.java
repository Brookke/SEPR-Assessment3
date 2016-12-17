package org.teamfarce.mirch;

import java.util.ArrayList;

import org.junit.Test;

public class ScenarioBuilder {
	/**
	 * 
	 * @return ArrayList of Suspects
	 * 
	 * Generates an ArrayList of suspects and returns the ArrayList
	 */
	public ArrayList<Suspect> generateSuspects(){
		return null;
	}
	
	/**
	 * 
	 * @param suspectList
	 * @return Suspect
	 * 
	 * Chooses a suspect from the suspect list and returns the Suspect Object
	 */
	public Suspect chooseVictim(ArrayList<Suspect> suspectList){
		
		return null;
	}
	
	/**
	 * 
	 * @param suspectList
	 * @return updates suspectList with murderer marked
	 * 
	 * Randomly selects a murderer from the suspect list, and marks that suspect
	 * Then returns the edited list
	 */
	public ArrayList<Suspect> chooseMurderer(ArrayList<Suspect> suspectList){
		
		return suspectList;
	}
	
	/**
	 * 
	 * @param size
	 * @return ArrayList of rooms
	 * 
	 * Generates an array list of rooms of size 'size' and returns the list
	 */
	public ArrayList<Room> generateRooms(int size){
		return null;
	}
	
	/**
	 * 
	 * @param props
	 * @param roomID
	 * @return ArrayList of Props
	 * 
	 * Takes an ArrayList of props and a room ID, adds additional props to the list and returns the extended list
	 */
	public ArrayList<Prop> generateProps(ArrayList<Prop> props, int roomID){
		
		return null;
	}
	
	/**
	 * 
	 * @return Clue
	 * 
	 * Generates a clue object and returns it
	 */
	public Clue generateClue(){
		return null;
	}
	
	
	public GameSnapshot generateGame(int roomNumber/*Pass character traits here*/){
		//Generate suspects
		ArrayList<Suspect> suspects = this.generateSuspects();
		
		//Choose Victim
		Suspect victim = this.chooseVictim(suspects);
		
		//Remove victim from suspect list
		boolean success = suspects.remove(victim);
		if (success){
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
		for (Room room: rooms){
			props = this.generateProps(props, room.id);
		}
		
		//For Each Prop, generate Clues
		//Iterate through each prop in the props ArrayList
		for (Prop prop : props){
			prop.clue.add(this.generateClue()); //add a clue to the prop
		}
		
		//Generate Detective - using character traits
		
		//Generate Dialogue Tree
		//Iterate through each suspect in the suspect list
		for (Suspect suspect : suspects){
			//generate a dialogue tree for the suspect
		}
		
		//Produce Game Snapshot
		
		//Return Game Snapshot
		return null;
		
	}
	
	@Test
	public void testGenerateGame(){
		this.generateGame(10);
		//temporary output until test is fully built
		System.out.println("Generate Game Successful");
	} 

}
