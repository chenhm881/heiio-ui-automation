package selenium.ui.helper;

import selenium.ui.common.configuration.BrowserStackConfiguration;
import selenium.ui.common.configuration.CommonConfiguration;
import selenium.ui.enums.Agent;
import com.browserstack.local.Local;
import org.apache.commons.lang3.RandomStringUtils;
import selenium.ui.base.Context;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by qxiang on 9/15/2017.
 */
public class BrowserStackHelper {
    private static String BSURL = "https://www.browserstack.com/automate/sessions/%s.json";
    private static String BSAPPURL = "https://www.browserstack.com/app-automate/sessions/%s.json";
    private static final int BROWSERSTACKLOCALRETRY_TIMES = 3;

    /**
     * start browser stack local vpn
     */
    public static void startBrowserStackLocal() {
        if (Agent.browserStack.equals(BrowserStackConfiguration.getAgent()) && BrowserStackConfiguration.getIsLocalBrowserstack()) {
            final Local local = new Local();
            final HashMap<String, String> options = new HashMap<String, String>();
            options.put("key", BrowserStackConfiguration.getBsAccessKey());
            String localIdentifier = RandomStringUtils.randomAlphabetic(10);
            options.put("localIdentifier", localIdentifier);
            if (BrowserStackConfiguration.getIsDebug()) {
                options.put("-verbose", "3");
                options.put("-force-local", "");
                options.put("logFile", "build/reports/bs_log.txt");
            }
            Context.put("localIdentifier", localIdentifier);

            RetryAdapter adapter = new RetryAdapter()
            {
                @Override
                public void doBusiness() throws Exception
                {
                    local.start(options);
                    Context.put("local", local);
                }

                @Override
                public String executionName()
                {
                    return "Start BrowserStack local";
                }

                @Override
                public void doCloseBusiness()
                {
                    try
                    {
                        if(local.isRunning()){
                            local.stop();
                        }
                    }
                    catch (Exception e)
                    {
                    }
                }

                @Override
                public int getMaxRetryNo()
                {
                    return BROWSERSTACKLOCALRETRY_TIMES;
                }
            };
            RetryHelper.startRetry(adapter);
        }
    }

    /**
     * stop browser stack local vpn
     */
    public static void stopBrowserStackLocal() {
        Local local = (Local) Context.get("local");
        if (local != null) {
            try {
                local.stop();
            } catch (Exception e) {
            }
        }
    }

}
