
package com.codebag.code.mycode.test.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.codebag.bag.MyCode;
import com.codebag.bag.Entry;
import com.codebag.bag.main.InovkedViewActivity;
/**
 * FixedThreadPool 保持固定数量线程的线程池
 * 
 * @author peter
 *
 */
public class FixedThreadPool extends MyCode {

	public FixedThreadPool(InovkedViewActivity context) {
		super(context);
	}

	@Entry()
	public void runFixedThreadPool() {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		for(int i = 0; i < 10; i++ ) {
			executor.execute(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						System.out.println(Thread.currentThread().getName());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			});
		}
//		executor.shutdown();
//		try {
//			executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

}

