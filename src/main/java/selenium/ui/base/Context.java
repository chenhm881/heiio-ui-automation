package selenium.ui.base;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.ITestContext;


public class Context
{
    private static final String TEST_SUITE_TITLE_DELIMITER = "]]";

    private static Map<String, Map<String, Object>> contextTitleToTestSuiteObjectsMapMap = new ConcurrentHashMap<>();
    private static Map<String, Map<String, Object>> contextTitleToTestMethodObjectsMapMap = new ConcurrentHashMap<>();
    private static Map<Long, String> threadIdToContextTitleMap = new ConcurrentHashMap<>();
    public static Object get(String key)
    {
        long threadId = Thread.currentThread().getId();
        return get(key, threadId);
    }

    public static Object get(String key, long threadId)
    {
        if(threadId == -1) threadId = Thread.currentThread().getId();
        Object object = null;
        String contextTitle = threadIdToContextTitleMap.get(threadId);


        Map<String, Object> testMethodObjectsMap = contextTitleToTestSuiteObjectsMapMap.get(contextTitle);
        object = testMethodObjectsMap.get(key);

        // If the thread is registered to the test suite context or the object was not found in the test set context
        // then we'll search for the object in the test suite context.
        if(object == null)
        {
            int contextSuiteTitleEnd = contextTitle.indexOf(TEST_SUITE_TITLE_DELIMITER) +
                                       TEST_SUITE_TITLE_DELIMITER.length();
            String contextSuiteTitle = contextTitle.substring(0, contextSuiteTitleEnd);
            Map<String, Object> testSuiteObjectsMap =
                    contextTitleToTestSuiteObjectsMapMap.get(contextSuiteTitle);
            //object = testSuiteObjectsMap.get(key);
        }

        // If the object was never placed in a context then it might still be null
        return object;
    }


    public static void put(String key, Object object)
    {

        long threadId = Thread.currentThread().getId();
        String contextTitle = threadIdToContextTitleMap.get(threadId);
        Map<String, Object> testMethodObjectsMap = contextTitleToTestSuiteObjectsMapMap.get(contextTitle);
        Object testObject = testMethodObjectsMap.get(key);
        if(testObject == null) {
            testMethodObjectsMap.put(key, object);
        }
    }

    public static void registerThreadToTestSuiteContext(ITestContext testSet)
    {
        long threadId = Thread.currentThread().getId();
        String testSuiteTitle = testSet.getSuite().getName();
        String testSuiteContextTitle = String.format("%s%s", testSuiteTitle, TEST_SUITE_TITLE_DELIMITER);
        threadIdToContextTitleMap.put(threadId, testSuiteContextTitle);

        // Create the test suite objects map if it does not already exist.
        if(contextTitleToTestSuiteObjectsMapMap.get(testSuiteContextTitle) == null)
        {
            Map<String, Object> testSuiteObjectsMap = new ConcurrentHashMap<>();
            contextTitleToTestSuiteObjectsMapMap.put(testSuiteContextTitle, testSuiteObjectsMap);
        }

        // Store this object in case we need to retrieve it for passing to another thread for registration.
        Context.put("testSet", testSet);
    }


}