package com.partnerPortal.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class HomePage extends TestBase {

	@FindBy(xpath = "//span[@class='username']")
	@CacheLookup
	WebElement userNameLabel;

	@FindBy(className = "welcome-title-tl")
	WebElement title;

	@FindBy(xpath="//p[@class='username-rank']")
	WebElement lastLoginDetails;

	@FindBy(id = "MTDId")
	WebElement mtd;

	@FindBy(id="YTDId")
	WebElement ytd;

	@FindBy(xpath = "//a[contains(text(), 'Reports')]")
	WebElement reportsLink;

	@FindBy(name = "report")
	WebElement selectReport;

	@FindBy(id = "fromDate")
	WebElement fromDate;

	@FindBy(id="toDate")
	WebElement toDate;

	@FindBy(xpath="//*[@id=\"ui-datepicker-div\"]/div/div/span[1]")
	WebElement month;

	@FindBy(xpath = "//*[@id=\"ui-datepicker-div\"]/div/div/span[2]")
	WebElement year;

	@FindBy(xpath="//button[contains(@class, 'btn-dark') and text()='Submit']")
	WebElement btnSubmit;

	@FindBy(linkText = "Logout")
	WebElement logout;

	/*@FindBy(xpath="//*[@id=\"ui-datepicker-div\"]//table[@class=\"ui-datepicker-calendar\"]//td")
	WebElement dates;*/
	

	// Initializing the Page Objects:
	public HomePage() {
		PageFactory.initElements(driver, this);
	}

	public HomePage verifyHomePageTitle() {
		// Get the actual title
		String actualTitle = driver.getTitle();
		// Log the actual title for debugging purposes
		System.out.println("Actual Title: " + actualTitle);
		System.out.println("Actual Title length: " + actualTitle.length());
		String expectedTitle = "DA&EC Dashboard";
		System.out.println(expectedTitle);
		System.out.println("expectedTitle length : " + expectedTitle.length());
		// Verify the full title
		//Assert.assertEquals(expectedTitle, actualTitle);
		if (expectedTitle.contains(actualTitle.trim())) {
			System.out.println("Pass");
		} else {
			System.out.println("not equal");
		}
		return new HomePage();

	}
public HomePage verifyTitle(){
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
		/*WebElement titleElement=title;
String titleText=titleElement.getText();
// Verify the username
	String expectedUsername = "Welcome to Shriram Life Drishti Portal";
	Assert.assertEquals(expectedUsername,titleText,"Username does not match!");*/
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
		Assert.assertEquals(expectedUsername,usernameText,"Username does not match!");
		return new HomePage();
	}
	public HomePage verifyLoginDetails(){
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
		/*// Verify the last login date
		String expectedLoginText = "Last Login : ";
		Assert.assertTrue("Login text does not start with 'Last Login : '!", usernameRankText.startsWith(expectedLoginText));*/
		return new HomePage();
	}
	
public HomePage clickMTD(){
		mtd.click();
		return new HomePage();
}
public HomePage clickYTD(){
		ytd.click();
		return new HomePage();
}
public HomePage reports(){
	reportsLink.click();
	return new HomePage();
}
public HomePage selectDropdown(){
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
public HomePage selectToDate(){
		WebElement dateInput=toDate;
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
		WebElement dayToSelect = driver.findElement(By.xpath("//table[@class='ui-datepicker-calendar']//a[@data-date='22']"));
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

	public HomePage clickSubmit(){
		btnSubmit.click();
		return new HomePage();
	}
	
public HomePage clickLogout(){
	logout.click();
		return new HomePage();
}
	
	/*public TasksPage clickOnTasksLink(){
		tasksLink.click();
		return new TasksPage();
	}
	
	public void clickOnNewContactLink(){
		Actions action = new Actions(driver);
		action.moveToElement(contactsLink).build().perform();
		newContactLink.click();
		
	}*/
	
	
	
	
	
	
	

}
