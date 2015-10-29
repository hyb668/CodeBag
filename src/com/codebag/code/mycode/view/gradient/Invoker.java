package com.codebag.code.mycode.view.gradient;

import android.content.Context;
import android.widget.FrameLayout;

import com.codebag.R;
import com.codebag.bag.MyCode;
import com.codebag.bag.Entry;
import com.codebag.bag.main.InovkedViewActivity;

public class Invoker extends MyCode {

	public Invoker(InovkedViewActivity context) {
		super(context);
	}

	@Entry()
	public void showGradient() {
		Gradient g = new Gradient(getActivity());
		g.setGradient(0xffffffff, 0);
		showView(g);
	}
	
	@Entry()
	public void showGradientTwo() {
		FrameLayout fl = new FrameLayout(getActivity());
		fl.setBackgroundResource(R.drawable.gradient);
		showView(fl);
	}

}
