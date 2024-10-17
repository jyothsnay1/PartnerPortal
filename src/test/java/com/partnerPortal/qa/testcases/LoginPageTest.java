package com.partnerPortal.qa.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.partnerPortal.qa.base.TestBase;
import com.partnerPortal.qa.pages.HomePage;
import com.partnerPortal.qa.pages.LoginPage;

import java.time.Duration;

public class LoginPageTest extends TestBase{
	LoginPage loginPage;
	HomePage homePage;
	
	public LoginPageTest(){
		super();
	}
	
	@BeforeMethod
	public void setUp(){
		initialization();
		loginPage = new LoginPage();	
	}

	@Test(priority=1)
	public void loginPageTitleTest(){
		String title = loginPage.validateLoginPageTitle();
		Assert.assertEquals(title, "DA&EC","Home page title not matched");
	}
	
	/*@Test(priority=2)
	public void crmLogoImageTest(){
		boolean flag = loginPage.validateCRMImage();
		Assert.assertTrue(flag);
	}*/
	
	@Test()
	public void loginTest() throws Exception{
		homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
	}
	@Test(priority=2)
public void forgotPassword(){
		loginPage.clickForgotPassword();
		loginPage.requestPasswordReset("jyothsna.y@shriramlife.com");
		// Verify the success message
		boolean isMessageCorrect = loginPage.verifySuccessMessage("Password reset link has been sent to your mail id. Please verify!");

		// Assert the result (using an assertion library like JUnit or TestNG)
		if (isMessageCorrect) {
			System.out.println("Success message verified!");
		} else {
			System.out.println("Success message verification failed!");
		}
	}
	@AfterMethod
	public void tearDown(){
		driver.quit();
	}
		}
