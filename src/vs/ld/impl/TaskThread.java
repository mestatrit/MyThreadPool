package vs.ld.impl;
import vs.ld.inf.ITask;
import vs.ld.inf.ITaskThread;


/**
 * 
 * 任务线程
 * 
 * @author: lidong
 * @version: 2010-6-29 下午04:01:27
 */
public class TaskThread implements ITaskThread {

	/**
	 * 任务
	 */
	private ITask task;

	/**
	 * 构造函数
	 * 
	 * @param task_
	 */
	public TaskThread(ITask task_) {
		task = task_;
	}

	/**
	 * 继承与Runnable
	 */
	public void run() {
		// 执行任务
		task.dotask();
	}
}