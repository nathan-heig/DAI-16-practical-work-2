package ch.heigvd.dai;

import java.io.*;

public class Utils {

    public static final String splitter = " ";
    public static final char delimiter = '\u0004';

    public static enum Command {
        LOGIN_USER,
        REGISTER_USER,
        LOGIN_ROOM,
        REGISTER_ROOM,
        WRITE_MESSAGE,
        QUIT;


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
        INVALID_ROOM_NAME,
        INVALID_PASSWORD,
        USER_ALREADY_EXISTS,
        ROOM_ALREADY_EXISTS,
        ERROR;

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
        StringBuffer response = new StringBuffer();
        int c;
        while ((c = in.read()) != -1 && ((char)c) != delimiter) {
            response.append((char) c);
        }
        return response.toString();
    }
}
