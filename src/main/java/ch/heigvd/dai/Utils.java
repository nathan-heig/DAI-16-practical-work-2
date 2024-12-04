package ch.heigvd.dai;

import java.io.*;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;

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

    public static int send(BufferedWriter out, SecretKeySpec secretKey, String ... messages){
        try {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < messages.length; i++) {
                sb.append(messages[i]);
                if (i < messages.length - 1) {
                    sb.append(splitter);
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

    public static String getResponse(BufferedReader in){
        try {
            return readUntil(in);
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

    public static SecretKeySpec doDiffieHellman(BufferedReader in, BufferedWriter out) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        out.write(Base64.getEncoder().encodeToString(publicKeyBytes) + "\n");
        out.flush();

        String otherPublicKeyStr = in.readLine();
        byte[] otherPublicKeyBytes = Base64.getDecoder().decode(otherPublicKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        PublicKey otherPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(otherPublicKeyBytes));

        KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
        keyAgree.init(keyPair.getPrivate());
        keyAgree.doPhase(otherPublicKey, true);
        byte[] sharedSecret = keyAgree.generateSecret();
        return new SecretKeySpec(sharedSecret, 0, 16, "AES");
    }
}
