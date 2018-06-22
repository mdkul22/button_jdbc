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

public void insertAlertRow() {
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
	try{
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Connecting to the database");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = conn.createStatement();
		System.out.println("Creating statement..");	
		String sql;
		sql = "INSERT INTO tqb_alert "   ;
        sql += "(mac, alert) ";
        sql += "VALUES ";
        sql += "(" + mac + ", " + alertkey + ");";
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
System.out.println("Exit insert alert method!");
}

}
