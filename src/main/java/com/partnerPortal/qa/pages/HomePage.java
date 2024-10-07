package com.partnerPortal.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.partnerPortal.qa.base.TestBase;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

import static com.partnerPortal.qa.base.TestBase.driver;

public class HomePage extends TestBase {
	// Private instance variables
	private String uiNatchApproved;
	private String uiTotalPolicyIssued;
	private String uiNatchApprovalRate;

	public String getTotalLogins() {
		return preIssuance_TotalLogins.getText();
	}

	public String getCancelled() {
		return cancelled_PreIssuance_FYIP.getText();
	}

	public String getPercentage() {
		return per_PreIssuance_FYIP.getText();
	}

	public String getTotalIssuance() {
		return TotalIssuance_PostIssuance_FYIP.getText();
	}

	public String getPostCancelled() {
		return postCancelled_PostIssuance_FYIP.getText();
	}

	public String getPostPercentage() {
		return postper_PostIssuance_FYIP.getText();
	}

	@FindBy(xpath = "//span[@class='username']")
	@CacheLookup
	WebElement userNameLabel;

	@FindBy(className = "welcome-title-tl")
	WebElement title;

	@FindBy(xpath = "//p[@class='username-rank']")
	WebElement lastLoginDetails;

	@FindBy(id = "MTDId")
	WebElement mtd;

	@FindBy(id = "YTDId")
	WebElement ytd;

	@FindBy(xpath = "//a[contains(text(), 'Reports')]")
	WebElement reportsLink;

	@FindBy(name = "report")
	WebElement selectReport;

	@FindBy(id = "fromDate")
	WebElement fromDate;

	@FindBy(id = "toDate")
	WebElement toDate;

	@FindBy(xpath = "//*[@id=\"ui-datepicker-div\"]/div/div/span[1]")
	WebElement month;

	@FindBy(xpath = "//*[@id=\"ui-datepicker-div\"]/div/div/span[2]")
	WebElement year;

	@FindBy(xpath = "//button[contains(@class, 'btn-dark') and text()='Submit']")
	WebElement btnSubmit;

	@FindBy(linkText = "Logout")
	WebElement logout;

	@FindBy(id = "totalValue")
	WebElement preIssuance_TotalLogins;

	@FindBy(id = "recamount")
	WebElement totalDues_persistency_MTD;

	@FindBy(id = "AvailbleAmount")
	WebElement availableAmount_persistency_MTD;

	@FindBy(id = "DueAmount")
	WebElement dueAmount_persistency_MTD;

	@FindBy(id = "cancelled")
	WebElement cancelled_PreIssuance_FYIP;

	@FindBy(id = "per")
	WebElement per_PreIssuance_FYIP;

	@FindBy(id = "PosttotalValue")
	WebElement TotalIssuance_PostIssuance_FYIP;

	@FindBy(id = "Postcancelled")
	WebElement postCancelled_PostIssuance_FYIP;

	@FindBy(id = "Postper")
	WebElement postper_PostIssuance_FYIP;

	public HomePage(WebDriver driver) {
		this.driver = driver;
	}

	// Initializing the Page Objects:
	public HomePage() {

		PageFactory.initElements(driver, this);
	}

	public HomePage verifyHomePageTitle() {
		// Get the actual title
		String actualTitle = driver.getTitle();
		// Log the actual title for debugging purposes

		String expectedTitle = "DA&EC Dashboard";

		// Verify the full title
		//Assert.assertEquals(expectedTitle, actualTitle);
		if (expectedTitle.contains(actualTitle.trim())) {
			System.out.println("Pass");
		} else {
			System.out.println("not equal");
		}
		return new HomePage();

	}

	public HomePage verifyTitle() {
		// Locate the title element
		WebElement titleElement = title;
		// Verify the title text
		String expectedTitle = "Welcome to Shriram Life Drishti Portal";
		String actualTitle = titleElement.getText();

		if (actualTitle.equals(expectedTitle)) {
			System.out.println("Title verification passed!");
		} else {
			System.out.println("Title verification failed! Expected: " + expectedTitle + ", but got: " + actualTitle);
		}
		return new HomePage();
	}

