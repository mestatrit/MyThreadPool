package vs.ld.test;

import vs.ld.threadpool.ThreadPoolManager;

public class RunThreadPool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 线程池为10个
		ThreadPoolManager tpm = new ThreadPoolManager(10);
		// 执行
		tpm.execute();
	}

}
