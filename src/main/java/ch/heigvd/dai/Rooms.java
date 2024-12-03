package ch.heigvd.dai;

import java.io.*;
import java.util.ArrayList;


public class Rooms extends ArrayList<Room> {
    public static final String roomLocation = "rooms";

    public Rooms() {
        File roomFolder = new File(roomLocation);
        if (!roomFolder.exists()) {
            roomFolder.mkdir();
        }
        for (File roomFile : roomFolder.listFiles()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(roomFile));){
                String name = roomFile.getName();
                String password = reader.readLine();
                super.add(new Room(name, password));
            } catch (Exception e) {
                System.out.println("Impossible de charger la salle :" + e.toString());
            }
        }
    }

    public Room find(String roomName) {
        for (Room existingRoom : this) {
            if (existingRoom.getName().equals(roomName)) {
                return existingRoom;
            }
        }
        return null;
    }

    @Override
    public boolean add(Room room) {
        // Vérifiez si une salle avec le même nom existe déjà
        if (find(room.getName()) != null) {
            return false;
        }

        // Créez la salle si elle n'existe pas déjà
        if (super.add(room)) {
            return addRoomToFile(room);
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        return false;// on ne peut pas supprimer une salle
    }

    private static Boolean addRoomToFile(Room room) {
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
