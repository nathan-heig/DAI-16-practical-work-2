package ch.heigvd.dai;

import java.io.*;
import java.util.Vector;

public class Users{
    public static final String userLocation = "users.txt";
    Vector<User> users = new Vector<User>();

    public Users(){
        try (BufferedReader reader = new BufferedReader(new FileReader(userLocation));){    
            String line;
            while ((line = reader.readLine()) != null) {
                users.add(User.fromString(line));
            }
        } catch (FileNotFoundException e){
            System.out.println("Fichier des utilisateurs introuvable, création d'un nouveau fichier");
            try {
                new File(userLocation).createNewFile();
            } catch (IOException e1) {
                System.out.println("Impossible de créer le fichier des utilisateurs");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public User find(String userLogin) {
        for (User existingUser : this.users) {
            if (existingUser.getLogin().equals(userLogin)) {
                return existingUser;
            }
        }
        return null;
    }

    public boolean add(User user) {
        if (find(user.getLogin()) != null) {
            return false;
        }
        if (users.add(user)) {
            return addUserToFile(user);
        } else {
            return false;
        }
    }


    public Boolean addUserToFile(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userLocation, true));){
            writer.write(user.toString() + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("Impossible d'écrire dans le fichier des utilisateurs :" + e.toString());
            return false;
        }
    }

}
