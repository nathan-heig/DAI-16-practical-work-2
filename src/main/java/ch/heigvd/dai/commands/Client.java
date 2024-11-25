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



    private static void Login(){
            while(true){
                System.out.print("Entrez votre pseudo : ");
                String pseudo = System.console().readLine();
                System.out.print("Entrez votre mot de passe : ");
                char[] password = System.console().readPassword();    
                
                Utils.send(Utils.Command.LOGIN + " " + pseudo + " " + new String(password), out);
                Utils.Response response = Utils.getResponse(in);
                if(response == Utils.Response.OK){
                    System.out.println("Connexion réussie");
                    break;
                }
                else{
                    System.out.println("Connexion échouée : " + response);
                }
            }
    }
    private static void createRoom(){
        System.out.print("Entrez le nom de la salle : ");
        String roomName = System.console().readLine();
        System.out.print("Entrez le mot de passe de la salle : ");
        String password = System.console().readLine();
        Utils.send(Utils.Command.REGISTER_ROOM + " " + roomName + " " + password, out);
        Utils.Response response = Utils.getResponse(in);
        if(response == Utils.Response.OK){
            System.out.println("Salle créée");
        }
        else{
            System.out.println("Création de salle échouée : " + response);
        }
    }
    


    @Override
    public Integer call() throws Exception {
        socket = new Socket(HOST, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Login();
        createRoom();


        return 0;
    }
}