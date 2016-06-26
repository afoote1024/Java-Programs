
import java.sql.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;


public class GradesServer extends JFrame
{
   private JTextArea display;

   private DatagramPacket sendPacket, receivePacket;
   private DatagramSocket socket;
   private coursegrades grades[];
   private Connection databaseConnection;
   private String strreceive;
   private int numrecs;



	 public void readDBfile()throws ClassNotFoundException
	 {
	  int i;
	  Statement statement;
	  try{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		String sourceURL = new String("jdbc:odbc:JDBCgrades");
		 databaseConnection =
		DriverManager.getConnection(sourceURL, "anonymous", "guest");



		String query ="SELECT * FROM qryCourseGrades";

		 statement = databaseConnection.createStatement ();
		ResultSet resultset = statement.executeQuery(query);

		grades = new coursegrades[10];
		boolean qryretcode = true;

		for (i = 0; i < 10 && qryretcode==true; i ++)
		{
		qryretcode =resultset.next();
		if (qryretcode)
		{
		grades[i] = new coursegrades();
		grades[i].readDBGrades(resultset);
		System.out.println(" " + grades[i].get_name()+
                             " " + grades[i].get_total()+
			     " " + grades[i].get_percent());
		}// end of if
		}// end of for
		numrecs = i;
		statement.close(); // close db connection
		}
                catch (ClassNotFoundException cnfe)
	    {
	    System.out.println(cnfe);
	    cnfe.printStackTrace();
        	}
		catch( SQLException sqle)
		{
			System.out.println(sqle);
			sqle.printStackTrace();
	}
	display.append((--numrecs) + " Records read");



	 } // end of readDBfile
      public void readMySQLDB()//throws ClassNotFoundException
      {
        int i;
        Statement statement = null;
	   String url = "jdbc:mysql://localhost/IS345"; //?user=is345&password=password";
	  
	
	  // Load the driver to allow connection to the database


		 try {
			Class.forName( "com.mysql.jdbc.Driver" ); //.newInstance();

			databaseConnection = DriverManager.getConnection(
		   url, "is345", "password"); 
		   //&password=password");
			 
		   //	url); //+ "?user=is345" + "&password=password" );
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
			String query ="SELECT * FROM tblCourse INNER JOIN ";
			query = query + "tblStudent ON tblCourse.CourseID = tblStudent.CourseID;";

	  statement = databaseConnection.createStatement ();
	 ResultSet resultset = statement.executeQuery(query);

	 grades = new coursegrades[30];
	 boolean qryretcode = true;

	 for (i = 0; i < 22 && qryretcode==true; i ++)
	 {
		qryretcode = resultset.next();
		 if (qryretcode)
		 {
		 grades[i] = new coursegrades();
		 grades[i].readDBGrades(resultset);
		 System.out.println(" " + grades[i].get_name()+
							  " " + grades[i].get_total()+
				  " " + grades[i].get_percent());
		 }// end of if
		 }// end of for
		 numrecs = i;
	}
	 catch ( SQLException sqlex ) {
		sqlex.printStackTrace();
	 }
   finally
   {
   try{
	  statement.close();
	databaseConnection.close();
	  }
		 catch ( SQLException sqlex ) {
			sqlex.printStackTrace();
		 }
	   }
		
		
  

              
        display.append((--numrecs) + " Records read");



	  }// end of readMySQLDB



	 public void writefile()throws IOException
	 {
		 DataOutputStream output = new DataOutputStream(
		 new FileOutputStream ("ff.dat"));
		int i;
		for (i = 0; i < numrecs; i ++)
				{
					grades[i].writeGrades(output);
				}
		display.append((i) + " Records written to file");
		try{
			output.close();
		}catch(IOException ecp){}

	 }  // end of writefile


		public void UpdateDB()throws SQLException
	 {
		Statement state = databaseConnection.createStatement ();
		int i=0;
		int retcode;
		for (i = 0; i < numrecs; i ++)
			{
			try{
			  retcode = grades[i].writeDBGrades(state);

			  }catch(SQLException ecp){
			    ecp.printStackTrace();
			}
			}// end of for
		display.append((i) + " Records updated");
		try{
			state.close();
		}catch(SQLException ecp){}

	 }  // end of UPDateDB

   public GradesServer()
   {
      super( "GradesServer" );
      display = new JTextArea();
      getContentPane().add(new JScrollPane(display), BorderLayout.CENTER);
      setSize( 400, 300 );
      setVisible( true );
	//  try{
			readMySQLDB();
	//  }catch (ClassNotFoundException ifs){}
      try {
         socket = new DatagramSocket( 5000 );
      }
      catch( SocketException se ) {
         se.printStackTrace();
         System.exit( 1 );
      }
   }

