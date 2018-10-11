package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable
{
    private final Socket CLIENTSOCKET;

    //  client input and output streams
    private Scanner scanner;
    private PrintWriter printWriter;

    //  for clientHandler
    private boolean isAlive = true;

    //  for client
    private HeartBeatListener clientHeartBeatListener;

    public ClientHandler(Socket clientSocket)
    {
        this.CLIENTSOCKET = clientSocket;

        try
        {
            this.scanner = new Scanner(this.CLIENTSOCKET.getInputStream());
            this.printWriter = new PrintWriter(this.CLIENTSOCKET.getOutputStream(), true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //  create and start heartBeat thread
        this.clientHeartBeatListener = new HeartBeatListener(this);
        Thread heartBeatThread = new Thread( this.clientHeartBeatListener);
        heartBeatThread.start();
    }

    //  handlers
    private void handleClientSocket()
    {
        String message, cmd, rest;

        while (this.isAlive && (message = scanner.nextLine()) != null)
        {
            //  get cmd for input
            String[] msgArray = message.split(" ", 2);
            //  veriable for cmd from server
            cmd = msgArray[0];

            //  listen for cmd or send message to client
            switch (cmd)
            {
                case "JOIN":
                    rest = msgArray[1];
                    handleJOIN(rest);
                    break;
                case "DATA":
                    rest = msgArray[1];
                    handleDATA(rest);
                    break;
                case "DASE":
                    rest = msgArray[1];
                    handleDASE(rest);
                    break;
                case "IMAV":
                    handleIMAV();
                    break;
                case "QUIT":
                    handleQUIT();
                    break;
                default:
                    //  unknown command
                    printWriter.println(SERVER_ERROR_CODES.UNKNOWN_COMMAND());
            }
        }
    }
    private void handleQUIT()
    {
        //  remove client from the list
        Server.removeClientBySocket(this.CLIENTSOCKET);

        //  send updated list to connected clients
        sendListToClients();

        //  close connection
        closeClientConnection();
    }
    private void handleIMAV()
    {
        //  set isAlive = true
        this.clientHeartBeatListener.setIsClientAlive(true);
    }
    private void handleDATA(String rest)
    {
        //  get username and message from user DATA msg
        String[] msgArray = rest.split(":", 2);
        String username = msgArray[0];
        String msg = msgArray[1];

        Set<Socket> clientSockets = Server.getClientSockets();

        //  send messages to all other clients
        sendMessages(clientSockets, username, msg);
    }
    private void handleDASE(String rest)
    {
        //  split the rest of the message for every :
        String[] anArray = rest.split(": ", 3);
        String username = anArray[0];
        String[] usernames = anArray[1].split("\\s+"); //  split the string for every white space
        String msg = anArray[2];

        //  store the selected usernames in a Set
        Set<String> selectedUsers = new HashSet<>(Arrays.asList(usernames));

        //  send messages
        sendMessages(Server.getSocketsByUsernames(selectedUsers), username, msg);
    }
    private void sendMessages(Set<Socket> sockets, String username, String msg)
    {
        //  send messages to all other clients
        for(Socket c : sockets)
        {
            //  only send data to the other clients
            if (!this.CLIENTSOCKET.equals(c))
            {
                try
                {
                    PrintWriter out = new PrintWriter(c.getOutputStream(), true);
                    out.println(SERVER_CMD.DATA(username, msg));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    private void handleJOIN(String rest)
    {
        //  store all usernames
        Set<String> usernames = Server.getClientsMap().keySet();

        //  if username does not exits put the new user in the list of clients
        if (!usernames.contains(rest))
        {
            Server.addClient(rest, this.CLIENTSOCKET);

            //  send J_OK indication client is accepted
            printWriter.println(SERVER_CMD.J_OK());

            //  send updated list to all connected clients
            sendListToClients();
        }
        else
        {
            //  inform client that username is in use
            printWriter.println(SERVER_ERROR_CODES.DUPLICATE_USERNAME());
        }
    }

    //  methods
    public void closeClientConnection()
    {
        //  close connection
        try
        {
            if (CLIENTSOCKET!=null)
            {
                Server.getServerWindow().writeToInfobox("Closing down connection from: " + Server.getUsernameBySocket(this.getCLIENTSOCKET()));
                CLIENTSOCKET.close();
            }
        }
        catch(IOException ioEx)
        {
            Server.getServerWindow().writeToInfobox("Unable to disconnect!");
        }
        isAlive = false;
    }
    public void sendListToClients()
    {
        //  send updated list to all connected clients
        Set<Socket> clientSockets = Server.getClientSockets();

        for (Socket c:clientSockets)
        {
            try
            {
                PrintWriter out = new PrintWriter(c.getOutputStream(), true);

                out.println(SERVER_CMD.LIST(Server.getClientUsernames()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public Socket getCLIENTSOCKET()
    {
        return this.CLIENTSOCKET;
    }


    @Override
    public void run()
    {
        boolean isInterrupted = false;
        try
        {
            while (this.isAlive)
            {
                handleClientSocket();
            }
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