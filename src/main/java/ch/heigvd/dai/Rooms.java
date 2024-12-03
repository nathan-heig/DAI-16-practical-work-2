package ch.heigvd.dai;

import java.io.*;
import java.util.ArrayList;


public class Rooms {
    public static final String roomLocation = "rooms";
    public static final String splitter = ":";
    
    private static String getRoomPath(String roomName) {
        return roomLocation + "/" + roomName;
    }

    public static String[] getRoomsName() {
        File folder = new File(roomLocation);
        if (folder.exists() && folder.isDirectory()) {
            return folder.list();
        }
        return null;
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
        File room = new File(getRoomPath(roomName));
        try {
            room.createNewFile();
            try (java.io.BufferedWriter writer = new BufferedWriter(new FileWriter(room));){
                writer.write(roomPassword + splitter + CreatorName + "\n");
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

    public static Boolean checkPassword(String roomName, String roomPassword) {
        try (BufferedReader roomReader = new BufferedReader(new FileReader(getRoomPath(roomName)));) {
            return roomPassword.equals(roomReader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean addMessage(String roomName, String writer, String message) {
        try (BufferedWriter roomWriter = new BufferedWriter(new FileWriter(getRoomPath(roomName), true))){
            roomWriter.write(writer + ":" +message + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("Impossible d'écrire dans la salle :" + e.toString());
            return false;
        }
    }

    public static ArrayList<String> getMessages(String roomName, int fisrtLine) {
        ArrayList<String> message = new ArrayList<>();
        try (BufferedReader roomReader = new BufferedReader(new FileReader(getRoomPath(roomName)));){
            String line;
            roomReader.readLine(); // skip password
            int i = 0;
            while ((line = roomReader.readLine()) != null) {
                if (i >= fisrtLine) {
                    message.add(line);
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println("Impossible de lire les messages de la salle :" + e.toString());
            return null;
        }
        return message;
    }
}
