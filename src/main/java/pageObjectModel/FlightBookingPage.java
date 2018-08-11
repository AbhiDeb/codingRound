package pageObjectModel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FlightBookingPage {

	WebDriver driver = null;
	
	@FindBy(id = "OneWay")
	public WebElement oneWayButton;
	
	@FindBy(id = "ToTag")
	public WebElement toTagText;
	
	@FindBy(id = "FromTag")
	public WebElement fromTagText;
	
	@FindBy(id = "ui-id-1")
	public WebElement originOptions;
	
	@FindBy(id = "ui-id-2")
	public WebElement destinationOptions;
	
	@FindBy(xpath = "//*[@id='ui-datepicker-div']/div[1]/table/tbody/tr[3]/td[7]/a")
	public WebElement datePicker;
	
	@FindBy(id = "SearchBtn")
	public WebElement buttonSearchButton;
	
//	@FindBy(className = "searchSummary")
//	public WebElement searchSummary;
	
	public By searchSummary = By.className("searchSummary");
	
	public FlightBookingPage(WebDriver driver) {
		// TODO Auto-generated constructor stub
		this.driver = driver; 
		PageFactory.initElements(driver, this);
	}

}
