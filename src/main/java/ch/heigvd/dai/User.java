package ch.heigvd.dai;

public class User {
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public static User fromString(String user){
        String[] parts = user.split(":",2);
        return new User(parts[0], parts[1]);
    }

    public String toString(){
        return login + ":" + password;
    }

}
