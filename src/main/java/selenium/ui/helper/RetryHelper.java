package selenium.ui.helper;

/**
 * by vjiang, created at: 3/13/2018
 */
public class RetryHelper
{
    public static boolean startRetry(RetryAdapter adapter)
    {
        String name = adapter.executionName();
        adapter.initRetry();
        while (adapter.getRetryLeft() > 0)
        {
            try
            {
                String message = String.format("Attempting to [%s] with [%s] retry times", name, adapter.getRetryLeft());
                adapter.executeRetry();
                return true;
            } catch (Throwable t)
            {
                try
                {
                    int retryLeft = adapter.getRetryLeft();
                    String errorMessage = String.format("Do %s failed", name);
                    if (adapter.getRetryLeft() == 0)
                    {
                       
                    } else
                    {
                        if (retryLeft == 1)
                        {
                            errorMessage += " [1] retry left...";
                        } else
                        {
                            errorMessage += String.format(" [%s] retries left...", retryLeft);
                        }
                        
                        int retryDelay = adapter.getRetryDelay();
                        if (retryDelay > 0)
                        {
                            String retryMessage = String.format("Waiting [%s] seconds before retrying [%s]", retryDelay, name);
                           
                        }
                    }
                } finally
                {
                    adapter.doCloseBusiness();
                }
            }
        }
        return false;
    }
}
