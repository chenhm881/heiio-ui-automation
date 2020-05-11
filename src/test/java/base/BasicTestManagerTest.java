package base;

import org.testng.annotations.Test;
import org.testng.xml.XmlTest;
import org.testng.xml.XmlSuite;

import static org.testng.Assert.assertTrue;

import org.testng.ITestContext;

public class BasicTestManagerTest {
    basicTestManager basicTestManager = new basicTestManager();

    @Test
    public void beforeTestSuiteWithParallelIsMthodsAndInterleaveThreadOutputIsTrue(ITestContext testSet) throws Exception{
        String xmlParamsList = "deviceName=iPhone 6s\\\\nhotelId=66007";
        XmlTest suite = testSet.getCurrentXmlTest();
        suite.setParallel(XmlSuite.ParallelMode.METHODS);
        System.setProperty("interleaveThreadOutput", "true");
        basicTestManager.beforeTestSuite(testSet, xmlParamsList);
        assertTrue(true,"It should be true when the property of interleaveThreadOutput equals true and test cases run in parallel for methods.");
        System.clearProperty("interleaveThreadOutput");
    }
}