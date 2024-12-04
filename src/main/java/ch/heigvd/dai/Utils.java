package ch.heigvd.dai;

import java.io.*;

public class Utils {

    public static final String splitter = "\n";
    public static final char delimiter = '\u0004';

    public static enum Command {
        LOGIN_USER,
        REGISTER_USER,
        LOGIN_ROOM,
        REGISTER_ROOM,
        WRITE_MESSAGE,
        GET_MESSAGES,
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

    public static int send(BufferedWriter out, String ... messages){
        try {
            for (int i = 0; i < messages.length; i++) {
                out.write(messages[i]);
                if (i < messages.length - 1) {
                    out.write(splitter);
                }
            }
            out.write(delimiter);
            out.flush();
            return 0;
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return 1;
    } 

    public static String getResponse(BufferedReader in) throws IOException {
        return readUntil(in);
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
