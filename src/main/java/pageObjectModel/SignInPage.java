package pageObjectModel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignInPage {

	WebDriver driver = null;
	
	@FindBy(linkText = "Your trips")
	public WebElement yourTripsLink;
	
	@FindBy(id = "SignIn")
	public WebElement signInButton;
	
	public String iframeId = "modal_window";
	
	@FindBy(id = "signInButton")
	public WebElement signInButtoninFrame;
	
	@FindBy(id = "errors1")
	public WebElement errorTextMessage;
	
	public SignInPage(WebDriver driver) {
		// TODO Auto-generated constructor stub
		this.driver = driver; 
		PageFactory.initElements(driver, this);
	}

}
