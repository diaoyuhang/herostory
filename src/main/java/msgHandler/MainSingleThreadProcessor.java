package msgHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainSingleThreadProcessor {

    public static final MainSingleThreadProcessor instance = new MainSingleThreadProcessor();

    private ExecutorService threadPool = Executors.newSingleThreadExecutor();
    private MainSingleThreadProcessor() {
    }

    public void addRunnable(Runnable runnable){

        threadPool.submit(runnable);
    }
}
