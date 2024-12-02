package ch.heigvd.dai.commands;

import java.io.*;
import java.net.Socket;
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
    private final int PORT = parent.getPort();
    private static Socket socket;
    private static BufferedReader in;
    private static BufferedWriter out;


    private static Utils.Response loginRoom(){
        return login(Utils.Command.LOGIN_ROOM);
    }
    private static Utils.Response loginUser(){
        return login(Utils.Command.LOGIN_USER);
    }

    private static Utils.Response login(Utils.Command command){
        System.out.print("Entrez le " + command + " : ");
        String pseudo = System.console().readLine();
        System.out.print("Entrez le mot de passe : ");
        char[] password = System.console().readPassword();    
        
        Utils.send(command + " " + pseudo + " " + new String(password), out);
        return Utils.getResponse(in);
    }

    private static Utils.Response registerRoom(){
        return register(Utils.Command.REGISTER_ROOM);
    }
    private static Utils.Response registerUser(){
        return register(Utils.Command.REGISTER_USER);
    }

    private static Utils.Response register(Utils.Command command){
        System.out.print("Entrez le nom : ");
        String name = System.console().readLine();
        System.out.print("Entrez le mot de passe : ");
        String password = System.console().readLine();
        Utils.send(command + " " + name + " " + password, out);
        return Utils.getResponse(in);
    }

    private static Utils.Response sendMessage(){
        System.out.print("Entrez le message : ");
        String message = System.console().readLine();
        Utils.send(Utils.Command.WRITE_MESSAGE + " " + message, out);
        return Utils.getResponse(in);
    }
    


    @Override
    public Integer call() throws Exception {
        socket = new Socket(HOST, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        
        
        System.out.println("Bienvenue sur le client de messagerie");
        System.out.println("Veuillez vous connecter ou vous inscrire à un user");
        
        Utils.Response response = null;
        while(true){
            System.out.println("1. Se connecter\n2. S'inscrire\n3. Quitter");
            Integer command = Integer.parseInt(System.console().readLine());
            switch (command) {
                case 1 -> {
                    response = loginUser();
                }
                case 2 -> {
                    response = registerUser();
                }
                case 3 -> {
                    Utils.send(Utils.Command.QUIT.toString(), out);
                    return 0;
                }
            }
            if (response == Utils.Response.OK) {
                System.out.println("Vous êtes connecté");
                break;
            }
            System.out.println("Erreur lors de la connexion : " + response);
        }


        return 0;
    }
}