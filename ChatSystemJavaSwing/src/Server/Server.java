package Server;

import Server.Window.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Canvas implements Runnable
{
    private static ServerSocket serverSocket;
    private static int portNumber = 9999;
    private static Map<String, Socket> clientsMap;
    private static Thread serverThread;

    //  window
    private static ServerWindow serverWindow;
    private static final int WIDTH = 600, HEIGHT = 650;
    private static final String TITLE = "Wulff's Chat App";

    private Server()
    {
        this.clientsMap = new HashMap<>();
    }
    private boolean isValidPort(String port)
    {
        boolean isValid = false;

        //  check for valid portNumber
        if (port != null)
        {
            if (!port.equals("") &&
                    !port.matches("[a-zA-Z]+"))
            {
                int portTemp = Integer.parseInt(port);
                if (portTemp > 2000)
                {
                    portNumber = Integer.parseInt(port);
                    isValid = true;
                }
            }
            else
            {
                serverWindow.writeToInfobox("PORT NOT VALID");
            }
        }
        else
        {
            serverWindow.writeToInfobox("Input PORT NUMBER");
        }

        return isValid;
    }

    public void createServerSocket(String port)
    {
        if (isValidPort(port))
        {
            try
            {
                //  create a socket for the server
                serverSocket = new ServerSocket(Integer.parseInt(port));

                //  start server Thread
                serverThread.start();

                //  display servers info
                this.serverWindow.writeToInfobox("Server Socket created: " + "port: " + serverSocket.getLocalPort() + " IP: " + serverSocket.getInetAddress());

                //  remove elements for connecting to server
                this.serverWindow.setElementsToConnectToServerVisible(false);

                //  set list of users visible
                this.serverWindow.setUserListVisible(true);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                this.serverWindow.writeToInfobox("Unable to set up portNumber!");
                System.exit(1);
            }
        }

    }

    public static Map<String, Socket> getClientsMap()
    {
        return clientsMap;
    }
    public static Set<String> getClientUsernames()
    {
        return new HashSet<>(clientsMap.keySet());
    }
    public static Set<Socket> getClientSockets()
    {
        return new HashSet<>(clientsMap.values());
    }
    public static synchronized void removeClientBySocket (Socket clientSocket)
    {
        //  remove client from list
        Iterator<Map.Entry<String, Socket>> entries = clientsMap.entrySet().iterator();
        while (entries.hasNext())
        {
            Map.Entry<String, Socket> entry = entries.next();

            //  check for the right client to remove
            if (entry.getValue().equals(clientSocket))
            {
                entries.remove();
                break;
            }
        }

        //  update windows client list
        serverWindow.addAllClientsToList(clientsMap.keySet());
    }
    public static synchronized void addClient(String username, Socket clientSocket)
    {
        clientsMap.put(username, clientSocket);
        serverWindow.addAllClientsToList(clientsMap.keySet());
    }
    public static String getUsernameBySocket(Socket clientSocket)
    {
        //  return clientsocket if no username
        String username = clientSocket.toString();

        //  get username by client socket
        Iterator<Map.Entry<String, Socket>> entries = clientsMap.entrySet().iterator();
        while (entries.hasNext())
        {
            Map.Entry<String, Socket> entry = entries.next();

            //  check for the right client to remove
            if (entry.getValue().equals(clientSocket))
            {
                username = entry.getKey();
                break;
            }
        }return username;
    }
    public static Set<Socket> getSocketsByUsernames(Set<String> usernames)
    {
        Set<Socket> sockets = new HashSet<>();

        for (String username: usernames)
        {
            if (clientsMap.containsKey(username))
            {
                sockets.add(clientsMap.get(username));
            }
        }return sockets;
    }
    public static ServerWindow getServerWindow()
    {
        return serverWindow;
    }

    @Override
    public void run()
    {
        while(true)
        {
            //  wait for clients to connect
            Socket clientSocket = null;

            //  client handler join
            //////////////////////////////////////////////////
            try
            {
                clientSocket = serverSocket.accept();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            //  accepted connection from a client
            this.serverWindow.writeToInfobox("Client connection accepted: " + clientSocket);


            //  handle client in a new thread
            ClientHandler clientHandler = new ClientHandler (clientSocket);

            //  create a new thread to execute the clientHandler
            Thread clientThread = new Thread(clientHandler);

            //  execute the thread with the runnable clientHandler
            clientThread.start();
        }
    }

    public static void main(String[] args)
    {
        Server server = new Server();

        serverThread = new Thread(server);

        //  set dimensions for the app
        server.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        server.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        server.setMaximumSize(new Dimension(WIDTH, HEIGHT));

        //  create frame
        JFrame frame = new JFrame(TITLE);

        //  create new client window and add components to the frame
        server.serverWindow = new ServerWindow(frame, server);

        //  set preWindow visible
        server.serverWindow.setVisible(true);

        //  add client to the frame
        frame.add(server);

        //  set frame settings
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // exit by redx
        frame.setLocationRelativeTo(null); // centers the window
        frame.setResizable(false); // cannot resize the game window

        frame.setVisible(true); // show the window
    }
}