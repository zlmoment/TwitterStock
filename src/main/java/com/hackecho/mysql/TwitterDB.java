package com.hackecho.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TwitterDB {
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/twitterdb", "root", "");

		} catch (Exception e) {
			System.out.println("Failed to connect database " + e.getMessage());
		}
		return con;
	}

	public void query(String sql) throws Exception {
		try {
			connect = getConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery(sql);
		} catch (Exception e) {
			throw e;
		} finally {
			connect.close();
		}
	}
	
	public void update(String sql) throws Exception {
		try {
			connect = getConnection();
			statement = connect.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			throw e;
		} finally {
			connect.close();
		}
	}

	public boolean insert(String sql) throws SQLException {
		try {
			connect = getConnection();
			// String sql = "INSERT INTO staff(name, age, sex, address)" +
			// " VALUES ('Tom1', 32, 'M', 'china')";
			statement = connect.createStatement();
			int count = statement.executeUpdate(sql);
			if (count != 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Faile insert " + e.getMessage());
		} finally {
			connect.close();
		}
		return true;
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		while (resultSet.next()) {
			String id = resultSet.getString("id");
			String name = resultSet.getString("name");
			String age = resultSet.getString("age");
			String sex = resultSet.getString("sex");
			String address = resultSet.getString("address");
			System.out.println("Id: " + id);
			System.out.println("name: " + name);
			System.out.println("age: " + age);
			System.out.println("sex: " + sex);
			System.out.println("address: " + address);
		}
	}
}
