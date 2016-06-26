

import java.io.*;
import java.sql.*;
/*
 *
 * coursegrades
 *
 */
public class coursegrades
{
 private String courseID;
 private String coursename;
 private String Name;
 private int totalgrade;
 private double percent;
 private int StudentID;

 public coursegrades(String pname)
 {

	 courseID = "IS110B";
         coursename = "Intro to Computers";
	 Name = new String(pname);

 }

 public coursegrades()
 {

	 courseID = "IS110B";
         coursename = "Intro to Computers";
 }
 public coursegrades(String cid, String cname, String pname)
 {
	 courseID =  cid;
         coursename = cname;
	 Name = pname;
 }

 public int calc_total_grade(int grades[], double totpos)
 {
	 totalgrade = 0;
   	 for (int i = 0; i < 4; i++)
	{

	totalgrade += grades[i] ;
	 }

	percent = (double)totalgrade / totpos;
	 return totalgrade;
 }

 public double get_percent()
 {
	 return percent;
 }

 public int get_total()
 {
	 return totalgrade;
 }

  public String get_name()
  {
	  return Name;
  }

  public String get_Course_ID()
  {
	return courseID;
  }

public String get_Course_Name()
{
	return coursename;
}

//Project 2 adds

  public void set_total(int newtot)
  {
	  totalgrade = newtot;
  }
    public void set_percent(double newtot)
  {
	  percent = newtot * 100.00;
  }

    public double calc_percent(int newtot)
  {
	  percent = newtot/400.00 * 100.00;
	  return percent;
  }
  public void set_name(String nm)
	   {
	   Name = new String(nm);
	   }

 public boolean readGrades(DataInputStream input)
	{
		boolean ret_code = true;
			try{
			courseID = new String(input.readUTF());
			coursename = new String(input.readUTF());
			Name = new String(input.readUTF());
			totalgrade=input.readInt();
			percent = input.readDouble();
			ret_code = true;
			} 	catch (EOFException eof)
			{
			System.out.println ("End of File ");
			ret_code = false;
		//	break;
    		}
			catch (IOException excp)
			{
			System.out.println("Error Reading file");
			System.err.println(" File Error " + excp.toString());
			System.exit(1);
			}
			return ret_code;
	} // end of readGrades
   public boolean readDBGrades(ResultSet rs)
	{
		boolean ret_code = true;
		try{
			courseID = rs.getString(1);
			coursename = rs.getString(2);
			StudentID = rs.getInt(3);
			Name = rs.getString(4);
			totalgrade=rs.getInt(5);
			percent = rs.getDouble(6);

    		}
			catch (SQLException excp)
			{
			System.out.println("Error Reading DB");
			System.err.println(" DB Error " + excp.toString());
			System.exit(1);
			}
			return ret_code;
	} // end of readDBGrades

  public boolean writeGrades(DataOutputStream output)
	{
		boolean ret_code = true;
			try{
			output.writeUTF(coursename);
			output.writeUTF(courseID);
			output.writeUTF(Name);
			output.writeInt(totalgrade);
			output.writeDouble(percent);
			ret_code = true;
    		}
			catch (IOException excp)
			{
			System.out.println("Error Reading file");
			System.err.println(" File Error " + excp.toString());
			System.exit(1);
			ret_code = false;
			}
			return ret_code;
	} //end of writeGrades
   public int writeDBGrades(Statement state)throws SQLException
	{
		String query = "UPDATE qryCourseGrades SET " +
				   "CourseID='"+ courseID.trim() +
				   "', CourseName='"+ coursename.trim() +
				   "', StuName='" + Name.trim() +
				   "', Percentage=" + percent +
				   ", Test=" + totalgrade +
				   " WHERE ID=" + StudentID + ";";

		int result = state.executeUpdate(query);
		return result;
	} //end of writeDBGrades
}

