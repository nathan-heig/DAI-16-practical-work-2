package ch.heigvd.dai;

import java.io.*;


public class Rooms {
    public static final String roomLocation = "rooms";

    public static String[] getRoomsName() {
        File folder = new File(roomLocation);
        if (folder.exists() && folder.isDirectory()) {
            return folder.list();
        } else {
            System.out.println("Le dossier spécifié n'existe pas ou n'est pas un dossier.");
            return null;
        }
    }
    public static Boolean exist(String roomName) {
       String[] rooms = getRoomsName();
        if (rooms != null) {
            for (String room : rooms) {
                if (room.equals(roomName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Boolean createRoom(String roomName, String roomPassword, String CreatorName) {
        File room = new File(roomLocation + "/" + roomName);
        try {
            room.createNewFile();
            try (java.io.BufferedWriter writer = new BufferedWriter(new FileWriter(room));){
                writer.write(roomPassword);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                room.delete();
            }
        } catch (Exception e) {
            System.out.println("Impossible de créer la salle :" + e.toString());
            room.delete();
        }
        return false;
    }
}
