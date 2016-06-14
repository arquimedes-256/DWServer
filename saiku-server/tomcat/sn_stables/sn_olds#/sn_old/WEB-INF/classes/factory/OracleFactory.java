package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleFactory {

	private static Connection connection = null;
	public static Connection getConnection() 
	{
		if(connection == null)
		{
			createNewConnection();
		}
		return connection;
	}
	public static void createNewConnection() {
		try {
			 
			Class.forName("oracle.jdbc.driver.OracleDriver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
		}

		try {
 
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@//192.168.107.59:1521/ORCL", "TOP",
					"RDAJNTOP_2012");
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
		
	}
}
