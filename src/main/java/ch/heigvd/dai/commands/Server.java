package ch.heigvd.dai.commands;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.heigvd.dai.Users;
import ch.heigvd.dai.ClientHandler;
import ch.heigvd.dai.Rooms;

import picocli.CommandLine;

@CommandLine.Command(name = "server", description = "Begin a messenger server")
public class Server implements Callable<Integer> {
    @CommandLine.ParentCommand
    protected Root parent;

    private final int PORT = 1234;
    private final int MAX_THREADS = 10;

    private Users users = new Users();
    private Rooms rooms = new Rooms();


    @Override
    public Integer call() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Le serveur est en cours d'exécution sur le port " + PORT);
            ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    executor.submit(new ClientHandler(clientSocket, users, rooms));  
                } catch(Exception e) {
                    executor.shutdown();
                }
            }
        }
    }
}