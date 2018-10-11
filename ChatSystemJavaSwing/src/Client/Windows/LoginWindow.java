package Client.Windows;

import Client.Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JPanel
{
    private JLabel headLine;
    private JLabel info;
    private JTextField usernameInput;
    private JButton btn;

    private Client client;

    public LoginWindow(JFrame frame, Client client)
    {
        //  client
        this.client = client;

        //  head line
        this.headLine = new JLabel("Login with Username");
        this.headLine.setBounds(40, 20, 520, 55);
        this.headLine.setForeground(Color.BLACK);
        this.headLine.setFont(new Font("Times New Roman", Font.BOLD, 30));

        //  info label
        this.info = new JLabel("Username max length 12, only letters, ‘-‘ and ‘_’ allowed");
        this.info.setBounds(40, 46, 520, 120);
        this.info.setForeground(Color.BLACK);
        this.info.setFont(new Font("Times New Roman", Font.ITALIC, 20));

        //  default server label
        this.usernameInput = new JTextField();
        this.usernameInput.setBounds(40, 220, 250, 35);
        this.usernameInput.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        //  default server btn
        this.btn = new JButton();
        this.btn.setForeground(Color.BLACK);
        this.btn.setFont(new Font(("Times New Roman"), Font.BOLD, 15));
        this.btn.setText("Send");
        this.btn.setBackground(Color.BLACK);
        this.btn.setOpaque(false);
        this.btn.setContentAreaFilled(false);
        this.btn.setBounds(40, 270, 150, 55);
        this.btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.connectToServerWithUsername(usernameInput.getText().toLowerCase());
            }
        });

        //  add elements to the frame
        frame.add(this.headLine);
        frame.add(this.info);
        frame.add(this.usernameInput);
        frame.add(this.btn);
    }

    public void setVisible(boolean b)
    {
        this.headLine.setVisible(b);
        this.info.setVisible(b);
        this.usernameInput.setVisible(b);
        this.btn.setVisible(b);
    }
}