package ch.heigvd.dai;

import java.io.*;
import java.util.Vector;


public class Rooms {
    public static final String roomsLocation = "data/rooms";
    Vector<Room> rooms = new Vector<Room>();

    public Rooms() {
        File roomFolder = new File(roomsLocation);
        if (!roomFolder.exists()) {
            roomFolder.mkdir();
        }
        for (File roomFile : roomFolder.listFiles()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(roomFile));){
                String name = roomFile.getName();
                String password = reader.readLine();
                rooms.add(new Room(name, password));
            } catch (Exception e) {
                System.out.println("Impossible de charger la salle :" + e.toString());
            }
        }
    }

    public Room find(String roomName) {
        for (Room existingRoom : this.rooms) {
            if (existingRoom.getName().equals(roomName)) {
                return existingRoom;
            }
        }
        return null;
    }

    public boolean add(Room room) {
        // Vérifiez si une salle avec le même nom existe déjà
        if (find(room.getName()) != null) {
            return false;
        }

        if (rooms.add(room)) {
            return addRoomToFile(room);
        } else {
            return false;
        }
    }

    private synchronized static Boolean addRoomToFile(Room room) {
        File roomFile = new File(room.getRoomPath());
        try {
            roomFile.createNewFile();
            try (java.io.BufferedWriter writer = new BufferedWriter(new FileWriter(roomFile));){
                writer.write(room.getPassword() + "\n");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                roomFile.delete();
            }
        } catch (Exception e) {
            System.out.println("Impossible de créer la salle :" + e.toString());
            roomFile.delete();
            return false;
        }
        return false;
    }
}
