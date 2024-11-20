package ch.heigvd.dai;

import java.util.HashMap;
import java.io.*;

public class Users extends HashMap<String, String> {
    public static final String userLocation = "users.txt";

    public Users loadUsers(){
        try (BufferedReader reader = new BufferedReader(new FileReader(userLocation));){    
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                put(parts[0], parts[1]);
            }
        } catch (FileNotFoundException e){
            System.out.println("Fichier des utilisateurs introuvable, création d'un nouveau fichier");
            save();
        } catch (Exception e) {
            e.printStackTrace();
        } 

        return this;
    }
    public void save(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(userLocation));
            for (String login : keySet()) {
                writer.write(login + ":" + get(login) + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
