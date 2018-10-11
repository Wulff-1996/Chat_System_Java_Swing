package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

public class Write implements Runnable

{
    private Client client;
    private PrintWriter printWriter;

    public Write(Client client)
    {
        this.client = client;

        try
        {
            this.printWriter = new PrintWriter(this.client.getSocket().getOutputStream(), true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //  the heartBeat Thread have to send IMAV to the server, and client have to send JOIN
    public void write(String msg)
    {
        this.printWriter.println(msg);
    }
    //  for writing for users
    public void writeToUsers(String msg)
    {
        //  check for quit
        if (msg.equals("QUIT"))
        {
            //  send QUIT to SERVER
            printWriter.println(CLIENT_CMD.QUIT());

            //  close connection
            this.client.closeConnection();
        }
        //  send data to users, if none or all selected user DATA, else DASE(send to selected users)
        if (this.client.isAllUsersSelected())
        {
            //  send DATA to all
            printWriter.println(CLIENT_CMD.DATA(this.client.getUsername(), msg));
        }else printWriter.println(CLIENT_CMD.DASE(this.client.getUsername(), this.client.getSelectedUsers(), msg));
    }

    @Override
    public void run()
    {
        boolean isInterrupted = false;
        try
        {
            while (true) {}
        }
        catch (NoSuchElementException e)
        {
            isInterrupted = true;
        }
        finally
        {
            if (isInterrupted)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}