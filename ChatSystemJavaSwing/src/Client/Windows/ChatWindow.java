package Client.Windows;

import Client.Client;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.util.*;

import static javax.imageio.ImageIO.read;

public class ChatWindow extends JPanel
{
    //  send button
    private JButton button;

    //  input from user
    private JTextField inputBox;  //  for user input

    //  list of active users
    private JLabel usernameTitle; //  username
    private JLabel usernameLabel;  //  the given username set by the client
    private JLabel activeUsersTitleLabel;  //  users
    private DefaultListModel<String> model; //  for storing strings
    private JList<String> list;  //  display list
    private JScrollPane activeUsers;  //  make list scrollable

    //  chat box
    private JScrollPane scrollPane;  //  make tPane (chatBox) scrollable
    private StyledDocument doc;  //  doc for styling text
    private SimpleAttributeSet right;  //  text alignment to the right for user input
    private SimpleAttributeSet left;  //  text alignment to the left for msg received

    //  background
    private ImageIcon image;

    //  client
    private Client client;


    public ChatWindow(JFrame frame, Client client)
    {
        //  client
        this.client = client;

        //  white boarder to make it look like it is boarderless
        Border borderless = BorderFactory.createLineBorder(Color.WHITE);

        //  button
        this.button = new JButton(">");
        this.button.setForeground(Color.BLACK);
        this.button.setBackground(Color.WHITE);
        this.button.setBorder(borderless);
        this.button.setFont(new Font("Serif", Font.BOLD, 40));
        this.button.setBounds(528, 606, 69, 40);
        this.button.addActionListener(e ->
        {
            if (inputBox.getText().length() > 0) //  dont send empty messages
            {
                //  print on screen
                writeRight(inputBox.getText());

                //  print msg to server
                this.client.getWrite().writeToUsers(inputBox.getText());

                //  reset input field
                inputBox.setText("");
            }
        });

        //  input box
        this.inputBox = new JTextField();
        this.inputBox.setBounds(208, 606, 310, 44);
        this.inputBox.setFont(new Font("Serif", Font.PLAIN, 20));

        //  chat box
        //  area for displaying messages
        JTextPane tPane = new JTextPane();
        tPane.setEditable(false);
        tPane.setFont(new Font("Serif", Font.PLAIN, 20));
        this.scrollPane = new JScrollPane(tPane);
        this.scrollPane.setBorder(borderless);
        this.scrollPane.setBounds(208, 25, 391, 577);
        this.doc = tPane.getStyledDocument();

        //  create style for left alignment
        this.left = new SimpleAttributeSet();
        StyleConstants.setAlignment(this.left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setSpaceAbove(this.left, 5);
        StyleConstants.setSpaceBelow(this.left, 5);
        StyleConstants.setForeground(this.left, Color.BLACK);
        StyleConstants.setBackground(this.left, Color.lightGray);

        //  create style for right alignment
        this.right = new SimpleAttributeSet();
        StyleConstants.setAlignment(this.right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setSpaceAbove(this.right, 5);
        StyleConstants.setSpaceBelow(this.right, 5);
        StyleConstants.setForeground(this.right, Color.WHITE);
        StyleConstants.setBackground(this.right, Color.blue);

        //  list of active users
        this.usernameTitle = new JLabel("Username");
        this.usernameTitle.setForeground(Color.BLACK);
        this.usernameTitle.setBounds(30, 0, 170, 60);
        this.usernameTitle.setFont(new Font("Serif", Font.PLAIN, 15));

        this.usernameLabel = new JLabel();
        this.usernameLabel.setForeground(Color.blue);
        this.usernameLabel.setBounds(30, 30, 170, 60);
        this.usernameLabel.setFont(new Font("Serif", Font.BOLD, 18));

        this.activeUsersTitleLabel = new JLabel("Users");
        this.activeUsersTitleLabel.setForeground(Color.BLACK);
        this.activeUsersTitleLabel.setBounds(30, 50, 170, 60);
        this.activeUsersTitleLabel.setFont(new Font("Serif", Font.PLAIN, 15));

        this.model = new DefaultListModel<>();
        this.list = new JList<>(this.model);
        this.list.setForeground(Color.orange);
        this.list.setFont(new Font("Serif", Font.BOLD, 22));
        this.activeUsers = new JScrollPane(list);
        this.activeUsers.setBorder(borderless);
        this.activeUsers.setBounds(30, 100, 170, 500);

        //  for selecting multiple users
        ListSelectionModel listSelectionModel = list.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        //  set background to white
        try
        {
            this.image = new ImageIcon(read(getClass().getResource("/white.jpg")));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        JLabel backgroundImage = new JLabel();
        backgroundImage.setBounds(0,0,800,900);
        backgroundImage.setIcon(image);

        //  add components to the frame
        frame.add(this.button);
        frame.add(inputBox);
        frame.add(this.scrollPane);
        frame.add(this.usernameTitle);
        frame.add(this.usernameLabel);
        frame.add(this.activeUsersTitleLabel);
        frame.add(this.activeUsers);
        frame.add(backgroundImage);  //  always keep as background therefor it is not in the isVisible method
    }

    public void setVisible(boolean isVisible)
    {
        this.button.setVisible(isVisible);
        this.inputBox.setVisible(isVisible);
        this.usernameTitle.setVisible(isVisible);
        this.usernameLabel.setVisible(isVisible);
        this.activeUsersTitleLabel.setVisible(isVisible);
        this.activeUsers.setVisible(isVisible);
        this.scrollPane.setVisible(isVisible);
    }

    //  getters and setters
    public void addAllUsers(Set<String> users)
    {
        //  reset list
        this.model.clear();

        //  add all elements
        for (String user: users)
        {
            this.model.addElement(user);
        }
    }
    public Set<String> getSelectedValues()
    {
        return new HashSet<>(this.list.getSelectedValuesList());
    }
    public void setUsernameLabel(String username)
    {
        this.usernameLabel.setText(username);
    }
    public void writeLeft(String text)
    {
        try
        {
            this.doc.setParagraphAttributes(this.doc.getLength(), 1, this.left, false);
            this.doc.insertString(this.doc.getLength(), text + "\n", this.left);
            this.doc.setParagraphAttributes(this.doc.getLength(), 1, this.left, false);
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }
    private void writeRight(String text)
    {
        try
        {
            this.doc.setParagraphAttributes(this.doc.getLength(), 1, this.right, false);
            this.doc.insertString(this.doc.getLength(), text + "\n", this.right);
            this.doc.setParagraphAttributes(this.doc.getLength(), 1, this.right, false);
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }
}