package selenium.ui.capabilities;

import org.apache.commons.lang3.StringUtils;

import selenium.ui.common.configuration.BrowserStackConfiguration;
import selenium.ui.base.Context;

public class BrowserStackCapabilities extends BasicCapabilities{

    /**
     *
     */
    private static final long serialVersionUID = -5257649704271075574L;

    public BrowserStackCapabilities() {
        this.setCapability("browser", BrowserStackConfiguration.getBrowserName());
        this.setCapability("browser_version", BrowserStackConfiguration.getPlatformVersion());
        this.setCapability("device", BrowserStackConfiguration.getDeviceName());
        this.setCapability("realMobile", BrowserStackConfiguration.getRealMobile());
        this.setCapability("browserstack.appium_version", BrowserStackConfiguration.getAppiumVersion());
        this.setCapability("deviceOrientation", BrowserStackConfiguration.getOrientation());
        this.setCapability("browserstack.local", BrowserStackConfiguration.getIsLocalBrowserstack());
        this.setCapability("browserstack.debug", BrowserStackConfiguration.getIsDebug());
        this.setCapability("project", BrowserStackConfiguration.getProject());
        this.setCapability("takesScreenshot", BrowserStackConfiguration.getTakesScreenshot());
        this.setCapability("browserstack.networkLogs",BrowserStackConfiguration.getIsNetworkLogs());

        if (BrowserStackConfiguration.getIsLocalBrowserstack()
                && StringUtils.isNotEmpty((String) Context.get("localIdentifier"))) {
            this.setCapability("browserstack.localIdentifier", Context.get("localIdentifier"));
        }
    }
}