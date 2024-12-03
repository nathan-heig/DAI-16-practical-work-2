package ch.heigvd.dai.commands;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import ch.heigvd.dai.Utils;
import picocli.CommandLine;

@CommandLine.Command(name = "client", description = "Begin a messenger client")
public class Client implements Callable<Integer> {
    @CommandLine.ParentCommand
    protected Root parent;

    @CommandLine.Option(
            names = {"-a", "--address"},
            description = "The IP address.",
            required = false)
    private String ipAddr = "localhost";


    private final String HOST = ipAddr;
    private final int PORT = 1234;
    private static Socket socket;
    private static BufferedReader in;
    private static BufferedWriter out;

    private static Boolean isGoodRep(String rep){
        return rep.equals(Server.Response.OK.toString());
    }
    private static String getErrorMessage(String rep){
        return rep.split(Utils.splitter,2)[1];
    }

    private static String loginRoom(){
        return login(Utils.Command.LOGIN_ROOM);
    }
    private static String loginUser(){
        return login(Utils.Command.LOGIN_USER);
    }
    private static String login(Utils.Command command){
        System.out.print("Entrez le " + command + " : ");
        String pseudo = System.console().readLine();
        System.out.print("Entrez le mot de passe : ");
        String password = System.console().readLine();   
        
        Utils.send(out, command.toString(),pseudo,password);
        return Utils.getResponse(in);
    }

    private static String registerRoom(){
        return register(Utils.Command.REGISTER_ROOM);
    }
    private static String registerUser(){
        return register(Utils.Command.REGISTER_USER);
    }
    private static String register(Utils.Command command){
        System.out.print("Entrez le nom : ");
        String name = System.console().readLine();
        System.out.print("Entrez le mot de passe : ");
        String password = System.console().readLine();
        Utils.send(out, command.toString(),name,password);
        return Utils.getResponse(in);
    }

    private static String sendMessage(){
        System.out.print("Entrez le message : ");
        String message = System.console().readLine();
        Utils.send(out, Utils.Command.WRITE_MESSAGE.toString(),message);
        return Utils.getResponse(in);
    }

    @Override
    public Integer call() throws Exception {
        socket = new Socket(HOST, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        
        
        System.out.println("Bienvenue sur le client de messagerie");
        System.out.println("Veuillez vous connecter ou vous inscrire à un user");
        
        String response = null;
        while(true){
            Integer command;
            System.out.println("1. Se connecter\n2. S'inscrire\n3. Quitter");
            try{
            command = Integer.parseInt(System.console().readLine());
            } catch (Exception e) {
                System.out.println("Veuillez entrer un nombre");
                continue;
            }
            switch (command) {
                case 1 -> {
                    response = loginUser();
                }
                case 2 -> {
                    response = registerUser();
                }
                case 3 -> {
                    Utils.send(out, Utils.Command.QUIT.toString());
                    return 0;
                }
            }
            if (response.equals(Server.Response.OK.toString())) {
                System.out.println("Vous êtes connecté");
                break;
            }
            System.out.println(getErrorMessage(response));
        }
        while(true){
            Integer command;
            System.out.println("1. Se connecter à une salle\n2. Créer une salle\n3. Quitter");
            try {
            command = Integer.parseInt(System.console().readLine());
            } catch (Exception e) {
                System.out.println("Veuillez entrer un nombre");
                continue;
            }
            switch (command) {
                case 1 -> {
                    response = loginRoom();
                }
                case 2 -> {
                    response = registerRoom();
                }
                case 3 -> {
                    Utils.send(out, Utils.Command.QUIT.toString());
                    return 0;
                }
            }
            if (isGoodRep(response)) {
                System.out.println("Vous êtes connecté à une salle");
                break;
            }
            System.out.println(getErrorMessage(response));
        }
        while(true){
            ArrayList<String> messages = new ArrayList<>();
            System.out.println("1. Envoyer un message\n2. getMessages.\n3. Quitter");
            Integer command = Integer.parseInt(System.console().readLine());
            switch (command) {
                case 1 -> {
                    response = sendMessage();
                }
                case 2 -> {
                    Utils.send(out, Utils.Command.GET_MESSAGES.toString(),String.valueOf(messages.size()));
                    response = Utils.getResponse(in);
                    System.out.println(response);
                    response = Server.Response.OK.toString();
                }
                case 3 -> {
                    Utils.send(out, Utils.Command.QUIT.toString());
                    return 0;
                }
            }
            if (isGoodRep(response)) {
                System.out.println("Message envoyé");
            }
            else {
                System.out.println(getErrorMessage(response));
            }
        }
    }
}