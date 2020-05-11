package base;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.internal.MethodHelper;

import selenium.ui.base.Config;
import selenium.ui.base.Context;
import selenium.ui.helper.BrowserStackHelper;

public class basicTestManager {

    private static final String BASE_PROPERTIES_FILE_NAME = "base";

    @BeforeSuite(alwaysRun = true)
    @Parameters({ "xmlParamsList" })
    protected void beforeTestSuite(ITestContext testSet, @Optional String xmlParamsList) {
        // Initialize the context system for the test suite.
        Context.registerThreadToTestSuiteContext(testSet);
        ISuite testSuite = testSet.getSuite();
        if (Context.get("config") == null)
            configureTestSuiteConfig(xmlParamsList);
   
        String parallel = testSet.getCurrentXmlTest().getParallel().toString();
        //setupCustomTestSuiteComponents();
    }

    @BeforeMethod(alwaysRun = true)
    protected void beforeTestMethod(ITestContext testSet)
            throws Exception
    {
        setupCustomTestMethodComponents();       
    }

    @AfterMethod(alwaysRun = true)
    protected void afterTestMethod(ITestContext testSet)
    {
        tearDownCustomTestMethodComponents();
    }

    protected void configureTestSuiteConfig(String xmlParamsList)
    {
        Config testSuiteConfig = (Config)Context.get("testSuiteConfig");
        if(testSuiteConfig == null) initTestSuiteConfig(xmlParamsList);
    }

    private synchronized void initTestSuiteConfig(String xmlParamsList)
    {
        Config testSuiteConfig = (Config)Context.get("testSuiteConfig");
        if(testSuiteConfig != null) return;

        // Generate new config object for the test suite context
        testSuiteConfig = new Config();
        Context.put("config", testSuiteConfig);

        // Load test suite XML parameters into the config object
        Config.setXmlTestSuiteParams(xmlParamsList);

        // Make sure that a sourceSet has been declared
        String sourceSet = Config.getString("sourceSet");
        if(sourceSet == null || sourceSet.isEmpty())
        {
            Map<String, String> defaultValueMap = new HashMap<>();
            defaultValueMap.put("sourceSet", "test");
            defaultValueMap.put("isRemote", "true");
            defaultValueMap.put("seleniumGrid", "10.2.68.227:4444");
            Config.addDefaultValues(defaultValueMap);
        }

        Config.addPropertiesFromSharedConfigFile(BASE_PROPERTIES_FILE_NAME);
        Context.put("testSuiteConfig", testSuiteConfig);
    }

    protected void setupCustomTestMethodComponents() throws Exception {
        // NO-OP
    }
    protected void tearDownCustomTestMethodComponents()
    {
        // NO-OP
    }

}
