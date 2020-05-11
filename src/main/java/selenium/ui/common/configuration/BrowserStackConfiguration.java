package selenium.ui.common.configuration;

import selenium.ui.base.Config;
import selenium.ui.base.Context;

/**
 * Created by qxiang on 9/7/2017.
 */
public class BrowserStackConfiguration extends CommonConfiguration{

    public static Boolean getIsLocalBrowserstack() {

        return Config.getBoolean("isLocalBrowserstack");
    }

    public static String getBrowserstackConsole() {

        return Config.getString("browserstackConsole");
    }

    public static String getBsUserName() {

        return Config.getString("BROWSERSTACK_USERNAME");
    }

    public static String getBsAccessKey() {

        return Config.getString("BROWSERSTACK_ACCESS_KEY");
    }

    public static String getProject() {

        return Config.getString("project");
    }

    public static String getLocalIdentifier() {

        return (String) Context.get("localIdentifier");
    }

    public  static Boolean getIsNetworkLogs(){
        return Config.getBoolean("isNetworkLogs");
    }
}