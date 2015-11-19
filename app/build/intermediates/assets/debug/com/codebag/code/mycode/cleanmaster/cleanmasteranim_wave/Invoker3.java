package com.codebag.code.mycode.cleanmaster.cleanmasteranim_wave;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.codebag.R;
import com.codebag.bag.MyCode;
import com.codebag.bag.Entry;
import com.codebag.bag.main.InovkedViewActivity;
import com.codebag.code.mycode.cleanmaster.cleanmasteranim_wave.CradCleanDial;
import com.codebag.code.mycode.utils.DisplayUtil;
import com.codebag.code.mycode.utils.MultiViews;
import com.codebag.code.mycode.utils.MultiViews.MyAdapter;

public class Invoker3 extends MyCode {
public CradCleanDial c;
	
	public Invoker3(InovkedViewActivity context) {
		super(context);
		c = new CradCleanDial(context);
	}
	
	@Entry
	public void showButtons() {
		MultiViews buttons = new MultiViews(getActivity(), 3);
		buttons.setAdapter(new MyAdapter(){

			@Override
			public int getCount() {
				return 7;
			}

			@Override
			public View getView(int position) {
				Button b = new Button(getActivity());
				b.setOnClickListener(listener);
				b.setText(position + "");
				return b;
			}
			
		});
		
		FrameLayout fl = new FrameLayout(getActivity());
		int d = DisplayUtil.dip2px(getActivity(), 110);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(d, d);
		params.gravity = Gravity.CENTER;
		fl.addView(c, params);
//		fl.addView(buttons, fillParentParams(Gravity.BOTTOM));
		fl.setBackgroundColor(Color.DKGRAY);
		showView(fl);
	}
	
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case 0:
				c.showMagnifier();
				break;
			case 1:
				c.setCakeProgress(60);
				break;
			case 2:
				c.setWaveProgress(60);
				break;
			case 3:
				c.showRoket();
				break;
			case 4:
				c.setDialMarkResource(R.drawable.card_danager_memory);
				break;
			case 5:
				c.setDialMarkResource(R.drawable.card_danager_storage);
				break;
			case 6:
				c.setDialMarkResource(R.drawable.card_danager_system);
				break;
			}
		}
		
	};

}
