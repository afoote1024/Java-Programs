/*
 * Created on Sep 28, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;


/*
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BankArray extends JFrame {
	
	private String strinput = " ";
		private int select;
	private Checking bankAccountC[];
	private Savings bankAccountS[];
		private Account bankA[];
		private int recct = 0;
		private Connection connection;
		private Statement statement;
		private String query;
		private ResultSet resultSet;
		private JList lstName;
			private JScrollPane scrNames;
		private JTextField txtAcctID;
		private JTextField txtBalance;
			private JLabel lblName, lblBalance, lblAcctID;
			private JRadioButton rdrCheck, rdrSaving;
		private JTextField txtName;
		private JButton btnOK, btnWithdraw;
	public BankArray()  // constructor
		 {
			bankA = new Account[20];
			bankAccountC = new Checking[10];
			bankAccountS = new Savings[10];
		  String url = "jdbc:mysql://localhost/IS345"; 
	  
	
		  // Load the driver to allow connection to the database
			  query = "SELECT tblAccount.AccountID, tblAccount.AccountName, " +
        " tblChecking.Balance FROM tblAccount INNER JOIN " +
        " tblChecking ON tblAccount.AccountID = tblChecking.AccountID " +
        " order by tblAccount.AccountID;";
			 try {
				Class.forName( "com.mysql.jdbc.Driver" ); //.newInstance();

				connection = DriverManager.getConnection(
			   url, "is345", "password"); 
			   
			  }
			catch ( ClassNotFoundException cnfex ) {
		   System.err.println(
			  "Failed to load JDBC/ODBC driver." );
		   cnfex.printStackTrace();
		   System.exit( 1 );  // terminate program
		   }
		 catch ( SQLException sqlex ) {
				System.err.println( "Unable to connect " + sqlex.getMessage() );
				sqlex.printStackTrace();
				System.exit( 1 );  // terminate program
			 }

			 try{
				   statement = connection.createStatement();
				   resultSet =
						   statement.executeQuery( query );

			 boolean readgood=true;
			 readgood = resultSet.next();
	
			 int i = 0;

		  while(readgood)
		  {
			bankAccountC[i] = new Checking();
			bankAccountC[i].querySet(resultSet);
			  readgood = resultSet.next();
			  i++;
			}
			recct = i; 
		}
		 catch ( SQLException sqlex ) {
			sqlex.printStackTrace();
		 }
		 
		 query = "SELECT tblAccount.AccountID, tblAccount.AccountName, " +
				" tblSavings.Balance FROM tblAccount INNER JOIN " +
				" tblSavings ON tblAccount.AccountID = tblSavings.AccountID " +
				" order by tblAccount.AccountID;";
		 try{
			statement = connection.createStatement();
				resultSet =
						statement.executeQuery( query );

					 boolean readgood=true;
					 readgood = resultSet.next();
					 int i = 0;

				  while(readgood)
				  {
					bankAccountS[i] = new Savings();
					bankAccountS[i].querySet(resultSet);
					  readgood = resultSet.next();
					  i++;
					}
		
					recct = i; 
				}
				 catch ( SQLException sqlex ) {
					sqlex.printStackTrace();
				 }	 
	   finally
	   {
	   try{
		  statement.close();
		  connection.close();
		  }
			 catch ( SQLException sqlex ) {
				sqlex.printStackTrace();
			 }
		   }
	   }// end of BankArray
	public void Init()
	   {
	getContentPane().setLayout(null);
		 txtAcctID = new JTextField(10);
		 txtBalance = new JTextField(6);
		
		ButtonGroup radioGroup;
		 radioGroup = new ButtonGroup();
			rdrCheck = new JRadioButton("Checking", true);
			rdrSaving = new JRadioButton("Savings", false);
			 btnOK = new JButton("OK");
			btnWithdraw = new JButton("Withdraw");
			 txtName = new JTextField(12);
			 txtName.setEditable(false);
			 txtAcctID.setEditable(false);
			 txtBalance.setEditable(false);

			 txtAcctID.setBounds(
				  255,50,125,25);
			 txtBalance.setBounds(
				 255,215,125,25);
			 txtName.setBounds(
				 255,107,125,25);
		
			btnWithdraw.setBounds(
						 255,265,105,35);
			 btnOK.setBounds(
				  100,265,105,35);
				rdrCheck.setBounds(38, 155, 168, 20);
				rdrSaving.setBounds(38, 180, 168, 20);
				radioGroup.add(rdrCheck);
				radioGroup.add(rdrSaving);
				String strinput[] = new String[20];
				ArrayList<Checking> alist = new ArrayList <Checking>();  
				Vector<Checking> vct = new Vector <Checking>();
				Enumeration<Checking> items = vct.elements();
				for(int i = 0;i < 5;i ++)
				{
					bankA[i]=bankAccountC[i];
					strinput[i]=bankA[i].getAccountName();
					alist.add(bankAccountC[i]);
					vct.add(bankAccountC[i]);
					Checking ck = items.nextElement();
					System.out.print("\n" + ck.getAccountName());	
				}
				Iterator<Checking> iter=alist.iterator();
				for (int i = 0; i < 5; i ++)
					{
						//bankA[i] = bankAccountC[i];
					//	strinput[i]= bankA[i].getAccountName();
					Checking cka = (Checking) iter.next();
							System.out.print("\n" + cka.getAccountName());;
					  }
								
				 for (int x = 0; x < 5; x ++)
					  {
					  bankA[x +5] = bankAccountS[x];
					  strinput[x+5]= bankA[x +5].getAccountName();
					}
					
					
				lstName = new JList(strinput);
			    lstName.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				 JScrollPane scrNames=new JScrollPane(lstName);
				 scrNames.setBounds(20, 45, 190,80);
				 getContentPane().add(scrNames);
				getContentPane().add(txtName);
				getContentPane().add(txtAcctID);
				getContentPane().add(txtBalance);
				getContentPane().add(rdrCheck);
				getContentPane().add(rdrSaving);
				
				getContentPane().add(btnOK);
					btnOK.addActionListener(
				  new ActionListener()
				  {
					public void actionPerformed(ActionEvent e)
					{
					  // add code here
						int i = lstName.getSelectedIndex();
						   if (rdrSaving.isSelected())
						   {i+=5;
			   }
				//   bankA[i].Withdrawal(10);
				   txtName.setText(bankA[i].getAccountName());;
				   txtBalance.setText(Float.toString(bankA[i].getBalance()));
			   }
					}
							);
					getContentPane().add(btnWithdraw);
						   btnWithdraw.addActionListener(
						 new ActionListener()
						 {
						   public void actionPerformed(ActionEvent e)
						   {
							 // add code here
							   int i = lstName.getSelectedIndex();
									   if (rdrSaving.isSelected())
									   {i+=5;
						   }
							   bankA[i].Withdrawal(10);
							   txtName.setText(bankA[i].getAccountName());;
							   txtBalance.setText(Float.toString(bankA[i].getBalance()));
						   }
						 }
						   );

	   }

	public static void main (String args[])
	{

	//	int select ;
		BankArray ba = new BankArray();
		ba.Init();
		ba.setSize(400,350);
		ba.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ba.setVisible(true);
	}	
	}// end of class - Bank

