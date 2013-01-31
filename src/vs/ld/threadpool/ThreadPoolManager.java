package vs.ld.threadpool;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import vs.ld.impl.Task;
import vs.ld.impl.TaskThread;
import vs.ld.inf.ITask;
import vs.ld.inf.ITaskThread;

/**
 * 
 * 线程池管理.
 * 
 * @author: lidong
 * @version: 2010-6-29 上午08:53:55
 */
public class ThreadPoolManager
{

	/**
	 * 保留的线程池大小.<br>
	 * 如果正在运行的线程数量小于 CORE_POOL_SIZE，那么马上创建线程运行任务；
	 */
	private final static int CORE_POOL_SIZE = 6;

	/**
	 * 线程池的最大大小.<br>
	 */
	private final static int MAXIMUM_POOL_SIZE = 10;

	/**
	 * 空闲线程结束的超时时间.<br>
	 * 当一个线程无事可做，超过一定的时间（KEEP_ALIVE_TIME）时，线程池会判断，<br>
	 * 如果当前运行的线程数大于 CORE_POOL_SIZE，那么这个线程就被停掉。
	 */
	private final static int KEEP_ALIVE_TIME = 600;

	/**
	 * 表示 KEEP_ALIVE_TIME的单位.<br>
	 * TimeUnit.SECONDS: 秒(s)
	 */
	private final static TimeUnit UNIT = TimeUnit.SECONDS;

	/**
	 * 存放任务的队列.<br>
	 * 当一个线程完成任务时，它会从队列中取下一个任务来执行。
	 */
	private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();

	/**
	 * 线程池声明.
	 */
	private MyThreadPoolExecutor threadPool = null;

	/**
	 * 
	 * 默认构造函数。<br>
	 * 创建默认线程池:<br>
	 * <ul>
	 * <li>corePoolSize 保留的线程池大小 : <b>6</b></li>
	 * <li>maximumPoolSize 线程池的最大大小 : <b>10</b></li>
	 * <li>keepAliveTime 空闲线程结束的超时时间 : <b>600</b></li>
	 * <li>unit keepAliveTime的单位: <b>秒(s)</b></li>
	 * <li>workQueue 存放任务的队列: <b>LinkedBlockingQueue</b></li>
	 * </ul>
	 */
	public ThreadPoolManager ()
	{
		// 创建默认线程池
		// 我们这里的任务队列用 LinkedBlockingQueue的好处在于没有大小限制。
		// 这样的话，因为队列不会满，所以 execute()不会抛出异常，
		// 而线程池中运行的线程数也永远不会超过 corePoolSize 个，
		// maximumPoolSize， keepAliveTime， unit 参数也就没有意义了。
		threadPool = new MyThreadPoolExecutor(CORE_POOL_SIZE, // 保留的线程池大小
				MAXIMUM_POOL_SIZE,// 线程池的最大大小
				KEEP_ALIVE_TIME, // 空闲线程结束的超时时间
				UNIT, // 表示 KEEP_ALIVE_TIME的单位秒(s)
				workQueue // 存放任务的队列
		);
	}

	/**
	 * 
	 * 构造函数。<br>
	 * 创建指定大小的线程池:<br>
	 * 
	 * @param corePoolSize
	 *            保留的线程池大小，默认大小为<b>6</b>
	 * 
	 */
	public ThreadPoolManager(int corePoolSize) {
		// 创建指定大小的线程池
		// 我们这里的任务队列用 LinkedBlockingQueue的好处在于没有大小限制。
		// 这样的话，因为队列不会满，所以 execute()不会抛出异常，
		// 而线程池中运行的线程数也永远不会超过 corePoolSize 个，
		// maximumPoolSize， keepAliveTime， unit 参数也就没有意义了。
		if (corePoolSize <= 0) {
			// 如果指定大小小于等于0，线程池大小为默认值
			corePoolSize = CORE_POOL_SIZE;
		}
		// 创建指定大小的线程池
		threadPool = new MyThreadPoolExecutor(corePoolSize, // 保留的线程池大小
				MAXIMUM_POOL_SIZE,// 线程池的最大大小
				KEEP_ALIVE_TIME, // 空闲线程结束的超时时间
				UNIT, // 表示 KEEP_ALIVE_TIME的单位秒(s)
				workQueue // 存放任务的队列
		);
	}

	/**
	 *
	 * 功能： 执行线程池任务.
	 *
	 * @author lidong
	 * @param taskType
	 *            任务类型
	 * @param paramInfo
	 *            参数信息，创建任务对象的参数
	 * 
	 */
	public void execute() {
 
			// 循环所有的表,创建任务 并在线程池中运行
			for (int i = 1; i <= 100; i++) {    
				// 创建任务
				ITask task = createTask(i);
				// 创建该任务线程
				ITaskThread taskThread = createThread(task);
				// 把线程追加到线程池中
				threadPool.execute(taskThread);
			}
			
			// 等待任务结束,主线程才继续运行
			threadPool.isEndTask();
			// 关闭连接池
			threadPool.shutdown();
 
	}

	/**
	 * 
	 * 功能： 创建任务
	 * 
	 * @author lidong
	 * @param i
	 * 
	 * @return ITask
	 */
	public ITask createTask (final int i)
	{
		return new Task(i);
	}

	/**
	 * 
	 * 功能： 创建任务线程
	 * 
	 * @author lidong
	 * @param task
	 * @return ITaskThread 范例：
	 */
	private ITaskThread createThread (final ITask task)
	{
		// 创建任务线程
		final ITaskThread thread = new TaskThread(task);
		return thread;
	}

	/**
	 * 
	 * 功能： 立即关闭线程池,任务队列中的不再处理
	 * @author lidong
	 * void
	 * 范例：
	 */
	public void shutdownNow ()
	{
		// 关闭连接池
		this.threadPool.shutdownNow();
	}
	
	/**
	 * 
	 * 功能：任务队列中所有任务完成以后关闭线程池
	 * @author lidong
	 * void
	 * 范例：
	 */
	public void shutdown ()
	{
		// 关闭连接池
		this.threadPool.shutdown();
	}
}
