package com.javalive09.sample.function.asynctask;

import android.os.AsyncTask;
import android.util.Log;
import com.javalive09.codebag.Entry;

/**
 * 测试asynctask 的最多线程数
 */
public class Invoke extends Entry {

	public void multiAsyncTask() {
		for(int i=0; i< 2000; i++) {
			doAsyncTask();
		}
	}

	private void doAsyncTask() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					String name = Thread.currentThread().getName();
					Log.i("peter", "thread name =" + name);
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

}