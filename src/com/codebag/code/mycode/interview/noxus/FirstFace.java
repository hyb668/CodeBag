package com.codebag.code.mycode.interview.noxus;

import java.util.HashMap;

import android.graphics.Color;

import com.codebag.bag.MyCode;
import com.codebag.bag.Entry;
import com.codebag.bag.main.InovkedViewActivity;
import com.codebag.code.mycode.utils.Log;

/**
 * onTouchEvent() 返回值的作用是：当前的view处理touch事件的反馈，返回true。说明已经处理。
 * 返回false，说明未处理，需要父容器处理。是一个反向流
 * 
 * @author peter
 *
 */
public class FirstFace extends MyCode {

	public FirstFace(InovkedViewActivity context) {
		super(context);
	}

	@Entry
	public void putSameHashCodeObject2HashMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("1", new SameHashCodeObj(1));
		map.put("2", new SameHashCodeObj(2));
		map.put("3", new SameHashCodeObj(3));
		Log.addLog("peter", this, map.toString());
	}
	
	@Entry
	public void viewOnTouchReturnFalse() {
		FatherView fv = new FatherView(getActivity());
		fv.setBackgroundColor(Color.BLUE);
		SonViewOne sv = new SonViewOne(getActivity());
		sv.setBackgroundColor(Color.GREEN);
		fv.addView(sv);
		showView(fv);
	}
	
	@Entry
	public void viewOnTouchReturnTrue() {
		FatherView fv = new FatherView(getActivity());
		fv.setBackgroundColor(Color.BLUE);
		SonViewTwo sv = new SonViewTwo(getActivity());
		sv.setBackgroundColor(Color.GREEN);
		fv.addView(sv);
		showView(fv);
	}
	
	@Entry
	public void fatherViewOnTouchReturnFalse() {
		GrandFatherView gv = new GrandFatherView(getActivity());
		gv.setBackgroundColor(Color.GRAY);
		FatherView fv = new FatherView(getActivity());
		fv.setBackgroundColor(Color.BLUE);
		SonViewOne sv = new SonViewOne(getActivity());
		sv.setBackgroundColor(Color.GREEN);
		fv.addView(sv);
		gv.addView(fv);
		showView(gv);
	}
	
	
}
