package test;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
import pageObjectModel.SignInPage;
import reporting.ReporterClass;

public class SignInTest {

	WebDriver driver = null;
	SignInPage signInPage = null;
	ExtentHtmlReporter htmlReporter;
	ExtentReports extent;
	ExtentTest logger;
	String hostname;
	ReporterClass customReporting;

	@BeforeSuite
	public void setUp() {
		GenericFunctions.setDriverPath();

		// Extent Report changes
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/ExtentReports/"+this.getClass().getName().replace("test.", "")+"ExtentReport.html");
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
		htmlReporter.config().setReportName(this.getClass().getName().replace("test.", ""));
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.STANDARD);
	}

	@BeforeMethod
	public void beforeMethod(Method m) {

		driver = new ChromeDriver();
		driver.get("https://www.cleartrip.com/");
		GenericFunctions.waitForLoad(driver, 40);
		driver.manage().window().maximize();
		signInPage = new SignInPage(driver);

		logger = extent.createTest(m.getName());
		customReporting = new ReporterClass(driver, extent, logger, m.getName());
	}

	@Test
	public void shouldThrowAnErrorIfSignInDetailsAreMissing() {
		boolean returnBoolean;
		try {

			returnBoolean = GenericFunctions.clickElement(driver, signInPage.yourTripsLink);
			customReporting.assertTrue(returnBoolean, "Your Trips link is clicked");

			returnBoolean = GenericFunctions.clickElement(driver, signInPage.signInButton);
			customReporting.assertTrue(returnBoolean, "Sign In button is clicked");

			driver.switchTo().frame(signInPage.iframeId);

			returnBoolean = GenericFunctions.clickElement(driver, signInPage.signInButtoninFrame);
			customReporting.assertTrue(returnBoolean, "Sign In button in the frame is clicked");

			String errors1 = signInPage.errorTextMessage.getText();
			customReporting.assertTrue(errors1.contains("There were errors in your submission"),
					"Error message shown is " + errors1);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {

		// close the browser
		driver.quit();
		driver = null;

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
		extent.flush();
	}

}
