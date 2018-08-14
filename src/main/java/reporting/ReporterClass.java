package reporting;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.asserts.IAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import common.GenericFunctions;

public class ReporterClass extends org.testng.asserts.SoftAssert {
	private ExtentReports extent;
	private ExtentTest logger;
	private String methodName;
	private WebDriver driver;
	
	public ReporterClass(WebDriver driver, ExtentReports extent, ExtentTest logger, String methodName) {
		this.extent = extent;
		this.logger = logger;
		this.methodName = methodName;
		this.driver = driver;
	}
	
	abstract private static class SimpleAssert implements IAssert {
		private final String m_message; 
		
		public SimpleAssert(String message) {
			m_message = message;
		}

		public String getMessage() {
			// TODO Auto-generated method stub
			return m_message;
		}


		public Object getActual() {
			// TODO Auto-generated method stub
			return null;
		}


		public void doAssert() {
			// TODO Auto-generated method stub
			
		}

		public Object getExpected() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
	
	@Override
	public void assertTrue(final boolean condition,final String message) {
		if(condition) {
			logger.log(Status.PASS, MarkupHelper.createLabel(message, ExtentColor.GREEN));
		} else {
			logger.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
			try {
				logger.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(GenericFunctions.getScreenshot(driver)).build());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		doAssert(new SimpleAssert(message) {

			@Override
			public Object getActual() {
				// TODO Auto-generated method stub
				return condition;
			}

			@Override
			public void doAssert() {
				// TODO Auto-generated method stub
				super.doAssert();
				org.testng.Assert.assertTrue(condition, message);
			}

			@Override
			public Object getExpected() {
				// TODO Auto-generated method stub
				return true;
			}
			
		});
		
		
	}

	@Override
	public void assertEquals(final String actual, final String expected, final String message) {
		// TODO Auto-generated method stub
		
		if(actual.equalsIgnoreCase(expected))
			logger.log(Status.PASS, MarkupHelper.createLabel(message, ExtentColor.GREEN));
		else {
			logger.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
			try {
				logger.log(Status.FAIL, (Markup) logger.addScreenCaptureFromPath(GenericFunctions.getScreenshot(driver)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		doAssert(new SimpleAssert(message) {

			@Override
			public Object getActual() {
				// TODO Auto-generated method stub
				return actual;
			}

			@Override
			public void doAssert() {
				// TODO Auto-generated method stub
				super.doAssert();
				org.testng.Assert.assertEquals(actual, expected, message);
			}

			@Override
			public Object getExpected() {
				// TODO Auto-generated method stub
				return expected;
			}
			
		});
	}

	@Override
	public void assertEquals(final boolean actual, final boolean expected, final String message) {
		// TODO Auto-generated method stub
		
		if(actual && expected)
			logger.log(Status.PASS, MarkupHelper.createLabel(message, ExtentColor.GREEN));
		else {
			logger.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
			try {
				logger.log(Status.FAIL, (Markup) logger.addScreenCaptureFromPath(GenericFunctions.getScreenshot(driver)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		doAssert(new SimpleAssert(message) {

			@Override
			public Object getActual() {
				// TODO Auto-generated method stub
				return actual;
			}

			@Override
			public void doAssert() {
				// TODO Auto-generated method stub
				super.doAssert();
				org.testng.Assert.assertEquals(actual, expected, message);
			}

			@Override
			public Object getExpected() {
				// TODO Auto-generated method stub
				return expected;
			}
			
		});
	}
	
	
}
