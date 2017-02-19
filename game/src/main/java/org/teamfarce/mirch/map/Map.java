package org.teamfarce.mirch.map;


import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Vector2Int;
import org.teamfarce.mirch.entities.Direction;
import org.teamfarce.mirch.entities.Suspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * The map is a collection of Rooms , it links them all together.
 */
public class Map {
    MIRCH game;

    List<Room> rooms = new ArrayList<Room>();

    public Map(MIRCH game) {
        this.game = game;
    }

    /**
     * This function initialises all the rooms of the Ron Cooke Hub and their transitions
     */
    public List<Room> initialiseRooms() {

        Room mainRoom = new Room(0, "mainroom.tmx", "Main Foyer");

        Room rch037 = new Room(1, "rch037.tmx", "RCH/037 Lecture Theatre");

        Room portersOffice = new Room(2, "portersoffice.tmx", "Porters Office");

        Room kitchen = new Room(3, "kitchen.tmx", "Kitchen");

        Room islandOfInteraction = new Room(4, "islandofinteraction.tmx", "Island of Interaction");

        Room toilet = new Room(5, "toilet.tmx", "Toilet");

        Room computerRoom = new Room(6, "computerroom.tmx", "Computer Room");

        Room lakeHouse = new Room(7, "lakehouse.tmx", "Lakehouse");

        Room outside = new Room(8, "outside.tmx", "Outside Ron Cooke Hub");

        Room pod = new Room(9, "pod.tmx", "Pod");


        mainRoom.addTransition(new Room.Transition().setFrom(17, 17).setTo(portersOffice, 1, 5, Direction.EAST))    //To Porters Office

                .addTransition(new Room.Transition().setFrom(27, 13).setTo(kitchen, 1, 3, Direction.EAST))    //To Kitchen

                .addTransition(new Room.Transition().setFrom(26, 29).setTo(islandOfInteraction, 11, 14, Direction.WEST))    //To Island of Interaction

                .addTransition(new Room.Transition().setFrom(18, 2).setTo(toilet, 1, 1, Direction.EAST))    //To Toilet

                .addTransition(new Room.Transition().setFrom(2, 8).setTo(computerRoom, 2, 2, Direction.EAST))      //To Computer Room

                .addTransition(new Room.Transition().setFrom(3, 5).setTo(outside, 19, 4, Direction.SOUTH)) //To Outside
                .addTransition(new Room.Transition().setFrom(4, 5).setTo(outside, 20, 4, Direction.SOUTH)) //To Outside

                .addTransition(new Room.Transition().setFrom(11, 1).setTo(rch037, 2, 5, Direction.SOUTH))  //To RCH/037
                .addTransition(new Room.Transition().setFrom(12, 1).setTo(rch037, 3, 5, Direction.SOUTH));  //To RCH/037

        rch037.addTransition(new Room.Transition().setFrom(2, 5).setTo(mainRoom, 11, 1, Direction.NORTH))  //To Main Room
                .addTransition(new Room.Transition().setFrom(3, 5).setTo(mainRoom, 12, 1, Direction.NORTH))  //To Main Room

                .addTransition(new Room.Transition().setFrom(11, 17).setTo(computerRoom, 7, 1, Direction.NORTH))    //To Computer Room
                .addTransition(new Room.Transition().setFrom(12, 17).setTo(computerRoom, 8, 1, Direction.NORTH));  //To Computer Room

        portersOffice.addTransition(new Room.Transition().setFrom(1, 5).setTo(mainRoom, 17, 17, Direction.WEST));  //To Main Room

        kitchen.addTransition(new Room.Transition().setFrom(1, 3).setTo(mainRoom, 27, 13, Direction.WEST));  //To Main Room

        islandOfInteraction.addTransition(new Room.Transition().setFrom(11, 14).setTo(mainRoom, 26, 29, Direction.WEST));  //To Main Room

        toilet.addTransition(new Room.Transition().setFrom(1, 1).setTo(mainRoom, 18, 2, Direction.WEST));  //To Main Room

        computerRoom.addTransition(new Room.Transition().setFrom(13, 11).setTo(lakeHouse, 11, 1, Direction.NORTH))    //To Lakehouse

                .addTransition(new Room.Transition().setFrom(7, 1).setTo(rch037, 11, 17, Direction.SOUTH))    //To RCH/037
                .addTransition(new Room.Transition().setFrom(8, 1).setTo(rch037, 12, 17, Direction.SOUTH))    //To RCH/037

                .addTransition(new Room.Transition().setFrom(2, 2).setTo(mainRoom, 2, 8, Direction.EAST));  //To Main Room

        lakeHouse.addTransition(new Room.Transition().setFrom(11, 1).setTo(computerRoom, 13, 11, Direction.WEST));  //To Computer Room

        outside.addTransition(new Room.Transition().setFrom(19, 4).setTo(mainRoom, 3, 5, Direction.NORTH))    //To Main Room
                .addTransition(new Room.Transition().setFrom(20, 4).setTo(mainRoom, 4, 5, Direction.NORTH))    //To Main Room

                .addTransition(new Room.Transition().setFrom(9, 11).setTo(pod, 18, 9, Direction.WEST))    //To Pod
                .addTransition(new Room.Transition().setFrom(9, 12).setTo(pod, 18, 10, Direction.WEST));  //To Pod

        pod.addTransition(new Room.Transition().setFrom(18, 9).setTo(outside, 9, 11, Direction.EAST))    //To Outside
                .addTransition(new Room.Transition().setFrom(18, 10).setTo(outside, 9, 12, Direction.EAST));  //To Outside

        List<Room> rooms = Arrays.asList(mainRoom, rch037, portersOffice, kitchen, islandOfInteraction, toilet, computerRoom, lakeHouse, outside, pod);

        /**
         * Assign the murder room
         */
        rooms.get(new Random().nextInt(rooms.size())).setMurderRoom();

        this.rooms = rooms;

        return rooms;
    }

