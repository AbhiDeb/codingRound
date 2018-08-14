package test;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import common.GenericFunctions;
import pageObjectModel.HotelBookingPage;
import reporting.ReporterClass;

public class HotelBookingTest {

	WebDriver driver = null;
	ExtentHtmlReporter htmlReporter;
	ExtentReports extent;
	ExtentTest logger;
	String hostname;
	ReporterClass customReporting;

	HotelBookingPage hotelPage = null;

	@BeforeSuite
	public void setUp() {
		GenericFunctions.setDriverPath();

		// Extent Report changes
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/ExtentReport.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		extent.setSystemInfo("Host Name", hostname);
		extent.setSystemInfo("Environment", "Automation Testing");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));

		htmlReporter.config().setDocumentTitle("TestVagarant Coding Round");
		htmlReporter.config().setReportName("TestVagarant Coding Round");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.STANDARD);
	}

	@BeforeMethod
	public void beforeMethod(Method m) {

		driver = new ChromeDriver();
		driver.get("https://www.cleartrip.com/");
		GenericFunctions.waitForLoad(driver, 40);
		driver.manage().window().maximize();
		hotelPage = new HotelBookingPage(driver);

		// Extent Report changes
		logger = extent.createTest(m.getName());
		customReporting = new ReporterClass(driver, extent, logger, m.getName());
	}

	@Test
	public void shouldBeAbleToSearchForHotels() {
		boolean returnBoolean;
		try {

			returnBoolean = GenericFunctions.clickElement(driver, hotelPage.hotelLink);
			customReporting.assertTrue(returnBoolean, "Hotel link is clicked");

//			hotelPage.localityTextBox.sendKeys("Indiranagar, Bangalore");
			GenericFunctions.sendKeys(driver, hotelPage.localityTextBox, "Indiranagar, Bangalore");

			// wait for the auto complete options to appear
//			GenericFunctions.waitFor(2000);
			GenericFunctions.waitForElement(driver, hotelPage.whereTag, 4);
			List<WebElement> whereOptions = hotelPage.whereTag.findElements(By.className("list"));
			whereOptions.get(0).click();

			new Select(hotelPage.travellerSelection).selectByVisibleText("1 room, 2 adults");

			returnBoolean = GenericFunctions.clickElement(driver, hotelPage.searchButton);
			customReporting.assertTrue(returnBoolean, "Search button is clicked");

			returnBoolean = GenericFunctions.waitForElement(driver, hotelPage.searchSummayDiv, 10);
			customReporting.assertTrue(returnBoolean, "Search is successful");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {

		// close the browser
		driver.quit();
		driver = null;

		// Extent Report changes
		if (result.getStatus() == ITestResult.FAILURE) {
			logger.log(Status.FAIL,
					MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
			logger.log(Status.FAIL,
					MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
		} else if (result.getStatus() == ITestResult.SKIP) {
			// logger.log(Status.SKIP, "Test Case Skipped is "+result.getName());
			logger.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
		}
	}

	@AfterSuite
	public void endReport() {

		// Extent Report changes
		extent.flush();
	}

}
