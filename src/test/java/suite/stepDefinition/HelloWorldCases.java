package suite.stepDefinition;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.Given;

public class HelloWorldCases {
    WebDriver driver;
    
    @Given("^I navigate to baidu$")
    public void navigateToUrl() {
        driver = new ChromeDriver();
        //driver.get("http://www.sina.com");
        driver.navigate().to("http://wwww.baidu.com");
        assertTrue(true);
    }
}