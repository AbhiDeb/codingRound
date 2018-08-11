import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sun.javafx.PlatformUtil;

import pageObjectModel.FlightBookingPage;

public class FlightBookingTest {

    WebDriver driver = null;
    FlightBookingPage flighBookingPage = null;

    @BeforeMethod
    public void beforeMethod() {
    	setDriverPath();
    	driver = new ChromeDriver();
    }

    @Test
    public void testThatResultsAppearForAOneWayJourney() {

        try {
        
        driver.get("https://www.cleartrip.com/");
        waitForLoad(driver, 40);
        driver.manage().window().maximize();
        
        flighBookingPage = new FlightBookingPage(driver);
        
        flighBookingPage.oneWayButton.click();
        
        flighBookingPage.fromTagText.clear();
        flighBookingPage.fromTagText.sendKeys("Bangalore");

        //wait for the auto complete options to appear for the origin

        waitFor(2000);
        List<WebElement> originOptions = flighBookingPage.originOptions.findElements(By.tagName("li"));
        originOptions.get(0).click();

        flighBookingPage.toTagText.clear();
        flighBookingPage.toTagText.sendKeys("Delhi");

        //wait for the auto complete options to appear for the destination

        waitFor(2000);
        List<WebElement> destinationOptions = flighBookingPage.destinationOptions.findElements(By.tagName("li"));
        destinationOptions.get(0).click();

        flighBookingPage.datePicker.click();
        
        //all fields filled in. Now click on search
        flighBookingPage.buttonSearchButton.click();

        waitFor(5000);
        //verify that result appears for the provided journey search
        Assert.assertTrue(isElementPresent(flighBookingPage.searchSummary));

        
        } catch (Exception e) {
        	e.printStackTrace();
        }
        finally {
        	//close the browser
            driver.quit();
        }
    }
    
    @AfterMethod
    public void afterMethod() {
    	
        driver = null;
    }


    private void waitFor(int durationInMilliSeconds) {
        try {
            Thread.sleep(durationInMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private void setDriverPath() {
        if (PlatformUtil.isMac()) {
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }
        if (PlatformUtil.isWindows()) {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        }
        if (PlatformUtil.isLinux()) {
            System.setProperty("webdriver.chrome.driver", "chromedriver_linux");
        }
    }
    
    public void waitForLoad(WebDriver driver, int durationInSeconds) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, durationInSeconds);
        wait.until(pageLoadCondition);
    }
}