    /**
     * This method returns a list of NPCs that are in the defined Room parameter
     *
     * @param room - The room to check
     * @return List<Suspect> The suspects that are in the room
     */
    public List<Suspect> getNPCs(Room room) {
        List<Suspect> npcsInRoom = new ArrayList<Suspect>();

        for (Suspect s : game.gameSnapshot.getSuspects()) {
            if (s.getRoom().getID() == room.getID()) {
                npcsInRoom.add(s);
            }
        }

        return npcsInRoom;
    }

    /**
     * This method takes a list of NPCs and then randomly distibutes them around the rooms of the map
     *
     * @param NPCs - The NPCs to distribute
     */
    public void placeNPCsInRooms(List<Suspect> NPCs) {
        int amountOfRooms = rooms.size();

        List<Integer> roomsLeft = new ArrayList<>();

        for (int i = 0; i < amountOfRooms; i++) {
            roomsLeft.add(i);
        }

        for (Suspect loopNpc : NPCs) {
            /*
            Refill the rooms left list if there are more NPCs than Rooms. This will put AT LEAST one NPC per room if so.
             */
            if (roomsLeft.isEmpty()) {
                for (int i = 0; i < amountOfRooms; i++) {
                    roomsLeft.add(i);
                }
            }

            /*
            Pick a random room and put that NPC in it
             */
            int toTake = new Random().nextInt(roomsLeft.size());
            int selectedRoom = roomsLeft.get(toTake);
            roomsLeft.remove(toTake);

            loopNpc.setRoom(rooms.get(selectedRoom));
            Vector2Int position = loopNpc.getRoom().getRandomLocation();
            loopNpc.setTileCoordinates(position.x, position.y);

            System.out.println(loopNpc.getName() + " has been placed in room " + selectedRoom + " at " + position);
        }
    }

    /**
     * This method returns all the rooms in the map
     *
     * @return the rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }
}
