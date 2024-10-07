package com.partnerPortal.qa.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.partnerPortal.qa.util.TestUtil;
import com.partnerPortal.qa.util.WebEventListener;

public class TestBase {
	
	public static WebDriver driver;
	public static Properties prop;
	public  static EventFiringWebDriver e_driver;
	public static WebEventListener eventListener;

	private String DBurl = "jdbc:sqlserver://10.6.2.202;databaseName=Partner_Portal;encrypt=true;trustServerCertificate=true";
	private String user = "sa";
	private String password = "welcome3#";
	protected Connection connection;
	
	public TestBase(){
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+ "/src/main/java/com/partnerPortal"
					+ "/qa/config/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void initialization(){
		String browserName = prop.getProperty("browser");
		
		if(browserName.equals("chrome")){
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\y000281\\Downloads\\chromedriver-win64\\chromedriver.exe");
			driver = new ChromeDriver(); 
		}
		else if(browserName.equals("FF")){
			System.setProperty("webdriver.gecko.driver", "/Users/naveenkhunteta/Documents/SeleniumServer/geckodriver");	
			driver = new FirefoxDriver(); 
		}
		
		
		e_driver = new EventFiringWebDriver(driver);
		// Now create object of EventListerHandler to register it with EventFiringWebDriver
		eventListener = new WebEventListener();
		e_driver.register(eventListener);
		driver = e_driver;
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
		
		driver.get(prop.getProperty("url"));
		
	}


public void DatabaseQueryExecutor(){

	try {
		// Load the SQL Server JDBC driver
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		// Establish the connection
		connection = DriverManager.getConnection(DBurl, user, password);
		System.out.println("Server started successfully");
	} catch (ClassNotFoundException e) {
		System.err.println("JDBC Driver not found: " + e.getMessage());
	} catch (SQLException e) {
		System.err.println("Connection failed: " + e.getMessage());
	}
}
	/*protected void executeQuery(String query) {
		// Check if the connection is null
		if (connection == null) {
			System.err.println("No database connection established.");
			return;
		}

		// Try-with-resources for Statement and ResultSet
		try (Statement smt = connection.createStatement();
			 ResultSet rs = smt.executeQuery(query)) {

			// Loop through all rows
			while (rs.next()) {
				String metric = rs.getString("Metric"); // Fetch the Metric value
				String value = rs.getString("Value");   // Fetch the Value
				System.out.println("Metric: " + metric + ", Value: " + value);
			}
		} catch (SQLException e) {
			System.err.println("Query execution failed: " + e.getMessage());
			// Additional logging can be done here if necessary
		}
	}*/

	protected void closeConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("Connection closed successfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}


