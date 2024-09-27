package com.partnerPortal.qa.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.partnerPortal.qa.base.TestBase;
import com.partnerPortal.qa.pages.ContactsPage;
import com.partnerPortal.qa.pages.HomePage;
import com.partnerPortal.qa.pages.LoginPage;
import com.partnerPortal.qa.util.TestUtil;

public class HomePageTest extends TestBase {
	LoginPage loginPage;
	HomePage homePage;
	TestUtil testUtil;
	ContactsPage contactsPage;

	public HomePageTest() {
		super();
	}

	//test cases should be separated -- independent with each other
	//before each test case -- launch the browser and login
	//@test -- execute test case
	//after each test case -- close the browser
	
	/*@BeforeMethod
	public void setUp() {
		initialization();
		testUtil = new TestUtil();
		contactsPage = new ContactsPage();
		loginPage = new LoginPage();
		homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
	}*/
	@BeforeMethod
	public void setUp() {
		try {
			initialization();
			loginPage = new LoginPage();
			// Login might throw a checked exception
			homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		} catch (Exception e) {
			e.printStackTrace(); // Print the stack trace or handle the exception as needed
		}
	}
	@Test(priority=1)
	public void verifyHomePageTitleTest(){
		//String homePageTitle = homePage.verifyHomePageTitle();
homePage=homePage.verifyHomePageTitle();
		//Assert.assertEquals(homePage.verifyHomePageTitle(), "DA&EC Dashboard","Home page title not matched");
	}
	@Test(priority=2)
	public void verifyHomePageTitle(){
homePage=homePage.verifyTitle();
	}
	@Test(priority=3)
	public void verifyUserNameTest(){
		//testUtil.switchToFrame();
		Assert.assertTrue(homePage.verifyCorrectUserName());
	}
	
	@Test(priority=4)
	public void verifyUserDetails(){
		//testUtil.switchToFrame();
		homePage = homePage.verifyUsernameDetails();
	}
	@Test(priority=5)
	public void verifyLoginDetails(){
		homePage=homePage.verifyLoginDetails();
	}
@Test(priority=6)
public void clickMtd(){
		homePage=homePage.clickMTD();
}
	@Test(priority=7)
	public void clickYTD(){
		homePage=homePage.clickYTD();
	}
	@Test(priority=8)
	public void reports(){
		homePage=homePage.reports();
		homePage=homePage.selectDropdown();
		homePage=homePage.selectFromDate();
		homePage=homePage.calendar_FromDate();
		homePage=homePage.selectToDate();
		homePage=homePage.calendar_ToDate();
		homePage=homePage.clickSubmit();
	}
	@Test(priority=9)
	public void logout(){
		homePage=homePage.clickLogout();
	}
	
	@AfterMethod
	public void tearDown(){
		driver.quit();
	}

	

}
