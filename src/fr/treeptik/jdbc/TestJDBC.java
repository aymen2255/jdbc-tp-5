package fr.treeptik.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class TestJDBC {

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/employee_manager";
		Connection connection = DriverManager
				.getConnection(url, "root", "root");
		TestJDBC testJDBC = new TestJDBC();
		// testJDBC.addEmployee(connection);
		// testJDBC.getEmployee(connection, "employee1");
		// testJDBC.deleteEmployee(connection, 2);
		testJDBC.getAllInformationOfTable(connection, "work");
		connection.close();

	}

	public void addEmployee(Connection connection) throws Exception {
		// insert new adress
		String queryAdress = " INSERT INTO adress (city, state, street,zip )"
				+ " values (?, ?, ?, ?)";
		PreparedStatement preparedStmt = connection.prepareStatement(
				queryAdress, Statement.RETURN_GENERATED_KEYS);
		preparedStmt.setString(1, "city1");
		preparedStmt.setString(2, "state1");
		preparedStmt.setString(3, "street1");
		preparedStmt.setInt(4, 13000);
		preparedStmt.executeUpdate();
		ResultSet adressId = preparedStmt.getGeneratedKeys();

		adressId.next();

		java.util.Date date = Calendar.getInstance().getTime();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());

		String query = " INSERT INTO employee ( name, salary, startdate, adressid, departementid, last_name)"
				+ " values ( ?, ?, ?, ?, ?, ?)";

		PreparedStatement preparedStmt2 = connection.prepareStatement(query);

		preparedStmt2.setString(1, "employee1");
		preparedStmt2.setInt(2, 15000);
		preparedStmt2.setDate(3, sqlDate);
		preparedStmt2.setInt(4, adressId.getInt(1));
		preparedStmt2.setInt(5, 1);
		preparedStmt2.setString(6, "employee_last_name");
		preparedStmt2.executeUpdate();

	}

	public void getEmployee(Connection connection, String name)
			throws Exception {
		String query = " select * from employee e where e.name = ?";

		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, name);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {

			System.out.println(resultSet.getString("id") + "nom "
					+ resultSet.getString("name"));
		}

	}

	public void deleteEmployee(Connection connection, int id) throws Exception {
		// employee a supprimer et son adresse
		String q1 = "select * from employee where id = ?";
		PreparedStatement statement1 = connection.prepareStatement(q1);
		statement1.setInt(1, id);
		ResultSet resultSet1 = statement1.executeQuery();
		int adressid = 0;
		while (resultSet1.next()) {
			adressid = resultSet1.getInt("adressid");

		}

		// liste des employee dans la meme adresse

		String q2 = "select * from employee where adressid = ?";
		PreparedStatement statement2 = connection.prepareStatement(q2);
		statement2.setInt(1, adressid);
		ResultSet resultSet2 = statement2.executeQuery();

		while (resultSet2.next()) {
			System.out.println(resultSet2.getString("name"));
		}
		if (resultSet2.next() == false) {
			String q3 = " DELETE FROM adress WHERE id = ?";
			PreparedStatement statement3 = connection.prepareStatement(q3);
			statement3.setInt(1, adressid);
			statement3.executeUpdate();

			String q4 = " DELETE FROM employee WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(q4);
			statement.setInt(1, id);
			statement.executeUpdate();

		}

	}

	public void getAllInformationOfTable(Connection connection, String tableName) throws Exception {
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet res = meta.getTables(null, null, null,null);
		String table = "";
		
		while (res.next()) {

			
			if (tableName.equalsIgnoreCase(res.getString("TABLE_NAME"))) {
				System.out.println(res.getString("TABLE_NAME"));
				table = res.getString("TABLE_NAME");
			}
		}
		//System.out.println(table);
		ResultSet rsColumns = null;
		rsColumns = meta.getColumns(null, null, table, null);
	    while (rsColumns.next()) {
	      System.out.println(rsColumns.getString("TYPE_NAME"));
	      System.out.println(rsColumns.getString("COLUMN_NAME"));
	    }
	}

}
