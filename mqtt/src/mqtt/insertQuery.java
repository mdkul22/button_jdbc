package mqtt;
import org.json.*;
import java.util.*;
import java.sql.*;

public class insertQuery {
static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
static final String DB_URL = "jdbc:mysql://localhost/button_data?useSSL=false";
static final String USER = "root";
static final String PASS = "root";
static private String row;

public insertQuery(String x) {
row = x;
}

public void insertSetupRow() {
	Connection conn = null;
	Statement stmt =  null;
	try{
	JSONObject jsonObj = new JSONObject(row);
	String mac = jsonObj.getString("mac");
	String building = jsonObj.getString("building");
	String floor = jsonObj.getString("floor");
	String posX = jsonObj.getString("posX");
	String posY = jsonObj.getString("posY");
	System.out.println("Extract the JSONs");
	
	System.out.println("try block!");
	try{
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Connecting to the database");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = conn.createStatement();
		System.out.println("Creating statement..");	
		String sql;
		sql = "INSERT INTO tqb_setup "   ;
        sql += "(mac, building, floor, posX, posY) ";
        sql += "VALUES ";
        sql += "('" + mac + "', '" + building + "', '";
        sql += floor + "', '" + posX + "', '" + posY + "');";
		int rs = stmt.executeUpdate(sql);
		System.out.println("inserted row!");
		stmt.close();
		conn.close();
		}
	catch(SQLException se){
		se.printStackTrace();
		}
	catch(Exception e){
		e.printStackTrace();
		}
	finally{
		try{
			if(conn!=null)
				conn.close();
		}
		catch(SQLException se){
			se.printStackTrace();
		}
	}
	}
	catch(JSONException e)
	{	
		System.out.println("JSON ERROR!");
		return;
	}
	
System.out.println("Exit insert setup method!");
}

public String insertAlertRow() {
	Connection conn = null;
	Statement stmt =  null;
	try {
	JSONObject jsonObj = new JSONObject(row);
	String mac = jsonObj.getString("mac");	
	// getting keys of json
    Set<String> keys = jsonObj.keySet();	
    Iterator itr = keys.iterator();
    String alertkey;
    do{
    	alertkey = itr.next().toString();
        if(!alertkey.equals("mac"))
        	break;
       
    }while(itr.hasNext());
    String alertVal = jsonObj.getString(alertkey);
    System.out.println("alert value = " + alertVal);
	try{
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Connecting to the database");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = conn.createStatement();
		if(alertVal.equals("1"))
		{
		System.out.println("Creating statement..");	
		String sql;
		sql = "INSERT INTO table_alert "   ;
        sql += "(mac, alert, alertVal) ";
        sql += "SELECT * FROM (SELECT '" +  mac + "','" +  alertkey + "','" + alertVal + "') AS tmp";
		sql += " WHERE NOT EXISTS (";
		sql += "SELECT mac FROM table_alert WHERE mac = '" + mac + "' && alert='" + alertkey + "'";
		sql += ") LIMIT 1;";
		int rs = stmt.executeUpdate(sql);
		System.out.println("inserted row! " + rs);
		stmt.close();
		conn.close();
		return mac;
		// delete the sql table and restart table again
		}
		else if (alertVal.equals("0")) {
			System.out.println("Creating delete statement..");	
			String sql;
			sql = "DELETE FROM table_alert "   ;
			sql += "WHERE mac='" + mac + "' && " + "alert='" + alertkey+"';";
			int rs = stmt.executeUpdate(sql);
			System.out.println("deleted row!");
			stmt.close();
			conn.close();
			return "D";
		}
		return "-1";
	}
	catch(SQLException se){
		se.printStackTrace();
		return "-1";
		}
	catch(Exception e){
		e.printStackTrace();
		return "-1";
		}
	finally{
		try{
			if(conn!=null)
				conn.close();
		}
		catch(SQLException se){
			se.printStackTrace();
			return "-1";
		}
	System.out.println("Exit insert alert method!");
	}
	}
	catch(JSONException e)
	{
		return "-1";
	}
	
}

public String tableLinker(String mac)
{
Connection conn = null;
Statement stmt =  null;
try{
System.out.println("mac is "+mac);
Class.forName("com.mysql.jdbc.Driver");
System.out.println("Connecting to the database");
conn = DriverManager.getConnection(DB_URL, USER, PASS);
stmt = conn.createStatement();
System.out.println("Creating statement..");
String sql;
sql = "select mac, building, floor, posX, posY from tqb_setup where mac='" + mac + "';";
ResultSet rs = stmt.executeQuery(sql);
String building = new String();
String floor= new String();
String posX= new String();
String posY= new String();
String alert= new String();
String alertval= new String();
while(rs.next()){
 building = rs.getString("building");
 floor = rs.getString("floor");
 posX = rs.getString("posX");
 posY = rs.getString("posY");
System.out.print("building: " + building);
}
sql = "SELECT * FROM table_alert WHERE mac='" + mac + "';";
rs = stmt.executeQuery(sql);
while(rs.next()){
alert = rs.getString("alert");
alertval = rs.getString("alertVal");
}
rs.close();
stmt.close();
conn.close();
// writing string json which will be published to be read by mobiles
String json = new String();
json += "{";
json += "\"mac\": " + mac + ",";
json += "\"building\": " + building + ",";
json += "\"floor\": " + floor + ",";
json += "\"posX\": " + posX + ",";
json += "\"posY\": " + posY + ",";
json += "\"alert\": " + alert;
json += "}";
return json;

}
catch(SQLException se){
se.printStackTrace();
return "-1";
}
catch(Exception e){
e.printStackTrace();
return "-1";
}
finally{
try{
if(conn!=null)
conn.close();
System.out.println("BYE");
}
catch(SQLException se){
se.printStackTrace();
return "-1";
}
}
}
}
