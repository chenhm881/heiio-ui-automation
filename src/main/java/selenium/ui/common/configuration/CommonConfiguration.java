package selenium.ui.common.configuration;

import selenium.ui.enums.Agent;
import selenium.ui.base.Config;

public class CommonConfiguration {
    public static final String AGENT = "agent";

    public static String getDeviceName() {

        return Config.getString("deviceName");
    }

    public static String getPlatformName() {

        return Config.getString("platformName");
    }

    public static String getPlatformVersion() {

        return Config.getString("platformVersion");
    }

    public static Boolean getFullReset() {

        return Config.getBoolean("fullReset");
    }

    public static Boolean getTakesScreenshot() {

        return Config.getBoolean("takesScreenshot");
    }

    public static String getOrientation() {

        return Config.getString("orientation");
    }

    public static String getLanguage() {

        return Config.getString("language");
    }

    public static String getBrowserName() {

        return Config.getString("browserName");
    }

    public static String getOs(){
        return Config.getString("os");
    }

    public static Boolean getIsDebug() {

        return Config.getBoolean("isDebug");
    }

    public static String getAppiumVersion() {

        return Config.getString("appiumVersion");
    }

    public static String getAppPackage() {

        return Config.getString("appPackage");
    }

    public static String getAppActivity() {

        return Config.getString("appActivity");
    }

    public static String getApp() {

        return Config.getString("app");
    }

    public static Boolean getRealMobile() {

        return Config.getBoolean("realMobile");
    }
    public static String getUuid() {

        return Config.getString("uuid");
    }

    public static String getUseName() {

        return Config.getString("superUserName");
    }

    public static Agent getAgent() {

        Agent agent = null;

        try {
            agent = Config.getEnum(AGENT, Agent.class);
        } catch (Exception ex) {
           
        }

        return agent;
    }

}