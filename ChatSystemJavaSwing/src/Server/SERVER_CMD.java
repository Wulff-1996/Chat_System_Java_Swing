package Server;

import java.util.List;
import java.util.Set;

public class SERVER_CMD
{
    public static String J_OK()
    {
        return "J_OK";
    }
    public static String J_ER(int err_code, String err_message)
    {
        return "J_ER " + err_code + ": " + err_message;
    }
    public static String DATA (String username, String message)
    {
        return "DATA " + username + ": " + message;
    }
    public static String LIST (Set<String> usernames)
    {
        return "LIST " + String.join(" ", usernames);
    }
}