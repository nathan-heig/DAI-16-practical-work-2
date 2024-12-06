package ch.heigvd.dai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Room {
    private String name;
    private String password;

    public String getRoomPath() {
        return Rooms.roomsLocation + "/" + name;
    }


    public Room(String name, String password) {
        if(name.contains(":")){
            throw new IllegalArgumentException("Le nom de la salle ne peut pas contenir le caractère :");
        }
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Boolean equals(String name) {
        return this.name.equals(name) && this.password.equals(password);
    }

    public Boolean addMessage(String writer, String message) {
        try (BufferedWriter roomWriter = new BufferedWriter(new FileWriter(getRoomPath(), true))){
            roomWriter.write(writer + ":" +message + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("Impossible d'écrire dans la salle :" + e.toString());
            return false;
        }
    }

    public ArrayList<String> getMessages(int fisrtLine) {
        ArrayList<String> message = new ArrayList<>();
        try (BufferedReader roomReader = new BufferedReader(new FileReader(getRoomPath()));){
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
