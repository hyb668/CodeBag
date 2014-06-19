package com.codebag.code.mycode.test;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.TextView;

import com.codebag.bag.CaseListView;
import com.codebag.bag.Entry;
import com.codebag.bag.Log;
import com.codebag.code.mlib.myService;

public class TestServiceLife extends CaseListView {

	
	public TestServiceLife(Context context) {
		super(context);
	}

	@Entry()
	public void startservice() {
		Intent intent = new Intent(getContext(),myService.class);
		getContext().startService(intent);
	}
	
	@Entry()
	public void stopservice() {
		Intent intent = new Intent(getContext(),myService.class);
		getContext().stopService(intent);
	}
	
	@Entry()
	public void bindservice() {
		Intent intent = new Intent(getContext(),myService.class);
		getContext().bindService(intent, mServicecon, Service.BIND_AUTO_CREATE);
	}
	
	private ServiceConnection mServicecon = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				
			}
	};
	
	@Entry()
	public void unbindservice() {
		getContext().unbindService(mServicecon);
	}
	
}
