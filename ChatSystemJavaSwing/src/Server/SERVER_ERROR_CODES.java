package Server;

public class SERVER_ERROR_CODES
{
    public static String DUPLICATE_USERNAME()
    {
        return "J_ER 401: DUPLICATE USERNAME";
    }
    public static String UNKNOWN_COMMAND ()
    {
        return "J_ER 501: UNKNOWN_COMMAND";
    }
    public static String BAD_COMMAND ()
    {
        return "J_ER 501: BAD COMMAND";
    }
    public static String OTHER_ERROR ()
    {
        return "J_ER 500: OTHER ERROR";
    }
}