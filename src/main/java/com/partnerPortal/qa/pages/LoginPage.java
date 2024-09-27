package com.partnerPortal.qa.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.partnerPortal.qa.base.TestBase;

public class LoginPage extends TestBase{
	
	//Page Factory - OR:
	@FindBy(id="userCode")
	WebElement username;
	
	@FindBy(id="password")
	WebElement password;
	
	@FindBy(xpath="//button[contains(@class, 'login-btn') and contains(text(), 'Log In Now')]")
	WebElement loginBtn;

	@FindBy(id="submit")
	WebElement captchaCode;

	@FindBy(id="image")
	WebElement captchaText;
	
	@FindBy(xpath="//button[contains(text(),'Sign Up')]")
	WebElement signUpBtn;
	
	@FindBy(xpath="//img[contains(@class,'img-responsive')]")
	WebElement crmLogo;
	
	//Initializing the Page Objects:
	public LoginPage(){
		PageFactory.initElements(driver, this);
	}
	
	//Actions:
	public String validateLoginPageTitle(){
		return driver.getTitle();
	}
	
	public boolean validateCRMImage(){
		return crmLogo.isDisplayed();
	}
	
	public HomePage login(String un, String pwd)throws Exception,InterruptedException{
		username.sendKeys(un);
		password.sendKeys(pwd);
		String cap=captchaText.getText();
		captchaCode.sendKeys(cap);
		loginBtn.click();
		    	/*JavascriptExecutor js = (JavascriptExecutor)driver;
		    	js.executeScript("arguments[0].click();", loginBtn);*/
		    	
		return new HomePage();
	}
	
}
