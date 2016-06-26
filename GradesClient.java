/**
 * Created with IntelliJ IDEA.
 * User: Maka
 * Date: 10/20/13
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class GradesClient extends JFrame{

    public static void main(String args[])
    {
        GradesClient gC = new GradesClient();
        gC.setSize(460,340);
        gC.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gC.setVisible(true);
    }

    /* Below contains form items */
    private JMenuBar mBar;
        private JMenu mnuFile;
            private JMenuItem mnuNext;
            private JMenuItem mnuPrevious;
            private JMenuItem mnuExit;
    private JList lstNames;
    private JLabel lblHeader;
    private JLabel lblName;
    private JLabel lblTotalPoints;
    private JLabel lblPercent;
    private JTextField txtName;
    private JTextField txtTotalPoints;
    private JTextField txtPercent;
    private JButton btnOk;
    private JScrollPane scrNames;
    /*variable for sending and receiving from GradesServer*/
    private DatagramPacket goPacket;
    private DatagramPacket getPacket;
    private DatagramSocket gradSocket;

    private int recct = 0;


    public void Init()
    {
        getContentPane().setLayout(null);
        lblHeader = new JLabel("IS110B Intro to Computers");
        lblName = new JLabel("Name");
        lblTotalPoints = new JLabel("Total Points");
        lblPercent = new JLabel("Percent");
        txtName = new JTextField(20);
            txtName.setEditable(false);
        txtTotalPoints = new JTextField(10);
            txtTotalPoints.setEditable(false);
        txtPercent = new JTextField(10);
            txtPercent.setEditable(false);
        btnOk = new JButton("OK");

        // set locations for form object
        lblHeader.setBounds(new Rectangle(145,15,150,20));
        lblName.setBounds(new Rectangle(195,50,125,25));
        lblTotalPoints.setBounds(new Rectangle(195,105,125,25));
        lblPercent.setBounds(new Rectangle(195,160,125,25));
        txtName.setBounds(new Rectangle(290,50,125,25));
        txtTotalPoints.setBounds(new Rectangle(290,105,50,25));
        txtPercent.setBounds(new Rectangle(290,160,75,25));
        btnOk.setBounds(new Rectangle(200,220,60,30));
        // setting up menu bar
        mBar = new JMenuBar();
        mnuFile = new JMenu("File");
            mnuNext = new JMenuItem("Next");
            mnuPrevious = new JMenuItem("Previous");
            mnuExit = new JMenuItem("Exit");

        sendPacket("0");
        recct = Integer.parseInt(waitForPackets());
        String studentName[] = new String[(recct - 1)];
        // loop for receiving a names for the list box
        for(int i = 0; i < (recct-1); i++)
        {
            sendPacket("1 " + i);
            studentName[i] = waitForPackets();
        }
        lstNames = new JList<String>(studentName);
        scrNames = new JScrollPane(lstNames);
        scrNames.setBounds(15,50,155,135);

        mnuFile.add(mnuNext);
        mnuFile.add(mnuPrevious);
        mnuFile.add(mnuExit);
        mBar.add(mnuFile);
        setJMenuBar(mBar);

        getContentPane().add(lblHeader);
        getContentPane().add(lblName);
        getContentPane().add(lblTotalPoints);
        getContentPane().add(lblPercent);
        getContentPane().add(txtName);
        getContentPane().add(txtTotalPoints);
        getContentPane().add(txtPercent);
        getContentPane().add(scrNames);
        getContentPane().add(btnOk);

        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (lstNames.getSelectedIndex() != -1)
                {
                	int w = lstNames.getSelectedIndex();
                	sendPacket("1 " + w);
                	txtName.setText(waitForPackets());
                	sendPacket("2 " + w);
                	txtTotalPoints.setText(waitForPackets());
                	sendPacket("3 " + w);
                	txtPercent.setText(waitForPackets());
                }
            }
        });

        mnuNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int w = lstNames.getSelectedIndex();
                w++;
                if(w > (lstNames.getModel().getSize() - 1)) w = (lstNames.getModel().getSize() - 1);
                sendPacket("1 " + w);
                txtName.setText(waitForPackets());
                sendPacket("2 " + w);
                txtTotalPoints.setText(waitForPackets());
                sendPacket("3 " + w);
                txtPercent.setText(waitForPackets());
                lstNames.setSelectedIndex(w);
            }
        });

        mnuPrevious.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int w = lstNames.getSelectedIndex();
                w--;
                if(w < 0) w = 0;
                sendPacket("1 " + w);
                txtName.setText(waitForPackets());
                sendPacket("2 " + w);
                txtTotalPoints.setText(waitForPackets());
                sendPacket("3 " + w);
                txtPercent.setText(waitForPackets());
                lstNames.setSelectedIndex(w);
            }
        });

        mnuExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    public GradesClient()
    {
        super("IS345 Project 2 - Kevin Horejs");
        // open socket
        try
        {
            gradSocket = new DatagramSocket(1320);
        }
        catch (SocketException se)
        {
            se.printStackTrace();
            System.exit(1);
        }
        // initiate the form
        try
        {
            Init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // method for receiving packet from server program
    public String waitForPackets()
    {
        try
        {
            // setting up packet
            byte data[] = new byte[100];
            getPacket = new DatagramPacket(data, data.length);
            // waiting and receiving packet
            gradSocket.receive(getPacket);
        }
        catch (IOException ie)
        {
            System.out.println(ie.toString() + "\n");
            ie.printStackTrace();
        }
        return new String(getPacket.getData()).trim();
    }

    // method for sending packet to server program
    public void sendPacket(String str)
    {
        try
        {
            byte data[] = str.getBytes();
            goPacket = new DatagramPacket(data, data.length, InetAddress.getByName(""), 5000);
            gradSocket.send(goPacket);
        }
        catch (IOException ie)
        {
            System.out.println(ie.toString() + "\n");
            ie.printStackTrace();
        }
    }
}
