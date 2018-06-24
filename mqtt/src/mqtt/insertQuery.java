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
	JSONObject jsonObj = new JSONObject(row);
	String mac = jsonObj.getString("mac");
	String building = jsonObj.getString("building");
	String floor = jsonObj.getString("floor");
	String posX = jsonObj.getString("posX");
	String posY = jsonObj.getString("posY");
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
        sql += "(" + mac + ", " + building + ", ";
        sql += floor + ", " + posX + ", " + posY + ");";
		ResultSet rs = stmt.executeQuery(sql);
		System.out.println("inserted row!");
		rs.close();
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
System.out.println("Exit insert setup method!");
}

public String insertAlertRow() {
	Connection conn = null;
	Statement stmt =  null;
	JSONObject jsonObj = new JSONObject(row);
	String mac = jsonObj.getString("mac");
	// getting keys of json
    Set<String> keys = jsonObj.keySet();	
    Iterator itr = keys.iterator();
    String[] val = new String[2];
    int i = 0;
    String alertkey;
    do{
        val[i] = itr.next().toString();
        System.out.println(val[i]);
        i++;

    }while(itr.hasNext());
    alertkey = val[1];
    String alertVal = jsonObj.getString(alertkey);
	try{
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Connecting to the database");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = conn.createStatement();
		System.out.println("Creating statement..");	
		String sql;
		sql = "INSERT INTO tqb_alert "   ;
        sql += "(mac, alert, alertVal) ";
        sql += "VALUES ";
        sql += "(" + mac + ", " + alertkey + ", " + alertVal ");";
		
		ResultSet rs = stmt.executeQuery(sql);
		System.out.println("inserted row!");
		rs.close();
		stmt.close();
		conn.close();
		return mac;
		// delete the sql table and restart table again
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
	}
System.out.println("Exit insert alert method!");
}
public String tableLinker(String mac)
{
String mac = mac;
Connection conn = null;
Statement stmt =  null;
try{
Class.forName("com.mysql.jdbc.Driver");
System.out.println("Connecting to the database");
conn = DriverManager.getConnection(DB_URL, USER, PASS);
stmt = conn.createStatement();
System.out.println("Creating statement..");
String sql;
sql = "select building, floor, posX, posY from tqb_setup where mac=" + mac + ";";
ResultSet rs = stmt.executeQuery(sql);

while(rs.next()){
String building = rs.getString("building");
String floor = rs.getString("floor");
String posX = rs.getString("posX");
String posY = rs.getString("posY");
System.out.print("building: " + building);
}
sql = "select from tqb_alert where mac=" + mac + ";";
rs = stmt.executeQuery(sql);
while(rs.next()){
String alert = rs.getString("alert");
String alertval = rs.getString("alertVal");
}
rs.close();
stmt.close();
conn.close();
// writing string json which will be published to be read by mobiles
String json;
json += "{";
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
System.out.println("BYE");
}
}
