package Server.Window;

import Server.Server;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class ServerWindow
{
    //  headline
    private JLabel headLine;

    //  text area for information
    private JTextPane infoBox;
    private JScrollPane infoBoxScoll;

    //  label and input for port
    private JLabel portLabel;
    private JTextField portInput;

    //  CustomBtn for connecting to server
    private JButton CustomBtn;
    private JButton defaultBtn;

    //  label for user list
    private JLabel userListLabel;

    //  list of active users
    private DefaultListModel<String> model; //  for storing strings
    private JList<String> list;  //  display list
    private JScrollPane listScroll;  //  make list scrollable

    //  server
    private Server server;

    public ServerWindow(JFrame frame, Server server)
    {
        //  server
        this.server = server;

        //  white boarder to make it look like it is boarderless
        Border borderless = BorderFactory.createLineBorder(Color.WHITE);

        //  head line
        this.headLine = new JLabel("Connect to a Server");
        this.headLine.setBounds(40, 30, 520, 75);
        this.headLine.setForeground(Color.BLACK);
        this.headLine.setFont(new Font("Times New Roman", Font.BOLD, 30));

        //  text area for information
        this.infoBox = new JTextPane();
        this.infoBox.setEditable(false);
        this.infoBox.setFont(new Font("Serif", Font.PLAIN, 20));
        this.infoBoxScoll = new JScrollPane(this.infoBox);
        this.infoBoxScoll.setBorder(borderless);
        this.infoBoxScoll.setBounds(40, 125, 520, 300);

        //  label for ip
        this.portLabel = new JLabel("PORT");
        this.portLabel.setBounds(40, 440, 150, 40);
        this.portLabel.setForeground(Color.BLACK);
        this.portLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));

        //  ip input
        this.portInput = new JTextField();
        this.portInput.setBounds(40, 485, 200, 30);
        this.portInput.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        //  CustomBtn for connection to server
        this.CustomBtn = new JButton();
        this.CustomBtn.setForeground(Color.BLACK);
        this.CustomBtn.setFont(new Font(("Times New Roman"), Font.BOLD, 15));
        this.CustomBtn.setText("Create");
        this.CustomBtn.setBackground(Color.BLACK);
        this.CustomBtn.setOpaque(false);
        this.CustomBtn.setContentAreaFilled(false);
        this.CustomBtn.setBounds(40, 550, 200, 40);
        this.CustomBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                server.createServerSocket(portInput.getText());
            }
        });

        //  CustomBtn for connection to server
        this.defaultBtn = new JButton();
        this.defaultBtn.setForeground(Color.BLACK);
        this.defaultBtn.setFont(new Font(("Times New Roman"), Font.BOLD, 15));
        this.defaultBtn.setText("Deafult");
        this.defaultBtn.setBackground(Color.BLACK);
        this.defaultBtn.setOpaque(false);
        this.defaultBtn.setContentAreaFilled(false);
        this.defaultBtn.setBounds(300, 550, 200, 40);
        this.defaultBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                server.createServerSocket("9999");
            }
        });

        //  label for userlist
        this.userListLabel = new JLabel("Clients");
        this.userListLabel.setBounds(40, 440, 150, 40);
        this.userListLabel.setForeground(Color.BLACK);
        this.userListLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));

        //  list of connected users
        this.model = new DefaultListModel<>();
        this.list = new JList<>(this.model);
        this.list.setForeground(Color.orange);
        this.list.setFont(new Font("Serif", Font.BOLD, 22));
        this.listScroll = new JScrollPane(list);
        this.listScroll.setBorder(borderless);
        this.listScroll.setBounds(40, 485, 520, 140);

        //  add to the frame
        frame.add(headLine);
        frame.add(infoBoxScoll);
        frame.add(portLabel);
        frame.add(portInput);
        frame.add(CustomBtn);
        frame.add(defaultBtn);
        frame.add(userListLabel);
        frame.add(listScroll);
    }

    public void setVisible(boolean visible)
    {
        this.headLine.setVisible(visible);
        this.infoBoxScoll.setVisible(visible);
        this.portLabel.setVisible(visible);
        this.portInput.setVisible(visible);
        this.CustomBtn.setVisible(visible);
        this.defaultBtn.setVisible(visible);

        //  set list not visible
        this.userListLabel.setVisible(false);
        this.listScroll.setVisible(false);
    }
    public void setElementsToConnectToServerVisible(boolean visible)
    {
        this.portLabel.setVisible(visible);
        this.portInput.setVisible(visible);
        this.CustomBtn.setVisible(visible);
        this.defaultBtn.setVisible(visible);
    }
    public void setUserListVisible(boolean visible)
    {
        this.userListLabel.setVisible(visible);
        this.listScroll.setVisible(visible);
    }

    public void writeToInfobox(String text)
    {
        this.infoBox.setText(this.infoBox.getText() + "\n" + text + "\n");
    }
    public void addAllClientsToList(Set<String> usernames)
    {
        this.model.clear();

        for (String username:usernames)
        {
            this.model.addElement(username);
        }
    }
}
