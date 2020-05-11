package pagetest;

import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.SeleniumTestManager;
import selenium.ui.base.Context;


public class loginTest extends SeleniumTestManager {
    @Test(groups = {"test"}) 
    public void isOnPage(final ITestContext testContext, final Method method) {
        Context.registerThreadToTestSuiteContext(testContext);
        WebDriver webDriver = (WebDriver) Context.get("webDriver");
        webDriver.navigate().to("http://www.baidu.com");
        WebElement body = webDriver.findElement(By.cssSelector("body"));        
        System.out.println("body.getText();" + body.getText());
    }

}