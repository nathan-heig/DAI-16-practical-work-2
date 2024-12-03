package ch.heigvd.dai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private static Users users = new Users().loadUsers();


    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }
    public static enum Response {
        OK,
        ERROR;
    }

    private static void goodResponse(BufferedWriter out) {
        Utils.send(out, Response.OK.toString());
    }
    private static void badResponse(BufferedWriter out, String message) {
        Utils.send(out, Response.ERROR.toString(), message);
    }

    private Boolean checkLogged(String Login, BufferedWriter out) {
        if (Login == null) {
            badResponse(out, "Vous n'êtes pas connecté");
            return false;
        }
        return true;
    }

    @Override
    public void run(){
        try( BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))){
            String userLogin = null;
            String roomLogin = null;
            while(!clientSocket.isClosed()){
                String request = Utils.readUntil(in);
                String[] parts = request.split(Utils.splitter,2);
                
                Utils.Command command = Utils.Command.fromString(parts[0]);
                if (command == null) {
                    break;
                }
                
                switch (command) {
                    case LOGIN_USER -> {
                        String[] credentials = parts[1].split(Utils.splitter, 2);
                        String login = credentials[0];
                        String password = credentials[1];
                        
                        if (users.containsKey(login)) {
                            if (users.get(login).equals(password)) {
                                goodResponse(out);
                                userLogin = login;
                            }
                            else {
                                badResponse(out, "Invalid password");
                            }
                        } else {
                            badResponse(out, "Invalid login");
                        }
                    }
                    case REGISTER_USER -> {
                        String[] credentials = parts[1].split(Utils.splitter, 2);
                        String login = credentials[0];
                        String password = credentials[1];
                        
                        if (users.containsKey(login)) {
                            badResponse(out, "User already exists");
                        } else {
                            users.put(login, password);
                            goodResponse(out);
                            userLogin = login;
                        }
                    }
                    case REGISTER_ROOM -> {
                        if (!checkLogged(userLogin, out)) {break;}
                        String[] roomData = parts[1].split(Utils.splitter, 2);
                        String roomName = roomData[0];
                        String roomPassword = roomData[1];
                        
                        if (Rooms.exist(roomName)){
                            badResponse(out, "Room already exists");
                        } else {
                            if (Rooms.createRoom(roomName, roomPassword, userLogin)){
                                goodResponse(out);
                                roomLogin = roomName;
                            } else{
                                badResponse(out, "Impossible de créer la salle");
                            }
                        }
                    }
                    case LOGIN_ROOM -> {
                        if (!checkLogged(userLogin, out)) {break;}
                        String[] roomData = parts[1].split(Utils.splitter, 2);
                        String roomName = roomData[0];
                        String roomPassword = roomData[1];
                        
                        if (Rooms.exist(roomName)) {
                            if (Rooms.checkPassword(roomName, roomPassword)) {
                                goodResponse(out);
                                roomLogin = roomName;
                            } else {
                                badResponse(out, "Invalid password");
                            }
                        } else {
                            badResponse(out, "Invalid room name");
                        }
                    }
                    case WRITE_MESSAGE -> {
                        if (!checkLogged(userLogin, out) || !checkLogged(roomLogin, out)) {break;}
                        String message = parts[1];
                        if (Rooms.addMessage(roomLogin, userLogin, message)) {
                            goodResponse(out);
                        } else {
                            badResponse(out, "Impossible d'ajouter le message");
                        }
                    }
                    case GET_MESSAGES -> {
                        if (!checkLogged(userLogin, out) || !checkLogged(roomLogin, out)) {break;}
                        try {
                            int firstLine = Integer.parseInt(parts[1]);
                            ArrayList<String> messages = Rooms.getMessages(roomLogin, firstLine);
                            Utils.send(out, messages.toArray(new String[0]));
                        } catch (NumberFormatException e) {
                            badResponse(out, "Invalid line number");
                        }
                    }
                    case QUIT -> {
                        users.save();
                        break;
                    }
                }
            }         


        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
