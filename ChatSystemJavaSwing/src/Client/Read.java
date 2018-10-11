package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Read implements Runnable
{
    private Client client;
    private Scanner scanner;

    public Read(Socket clientSocket, Client client)
    {
        this.client = client;

        try
        {
            this.scanner = new Scanner(clientSocket.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        boolean isInterrupted = false;
        try
        {
            handleInputFromServer();
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

    public void handleInputFromServer ()
    {
        String message, cmd, rest;

        while ((message = scanner.nextLine()) != null)
        {
            //  get cmd for input
            String[] msgArray = message.split(" ", 2);
            //  veriable for cmd from server
            cmd = msgArray[0];

            //  listen for cmd or send message to client
            switch (cmd)
            {
                case "J_OK":
                    //  set the log in screen not visible
                    this.client.getLoginWindow().setVisible(false);
                    //  set chatWindow visible
                    this.client.getChatWindow().setVisible(true);
                    break;
                case "J_ER":

                    // split so that you can handle the different error codes
                    rest = msgArray[1];
                    String[] errArray = rest.split(": ", 2);
                    String errCode = errArray[0];

                    switch(errCode)
                    {
                        case "401":
                            //  dublicate username
                            this.client.getChatWindow().writeLeft(rest);
                            break;

                        case "501":
                            //  unknown command
                            this.client.getChatWindow().writeLeft(rest);
                            break;
                        case "502":
                            //  bad command
                            this.client.getChatWindow().writeLeft(rest);
                            break;
                        case "500":
                            //  other error
                            this.client.getChatWindow().writeLeft(rest);
                            break;
                    }
                    break;
                case "DATA":
                    rest = msgArray[1];
                    this.client.getChatWindow().writeLeft(rest);
                    break;
                case "LIST":
                    rest = msgArray[1];
                    this.client.updateActiveUsers(rest);
                    break;
            }
        }
    }
}