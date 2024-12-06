package ch.heigvd.dai.commands;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import ch.heigvd.dai.ClientHandler;
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


    private static Boolean isGoodRep(String rep){
        return rep.equals(ClientHandler.Response.OK.toString());
    }
    private static String getErrorMessage(String rep){
        return rep.split(Utils.splitter,2)[1];
    }

    private static String loginRoom(BufferedReader in, BufferedWriter out) throws IOException {
        return login(Utils.Command.LOGIN_ROOM, in, out);
    }
    private static String loginUser(BufferedReader in, BufferedWriter out) throws IOException {
        return login(Utils.Command.LOGIN_USER, in, out);
    }
    private static String login(Utils.Command command, BufferedReader in, BufferedWriter out) throws IOException {
        System.out.print("Entrez le " + command + " : ");
        String pseudo = System.console().readLine();
        System.out.print("Entrez le mot de passe : ");
        String password = System.console().readLine();   
        
        Utils.send(out, command.toString(),pseudo,password);
        return Utils.getResponse(in);
    }

    private static String registerRoom(BufferedReader in, BufferedWriter out) throws IOException {
        return register(Utils.Command.REGISTER_ROOM, in, out);
    }
    private static String registerUser(BufferedReader in, BufferedWriter out) throws IOException {
        return register(Utils.Command.REGISTER_USER, in, out);
    }
    private static String register(Utils.Command command, BufferedReader in, BufferedWriter out) throws IOException {
        System.out.print("Entrez le nom : ");
        String name = System.console().readLine();
        System.out.print("Entrez le mot de passe : ");
        String password = System.console().readLine();
        Utils.send(out, command.toString(),name,password);
        return Utils.getResponse(in);
    }

    private static String sendMessage(BufferedReader in, BufferedWriter out) throws IOException {
        System.out.print("Entrez le message : ");
        String message = System.console().readLine();
        Utils.send(out, Utils.Command.WRITE_MESSAGE.toString(),message);
        return Utils.getResponse(in);
    }

    @Override
    public Integer call() throws Exception {
        try (
            Socket socket = new Socket(ipAddr, parent.getPort());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));){
            
            
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
                        response = loginUser(in,out);
                    }
                    case 2 -> {
                        response = registerUser(in,out);
                    }
                    case 3 -> {
                        return 0;
                    }
                }
                if (isGoodRep(response)) {
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
                        response = loginRoom(in,out);
                    }
                    case 2 -> {
                        response = registerRoom(in,out);
                    }
                    case 3 -> {
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
                        response = sendMessage(in,out);
                    }
                    case 2 -> {
                        Utils.send(out, Utils.Command.GET_MESSAGES.toString(),String.valueOf(messages.size()));
                        response = Utils.getResponse(in);
                        System.out.println(response);
                        response = ClientHandler.Response.OK.toString();
                    }
                    case 3 -> {
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
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return 0;
    }
}