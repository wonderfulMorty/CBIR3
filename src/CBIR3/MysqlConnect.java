package CBIR3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class MysqlConnect {

	private String driver;
	private String url;
	private String user;
	private String password;	
	private Connection con ;
	private Statement stmt;

	public MysqlConnect() {
		   //driver = "com.mysql.jdbc.Driver";
		   driver="com.mysql.cj.jdbc.Driver"; 
		   url="jdbc:mysql://127.0.0.1:3306/";
		   user="root";
		   password="ln579683";
	}
	
	public void getConnectionAndStatement(String dbname){
		try {
			Class.forName(driver);
			url+=dbname;
			url+="?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			con = DriverManager.getConnection(url,user,password);
			if(!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			stmt = con.createStatement();			
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!"); 
			e.printStackTrace();
		}catch (SQLException e) {
			System.out.println("Sorry,SQLException!"); 
			e.printStackTrace();
		}catch (Exception e) {
			System.out.println("Sorry,Exception!"); 
			e.printStackTrace();
		}
		
	}
	
	public Statement getStatement(){
		return stmt;
	}
	
	public void closeMysql() {
		try {
			con.close();
			System.out.println("Succeeded closing the Database!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isClose(){
		try {
			if(!con.isClosed())
				return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
