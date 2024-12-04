package ch.heigvd.dai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final Users users;
    private final Rooms rooms;


    public ClientHandler(Socket clientSocket, Users users, Rooms rooms){
        this.clientSocket = clientSocket;
        this.users = users;
        this.rooms = rooms;
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

    private Boolean checkLogged(Object o, BufferedWriter out) {
        if (o == null) {
            badResponse(out, "Vous n'êtes pas connecté");
            return false;
        }
        return true;
    }

    @Override
    public void run(){
        try( BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))){
            User userLogin = null;
            Room roomLogged = null;
            while(!clientSocket.isClosed()){
                String request = Utils.readUntil(in);
                String[] parts = request.split(Utils.splitter,2);
                
                Utils.Command command = Utils.Command.fromString(parts[0]);
                if (command == null) {
                    clientSocket.close();
                    break;
                }
                
                switch (command) {
                    case LOGIN_USER -> {
                        String[] credentials = parts[1].split(Utils.splitter, 2);
                        String login = credentials[0];
                        String password = credentials[1];
                        User user = users.find(login);
                        
                        if (user != null) {
                            if (user.getPassword().equals(password)){
                                goodResponse(out);
                                userLogin = user;
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
                        User user = users.find(login);
                        
                        if (user != null) {
                            badResponse(out, "User already exists");
                        } else {
                            User newUser = new User(login, password);
                            users.add(newUser);
                            goodResponse(out);
                            userLogin = newUser;
                        }
                    }
                    case REGISTER_ROOM -> {
                        if (!checkLogged(userLogin, out)) {break;}
                        String[] roomData = parts[1].split(Utils.splitter, 2);
                        String name = roomData[0];
                        String password = roomData[1];
                        Room room = rooms.find(name);
                        
                        if (room != null) {
                            badResponse(out, "Room already exists");
                        } else {
                            Room newRoom = new Room(name, password);
                            if (rooms.add(newRoom)){
                                goodResponse(out);
                                roomLogged = newRoom;
                            } else{
                                badResponse(out, "Impossible de créer la salle");
                            }
                        }
                    }
                    case LOGIN_ROOM -> {
                        if (!checkLogged(userLogin, out)) {break;}
                        String[] roomData = parts[1].split(Utils.splitter, 2);
                        String name = roomData[0];
                        String password = roomData[1];

                        Room room = rooms.find(name);
                        if (room != null) {
                            if (room.getPassword().equals(password)) {
                                goodResponse(out);
                                roomLogged = room;
                            } else {
                                badResponse(out, "Invalid password");
                            }
                        } else {
                            badResponse(out, "Invalid room name");
                        }
                    }
                    case WRITE_MESSAGE -> {
                        if (!checkLogged(userLogin, out) || !checkLogged(roomLogged, out)) {break;}
                        String message = parts[1];
                        if (roomLogged.addMessage(userLogin.getLogin(), message)) {
                            goodResponse(out);
                        } else {
                            badResponse(out, "Impossible d'ajouter le message");
                        }
                    }
                    case GET_MESSAGES -> {
                        if (!checkLogged(userLogin, out) || !checkLogged(roomLogged, out)) {break;}
                        try {
                            int firstLine = Integer.parseInt(parts[1]);
                            ArrayList<String> messages = roomLogged.getMessages(firstLine);
                            Utils.send(out, messages.toArray(new String[0]));
                        } catch (NumberFormatException e) {
                            badResponse(out, "Invalid line number");
                        }
                    }
                    case QUIT -> {
                        clientSocket.close();
                        break;
                    }
                }
            }         


        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
