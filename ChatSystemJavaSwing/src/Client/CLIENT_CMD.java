package Client;

import java.util.Set;

public class CLIENT_CMD
{
    public static String JOIN(String username)
    {
        return "JOIN " + username;
    }
    public static String DATA(String username, String message)
    {
        return "DATA " + username + ": " + message;
    }
    public static String DASE(String username, Set<String> usernames, String message)
    {
        return "DASE " + username + ": " + String.join(" ", usernames) + ": " + message;
    }
    public static String IMAV ()
    {
        return "IMAV";
    }
    public static String QUIT ()
    {
        return "QUIT";
    }
}