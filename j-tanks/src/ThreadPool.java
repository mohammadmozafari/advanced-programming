/*** In The Name of Allah ***/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class holds a global thread-pool for executing our threads.
 * 
 * @author Seyed Mohammad Ghaffarian
 */
public class ThreadPool {
	
	private static ExecutorService executor;
	
	/**
	 * Initializes a new CachedThreadPool.
	 * @see Executors#newCachedThreadPool()
	 */
	public static void init() {
		executor = Executors.newCachedThreadPool();
	}
	
	/**
	 * {@link ExecutorService#execute(Runnable)}
	 */
	public static void execute(Runnable r) {
		if (executor == null)
			init();
		executor.execute(r);
	}
	
	/**
	 * {@link ExecutorService#shutdown()}
	 */
	public static void shutdown() {
		executor.shutdown();
	}
	
	/**
	 * {@link ExecutorService#shutdownNow()}
	 */
	public static void shutdownNow() {
		executor.shutdownNow();
	}
}
