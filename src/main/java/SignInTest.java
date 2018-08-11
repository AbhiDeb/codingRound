import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sun.javafx.PlatformUtil;

import pageObjectModel.SignInPage;

public class SignInTest {

	WebDriver driver = null;
	SignInPage signInPage = null;

	@BeforeMethod
	public void beforeMethod() {
		setDriverPath();
		driver = new ChromeDriver();
	}

	@Test
	public void shouldThrowAnErrorIfSignInDetailsAreMissing() {

		try {
			driver.get("https://www.cleartrip.com/");
			waitForLoad(driver, 40);
			driver.manage().window().maximize();
			
			signInPage = new SignInPage(driver);

			signInPage.yourTripsLink.click();
			signInPage.signInButton.click();

			driver.switchTo().frame(signInPage.iframeId);
			signInPage.signInButtoninFrame.click();

			String errors1 = signInPage.errorTextMessage.getText();
			Assert.assertTrue(errors1.contains("There were errors in your submission"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// close the browser
			driver.quit();
		}
	}

	private void waitFor(int durationInMilliSeconds) {
		try {
			Thread.sleep(durationInMilliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
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
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, durationInSeconds);
		wait.until(pageLoadCondition);
	}

}
