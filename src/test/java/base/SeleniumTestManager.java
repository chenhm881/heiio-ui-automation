package base;

import selenium.ui.base.Config;
import selenium.ui.base.WebDriverHelper;
import selenium.ui.helper.BrowserStackHelper;

public class SeleniumTestManager extends basicTestManager {

    @Override
    protected void configureTestSuiteConfig(String xmlParamsList)
    {
        // Run basic suite config setup (loads default values for BasicTestManager which we may override below)
        super.configureTestSuiteConfig(xmlParamsList);

        // Load shared values for the Selenium test manager into test suite context
        try {
            BrowserStackHelper.startBrowserStackLocal();
        } catch (Exception e) {
            
        }
    }

    @Override
    protected void setupCustomTestMethodComponents() {
        //-----------------------------------------------------------
        String browserType = Config.getString("browser") != null ? Config.getString("browser") : "chrome";
        try {
            WebDriverHelper.startWebDriver(browserType, Config.getBoolean("isRemote"), Config.getString("seleniumGrid"),
                    Config.getString("uuid"));
        } catch (Exception ex) {
            
        }

    }

    @Override
    protected void tearDownCustomTestMethodComponents() {
        WebDriverHelper.stopWebDriver();
    }


}