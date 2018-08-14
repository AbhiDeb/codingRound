package test;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import com.sun.javafx.PlatformUtil;

import common.GenericFunctions;
import pageObjectModel.FlightBookingPage;
import reporting.ReporterClass;

public class FlightBookingTest {

    WebDriver driver = null;
    FlightBookingPage flighBookingPage = null;
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
		flighBookingPage = new FlightBookingPage(driver);

		logger = extent.createTest(m.getName());
		customReporting = new ReporterClass(driver, extent, logger, m.getName());
	}

    @Test
    public void testThatResultsAppearForAOneWayJourney() {
    	boolean returnBoolean;
        try {
        
        returnBoolean = GenericFunctions.clickElement(driver, flighBookingPage.oneWayButton);
        
        customReporting.assertTrue(returnBoolean,"One Way Radio button is clicked");
        
        returnBoolean = GenericFunctions.clearElement(driver, flighBookingPage.fromTagText);
        
        customReporting.assertTrue(returnBoolean,"From textbox is cleared");
        
        GenericFunctions.sendKeys(driver, flighBookingPage.fromTagText, "Bangalore");
        
        //wait for the auto complete options to appear for the origin
        GenericFunctions.waitForElement(driver, flighBookingPage.originOptions, 4);
        List<WebElement> originOptions = flighBookingPage.originOptions.findElements(By.tagName("li"));
        GenericFunctions.clickElement(driver, originOptions.get(0));
        
        customReporting.assertTrue(flighBookingPage.fromTagText.getAttribute("value").contains("Bangalore"),"From textbox is filled with Bangalore");

        returnBoolean = GenericFunctions.clearElement(driver, flighBookingPage.toTagText);
        customReporting.assertTrue(returnBoolean,"To textbox is cleared");
        
        GenericFunctions.sendKeys(driver, flighBookingPage.toTagText, "Delhi");

        //wait for the auto complete options to appear for the destination

        GenericFunctions.waitForElement(driver, flighBookingPage.destinationOptions, 4);
        List<WebElement> destinationOptions = flighBookingPage.destinationOptions.findElements(By.tagName("li"));
        GenericFunctions.clickElement(driver, destinationOptions.get(0));

        customReporting.assertTrue(flighBookingPage.toTagText.getAttribute("value").contains("Delhi"),"To textbox is filled with Delhi");
        
        returnBoolean = GenericFunctions.clickElement(driver, flighBookingPage.datePicker);
        customReporting.assertTrue(returnBoolean,"Date is selected");
        
        //all fields filled in. Now click on search
        returnBoolean = GenericFunctions.clickElement(driver, flighBookingPage.buttonSearchButton);
        customReporting.assertTrue(returnBoolean,"Search is clicked");

        returnBoolean = GenericFunctions.waitForElement(driver, flighBookingPage.searchSummary, 5);
        //verify that result appears for the provided journey search
        customReporting.assertTrue(returnBoolean,"Search Summary is present");

        
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    @AfterMethod
	public void afterMethod(ITestResult result) {
    	
    	//close the browser
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
