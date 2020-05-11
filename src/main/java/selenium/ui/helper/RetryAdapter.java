package selenium.ui.helper;

/**
 * by vjiang, created at: 3/13/2018
 */
public abstract class RetryAdapter
{
    private int retryNo = 1;
    private int retryDelay = 15;

    public int getMaxRetryNo()
    {
        return this.retryNo;
    }

    public int getRetryDelay()
    {
        return this.retryDelay;
    }

    public abstract void doBusiness() throws Exception;

    /**
     * the name of this retry, used to show in error message
     */
    public abstract String executionName();

    public abstract void doCloseBusiness();

    int getRetryLeft()
    {
        return retryNo;
    }

    void executeRetry() throws Exception
    {
        retryNo--;
        doBusiness();
    }


    void initRetry()
    {
        this.retryNo = getMaxRetryNo();
        this.retryDelay = getRetryDelay();
    }

}
