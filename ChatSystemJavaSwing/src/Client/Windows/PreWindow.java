package Client.Windows;

import Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PreWindow extends JPanel
{
    private JLabel headLine;
    private JLabel info;
    private JLabel defaultServer;
    private JLabel customServer;
    private JLabel ipAddress;
    private JLabel port;

    private JTextField ipInput;
    private JTextField portInput;

    private JButton defaultBtn;
    private JButton customBtn;

    private Client client;

    public PreWindow(JFrame frame, Client client)
    {
        //  client
        this.client = client;

        //  head line
        this.headLine = new JLabel("Connect to a Server");
        this.headLine.setBounds(40, 20, 520, 55);
        this.headLine.setForeground(Color.BLACK);
        this.headLine.setFont(new Font("Times New Roman", Font.BOLD, 30));

        //  info label
        this.info = new JLabel("Connect by default or custom Server");
        this.info.setBounds(40, 46, 520, 120);
        this.info.setForeground(Color.BLACK);
        this.info.setFont(new Font("Times New Roman", Font.ITALIC, 20));

        //  default server label
        this.defaultServer = new JLabel("DEFAULT");
        this.defaultServer.setBounds(40, 220, 250, 35);
        this.defaultServer.setForeground(Color.BLACK);
        this.defaultServer.setFont(new Font("Times New Roman", Font.BOLD, 30));

        //  default server btn
        this.defaultBtn = new JButton();
        this.defaultBtn.setForeground(Color.BLACK);
        this.defaultBtn.setFont(new Font(("Times New Roman"), Font.BOLD, 15));
        this.defaultBtn.setText("Default");
        this.defaultBtn.setBackground(Color.BLACK);
        this.defaultBtn.setOpaque(false);
        this.defaultBtn.setContentAreaFilled(false);
        this.defaultBtn.setBounds(40, 270, 150, 55);
        this.defaultBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                client.connectToServer();
            }
        });

        //  custom server label
        this.customServer = new JLabel();
        this.customServer.setBounds(40, 370, 520, 55);
        this.customServer.setForeground(Color.BLACK);
        this.customServer.setFont(new Font("Times New Roman", Font.BOLD, 30));
        this.customServer.setText("CUSTOM SERVER");

        //  ip label
        this.ipAddress = new JLabel();
        this.ipAddress.setBounds(40, 460, 200, 55);
        this.ipAddress.setForeground(Color.BLACK);
        this.ipAddress.setFont(new Font("Times New Roman", Font.BOLD, 20));
        this.ipAddress.setText("IP ADDRESS");

        //  port label
        this.port = new JLabel();
        this.port.setBounds(310, 460, 200, 55);
        this.port.setForeground(Color.BLACK);
        this.port.setFont(new Font("Times New Roman", Font.BOLD, 20));
        this.port.setText("PORT");

        //  IP input
        this.ipInput = new JTextField();
        this.ipInput.setBounds(40, 530, 250, 35);
        this.ipInput.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        //  port input
        this.portInput = new JTextField();
        this.portInput.setBounds(310, 530, 250, 35);
        this.portInput.setFont(new Font("Times New Roman", Font.PLAIN, 15));


        //  custom server btn
        this.customBtn = new JButton();
        this.customBtn.setForeground(Color.BLACK);
        this.customBtn.setFont(new Font(("Times New Roman"), Font.BOLD, 15));
        this.customBtn.setText("Custom");
        this.customBtn.setBackground(Color.BLACK);
        this.customBtn.setOpaque(false);
        this.customBtn.setContentAreaFilled(false);
        this.customBtn.setBounds(40, 575, 150, 55);
        this.customBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.connectToServer(ipInput.getText(), portInput.getText());
            }
        });

        //  add elements to the frame
        frame.add(this.headLine);
        frame.add(this.info);
        frame.add(this.defaultServer);
        frame.add(this.customServer);
        frame.add(this.ipAddress);
        frame.add(this.port);
        frame.add(this.ipInput);
        frame.add(this.portInput);
        frame.add(this.defaultBtn);
        frame.add(this.customBtn);
    }

    public void setVisible(boolean b)
    {
        this.headLine.setVisible(b);
        this.info.setVisible(b);
        this.defaultServer.setVisible(b);
        this.customServer.setVisible(b);
        this.ipAddress.setVisible(b);
        this.port.setVisible(b);
        this.ipInput.setVisible(b);
        this.portInput.setVisible(b);
        this.defaultBtn.setVisible(b);
        this.customBtn.setVisible(b);
    }

    //  getters
    public String getPortInputText()
    {
        return portInput.getText();
    }

    //  setters
    public void setInfoText(String text)
    {
        this.info.setText(text);
    }
}