	public boolean verifyCorrectUserName() {
		return userNameLabel.isDisplayed();
	}

	public HomePage verifyUsernameDetails() {
		WebElement usernameElement = userNameLabel;
		String usernameText = usernameElement.getText();
		// Verify the username
		String expectedUsername = "PROSALES FINANCIAL SERVICES PVT LTD";
		Assert.assertEquals(expectedUsername, usernameText, "Username does not match!");
		return new HomePage();
	}

	public HomePage verifyLoginDetails() {
		WebElement usernameRankElement = lastLoginDetails;
		String usernameRankText = usernameRankElement.getText().trim();
		// Define date format and parse the last login time
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.ENGLISH);
		try {
			Date lastLoginDate = format.parse(usernameRankText);
			Date currentDate = new Date();

			// Check if last login is within the last 5 minutes (or any range you prefer)
			long timeDifference = currentDate.getTime() - lastLoginDate.getTime();
			if (timeDifference <= 5 * 60 * 1000) { // 5 minutes in milliseconds
				System.out.println("Last login verification passed! Last login was within the last 5 minutes.");
			} else {
				System.out.println("Last login verification failed! Last login was more than 5 minutes ago.");
			}

		} catch (ParseException e) {
			System.out.println("Error parsing last login time: " + e.getMessage());
		}
		return new HomePage();
	}

	public HomePage clickMTD() {
		mtd.click();
		return new HomePage();
	}

	public HomePage clickYTD() {
		ytd.click();
		return new HomePage();
	}

	public HomePage reports() {
		reportsLink.click();
		return new HomePage();
	}

	public HomePage selectDropdown() {
		// Create a Select object
		Select select = new Select(selectReport);

		// Select the option by visible text
		select.selectByVisibleText("Issuance");

		return new HomePage();
	}

	public HomePage selectFromDate() {
		WebElement dateInput = fromDate;
		dateInput.click();
		return new HomePage();
	}

	public HomePage selectToDate() {
		WebElement dateInput = toDate;
		dateInput.click();
		return new HomePage();
	}

	public HomePage calendar_FromDate() {
		// Click on the specific day (e.g., 23rd)
		WebElement dayToSelect = driver.findElement(By.xpath("//table[@class='ui-datepicker-calendar']//a[@data-date='1']"));
		dayToSelect.click();
		return new HomePage();
	}

	public HomePage calendar_ToDate() {
		// Click on the specific day (e.g., 23rd)
		WebElement dayToSelect = driver.findElement(By.xpath("//table[@class='ui-datepicker-calendar']//a[@data-date='1']"));
		dayToSelect.click();
		return new HomePage();
	}

		/*String selDate="22";
		String selMonth="09";
		String selYear="2024";
		
	String month_first=month.getText();
	System.out.println(month_first);
	String year_first=year.getText();
	System.out.println(year_first);
	if(month_first.equals(selMonth) && year_first.equals(selYear)){
		List<WebElement> allDates=driver.findElements(By.xpath("//*[@id=\"ui-datepicker-div\"]//table[@class=\"ui-datepicker-calendar\"]//td//a"));
		for(WebElement date:allDates){
			System.out.println(date.getText());
			if(date.getText().equals(selDate)){
				date.click();

			}
		}
	}*/

	public HomePage clickSubmit() {
		btnSubmit.click();
		return new HomePage();
	}

	public HomePage clickLogout() {
		logout.click();
		return new HomePage();
	}

	public HomePage verifyPreissuanceValues_FYIP() {
		System.out.println(getTotalLogins());
		System.out.println(getCancelled());
		System.out.println(getPercentage());
		return this;
	}

	public HomePage verifyPostIssuanceValues_FYIP() {
		System.out.println(getTotalIssuance());
		System.out.println(getPostCancelled());
		System.out.println(getPostPercentage());
		return this;
	}
	public HomePage verifyNachValues() {
		WebElement tableBody = driver.findElement(By.tagName("tbody"));
		List<WebElement> rows = tableBody.findElements(By.tagName("tr"));

		System.out.println("Number of rows: " + rows.size());

		String uiNatchApproved = null;
		String uiTotalPolicyIssued = null;
		String uiNatchApprovalRate = null;

		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));

			// Print out each row's cell count and content for debugging
			System.out.println("Row cells count: " + cells.size());
			for (WebElement cell : cells) {
				System.out.println("Cell text: " + cell.getText());
			}

			// Assuming the relevant data is in the second and third cells for the values you need
			if (cells.size() >= 4) {  // Ensure there are enough cells
				uiTotalPolicyIssued = cells.get(1).getText().trim(); // Total Policies Issued
				uiNatchApproved = cells.get(2).getText().trim(); // NATCH Approved
				uiNatchApprovalRate = cells.get(3).getText().trim(); // NATCH Approval Rate
			}
		}

		// Print values in the desired format
		System.out.println("Cell text: " + uiTotalPolicyIssued + " = UI Total Policy Issued: " + uiTotalPolicyIssued);
		System.out.println("Cell text: " + uiNatchApproved + " = UI NATCH Approved: " + uiNatchApproved);
		System.out.println("Cell text: " + uiNatchApprovalRate + " = UI NATCH Approval Rate: " + uiNatchApprovalRate);

		this.uiNatchApproved = uiNatchApproved;
		this.uiTotalPolicyIssued = uiTotalPolicyIssued;
		this.uiNatchApprovalRate = uiNatchApprovalRate;

		return this;
	}
	/*public HomePage verifyNachValues(String monthYear) {
		WebElement tableBody = driver.findElement(By.id("NachTable")).findElement(By.tagName("tbody"));
			List<WebElement> rows = tableBody.findElements(By.tagName("tr"));

			System.out.println("Number of rows: " + rows.size());

			String uiNatchApproved = null;
			String uiTotalPolicyIssued = null;
			String uiNatchApprovalRate = null;

			for (WebElement row : rows) {
				List<WebElement> cells = row.findElements(By.tagName("td"));

				// Ensure we have enough cells
				if (cells.size() >= 4) {
					String currentMonthYear = cells.get(0).getText().trim(); // Get the month (e.g., "Apr-24")

					// Check if this row corresponds to the specified monthYear
					if (currentMonthYear.equals(monthYear)) {
						uiTotalPolicyIssued = cells.get(1).getText().trim(); // Total Policies Issued
						uiNatchApproved = cells.get(2).getText().trim(); // NATCH Approved
						uiNatchApprovalRate = cells.get(3).getText().trim(); // NATCH Approval Rate

						// Print values in the desired format
						System.out.println("Cell text: " + uiTotalPolicyIssued + " = UI Total Policy Issued: " + uiTotalPolicyIssued);
						System.out.println("Cell text: " + uiNatchApproved + " = UI NATCH Approved: " + uiNatchApproved);
						System.out.println("Cell text: " + uiNatchApprovalRate + " = UI NATCH Approval Rate: " + uiNatchApprovalRate);

						// Set the values for comparison
						this.uiNatchApproved = uiNatchApproved;
						this.uiTotalPolicyIssued = uiTotalPolicyIssued;
						this.uiNatchApprovalRate = uiNatchApprovalRate;

						break; // Exit loop once the correct row is found
					}
				}
			}

			// Print final extracted values for verification
			System.out.println("Final UI NATCH Approved: " + this.uiNatchApproved);
			System.out.println("Final UI Total Policy Issued: " + this.uiTotalPolicyIssued);
			System.out.println("Final UI NATCH Approval Rate: " + this.uiNatchApprovalRate);

			return this;
		}*/
	// Getter methods
	public String getUiNatchApproved() {
		return uiNatchApproved;
	}

	public String getUiTotalPolicyIssued() {
		return uiTotalPolicyIssued;
	}

	public String getUiNatchApprovalRate() {
		return uiNatchApprovalRate;
	}

}




