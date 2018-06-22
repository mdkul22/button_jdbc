package mqtt;
import org.json.*
import java.sql.*

public class insertQuery {
static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
static final String DB_URL = "jdbc:mysql://localhost/button_data?useSSL=false";
static final String USER = "root";
static final String USER = "root";
private String table;

public insertQuery(String table) {
table = table;
}

public static void main(String[] args)
{
	new Listener(table).insertRow();
}

public void insertRow() {
	try{
 Class.forName("com.mysql.jdbc.Driver");
System.out.println("Connecting to the database");
conn = DriverManager.getConnection(DB_URL, USER, PASS);
stmt = conn.createStatement();
System.out.println("Creating statement..");
String sql;
sql = "select mac, building, floor, posX, posY from tqb_setup";
ResultSet rs = stmt.executeQuery(sql);

while(rs.next()){
String mac = rs.getString("mac");
String building = rs.getString("building");
String floor = rs.getString("floor");
String posX = rs.getString("posX");
String posY = rs.getString("posY");
System.out.print("mac: " + mac);
System.out.print("building: " + building);
}
System.out.println("Using Listener!");
rs.close();
stmt.close();
conn.close();
Listener sub = new Listener();
sub.doDemo();
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
