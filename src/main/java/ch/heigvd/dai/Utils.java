package ch.heigvd.dai;

import java.io.*;

public class Utils {

    public static final String splitter = " ";
    public static final char delimiter = '\n';

    public static enum Command {
        LOGIN,
        REGISTER,
        LOGIN_ROOM,
        CREATE_ROOM;


        public static Command fromString(String command){
            try {
                return Command.valueOf(command);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return null;
        }
    }
    public static enum Response {
        OK,
        CLIENT_NOT_LOGGED,
        INVALID_LOGIN,
        INVALID_ROOMNAME,
        INVALID_PASSWORD,
        USER_ALREADY_EXISTS,
        ROOM_ALREADY_EXISTS;

        public static Response fromString(String response){
            try {
                return Response.valueOf(response);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return null;
        }
    }

    public static int send(String message, BufferedWriter out){
        try {
            out.write(message + delimiter);
            out.flush();
            return 0;
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return 1;
    } 
    public static int send(Response response, BufferedWriter out){
        return send(response.toString(), out);
    }

    public static Response getResponse(BufferedReader in){
        try {
            return Response.fromString(readUntil(in));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public static String readUntil(BufferedReader in) throws IOException {
        String response = "";
        int c;
        while ((c = in.read()) != -1 && ((char)c) != delimiter) {
            response += ((char) c);
        }
        return response.toString();
    }
}
