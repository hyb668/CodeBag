package com.codebag.code.mycode.cleanmaster.cleanmasteranim_wave;

import android.graphics.Color;
import android.os.Handler;

import com.codebag.bag.MyCode;
import com.codebag.bag.Entry;
import com.codebag.bag.main.InovkedViewActivity;

public class Invoker2 extends MyCode {

	public Invoker2(InovkedViewActivity context) {
		super(context);
	}
	
	@Entry
	public void showCardWaveBar() {
		CardWaveBar bar = new CardWaveBar(getActivity());
		bar.setData(0, 500, 0);
		int waveColor = Color.parseColor("#33ffffff");
		int cirCleColor = Color.parseColor("#24a0ff");
		bar.setColor(waveColor, cirCleColor);
		bar.setProgress(80);
		showView(bar);
	}
	
	@Entry
	public void animCardWaveBar() {
		CardWaveBar bar = new CardWaveBar(getActivity());
		bar.setData(0, 500, 0);
		int waveColor = Color.parseColor("#33ffffff");
		int cirCleColor = Color.parseColor("#24a0ff");
		bar.setColor(waveColor, cirCleColor);
		bar.startAnimination(80);
		showView(bar);
	}

}
