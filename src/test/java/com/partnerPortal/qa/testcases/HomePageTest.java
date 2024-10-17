package com.partnerPortal.qa.testcases;

import com.partnerPortal.qa.util.DatabaseUtil;
import com.partnerPortal.qa.util.DatePicker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.partnerPortal.qa.base.TestBase;
import com.partnerPortal.qa.pages.HomePage;
import com.partnerPortal.qa.pages.LoginPage;
import com.partnerPortal.qa.util.TestUtil;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePageTest extends TestBase {
	LoginPage loginPage;
	HomePage homePage;
	TestUtil testUtil;
	DatabaseUtil dbUtil;

	public HomePageTest() {
		super();
	}

	@BeforeMethod
	public void setUp() {
		try {
			initialization();
			loginPage = new LoginPage();
			// Login might throw a checked exception
			homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
			dbUtil = new DatabaseUtil();
		} catch (Exception e) {
			e.printStackTrace(); // Print the stack trace or handle the exception as needed
		}
	}

	@Test(priority = 1)
	public void verifyHomePageTitleTest() {
		homePage = homePage.verifyHomePageTitle();
		//Assert.assertEquals(homePage.verifyHomePageTitle(), "DA&EC Dashboard","Home page title not matched");
	}

	@Test(priority = 2)
	public void verifyHomePageTitle() {
		homePage = homePage.verifyTitle();
	}

	@Test(priority = 3)
	public void verifyUserNameTest() {
		Assert.assertTrue(homePage.verifyCorrectUserName());
	}

	@Test(priority = 4)
	public void verifyUserDetails() {
		homePage = homePage.verifyUsernameDetails();
	}

	@Test(priority = 5)
	public void verifyLoginDetails() {
		homePage = homePage.verifyLoginDetails();
	}

	@Test(priority = 6)
	public void verifyPreIssuance_FTD() {
		// Retrieve UI values
		String uiTotalLogins = homePage.verifyPreissuanceValues_FYIP().getTotalLogins();
		String uiCancelled = homePage.verifyPreissuanceValues_FYIP().getCancelled();
		String uiPercentage = homePage.verifyPreissuanceValues_FYIP().getPercentage();

		try {
			// Database query execution
			String query = "exec [USP_Pre_Issuance_Cancellation] 'C0000051','F',''";
			ResultSet rs = dbUtil.executeQuery(query);

			String dbMetricTotalLogins = null;
			String dbMetricCancelled = null;
			String dbMetricPercentage = null;

			// Fetch values from ResultSet
			while (rs != null && rs.next()) {
				String metric = rs.getString("Metric");
				String value = rs.getString("Value");
				System.out.println("Metric: " + metric + ", Value: " + value);
				if ("Total login".equals(metric.trim())) { // Adjusted for case and spaces
					dbMetricTotalLogins = value;
				} else if ("Cancelled".equals(metric.trim())) {
					dbMetricCancelled = value;
				} else if ("%".equals(metric.trim())) { // Adjusted for case and spaces
					dbMetricPercentage = value;
				} else {
					System.out.println("Unexpected metric: " + metric);
				}
			}
			// Assertions to compare UI values with DB values
			Assert.assertEquals(uiTotalLogins, dbMetricTotalLogins, "Total Logins do not match!");
			Assert.assertEquals(uiCancelled, dbMetricCancelled, "Cancelled value does not match!");
			Assert.assertEquals(uiPercentage, dbMetricPercentage, "Percentage value does not match!");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 7)
	public void verifyPostIssuance_FTD() {
		// Step 1: Retrieve values from the UI using Selenium
		String uiTotalIssuance = homePage.verifyPostIssuanceValues_FYIP().getTotalIssuance().trim();
		String uiPostCancelled = homePage.verifyPostIssuanceValues_FYIP().getPostCancelled().trim();
		String uiPostPercentage = homePage.verifyPostIssuanceValues_FYIP().getPostPercentage().trim();

		// Step 2: Retrieve values from the database
		String dbMetricTotalIssuance = null;
		String dbMetricCancelled = null;
		String dbMetricPercentage = null;

		try {
			String query = "exec [USP_Post_Issuance_Cancellation] 'C0000051','F',''";
			ResultSet rs = dbUtil.executeQuery(query);

			// Check if ResultSet is empty
			if (!rs.isBeforeFirst()) {
				System.out.println("No results returned from the database query.");
				return; // Exit if no data
			}

			// Fetch values from ResultSet
			while (rs != null && rs.next()) {
				String metric = rs.getString("Metric").trim();
				String value = rs.getString("Value").trim();
				System.out.println("Metric: " + metric + ", Value: " + value);

				// Use if-else instead of switch
				if ("Total_Issued".equals(metric)) {
					dbMetricTotalIssuance = value;
				} else if ("Cancelled_Issuance".equals(metric)) {
					dbMetricCancelled = value;
				} else if ("%".equals(metric)) {
					dbMetricPercentage = value;
				} else {
					System.out.println("Unexpected metric: " + metric);
				}
			}
			Assert.assertEquals(uiTotalIssuance, dbMetricTotalIssuance, "Total Issuance do not match!");
			Assert.assertEquals(uiPostCancelled, dbMetricCancelled, "Cancelled value does not match!");
			Assert.assertEquals(uiPostPercentage, dbMetricPercentage, "Percentage value does not match!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test(priority = 8)
	public void verifyNach_FTD(){
		homePage=homePage.verifyNachValues();
		String query = "exec [USP_NATCH_Registration] 'C0000051','F',''";
		ResultSet rs = dbUtil.executeQuery(query);

		String dbNatchApproved = null;
		String dbTotalPolicyIssued = null;
		String dbNatchApprovalRate = null;
		try {
			if (rs != null && rs.isBeforeFirst()) {
				while (rs.next()) {
					dbNatchApproved = rs.getString("NATCH_Approved").trim();
					dbTotalPolicyIssued = rs.getString("Total_Policy_Issued").trim();
					dbNatchApprovalRate = rs.getString("NATCH_Approval_Rate").trim();
				}
			} else {
				System.out.println("No results returned from the database query.");
				return;
			}
			// Assertions to compare UI values with DB values
			Assert.assertEquals(homePage.getUiNatchApproved(), dbNatchApproved, "NATCH Approved does not match! Expected: " + dbNatchApproved + ", Found: " + homePage.getUiNatchApproved());
			Assert.assertEquals(homePage.getUiTotalPolicyIssued(), dbTotalPolicyIssued, "Total Policy Issued does not match! Expected: " + dbTotalPolicyIssued + ", Found: " + homePage.getUiTotalPolicyIssued());
			Assert.assertEquals(homePage.getUiNatchApprovalRate(), dbNatchApprovalRate, "NATCH Approval Rate does not match! Expected: " + dbNatchApprovalRate + ", Found: " + homePage.getUiNatchApprovalRate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Test(priority = 9)
	public void clickMtd() {
		homePage = homePage.clickMTD();
	}
	@Test(priority = 10)
	public void verifyPreIssuance_MTD() {
		homePage = homePage.clickMTD();
		// Retrieve UI values
		String uiTotalLogins = homePage.verifyPreissuanceValues_FYIP().getTotalLogins();
		String uiCancelled = homePage.verifyPreissuanceValues_FYIP().getCancelled();
		String uiPercentage = homePage.verifyPreissuanceValues_FYIP().getPercentage();

		try {
			// Database query execution
			String query = "exec [USP_Pre_Issuance_Cancellation] 'C0000051','M',''";
			ResultSet rs = dbUtil.executeQuery(query);

			String dbMetricTotalLogins = null;
			String dbMetricCancelled = null;
			String dbMetricPercentage = null;

			// Fetch values from ResultSet
			while (rs != null && rs.next()) {
				String metric = rs.getString("Metric");
				String value = rs.getString("Value");
				System.out.println("Metric: " + metric + ", Value: " + value);
				if ("Total login".equals(metric.trim())) { // Adjusted for case and spaces
					dbMetricTotalLogins = value;
				} else if ("Cancelled".equals(metric.trim())) {
					dbMetricCancelled = value;
				} else if ("%".equals(metric.trim())) { // Adjusted for case and spaces
					dbMetricPercentage = value;
				} else {
					System.out.println("Unexpected metric: " + metric);
				}
			}
			// Assertions to compare UI values with DB values
			Assert.assertEquals(uiTotalLogins, dbMetricTotalLogins, "Total Logins do not match!");
			Assert.assertEquals(uiCancelled, dbMetricCancelled, "Cancelled value does not match!");
			Assert.assertEquals(uiPercentage, dbMetricPercentage, "Percentage value does not match!");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Test(priority = 11)
	public void verifyPostIssuance_MTD() {
		homePage=homePage.clickMTD();
		// Step 1: Retrieve values from the UI using Selenium
		String uiTotalIssuance = homePage.verifyPostIssuanceValues_FYIP().getTotalIssuance().trim();
		String uiPostCancelled = homePage.verifyPostIssuanceValues_FYIP().getPostCancelled().trim();
		String uiPostPercentage = homePage.verifyPostIssuanceValues_FYIP().getPostPercentage().trim();

		// Step 2: Retrieve values from the database
		String dbMetricTotalIssuance = null;
		String dbMetricCancelled = null;
		String dbMetricPercentage = null;

		try {
			String query = "exec [USP_Post_Issuance_Cancellation] 'C0000051','M',''";
			ResultSet rs = dbUtil.executeQuery(query);

			// Check if ResultSet is empty
			if (!rs.isBeforeFirst()) {
				System.out.println("No results returned from the database query.");
				return; // Exit if no data
			}

			// Fetch values from ResultSet
			while (rs != null && rs.next()) {
				String metric = rs.getString("Metric").trim();
				String value = rs.getString("Value").trim();
				System.out.println("Metric: " + metric + ", Value: " + value);

				// Use if-else instead of switch
				if ("Total_Issued".equals(metric)) {
					dbMetricTotalIssuance = value;
				} else if ("Cancelled_Issuance".equals(metric)) {
					dbMetricCancelled = value;
				} else if ("%".equals(metric)) {
					dbMetricPercentage = value;
				} else {
					System.out.println("Unexpected metric: " + metric);
				}
			}
			Assert.assertEquals(uiTotalIssuance, dbMetricTotalIssuance, "Total Issuance do not match!");
			Assert.assertEquals(uiPostCancelled, dbMetricCancelled, "Cancelled value does not match!");
			Assert.assertEquals(uiPostPercentage, dbMetricPercentage, "Percentage value does not match!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test(priority = 12)
	public void verifyNach_MTD(){
		homePage=homePage.clickMTD();
		homePage=homePage.verifyNachValues();
		String query = "exec [USP_NATCH_Registration] 'C0000051','M',''";
		ResultSet rs = dbUtil.executeQuery(query);

		String dbNatchApproved = null;
		String dbTotalPolicyIssued = null;
		String dbNatchApprovalRate = null;
		try {
			if (rs != null && rs.isBeforeFirst()) {
				while (rs.next()) {
					dbNatchApproved = rs.getString("NATCH_Approved").trim();
					dbTotalPolicyIssued = rs.getString("Total_Policy_Issued").trim();
					dbNatchApprovalRate = rs.getString("NATCH_Approval_Rate").trim();
				}
			} else {
				System.out.println("No results returned from the database query.");
				return;
			}
			// Assertions to compare UI values with DB values
			Assert.assertEquals(homePage.getUiNatchApproved(), dbNatchApproved, "NATCH Approved does not match! Expected: " + dbNatchApproved + ", Found: " + homePage.getUiNatchApproved());
			Assert.assertEquals(homePage.getUiTotalPolicyIssued(), dbTotalPolicyIssued, "Total Policy Issued does not match! Expected: " + dbTotalPolicyIssued + ", Found: " + homePage.getUiTotalPolicyIssued());
			Assert.assertEquals(homePage.getUiNatchApprovalRate(), dbNatchApprovalRate, "NATCH Approval Rate does not match! Expected: " + dbNatchApprovalRate + ", Found: " + homePage.getUiNatchApprovalRate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Test(priority = 13)
	public void verifyPersistency_FYIP_MTD() {
		homePage = homePage.clickMTD();
		// Retrieve UI values
		String uiTotalDues = homePage.verifyPersistencyValues_FYIP().getTotalDues();
		String uiAvailableAmount = homePage.verifyPersistencyValues_FYIP().getAvailableAmount();
		String uiDueamount = homePage.verifyPersistencyValues_FYIP().getDueAmount();

		try {
			// Database query execution
			String query = "exec [USP_FYIP_Summary] 'C0000051','M',''";
			ResultSet rs = dbUtil.executeQuery(query);

			String dbTotalDues = null;
			String dbAvailableAmount = null;
			String dbDueamount = null;
			// Fetch values from ResultSet
			if (rs != null && rs.next()) {
				dbTotalDues = rs.getString("Dues_Prem");
				dbAvailableAmount = rs.getString("Collected_Prem");
				dbDueamount = rs.getString("FYIP_Per");
			} else {
				System.out.println("No data returned from the query.");
			}

			uiDueamount = uiDueamount.replace("%", "").trim();// Remove '%' if it exists in the UI value
			// Convert dbFYIPPer to integer string if necessary
			if (dbTotalDues.contains(".")) {
				dbTotalDues = String.valueOf((int) Double.parseDouble(dbTotalDues)); // Convert to integer string
			}
			if (dbAvailableAmount.contains(".")) {
				dbAvailableAmount = String.valueOf((int) Double.parseDouble(dbAvailableAmount)); // Convert to integer string
			}
			if (dbDueamount.contains(".")) {
				dbDueamount = String.valueOf((int) Double.parseDouble(dbDueamount)); // Convert to integer string
			}

			// Assertions to compare UI values with DB values
			Assert.assertEquals(uiTotalDues, dbTotalDues, "Dues Premium values do not match!");
			Assert.assertEquals(uiAvailableAmount, dbAvailableAmount, "Collected Premium values do not match!");
			Assert.assertEquals(uiDueamount, dbDueamount, "FYIP Percentage values do not match!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test(priority = 14)
	public void verify13thMonthData_MTD() {
		homePage = homePage.clickMTD();
		// Dynamic retrieval of expected values from the HTML table
		List<String[]> expectedData = new ArrayList<>();

		List<WebElement> rows = homePage.getPersistencyTableRows();
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			String[] rowData = new String[cells.size()];
			for (int j = 0; j < cells.size(); j++) {
				rowData[j] = cells.get(j).getText().replace("%", "").trim(); // Removing percentage sign if needed
			}
			expectedData.add(rowData);
		}

		// Print expected data
		System.out.println("Expected Data:");
		for (String[] data : expectedData) {
			System.out.println(Arrays.toString(data));
		}

		try {
			String query = "exec USP_Persistency_For_13 'C0000051','M',''";
			ResultSet rs = dbUtil.executeQuery(query);
			List<String[]> actualData = new ArrayList<>();

			while (rs != null && rs.next()) {
				String monthName = rs.getString("MonthName");
				String dues = formatValue(rs.getString("Dues"));
				String collected = formatValue(rs.getString("Collected"));
				String collectedPer = formatValue(rs.getString("Collected_Per")); // Handle percentage

				actualData.add(new String[]{monthName, dues, collected, collectedPer});
			}

			// Print actual data
			System.out.println("Actual Data:");
			for (String[] data : actualData) {
				System.out.println(Arrays.toString(data));
			}

			// Verify sizes
			if (expectedData.size() != actualData.size()) {
				System.out.println("Data size mismatch! Expected: " + expectedData.size() + ", Actual: " + actualData.size());
				return; // or throw an exception
			}

			// Verify expected vs actual values
			for (int i = 0; i < expectedData.size(); i++) {
				String[] expected = expectedData.get(i);
				String[] actual = actualData.get(i);

				Assert.assertEquals(actual[0], expected[0], "Month name does not match for index " + i);
				Assert.assertEquals(actual[1], expected[1], "Dues value does not match for " + expected[0]);
				Assert.assertEquals(actual[2], expected[2], "Collected value does not match for " + expected[0]);
				Assert.assertEquals(actual[3], expected[3], "Collected Percentage value does not match for " + expected[0]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Test(priority = 15)
	public void clickYTD() {
		homePage = homePage.clickYTD();
	}
	@Test(priority = 16)
	public void verifyPreIssuance_YTD() {
		homePage = homePage.clickYTD();
		// Retrieve UI values
		String uiTotalLogins = homePage.verifyPreissuanceValues_FYIP().getTotalLogins();
		String uiCancelled = homePage.verifyPreissuanceValues_FYIP().getCancelled();
		String uiPercentage = homePage.verifyPreissuanceValues_FYIP().getPercentage();

		try {
			// Database query execution
			String query = "exec [USP_Pre_Issuance_Cancellation] 'C0000051','Y',''";
			ResultSet rs = dbUtil.executeQuery(query);

			String dbMetricTotalLogins = null;
			String dbMetricCancelled = null;
			String dbMetricPercentage = null;

			// Fetch values from ResultSet
			while (rs != null && rs.next()) {
				String metric = rs.getString("Metric");
				String value = rs.getString("Value");
				System.out.println("Metric: " + metric + ", Value: " + value);
				if ("Total login".equals(metric.trim())) { // Adjusted for case and spaces
					dbMetricTotalLogins = value;
				} else if ("Cancelled".equals(metric.trim())) {
					dbMetricCancelled = value;
				} else if ("%".equals(metric.trim())) { // Adjusted for case and spaces
					dbMetricPercentage = value;
				} else {
					System.out.println("Unexpected metric: " + metric);
				}
			}
			// Assertions to compare UI values with DB values
			Assert.assertEquals(uiTotalLogins, dbMetricTotalLogins, "Total Logins do not match!");
			Assert.assertEquals(uiCancelled, dbMetricCancelled, "Cancelled value does not match!");
			Assert.assertEquals(uiPercentage, dbMetricPercentage, "Percentage value does not match!");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Test(priority = 17)
	public void verifyPostIssuance_YTD() {
		homePage=homePage.clickYTD();
		// Step 1: Retrieve values from the UI using Selenium
		String uiTotalIssuance = homePage.verifyPostIssuanceValues_FYIP().getTotalIssuance().trim();
		String uiPostCancelled = homePage.verifyPostIssuanceValues_FYIP().getPostCancelled().trim();
		String uiPostPercentage = homePage.verifyPostIssuanceValues_FYIP().getPostPercentage().trim();

		// Step 2: Retrieve values from the database
		String dbMetricTotalIssuance = null;
		String dbMetricCancelled = null;
		String dbMetricPercentage = null;

		try {
			String query = "exec [USP_Post_Issuance_Cancellation] 'C0000051','Y',''";
			ResultSet rs = dbUtil.executeQuery(query);

			// Check if ResultSet is empty
			if (!rs.isBeforeFirst()) {
				System.out.println("No results returned from the database query.");
				return; // Exit if no data
			}

			// Fetch values from ResultSet
			while (rs != null && rs.next()) {
				String metric = rs.getString("Metric").trim();
				String value = rs.getString("Value").trim();
				System.out.println("Metric: " + metric + ", Value: " + value);

				// Use if-else instead of switch
				if ("Total_Issued".equals(metric)) {
					dbMetricTotalIssuance = value;
				} else if ("Cancelled_Issuance".equals(metric)) {
					dbMetricCancelled = value;
				} else if ("%".equals(metric)) {
					dbMetricPercentage = value;
				} else {
					System.out.println("Unexpected metric: " + metric);
				}
			}
			Assert.assertEquals(uiTotalIssuance, dbMetricTotalIssuance, "Total Issuance do not match!");
			Assert.assertEquals(uiPostCancelled, dbMetricCancelled, "Cancelled value does not match!");
			Assert.assertEquals(uiPostPercentage, dbMetricPercentage, "Percentage value does not match!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test(priority = 18)
	public void verifyNach_YTD() {
		String monthYear = "Apr-24"; // Specify the month to validate
		homePage = homePage.clickYTD();
		homePage = homePage.verifyNachValues(monthYear); // Pass the monthYear

		String query = "exec [USP_NATCH_Registration] 'C0000051','Y',''";
		ResultSet rs = dbUtil.executeQuery(query);

		String dbNatchApproved = null;
		String dbTotalPolicyIssued = null;
		String dbNatchApprovalRate = null;

		try {
			if (rs != null && rs.isBeforeFirst()) {
				while (rs.next()) {
					String currentMonth = rs.getString("MonthName").trim();

					// Check if the current month matches the desired monthYear
					if (currentMonth.equals(monthYear)) {
						dbNatchApproved = rs.getString("NATCH_Approved").trim();
						dbTotalPolicyIssued = rs.getString("Total_Policy_Issued").trim();
						dbNatchApprovalRate = rs.getString("NATCH_Approval_Rate").trim();
						break; // Exit the loop after finding the desired month
					}
				}
			} else {
				System.out.println("No results returned from the database query.");
				return;
			}

			// Handle potential nulls before assertions
			Assert.assertNotNull(dbNatchApproved, "NATCH Approved value is null for month: " + monthYear);
			Assert.assertNotNull(dbTotalPolicyIssued, "Total Policy Issued value is null for month: " + monthYear);
			Assert.assertNotNull(dbNatchApprovalRate, "NATCH Approval Rate value is null for month: " + monthYear);

			// Assertions to compare UI values with DB values
			Assert.assertEquals(homePage.getUiNatchApproved(), dbNatchApproved,
					"NATCH Approved does not match! Expected: " + dbNatchApproved +
							", Found: " + homePage.getUiNatchApproved());

			Assert.assertEquals(homePage.getUiTotalPolicyIssued(), dbTotalPolicyIssued,
					"Total Policy Issued does not match! Expected: " + dbTotalPolicyIssued +
							", Found: " + homePage.getUiTotalPolicyIssued());

			// Format dbNatchApprovalRate to remove the "%" for comparison
			dbNatchApprovalRate = dbNatchApprovalRate.replace("%", "").trim();
			Assert.assertEquals(homePage.getUiNatchApprovalRate().replace("%", "").trim(), dbNatchApprovalRate,
					"NATCH Approval Rate does not match! Expected: " + dbNatchApprovalRate +
							", Found: " + homePage.getUiNatchApprovalRate());

		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Database query execution failed.");
		}
	}
	@Test(priority = 19)
	public void verifyPersistency_FYIP_YTD() {
		homePage = homePage.clickYTD();
		// Retrieve UI values
		String uiTotalDues = homePage.verifyPersistencyValues_FYIP().getTotalDues();
		String uiAvailableAmount = homePage.verifyPersistencyValues_FYIP().getAvailableAmount();
		String uiDueamount = homePage.verifyPersistencyValues_FYIP().getDueAmount();

		try {
			// Database query execution
			String query = "exec [USP_FYIP_Summary] 'C0000051','Y',''";
			ResultSet rs = dbUtil.executeQuery(query);

			String dbTotalDues = null;
			String dbAvailableAmount = null;
			String dbDueamount = null;
			// Fetch values from ResultSet
			if (rs != null && rs.next()) {
				dbTotalDues = rs.getString("Dues_Prem");
				dbAvailableAmount = rs.getString("Collected_Prem");
				dbDueamount = rs.getString("FYIP_Per");
			} else {
				System.out.println("No data returned from the query.");
			}

			uiDueamount = uiDueamount.replace("%", "").trim();// Remove '%' if it exists in the UI value
			// Convert dbFYIPPer to integer string if necessary
			if (dbDueamount.contains(".")) {
				dbDueamount = String.valueOf((int) Double.parseDouble(dbDueamount)); // Convert to integer string
			}

			// Assertions to compare UI values with DB values
			Assert.assertEquals(uiTotalDues, dbTotalDues, "Dues Premium values do not match!");
			Assert.assertEquals(uiAvailableAmount, dbAvailableAmount, "Collected Premium values do not match!");
			Assert.assertEquals(uiDueamount, dbDueamount, "FYIP Percentage values do not match!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Method to format values
	private String formatValue(String value) {
		if (value == null || value.isEmpty()) return "0"; // Handle empty values
		double num = Double.parseDouble(value);
		return String.format("%.2f", num).replace(".00", "").replace(".0", ""); // Remove unnecessary decimal places
	}
	@Test(priority = 20)
	public void verifyPersistency13MonthData_YTD() {
		homePage = homePage.clickYTD();

		// Retrieve UI values
		String uiDues = homePage.verifyPersistency13monthValues_YTD().getDues_persistency();
		String uiCollected = homePage.verifyPersistency13monthValues_YTD().getCollected_Persistency();
		String uiCollectedPer = homePage.verifyPersistency13monthValues_YTD().getCollectedPer_Persistency();

		try {
			// Database query execution
			String query = "exec [USP_Persistency_For_13] 'C0000051','Y',''";
			ResultSet rs = dbUtil.executeQuery(query);

			String dbDues = null;
			String dbCollected = null;
			String dbCollectedPer = null;

			// Check if ResultSet is not null and contains data
			if (rs != null && rs.next()) {
				// Retrieve ResultSetMetaData to validate column names
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					System.out.println("Column " + i + ": " + metaData.getColumnName(i));
				}

				// Update column names based on actual names in ResultSet
				dbDues = rs.getString("Dues"); // Ensure this matches the output from the DB
				dbCollected = rs.getString("Collected");
				dbCollectedPer = rs.getString("Collected_Per");
			} else {
				System.out.println("No data returned from the query.");
			}

			// Prepare UI collected percentage for comparison
			uiCollectedPer = uiCollectedPer.replace("%", "").trim(); // Remove '%' if it exists

			// Convert dbDues and dbCollected to integer string if necessary
			if (dbDues != null) {
				dbDues = String.valueOf((int) Double.parseDouble(dbDues)); // Convert to integer string
			}
			if (uiDues != null) {
				uiDues = String.valueOf((int) Double.parseDouble(uiDues)); // Convert to integer string
			}
			// Convert dbDues and dbCollected to integer string if necessary
			if (dbCollectedPer != null) {
				dbCollectedPer = String.valueOf((int) Double.parseDouble(dbCollectedPer)); // Convert to integer string
			}
			if (uiCollectedPer != null) {
				uiCollectedPer = String.valueOf((int) Double.parseDouble(uiCollectedPer)); // Convert to integer string
			}

// Assertions to compare UI values with DB values
			Assert.assertEquals(uiDues, dbDues, "Dues Persistency values do not match!");
			Assert.assertEquals(uiCollected, dbCollected, "Collected Persistency values do not match!");
			Assert.assertEquals(uiCollectedPer, dbCollectedPer, "Collected Percentage values do not match!");

		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Database query execution failed.");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("An unexpected error occurred.");
		}
	}
	@Test(priority = 21)
	public void reports() {
		homePage = homePage.reports();
		homePage = homePage.selectDropdown("Issuance");
		homePage = homePage.selectFromDate();
		//DatePicker datePicker = new DatePicker(driver);
		//datePicker.selectDate("10/02/2024"); // Format MM/dd/yyyy

		homePage=homePage.calendarFromDate();
		homePage = homePage.selectToDate();
        homePage=homePage.calendarToDate();
		homePage = homePage.clickSubmit();
	}
	@Test(priority = 22)
	public void logout() {
		homePage = homePage.clickLogout();
	}

	@AfterMethod
	public void tearDown() {
		if (dbUtil != null) {
			dbUtil.closeConnection(); // Close the database connection
		}
		driver.quit();
	}
}