   public void waitForPackets()
   {
	   int arrayctr=0;
	   StringBuffer ps = new StringBuffer();
       String sr;
	   while ( true ) {
         try {
            // set up packet
            byte data[] = new byte[ 100 ];
            receivePacket =
               new DatagramPacket( data, data.length );

            // wait for packet
            socket.receive( receivePacket );

  	String strrec = new String(receivePacket.getData() ).trim();
            // process packet
            display.append( "\nPacket received:" +
               "\nFrom host: " + receivePacket.getAddress() +
               "\nHost port: " + receivePacket.getPort() +
               "\nLength: " + receivePacket.getLength() +
               "\nContaining:\n\t" +
               strrec);
			display.append("\n"); // At position 0 is " +  strreceive.charAt(0));

	// 0 - number of records read

	if (strrec.charAt(0) == '0')
		{
		   strreceive = new String(String.valueOf (numrecs));
		   display.append(strreceive);
		}



	// 1 - Get Student Name

	if (strrec.charAt(0) == '1')
		{
                  if (strrec.length()>3)
                    ps.append(strrec.substring(2,4));
                    else
                      ps.append(strrec.charAt(2));

	    arrayctr = Integer.valueOf(ps.toString()).intValue();

   		strreceive = new String(
                     grades[arrayctr].get_name());
	    display.append(strreceive);
			}

	// 2 - Get Total

	if (strrec.charAt(0) == '2')
		{
                  if (strrec.length()>3)
                     ps.append(strrec.substring(2,4));
                     else
                       ps.append(strrec.charAt(2));

                arrayctr = Integer.valueOf(ps.toString()).intValue();
                strreceive = new String(String.valueOf(
			grades[arrayctr].get_total()));
		display.append(strreceive);
			}

	// 3 - Get Percent

	if (strrec.charAt(0) == '3')
	   {
             if (strrec.length()>3)
                     ps.append(strrec.substring(2,4));
                     else
                       ps.append(strrec.charAt(2));

	    arrayctr = Integer.valueOf(ps.toString()).intValue();
            strreceive = new String(String.valueOf (
						grades[arrayctr].get_percent()));//).toString();
	    display.append(strreceive);
			}


	// 4 - New Value for Name
	if (strrec.charAt(0) == '4')
        	{
                  if (strrec.length()>3)
                      ps.append(strrec.substring(2,4));
                      else
                        ps.append(strrec.charAt(2));

	        arrayctr = Integer.valueOf(ps.toString()).intValue();
		sr = new String(strrec.substring(4));
	        grades[arrayctr].set_name(sr);
		display.append (" New Student Name " + arrayctr + " " +
							  grades[arrayctr].get_name());
		strreceive = new String(
					grades[arrayctr].get_name());
			}

	// 5 - New Value for Total
	if (strrec.charAt(0) == '5')
		{
                  if (strrec.length()>3)
                      ps.append(strrec.substring(2,4));
                      else
                        ps.append(strrec.charAt(2));

	        arrayctr = Integer.valueOf(ps.toString()).intValue();
		sr = new String(strrec.substring(4));
	        grades[arrayctr].set_total(Float.valueOf(sr).intValue());
		display.append (" New Student Total " + arrayctr + " " +
							  grades[arrayctr].get_total());
			strreceive = new String(String.valueOf
				(grades[arrayctr].get_total()));
			}

	// 6 - New Value for Percent
	if (strrec.charAt(0) == '6')
		{
                  if (strrec.length()>3)
                      ps.append(strrec.substring(2,4));
                      else
                        ps.append(strrec.charAt(2));

	        arrayctr = Integer.valueOf(ps.toString()).intValue();
		sr = new String(strrec.substring(4));
	        grades[arrayctr].set_percent(Float.valueOf(sr).doubleValue());
		display.append (" New Student Percent " + arrayctr + " " +
				  grades[arrayctr].get_percent());
		strreceive = new String(String.valueOf (
				grades[arrayctr].get_percent()));
		}

	// 7 - Get Course ID
	if (strrec.charAt(0) == '7')
		{
		ps.append(strrec.charAt(2));
	        arrayctr = Integer.valueOf(ps.toString()).intValue();
        	strreceive = new String(String.valueOf(
					grades[arrayctr].get_Course_ID()));
		display.append(strreceive);
		}

	// 8 - Get Course Name
	if (strrec.charAt(0) == '8')
		{
		ps.append(strrec.charAt(2));
	        arrayctr = Integer.valueOf(ps.toString()).intValue();
                strreceive = new String(
				grades[arrayctr].get_Course_Name());
		display.append(strreceive);
		}

	// 9 - Write to File
	if (strrec.charAt(0) == '9')
		{ try{
			UpdateDB();
			  }catch(SQLException exp){}
		 strreceive = new String(" ");
		    }



	// send to client
	 byte bydata[] = strreceive.getBytes();
		 display.append( "\n\nSend data to client...");
           sendPacket = new DatagramPacket(
                          bydata,
                          bydata.length,
                          receivePacket.getAddress(),
                          receivePacket.getPort() );
            socket.send( sendPacket );
            display.append( "Packet sent\n" );
         }
         catch( IOException io ) {
            display.append( io.toString() + "\n" );
            io.printStackTrace();
         }
		 ps.setLength(0);
      }
   }// end of wait for packets

   public static void main( String args[] )
   {
      GradesServer s = new GradesServer();

      s.addWindowListener( new CloseWindowAndExit() );
      s.waitForPackets();
   }
}
