package selenium.ui.capabilities;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import selenium.ui.common.configuration.CommonConfiguration;
import selenium.ui.base.Config;

public class BasicCapabilities extends DesiredCapabilities {

    /**
     *
     */
    private static final long serialVersionUID = -5652420025627371275L;

    public BasicCapabilities() {
        this.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        this.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        this.setCapability(CapabilityType.SUPPORTS_WEB_STORAGE, false);
        this.setCapability("uuid", CommonConfiguration.getUuid());
    }
}
