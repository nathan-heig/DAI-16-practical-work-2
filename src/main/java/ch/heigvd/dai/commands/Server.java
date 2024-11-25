package ch.heigvd.dai.commands;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

import ch.heigvd.dai.Utils;
import ch.heigvd.dai.Users;
import ch.heigvd.dai.Rooms;

import picocli.CommandLine;

@CommandLine.Command(name = "server", description = "Begin a messenger server")
public class Server implements Callable<Integer> {
    @CommandLine.ParentCommand
    protected Root parent;

    private final int PORT = parent.getPort();
    private static Users users = new Users().loadUsers();

    private void deconectClient(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Boolean checkLogged(String Login, BufferedWriter out) {
        if (Login == null) {
            Utils.send(Utils.Response.CLIENT_NOT_LOGGED, out);
            return false;
        }
        return true;
    }


    @Override
    public Integer call() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Le serveur est en cours d'exécution sur le port " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                    String userLogin = null;
                    String roomLogin = null;
                    while(clientSocket.isConnected()){
                        String request = Utils.readUntil(in);
                        String[] parts = request.split(Utils.splitter,2);
                        
                        Utils.Command command = Utils.Command.fromString(parts[0]);
                        if (command == null) {
                            System.out.println("Commande inconnue : " + parts[0]);
                            deconectClient(clientSocket);
                            break;
                        }
                        
                        switch (command) {
                            case LOGIN -> {
                                String[] credentials = parts[1].split(Utils.splitter, 2);
                                String login = credentials[0];
                                String password = credentials[1];
                                if (users.containsKey(login)) {
                                    if (users.get(login).equals(password)) {
                                        Utils.send(Utils.Response.OK, out);
                                        userLogin = login;
                                    }
                                    else {
                                        Utils.send(Utils.Response.INVALID_PASSWORD, out);
                                    }
                                } else {
                                    Utils.send(Utils.Response.INVALID_LOGIN, out);
                                }
                            }
                            case REGISTER -> {
                                String[] credentials = parts[1].split(Utils.splitter, 2);
                                String login = credentials[0];
                                String password = credentials[1];
                                if (users.containsKey(login)) {
                                    Utils.send(Utils.Response.USER_ALREADY_EXISTS, out);
                                } else {
                                    users.put(login, password);
                                    Utils.send(Utils.Response.OK, out);
                                    userLogin = login;
                                }
                            }
                            case REGISTER_ROOM -> {
                                if (!checkLogged(userLogin, out)) {break;}
                                String[] roomData = parts[1].split(Utils.splitter, 2);
                                String roomName = roomData[0];
                                String roomPassword = roomData[1];
                                
                                if (Rooms.exist(roomName))
                                    Utils.send(Utils.Response.ROOM_ALREADY_EXISTS, out);
                                else
                                    if (Rooms.createRoom(roomName, roomPassword, userLogin)){
                                        Utils.send(Utils.Response.OK, out);
                                        roomLogin = roomName;
                                    } else
                                        Utils.send(Utils.Response.INVALID_ROOM_NAME, out);
                            }
                            case LOGIN_ROOM -> {
                                if (!checkLogged(userLogin, out)) {break;}
                                String[] roomData = parts[1].split(Utils.splitter, 2);
                                String roomName = roomData[0];
                                String roomPassword = roomData[1];
                                if (Rooms.exist(roomName)) {
                                    try (BufferedReader roomReader = new BufferedReader(new FileReader(Rooms.roomLocation + "/" + roomName))) {
                                        if (roomPassword.equals(roomReader.readLine())) {
                                            Utils.send(Utils.Response.OK, out);
                                            roomLogin = roomName;
                                        } else {
                                            Utils.send(Utils.Response.INVALID_PASSWORD, out);
                                        }
                                    } catch (IOException e) {
                                        Utils.send(Utils.Response.INVALID_ROOM_NAME, out);
                                    }
                                } else {
                                    Utils.send(Utils.Response.INVALID_ROOM_NAME, out);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}