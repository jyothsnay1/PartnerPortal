package com.partnerPortal.qa.util;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.time.Duration;



public class DatePicker {
    private WebDriver driver;

    public DatePicker(WebDriver driver) {
        this.driver = driver;
    }

    public void selectDate(String dateString) {
        // Convert the input date string to a LocalDate object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);

        // Open the datepicker input field
        WebElement datepickerInput = driver.findElement(By.id("ui-datepicker-div")); // Adjust selector
        datepickerInput.click();

        // Navigate to the desired month and year
        while (true) {
            WebElement monthElement = driver.findElement(By.className("ui-datepicker-month"));
            WebElement yearElement = driver.findElement(By.className("ui-datepicker-year"));
            String month = monthElement.getText();
            int year = Integer.parseInt(yearElement.getText());

            // Adjust month comparison
            if (month.equals(date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)) && year == date.getYear()) {
                break; // Desired month and year found
            }

            // Click the next button to navigate
            WebElement nextButton = driver.findElement(By.className("ui-datepicker-next"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextButton);
            nextButton.click();
        }
// Click the desired date
        String day = String.valueOf(date.getDayOfMonth());
        WebElement dayElement = driver.findElement(By.xpath("//a[text()='" + day + "']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dayElement);

        //System.out.println("Current Month: " + month + ", Current Year: " + year);
    }
}

