package com.partnerPortal.qa.testcases;

import com.partnerPortal.qa.util.DatabaseUtil;
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
	public void verifyPreIssuance_FYIP() {
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
	public void verifyPostIssuance_FYIP() {
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
	public void clickMtd() {
		homePage = homePage.clickMTD();
		//homePage=homePage.verifyPersistencyFYIP();
	}
	@Test(priority = 9)
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
	@Test(priority = 10)
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
	@Test(priority = 11)
	public void clickYTD() {
		homePage = homePage.clickYTD();
	}
	@Test(priority = 12)
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
	@Test(priority = 13)
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
@Test(priority = 14)
public void verifyNach_FYIP(){
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

	@Test(priority = 15)
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


	/*@Test(priority = 14)
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
					dbNatchApproved = rs.getString("NATCH_Approved").trim();
					dbTotalPolicyIssued = rs.getString("Total_Policy_Issued").trim();
					dbNatchApprovalRate = rs.getString("NATCH_Approval_Rate").trim();
				}
			} else {
				System.out.println("No results returned from the database query.");
				return;
			}

			// Print database values for debugging
			System.out.println("DB NATCH Approved: " + dbNatchApproved);
			System.out.println("DB Total Policy Issued: " + dbTotalPolicyIssued);
			System.out.println("DB NATCH Approval Rate: " + dbNatchApprovalRate);

			// Assertions to compare UI values with DB values
			Assert.assertEquals(homePage.getUiNatchApproved(), dbNatchApproved,
					"NATCH Approved does not match! Expected: " + dbNatchApproved +
							", Found: " + homePage.getUiNatchApproved());

			Assert.assertEquals(homePage.getUiTotalPolicyIssued(), dbTotalPolicyIssued,
					"Total Policy Issued does not match! Expected: " + dbTotalPolicyIssued +
							", Found: " + homePage.getUiTotalPolicyIssued());

			Assert.assertEquals(homePage.getUiNatchApprovalRate(), dbNatchApprovalRate,
					"NATCH Approval Rate does not match! Expected: " + dbNatchApprovalRate +
							", Found: " + homePage.getUiNatchApprovalRate());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/
	@Test(priority = 16)
	public void reports() {
		homePage = homePage.reports();
		homePage = homePage.selectDropdown();
		homePage = homePage.selectFromDate();
		homePage = homePage.calendar_FromDate();
		homePage = homePage.selectToDate();
		homePage = homePage.calendar_ToDate();
		homePage = homePage.clickSubmit();
	}

	@Test(priority = 17)
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