package com.partnerPortal.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.partnerPortal.qa.base.TestBase;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;

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

	@FindBy(linkText = "Forgot your password")
	WebElement forgotPassword;

	@FindBy(id="floatingInput")
	WebElement emailInputTextBox;

	@FindBy(id="forgetBtn")
	WebElement submit;

	@FindBy(id="SentLink")
	WebElement passwordSent;

	@FindBy(id="floatingInput")
	WebElement resetNewPassword;

	@FindBy(id="floatingPassword")
	WebElement resetConfirmPassword;

	@FindBy(id="ChangeBtn1")
	WebElement btnchangePassword;
	
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
	// Method to click on the "Forgot your password" link
	public HomePage clickForgotPassword() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust timeout as needed
		wait.until(ExpectedConditions.elementToBeClickable(forgotPassword)).click();
		return new HomePage();
	}
	public HomePage enterEmail(String email) {
		// Locate the email input field by its ID
		WebElement emailInput = emailInputTextBox;
		// Enter the email ID
		emailInput.sendKeys(email);
		return new HomePage();// Return the current instance for method chaining if needed
	}
	public HomePage clickSubmitButton() {
		// Locate the Submit button by its ID
		WebElement submitButton = submit;
		// Click the Submit button
		submitButton.click();
		return new HomePage();// Return the current instance for method chaining if needed
	}
	public boolean verifySuccessMessage(String expectedMessage) {
		// Locate the element that contains the success message
		WebElement messageElement = passwordSent; // Change to the actual ID or locator
		// Get the text from the element
		String actualMessage = messageElement.getText();
		// Validate the message
		return actualMessage.equals(expectedMessage);
	}
	public void requestPasswordReset(String email) {
		// Locate the email input field and submit button
		WebElement emailInput = emailInputTextBox; // Update with actual ID
		WebElement submitButton = submit; // Update with actual ID

		// Enter the email and click submit
		emailInput.sendKeys(email);
		submitButton.click();
	}
	/*public String getResetTokenFromEmail(String email) {
		// Implement logic to access Mailinator API or web interface
		// to retrieve the reset email and extract the token.
		// This will depend on the email service you're using.
		return extractedToken; // Return the token extracted from the email
	}*/
	public void resetPassword(String token, String newPassword) {
		// Construct the reset link with the token
		String resetLink = "https://drishti.shriramlife.in/ForgotChangePassword?token=" + token;

		// Navigate to the reset link
		driver.get(resetLink);

		// Locate password fields and submit button
		WebElement newPasswordField = resetNewPassword; // Update with actual ID
		WebElement confirmPasswordField = resetConfirmPassword; // Update with actual ID
		WebElement resetButton = btnchangePassword; // Update with actual ID

		// Enter the new password
		newPasswordField.sendKeys(newPassword);
		confirmPasswordField.sendKeys(newPassword);

		// Click the reset button
		resetButton.click();

		// Optionally validate success
	}
}
