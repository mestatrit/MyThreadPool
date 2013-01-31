package vs.ld.impl;
import vs.ld.inf.ITask;

/**
 * 具体任务实装类
 * @author lidong
 *
 */
public class Task implements ITask {
    
	private int i;
	
	/**
	 * 构造函数
	 * @param i
	 */
	public Task(int i) {
		this.i = i;
	}
	
	/**
	 * 具体任务
	 */
	public void dotask() {
		System.out.println("执行任务：" + i);
		// 测试的时候，每次执行停留一秒方便观察
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
