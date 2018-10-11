package Client;

import Client.Windows.ChatWindow;
import Client.Windows.LoginWindow;
import Client.Windows.PreWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Client extends Canvas
{
    //  window data
    private static final int WIDTH = 600, HEIGHT = 650;
    private static final String TITLE = "Wulff's Chat App";

    //  swing GUI
    private PreWindow preWindow;
    private LoginWindow loginWindow;
    private ChatWindow chatWindow;

    //  server
    private InetAddress host;
    private int port;

    //  client
    private Socket socket;
    private String username;
    private Write write; //  for sending JOIN

    //  list of online users
    private Set<String> activeUsers;  //  all active users
    private Set<String> selectedUsers;  //  selected active users

    private Client()
    {
        this.activeUsers = new HashSet<>();
        this.selectedUsers = new HashSet<>();
    }

    public void connectToServer(String IP, String port)
    {
        if (IP != null && port != null)
        {
            InetAddress ipAddress = null;

            //  check for valid IP Address
            try
            {
                ipAddress  = InetAddress.getByName(IP);
                this.host = ipAddress;
            } catch (UnknownHostException e)
            {
                this.preWindow.setInfoText("IP ADDRESS NOT FOUND!");
            }

            //  check for valid portNumber
            if (!this.preWindow.getPortInputText().equals("") &&
                    !this.preWindow.getPortInputText().matches("[a-zA-Z]+"))
            {
                this.port = Integer.parseInt(port);
                createSocket();
            }else this.preWindow.setInfoText("IP-ADDRESS or PORT NOT VALID");
        }else this.preWindow.setInfoText("Input IP-ADDRESS and PORT NUMBER");
    }
    public void connectToServer()
    {
        //  check for valid IP Address
        try
        {
            this.host = InetAddress.getLocalHost();
        } catch (UnknownHostException e)
        {
            this.preWindow.setInfoText("IP ADDRESS NOT FOUND!");
        }
        this.port = 9999;

        //  create client socket
        createSocket();
    }
    private void createSocket()
    {
        try
        {
            //  create a socket for the client with the servers address
            this.socket = new Socket(this.host, this.port);

            //  set chatbow visible and prewindow not
            this.preWindow.setVisible(false);
            this.loginWindow.setVisible(true);

            //  start threads
            //  instantiate the runnable for a reference when they are to be shut down
            Read read = new Read(this.socket, this);
            this.write = new Write(this);
            HeartBeat heartBeat = new HeartBeat(write);

            //  create threads for the runnable
            //  threads
            Thread readThread = new Thread(read);
            Thread writeThread = new Thread(this.write);
            Thread heartThread = new Thread(heartBeat);

            //  start write thread first for selecting to connect to localhost or custom
            readThread.start();
            heartThread.start();
            writeThread.start();
        }
        catch (IOException e)
        {
            this.preWindow.setInfoText(
                    "ERROR connection to server failed");
        }
    }
    public void closeConnection()
    {
        try
        {
            this.socket.close();
            System.exit(1);
        }
        catch(IOException ioEx)
        {
            System.exit(1);
        }
    }
    public void connectToServerWithUsername(String username)
    {
        if (!username.equals(""))
        {
            boolean isValid = true;

            //  split to chararray to check every char for being valid
            char[] chars = username.toCharArray();

            //  check if username is more than 12 letters
            if (chars.length <= 12)
            {
                //  check if username contains valid characters
                for (char letter: chars)
                {
                    if (!Character.isLetter(letter) && letter != '-' && letter != '_' )
                    {
                        isValid = false;
                    }
                }

                if (isValid)
                {
                    //  set users username
                    this.username = username.toLowerCase();
                    this.chatWindow.setUsernameLabel(username.toLowerCase());

                    //  send JOIN with the username
                    this.write.write(CLIENT_CMD.JOIN(this.username));
                }
            }
        }
    }
    public void updateActiveUsers(String listOfUsernames)
    {
        //  clear the list of active users
        this.activeUsers.clear();

        String[] usernames = listOfUsernames.split("\\s+");

        this.activeUsers.addAll(Arrays.asList(usernames));
        this.activeUsers.remove(this.username);  //  remove this username

        //  update list
        //  insert updated list
        this.chatWindow.addAllUsers(this.activeUsers);
    }
    //  for checking wether to send DATA or DASE (DASE can send to selected users only)
    public boolean isAllUsersSelected()
    {
        //  update selected users
        this.selectedUsers = this.chatWindow.getSelectedValues();

        //  if selected is empty all users are selected
        return this.selectedUsers.size() == 0 || this.selectedUsers.size() == this.activeUsers.size();
    }

    public static void main(String[] args)
    {
        Client client = new Client();

        //  set dimensions for the app
        client.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        client.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        client.setMaximumSize(new Dimension(WIDTH, HEIGHT));

        //  create frame
        JFrame frame = new JFrame(TITLE);

        //  create new client window and add components to the frame
        client.preWindow = new PreWindow(frame, client);
        client.loginWindow = new LoginWindow(frame, client);
        client.chatWindow = new ChatWindow(frame, client);

        //  set preWindow visible
        client.preWindow.setVisible(true);
        client.loginWindow.setVisible(false);
        client.chatWindow.setVisible(false);

        //  add client to the frame
        frame.add(client);

        //  set frame settings
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // exit by redx
        frame.setLocationRelativeTo(null); // centers the window
        frame.setResizable(false); // cannot resize the game window

        frame.setVisible(true); // show the window
    }

    //  getters and setters
    public String getUsername()
    {
        return username;
    }
    public Socket getSocket()
    {
        return socket;
    }
    public ChatWindow getChatWindow()
    {
        return chatWindow;
    }
    public Set<String> getSelectedUsers()
    {
        this.selectedUsers.clear();
        this.selectedUsers.addAll(chatWindow.getSelectedValues());
        return this.selectedUsers;
    }
    public Write getWrite()
    {
        return this.write;
    }
    public LoginWindow getLoginWindow()
    {
        return loginWindow;
    }
}