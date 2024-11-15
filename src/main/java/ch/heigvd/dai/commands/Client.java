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

    private static final String HOST = "localhost";
    private static final int PORT = 1234;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;



    public void connect(){
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


    @Override
    public Integer call() throws Exception {
        socket = new Socket(HOST, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        connect();


        return 0;
    }
}