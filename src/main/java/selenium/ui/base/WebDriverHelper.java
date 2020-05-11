package selenium.ui.base;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selenium.ui.capabilities.BrowserStackCapabilities;
import selenium.ui.common.configuration.BrowserStackConfiguration;


public class WebDriverHelper {
    
    public static void startWebDriver(String browserType, boolean isRemoteGrid, String gridUrl, String uuid) throws Exception {
        // Start by configuring generic capabilities that apply to any browser type
        BrowserStackCapabilities capabilities = new BrowserStackCapabilities();
        configureGenericCapabilities(capabilities, uuid);
        // Now configure capabilities for a specific browser type and create a WebDriver of that type.
        WebDriver webDriver = null;
        
        webDriver = generateChromeDriver(capabilities, isRemoteGrid, gridUrl);
        
        try {
            webDriver.manage().window().maximize();
        } catch (Exception ex) {
            
        }
        webDriver.manage().window().setSize(webDriver.manage().window().getSize());
        Context.put("webDriver", webDriver);
    }

   protected static void configureGenericCapabilities(DesiredCapabilities capabilities, String uuid) {
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        capabilities.setCapability(CapabilityType.SUPPORTS_WEB_STORAGE, false);

        // Enable extra logging
        configureLoggingCapability(capabilities);

        //Set unique id for tracking purposes
        capabilities.setCapability("uuid", uuid);
    }

    private static void configureLoggingCapability(DesiredCapabilities capabilities) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        logPrefs.enable(LogType.CLIENT, Level.ALL);
        logPrefs.enable(LogType.DRIVER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
    }
    
    private static WebDriver generateChromeDriver(DesiredCapabilities capabilities, boolean useGrid, String gridUrl) throws Exception {

        // update bug: it will copy chrome driver to temp directory when useGrid = true,
        if (useGrid) {

            // Configure Chrome driver settings
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("test-type");

            capabilities.setBrowserName("chrome");
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

            return generateRemoteWebDriver(capabilities, gridUrl);
        }

        // Find and specify the correct Chrome driver for the system
        WebDriver driver = new ChromeDriver();
        return driver;
    }

    private static synchronized RemoteWebDriver generateRemoteWebDriver(DesiredCapabilities desiredCapabilities, String gridIP) throws Exception {
        String bsUserName = BrowserStackConfiguration.getBsUserName();
        String bsAccessKey = BrowserStackConfiguration.getBsAccessKey();
        if (StringUtils.isNotEmpty(bsUserName) && StringUtils.isNotEmpty(bsAccessKey)) {
            bsUserName = bsUserName.trim();
            bsAccessKey = bsAccessKey.trim();
        } 
        String bsUrl = "https://" + bsUserName
                + ":" + bsAccessKey + "@hub.browserstack.com/wd/hub";
        URL url;
        String DAGridURL = "http://" + gridIP + "/wd/hub";
        try {
            url = new URL(bsUrl);
        } catch (MalformedURLException e) {
            String errorMessage = String.format("URL string [%s] is malformed.  Unable to instantiate RemoteWebDriver!",
                    DAGridURL);
            throw new Exception(e);
        }
        RemoteWebDriver remoteWebDriver = null;

        int retriesLeft = 3;
        while (retriesLeft > 0) {
            String connectMessage = String.format("Attempting to get RemoteWebDriver from Selenium Grid server at " +
                    "[%s]...", DAGridURL);
            Logger logger = LoggerFactory.getLogger(WebDriver.class);
            retriesLeft--;
            try {
                remoteWebDriver = new RemoteWebDriver(url, desiredCapabilities);
                return remoteWebDriver;
            } catch (Exception e) {
                String errorMessage = String.format("Unable to get RemoteWebDriver from Selenium Grid server at [%s]!",
                        DAGridURL);
                if (retriesLeft == 0) {
                    errorMessage += String.format(" No retries left (HALTING)! [%s]", e.toString());
                    logger.info(errorMessage);
                } else {
                    if (retriesLeft == 1) {
                        errorMessage += " [1] retry left...";
                    } else {
                        errorMessage += String.format(" [%s] retries left...", retriesLeft);
                    }
                    

                    int gridConnectRetryDelay = 15;
                    if (gridConnectRetryDelay > 0) {
                        String retryMessage = String.format("Waiting [%s] seconds before retrying connection to " +
                                "Selenium Grid", gridConnectRetryDelay);
                    }
                }
            }
        }
        return remoteWebDriver;
    }


    public static void stopWebDriver() {
        // Stop the web driver instance
        WebDriver webDriver = (WebDriver) Context.get("webDriver");
        if (webDriver != null) {
            // TODO: refactor this to add the webDriver instance to a BrowserPool for re-use based on DesiredCapabilities
            // Stop the web driver
            String webDriverStopStatus = "Stopping WebDriver... ";
            try {
                webDriver.quit();
            } catch (Exception e) {

            }
        }
    }

}   