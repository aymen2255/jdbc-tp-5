package fr.treeptik.jdbc.connection;

import java.sql.DriverManager;

public class Connection {
	public void getConn() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		String url= "jdbc:mysql://localhost:3306/employee_manager";
		Connection connection = (Connection) DriverManager.getConnection(url,"root","root");
	}
}
