package ch.heigvd.dai;

public class Message {
    private String message;
    private String sender;

    public Message(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }
    public static Message fromString(String message){
        String[] parts = message.split(":");
        return new Message(parts[1], parts[0]);
    }
    public String toString(){
        return sender + ":" + message;
    }
}
