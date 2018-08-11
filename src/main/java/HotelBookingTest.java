import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sun.javafx.PlatformUtil;

import pageObjectModel.HotelBookingPage;

public class HotelBookingTest {

//    WebDriver driver = new ChromeDriver();

	WebDriver driver = null;

	@BeforeMethod
	public void beforeMethod() {
		setDriverPath();
		driver = new ChromeDriver();
	}

	@Test
	public void shouldBeAbleToSearchForHotels() {

//        setDriverPath();
		try {
			driver.get("https://www.cleartrip.com/");
			waitForLoad(driver, 40);
	        driver.manage().window().maximize();
	        
	        HotelBookingPage hotelPage = new HotelBookingPage(driver);
	        hotelPage.hotelLink.click();

	        hotelPage.localityTextBox.sendKeys("Indiranagar, Bangalore");

	        //wait for the auto complete options to appear
	        waitFor(2000);
	        List<WebElement> whereOptions = hotelPage.whereTag.findElements(By.className("list"));
	        whereOptions.get(0).click();
	        
			new Select(hotelPage.travellerSelection).selectByVisibleText("1 room, 2 adults");
			hotelPage.searchButton.click();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// close the browser
			driver.quit();
		}
	}

	@AfterMethod
	public void afterMethod() {
		driver = null;
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
	
	private void waitFor(int durationInMilliSeconds) {
        try {
            Thread.sleep(durationInMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
